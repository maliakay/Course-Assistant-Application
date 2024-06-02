package com.example.courseassistantapplication.model;

// QuestionResult.java
import java.util.Map;

public class QuestionResult {

    private String question;
    private Map<String, Integer> answerCounts;

    public QuestionResult(String question, Map<String, Integer> answerCounts) {
        this.question = question;
        this.answerCounts = answerCounts;
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

