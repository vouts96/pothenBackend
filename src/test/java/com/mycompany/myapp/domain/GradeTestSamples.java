package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GradeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Grade getGradeSample1() {
        return new Grade().id(1L).name("name1");
    }

    public static Grade getGradeSample2() {
        return new Grade().id(2L).name("name2");
    }

    public static Grade getGradeRandomSampleGenerator() {
        return new Grade().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
