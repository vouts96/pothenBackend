package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.SubmissionAuditAsserts.*;
import static com.mycompany.myapp.domain.SubmissionAuditTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubmissionAuditMapperTest {

    private SubmissionAuditMapper submissionAuditMapper;

    @BeforeEach
    void setUp() {
        submissionAuditMapper = new SubmissionAuditMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSubmissionAuditSample1();
        var actual = submissionAuditMapper.toEntity(submissionAuditMapper.toDto(expected));
        assertSubmissionAuditAllPropertiesEquals(expected, actual);
    }
}
