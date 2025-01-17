package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SubmissionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Submission getSubmissionSample1() {
        return new Submission()
            .id(1L)
            .afm("afm1")
            .adt("adt1")
            .lastName("lastName1")
            .firstName("firstName1")
            .fatherName("fatherName1")
            .organizationUnit("organizationUnit1")
            .newOrganizationUnit("newOrganizationUnit1")
            .protocolNumber("protocolNumber1");
    }

    public static Submission getSubmissionSample2() {
        return new Submission()
            .id(2L)
            .afm("afm2")
            .adt("adt2")
            .lastName("lastName2")
            .firstName("firstName2")
            .fatherName("fatherName2")
            .organizationUnit("organizationUnit2")
            .newOrganizationUnit("newOrganizationUnit2")
            .protocolNumber("protocolNumber2");
    }

    public static Submission getSubmissionRandomSampleGenerator() {
        return new Submission()
            .id(longCount.incrementAndGet())
            .afm(UUID.randomUUID().toString())
            .adt(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .fatherName(UUID.randomUUID().toString())
            .organizationUnit(UUID.randomUUID().toString())
            .newOrganizationUnit(UUID.randomUUID().toString())
            .protocolNumber(UUID.randomUUID().toString());
    }
}
