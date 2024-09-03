package com.ab.hr.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UserLanguageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserLanguage getUserLanguageSample1() {
        return new UserLanguage().id(1L);
    }

    public static UserLanguage getUserLanguageSample2() {
        return new UserLanguage().id(2L);
    }

    public static UserLanguage getUserLanguageRandomSampleGenerator() {
        return new UserLanguage().id(longCount.incrementAndGet());
    }
}
