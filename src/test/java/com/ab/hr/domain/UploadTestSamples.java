package com.ab.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UploadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Upload getUploadSample1() {
        return new Upload().id(1L).fileType("fileType1");
    }

    public static Upload getUploadSample2() {
        return new Upload().id(2L).fileType("fileType2");
    }

    public static Upload getUploadRandomSampleGenerator() {
        return new Upload().id(longCount.incrementAndGet()).fileType(UUID.randomUUID().toString());
    }
}
