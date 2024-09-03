package com.ab.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CertificateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Certificate getCertificateSample1() {
        return new Certificate().id(1L).name("name1").description("description1");
    }

    public static Certificate getCertificateSample2() {
        return new Certificate().id(2L).name("name2").description("description2");
    }

    public static Certificate getCertificateRandomSampleGenerator() {
        return new Certificate()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
