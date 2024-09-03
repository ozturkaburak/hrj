package com.ab.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EducationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Education getEducationSample1() {
        return new Education()
            .id(1L)
            .schoolName("schoolName1")
            .department("department1")
            .degree("degree1")
            .description("description1")
            .activities("activities1")
            .clubs("clubs1");
    }

    public static Education getEducationSample2() {
        return new Education()
            .id(2L)
            .schoolName("schoolName2")
            .department("department2")
            .degree("degree2")
            .description("description2")
            .activities("activities2")
            .clubs("clubs2");
    }

    public static Education getEducationRandomSampleGenerator() {
        return new Education()
            .id(longCount.incrementAndGet())
            .schoolName(UUID.randomUUID().toString())
            .department(UUID.randomUUID().toString())
            .degree(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .activities(UUID.randomUUID().toString())
            .clubs(UUID.randomUUID().toString());
    }
}
