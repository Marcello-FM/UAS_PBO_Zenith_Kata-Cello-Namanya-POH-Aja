package com.zenith.frontend;

public class ResultData {

    private static ResultData instance;

    private int score = 0;

    private ResultData() {}

    public static ResultData getInstance() {
        if (instance == null) instance = new ResultData();
        return instance;
    }

    public int getScore()             { return score; }
    public void setScore(int score)   { this.score = score; }
}
