package com.ab.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserAssignmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserAssignment getUserAssignmentSample1() {
        return new UserAssignment().id(1L).orderOfQuestions("orderOfQuestions1").totalDurationInMins(1).accessUrl("accessUrl1");
    }

    public static UserAssignment getUserAssignmentSample2() {
        return new UserAssignment().id(2L).orderOfQuestions("orderOfQuestions2").totalDurationInMins(2).accessUrl("accessUrl2");
    }

    public static UserAssignment getUserAssignmentRandomSampleGenerator() {
        return new UserAssignment()
            .id(longCount.incrementAndGet())
            .orderOfQuestions(UUID.randomUUID().toString())
            .totalDurationInMins(intCount.incrementAndGet())
            .accessUrl(UUID.randomUUID().toString());
    }
}
