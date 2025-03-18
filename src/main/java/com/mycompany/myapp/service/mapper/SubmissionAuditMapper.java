package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Submission;
import com.mycompany.myapp.domain.SubmissionAudit;
import com.mycompany.myapp.service.dto.SubmissionAuditDTO;
import com.mycompany.myapp.service.dto.SubmissionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubmissionAudit} and its DTO {@link SubmissionAuditDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubmissionAuditMapper extends EntityMapper<SubmissionAuditDTO, SubmissionAudit> {
    @Mapping(target = "originalSubmission", source = "originalSubmission", qualifiedByName = "submissionId")
    SubmissionAuditDTO toDto(SubmissionAudit s);

    @Named("submissionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubmissionDTO toDtoSubmissionId(Submission submission);
}
