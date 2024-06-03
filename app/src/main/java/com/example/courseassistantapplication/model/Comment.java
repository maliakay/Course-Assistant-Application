package com.example.courseassistantapplication.model;

public class Comment {
    private String commentId;
    private String announcementId;
    private String stdId;
    private String content;

    public Comment(String commentId, String announcementId, String stdId, String content) {
        this.commentId = commentId;
        this.announcementId = announcementId;
        this.stdId = stdId;
        this.content = content;
    }

    public Comment() {
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(String announcementId) {
        this.announcementId = announcementId;
    }

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
