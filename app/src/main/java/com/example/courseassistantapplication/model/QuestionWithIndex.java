package com.example.courseassistantapplication.model;

public class QuestionWithIndex {
    private Question question;
    private String firebaseKey;

    public QuestionWithIndex(Question question, String firebaseKey) {
        this.question = question;
        this.firebaseKey = firebaseKey;
    }

    public Question getQuestion() {
        return question;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }
}
