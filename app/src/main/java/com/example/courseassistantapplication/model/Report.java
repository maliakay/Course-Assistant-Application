package com.example.courseassistantapplication.model;

import java.util.Date;

public class Report {
    private String reportScope;
    private String courseName;
    private String alıcı;
    private String reportSubject;
    private String reportBody;
    private Date reportDate;

    public Report(String reportScope, String courseName, String alıcı, String reportSubject, String reportBody, Date reportDate) {
        this.reportScope = reportScope;
        this.courseName = courseName;
        this.alıcı = alıcı;
        this.reportSubject = reportSubject;
        this.reportBody = reportBody;
        this.reportDate = reportDate;
    }

    public Report() { }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportScope() {
        return reportScope;
    }

    public void setReportScope(String reportScope) {
        this.reportScope = reportScope;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getAlıcı() {
        return alıcı;
    }

    public void setAlıcı(String alıcı) {
        this.alıcı = alıcı;
    }

    public String getReportSubject() {
        return reportSubject;
    }

    public void setReportSubject(String reportSubject) {
        this.reportSubject = reportSubject;
    }

    public String getReportBody() {
        return reportBody;
    }

    public void setReportBody(String reportBody) {
        this.reportBody = reportBody;
    }
}
