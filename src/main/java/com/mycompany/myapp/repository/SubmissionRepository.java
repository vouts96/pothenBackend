package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Submission;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Submission entity.
 */
@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    @Query("select submission from Submission submission where submission.user.login = ?#{authentication.name}")
    List<Submission> findByUserIsCurrentUser();

    default Optional<Submission> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Submission> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Submission> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select submission from Submission submission left join fetch submission.position left join fetch submission.grade left join fetch submission.committeeName left join fetch submission.user",
        countQuery = "select count(submission) from Submission submission"
    )
    Page<Submission> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select submission from Submission submission left join fetch submission.position left join fetch submission.grade left join fetch submission.committeeName left join fetch submission.user"
    )
    List<Submission> findAllWithToOneRelationships();

    @Query(
        "select submission from Submission submission left join fetch submission.position left join fetch submission.grade left join fetch submission.committeeName left join fetch submission.user where submission.id =:id"
    )
    Optional<Submission> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select submission from Submission submission where submission.user.login = :login")
    Page<Submission> findAllByUserLogin(Pageable pageable, @Param("login") String login);
}
