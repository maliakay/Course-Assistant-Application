package com.example.courseassistantapplication.model;

import java.util.Map;

public class QuestionResult {

    private String pollTitle;
    private String question;
    private Map<String, Integer> answerCounts;

    public QuestionResult(String pollTitle, String question, Map<String, Integer> answerCounts) {
        this.pollTitle = pollTitle;
        this.question = question;
        this.answerCounts = answerCounts;
    }

    public String getPollTitle() {
        return pollTitle;
    }

    public void setPollTitle(String pollTitle) {
        this.pollTitle = pollTitle;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, Integer> getAnswerCounts() {
        return answerCounts;
    }

    public void setAnswerCounts(Map<String, Integer> answerCounts) {
        this.answerCounts = answerCounts;
    }
}
