package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Committee;
import com.mycompany.myapp.domain.Grade;
import com.mycompany.myapp.domain.Position;
import com.mycompany.myapp.domain.Submission;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.CommitteeDTO;
import com.mycompany.myapp.service.dto.GradeDTO;
import com.mycompany.myapp.service.dto.PositionDTO;
import com.mycompany.myapp.service.dto.SubmissionDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Submission} and its DTO {@link SubmissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubmissionMapper extends EntityMapper<SubmissionDTO, Submission> {
    @Mapping(target = "position", source = "position", qualifiedByName = "positionName")
    @Mapping(target = "grade", source = "grade", qualifiedByName = "gradeName")
    @Mapping(target = "committeeName", source = "committeeName", qualifiedByName = "committeeName")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    SubmissionDTO toDto(Submission s);

    @Named("positionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    PositionDTO toDtoPositionName(Position position);

    @Named("gradeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    GradeDTO toDtoGradeName(Grade grade);

    @Named("committeeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CommitteeDTO toDtoCommitteeName(Committee committee);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
