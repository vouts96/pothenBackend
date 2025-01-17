package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.CommitteeAsserts.*;
import static com.mycompany.myapp.domain.CommitteeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommitteeMapperTest {

    private CommitteeMapper committeeMapper;

    @BeforeEach
    void setUp() {
        committeeMapper = new CommitteeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCommitteeSample1();
        var actual = committeeMapper.toEntity(committeeMapper.toDto(expected));
        assertCommitteeAllPropertiesEquals(expected, actual);
    }
}
