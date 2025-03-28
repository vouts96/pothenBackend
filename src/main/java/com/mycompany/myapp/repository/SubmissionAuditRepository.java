package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SubmissionAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubmissionAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubmissionAuditRepository extends JpaRepository<SubmissionAudit, Long> {
    Page<SubmissionAudit> findByOriginalSubmissionId(Long originalSubmissionId, Pageable pageable);
}
