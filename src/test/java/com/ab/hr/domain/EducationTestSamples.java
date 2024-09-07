package com.ab.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EducationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Education getEducationSample1() {
        return new Education().id(1L).name("name1").faculty("faculty1").degree("degree1").activities("activities1").clubs("clubs1");
    }

    public static Education getEducationSample2() {
        return new Education().id(2L).name("name2").faculty("faculty2").degree("degree2").activities("activities2").clubs("clubs2");
    }

    public static Education getEducationRandomSampleGenerator() {
        return new Education()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .faculty(UUID.randomUUID().toString())
            .degree(UUID.randomUUID().toString())
            .activities(UUID.randomUUID().toString())
            .clubs(UUID.randomUUID().toString());
    }
}
