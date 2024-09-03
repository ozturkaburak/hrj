package com.ab.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AboutMeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AboutMe getAboutMeSample1() {
        return new AboutMe().id(1L).url("url1");
    }

    public static AboutMe getAboutMeSample2() {
        return new AboutMe().id(2L).url("url2");
    }

    public static AboutMe getAboutMeRandomSampleGenerator() {
        return new AboutMe().id(longCount.incrementAndGet()).url(UUID.randomUUID().toString());
    }
}
