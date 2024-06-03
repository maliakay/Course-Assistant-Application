package com.example.courseassistantapplication.model;

import java.util.List;

public class Announcement {
    private String courseID;
    private String instructorEmail;
    private String content;
    private String announcementId;
    private List<Comment> comment;
    private boolean alert;


    public Announcement(String courseID, String instructorEmail, String content, String announcementId, List<Comment> comment, boolean alert) {
        this.courseID = courseID;
        this.instructorEmail = instructorEmail;
        this.content = content;
        this.announcementId = announcementId;
        this.comment = comment;
        this.alert = alert;
    }

    public Announcement() {
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(String announcementId) {
        this.announcementId = announcementId;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }


}
