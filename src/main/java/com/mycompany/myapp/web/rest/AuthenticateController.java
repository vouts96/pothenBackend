package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.security.SecurityUtils.AUTHORITIES_KEY;
import static com.mycompany.myapp.security.SecurityUtils.JWT_ALGORITHM;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.web.rest.vm.LoginVM;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Controller to authenticate users with both JWT and Taxisnet OAuth2.
 */
@RestController
@RequestMapping("/api")
public class AuthenticateController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticateController.class);

    private final JwtEncoder jwtEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final WebClient webClient;
    private final UserService userService;

    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds:0}")
    private long tokenValidityInSeconds;

    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds-for-remember-me:0}")
    private long tokenValidityInSecondsForRememberMe;

    @Value("${taxisnet.client-id}")
    private String taxisnetClientId;

    @Value("${taxisnet.client-secret}")
    private String taxisnetClientSecret;

    @Value("${taxisnet.token-uri}")
    private String taxisnetTokenUri;

    @Value("${taxisnet.user-info-uri}")
    private String taxisnetUserInfoUri;

    public AuthenticateController(
        JwtEncoder jwtEncoder,
        AuthenticationManagerBuilder authenticationManagerBuilder,
        WebClient.Builder webClientBuilder,
        UserService userService
    ) {
        this.jwtEncoder = jwtEncoder;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.webClient = webClientBuilder.build();
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = this.createToken(authentication, loginVM.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * OAuth2 Authentication via Taxisnet.
     *
     * @param code Authorization code from Taxisnet.
     * @return JWT token.
     */
    @GetMapping("/authenticate-oauth2")
    public ResponseEntity<JWTToken> authorizeWithOAuth2(@RequestParam String code) {
        try {
            // Step 1: Exchange authorization code for access token
            String accessToken = getAccessTokenFromTaxisnet(code);
            LOG.info("Received OAuth2 Access Token: {}", accessToken);

            // Step 2: Retrieve user info (XML Response) from Taxisnet
            String userInfoXml = getUserInfoFromTaxisnet(accessToken);
            LOG.info("User Info from Taxisnet: {}", userInfoXml);

            // Step 3: Register user in the system if they don't exist
            User registeredUser = userService.registerUserFromXml(userInfoXml);
            if (registeredUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Step 4: Generate JWT token
            String jwt = createToken(registeredUser.getLogin(), "ROLE_USER");

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBearerAuth(jwt);

            return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("OAuth2 authentication failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/authenticate-keycloak")
    public ResponseEntity<JWTToken> authorizeWithKeycloak(@RequestParam String code) {
        try {
            System.out.println("▶ Received code: " + code);

            String idToken = getIdTokenFromKeycloak(code);
            System.out.println("Received id_token");

            String[] parts = idToken.split("\\.");
            if (parts.length != 3) {
                System.err.println("Invalid JWT format");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            System.out.println("🔍 Decoded JWT payload: " + payloadJson);

            JSONObject payload = new JSONObject(payloadJson);
            String username = payload.optString("preferred_username", null);
            String email = payload.optString("email", null);
            String name = payload.optString("name", null);

            System.out.println("👤 Extracted: username=" + username + ", email=" + email + ", name=" + name);

            if (username == null) {
                System.err.println("Missing 'preferred_username' in token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            User user = userService.registerUserFromKeycloak(username, email, name);
            if (user == null) {
                System.err.println("Failed to register user: " + username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String jwt = createToken(user.getLogin(), "ROLE_USER");
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwt);
            return new ResponseEntity<>(new JWTToken(jwt), headers, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("❌ Exception in /authenticate-keycloak");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private String getIdTokenFromKeycloak(String code) {
        try {
            String requestBody =
                "client_id=public-client" + "&grant_type=authorization_code" + "&redirect_uri=https://147.102.74.34" + "&code=" + code;

            System.out.println("Sending token request to Keycloak...");
            System.out.println("client_id=public-client");
            System.out.println("grant_type=authorization_code");
            System.out.println("redirect_uri=https://147.102.74.34/");
            System.out.println("code=" + code);

            String tokenResponse = webClient
                .post()
                .uri("https://gpu.dslab.ece.ntua.gr:8443/realms/billys/protocol/openid-connect/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                    status -> status.isError(),
                    response ->
                        response
                            .bodyToMono(String.class)
                            .flatMap(body -> {
                                System.err.println("Keycloak token endpoint returned error: " + body);
                                return Mono.error(new RuntimeException("Keycloak error: " + body));
                            })
                )
                .bodyToMono(String.class)
                .block();

            System.out.println("Raw token response: " + tokenResponse);

            JSONObject json = new JSONObject(tokenResponse);
            return json.getString("id_token");
        } catch (Exception e) {
            System.err.println("Exception while calling Keycloak token endpoint:");
            e.printStackTrace();
            throw new RuntimeException("Failed to get id_token from Keycloak", e);
        }
    }

    /**
     * Calls Taxisnet to exchange authorization code for access token.
     */
    private String getAccessTokenFromTaxisnet(String code) {
        String requestBody =
            "client_id=" +
            taxisnetClientId +
            "&client_secret=" +
            taxisnetClientSecret +
            "&scope=read" +
            "&code=" +
            code +
            "&grant_type=authorization_code";

        return webClient
            .post()
            .uri(taxisnetTokenUri)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String.class)
            .map(response -> new JSONObject(response).getString("access_token"))
            .block(); // Blocking for simplicity
    }

    /**
     * Calls Taxisnet to get user info.
     */
    private String getUserInfoFromTaxisnet(String accessToken) {
        return webClient
            .get()
            .uri(taxisnetUserInfoUri)
            .header("Authorization", "Bearer " + accessToken)
            .header("Accept", "application/json")
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    /**
     * Creates JWT token from authentication.
     */
    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        Instant now = Instant.now();
        Instant validity = rememberMe
            ? now.plus(this.tokenValidityInSecondsForRememberMe, ChronoUnit.SECONDS)
            : now.plus(this.tokenValidityInSeconds, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    /**
     * Creates JWT token directly from username and authorities.
     */
    public String createToken(String username, String authorities) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.tokenValidityInSeconds, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(username)
            .claim(AUTHORITIES_KEY, authorities)
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
