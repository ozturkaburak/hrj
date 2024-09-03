package com.ab.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SkillTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Skill getSkillSample1() {
        return new Skill().id(1L).name("name1");
    }

    public static Skill getSkillSample2() {
        return new Skill().id(2L).name("name2");
    }

    public static Skill getSkillRandomSampleGenerator() {
        return new Skill().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
