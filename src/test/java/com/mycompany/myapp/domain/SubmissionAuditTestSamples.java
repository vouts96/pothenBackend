package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SubmissionAuditTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SubmissionAudit getSubmissionAuditSample1() {
        return new SubmissionAudit()
            .id(1L)
            .afm("afm1")
            .adt("adt1")
            .lastName("lastName1")
            .firstName("firstName1")
            .fatherName("fatherName1")
            .organizationUnit("organizationUnit1")
            .newOrganizationUnit("newOrganizationUnit1")
            .protocolNumber("protocolNumber1")
            .modifiedBy("modifiedBy1")
            .changeType("changeType1");
    }

    public static SubmissionAudit getSubmissionAuditSample2() {
        return new SubmissionAudit()
            .id(2L)
            .afm("afm2")
            .adt("adt2")
            .lastName("lastName2")
            .firstName("firstName2")
            .fatherName("fatherName2")
            .organizationUnit("organizationUnit2")
            .newOrganizationUnit("newOrganizationUnit2")
            .protocolNumber("protocolNumber2")
            .modifiedBy("modifiedBy2")
            .changeType("changeType2");
    }

    public static SubmissionAudit getSubmissionAuditRandomSampleGenerator() {
        return new SubmissionAudit()
            .id(longCount.incrementAndGet())
            .afm(UUID.randomUUID().toString())
            .adt(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .fatherName(UUID.randomUUID().toString())
            .organizationUnit(UUID.randomUUID().toString())
            .newOrganizationUnit(UUID.randomUUID().toString())
            .protocolNumber(UUID.randomUUID().toString())
            .modifiedBy(UUID.randomUUID().toString())
            .changeType(UUID.randomUUID().toString());
    }
}
