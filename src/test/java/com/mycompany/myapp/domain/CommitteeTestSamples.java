package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CommitteeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Committee getCommitteeSample1() {
        return new Committee().id(1L).name("name1");
    }

    public static Committee getCommitteeSample2() {
        return new Committee().id(2L).name("name2");
    }

    public static Committee getCommitteeRandomSampleGenerator() {
        return new Committee().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
