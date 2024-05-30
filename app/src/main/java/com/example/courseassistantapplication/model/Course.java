package com.example.courseassistantapplication.model;

import java.util.ArrayList;
import java.util.Date;

public class Course {
    private String courseId;
    private String courseName;
    private String date;
    private int numberOfGroups;
    private String completionStatus;
    private int getNumberOfStudents;
    private String emailOfInstructor;
    private ArrayList<Group> courseGroups;


    public void addGrouptoCourse(Group group){
        // 1. Check if instructors is null (initialize if it is)
        if (courseGroups == null) {
            courseGroups = new ArrayList<Group>();
        }
        // 2. Add the new element to the ArrayList
        courseGroups.add(group);
    }

    public ArrayList<Group> getCourseGroups() {
        return courseGroups;
    }

    public void setCourseGroups(ArrayList<Group> courseGroups) {
        this.courseGroups = courseGroups;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }

    public void setNumberOfGroups(int numberOfGroups) {
        this.numberOfGroups = numberOfGroups;
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }

    public int getGetNumberOfStudents() {
        return getNumberOfStudents;
    }

    public void setGetNumberOfStudents(int getNumberOfStudents) {
        this.getNumberOfStudents = getNumberOfStudents;
    }

    public String getEmailOfInstructor() {
        return emailOfInstructor;
    }

    public void setEmailOfInstructor(String emailOfInstructor) {
        this.emailOfInstructor = emailOfInstructor;
    }
}
