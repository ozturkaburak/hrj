package com.ab.hr.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UserAssignmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserAssignment getUserAssignmentSample1() {
        return new UserAssignment().id(1L);
    }

    public static UserAssignment getUserAssignmentSample2() {
        return new UserAssignment().id(2L);
    }

    public static UserAssignment getUserAssignmentRandomSampleGenerator() {
        return new UserAssignment().id(longCount.incrementAndGet());
    }
}
