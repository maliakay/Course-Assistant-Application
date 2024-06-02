package com.example.courseassistantapplication.model;

// Poll.java
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Poll {
    private String pollId;
    private String creatorMail;
    private String course;
    private String title;
    private List<String> questions;

    // Default constructor required for calls to DataSnapshot.getValue(Poll.class)
    public Poll() {
    }

    public Poll(String pollId,String creatorMail, String course, String title, List<String> questions) {
        this.pollId = pollId;
        this.creatorMail = creatorMail;
        this.course = course;
        this.title = title;
        this.questions = questions;
    }

    @Exclude
    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getCreatorMail() {
        return creatorMail;
    }

    public void setCreatorMail(String creatorMail) {
        this.creatorMail = creatorMail;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("pollId",pollId);
        result.put("creatorMail",creatorMail);
        result.put("course",course);
        result.put("title",title);
        result.put("questions",questions);

        return result;
    }
}
