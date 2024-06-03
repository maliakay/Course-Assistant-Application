package com.example.courseassistantapplication.model;

import java.util.Map;

public class Question {
    private String questionText;
    private Map<String, String> options;

    public Question() {
        // Default constructor required for calls to DataSnapshot.getValue(Question.class)
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
