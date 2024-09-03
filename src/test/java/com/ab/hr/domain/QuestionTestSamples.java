package com.ab.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QuestionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Question getQuestionSample1() {
        return new Question().id(1L).content("content1").options("options1").correctAnswer("correctAnswer1");
    }

    public static Question getQuestionSample2() {
        return new Question().id(2L).content("content2").options("options2").correctAnswer("correctAnswer2");
    }

    public static Question getQuestionRandomSampleGenerator() {
        return new Question()
            .id(longCount.incrementAndGet())
            .content(UUID.randomUUID().toString())
            .options(UUID.randomUUID().toString())
            .correctAnswer(UUID.randomUUID().toString());
    }
}
