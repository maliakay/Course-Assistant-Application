package com.example.courseassistantapplication.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course {
    private String courseId;
    private String courseName;
    private String date;
    private int numberOfGroups;
    private String completionStatus;
    private int getNumberOfStudents;
    private String emailOfInstructor;
    private List<Group> courseGroups;

    public Course() {
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

    public List<Group> getCourseGroups() {
        return courseGroups;
    }

    public void setCourseGroups(List<Group> courseGroups) {
        this.courseGroups = courseGroups;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("courseId", courseId);
        result.put("courseName", courseName);
        result.put("date", date); // store date as timestamp
        result.put("numberOfGroups", numberOfGroups);
        result.put("completionStatus", completionStatus);
        result.put("getNumberOfStudents", getNumberOfStudents);
        result.put("emailOfInstructor", emailOfInstructor);

        ArrayList<Map<String, Object>> groupMaps = new ArrayList<>();
        if (courseGroups != null) {
            for (Group group : courseGroups) {
                groupMaps.add(group.toMap());
            }
        }
        result.put("courseGroups", groupMaps);

        return result;
    }
}
