package com.zenith.frontend;

import java.util.Arrays;

public class ResultData {

    private static ResultData instance;

    private int score = 0;
    private int[] answers = new int[5];

    private ResultData() {}

    public static ResultData getInstance() {
        if (instance == null) instance = new ResultData();
        return instance;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int[] getAnswers() {
        return Arrays.copyOf(answers, answers.length);
    }

    public void setAnswers(int[] answers) {
        this.answers = Arrays.copyOf(answers, answers.length);
    }
}
