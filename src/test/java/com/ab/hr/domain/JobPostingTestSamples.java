package com.ab.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class JobPostingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static JobPosting getJobPostingSample1() {
        return new JobPosting().id(1L).title("title1").description("description1").location("location1").department("department1");
    }

    public static JobPosting getJobPostingSample2() {
        return new JobPosting().id(2L).title("title2").description("description2").location("location2").department("department2");
    }

    public static JobPosting getJobPostingRandomSampleGenerator() {
        return new JobPosting()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .department(UUID.randomUUID().toString());
    }
}
