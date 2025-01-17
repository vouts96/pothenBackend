package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Committee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Committee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommitteeRepository extends JpaRepository<Committee, Long> {}
