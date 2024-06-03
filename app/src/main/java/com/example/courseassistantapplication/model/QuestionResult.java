package com.example.courseassistantapplication.model;

import java.util.List;
import java.util.Map;

public class QuestionResult {

    private String pollTitle;
    private List<String> questions;
    private List<Map<String, Integer>> answerCounts;

    public QuestionResult(String pollTitle, List<String> questions, List<Map<String, Integer>> answerCounts) {
        this.pollTitle = pollTitle;
        this.questions = questions;
        this.answerCounts = answerCounts;
    }

    public String getPollTitle() {
        return pollTitle;
    }

    public void setPollTitle(String pollTitle) {
        this.pollTitle = pollTitle;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public List<Map<String, Integer>> getAnswerCounts() {
        return answerCounts;
    }

    public void setAnswerCounts(List<Map<String, Integer>> answerCounts) {
        this.answerCounts = answerCounts;
    }
}
