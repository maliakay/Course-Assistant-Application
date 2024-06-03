package com.example.courseassistantapplication.model;

import java.util.Map;

public class Question {
    private String questionText;
    private Map<String, String> options;

    public Question() {
        // Boş yapıcı metod Firebase için gerekli
    }

    public Question(String questionText, Map<String, String> options) {
        this.questionText = questionText;
        this.options = options;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}