package com.ab.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContactTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Contact getContactSample1() {
        return new Contact().id(1L).secondaryEmail("secondaryEmail1").phoneNumber("phoneNumber1");
    }

    public static Contact getContactSample2() {
        return new Contact().id(2L).secondaryEmail("secondaryEmail2").phoneNumber("phoneNumber2");
    }

    public static Contact getContactRandomSampleGenerator() {
        return new Contact()
            .id(longCount.incrementAndGet())
            .secondaryEmail(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString());
    }
}
