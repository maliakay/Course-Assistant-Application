package com.example.courseassistantapplication.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group {
    private String groupNumber;
    private ArrayList<String> instructorEmails;
    private ArrayList<String> studentEmails;

    public Group() {
    }

    public Group(ArrayList<String> instructorEmails) {
        this.instructorEmails = instructorEmails;
    }

    public Group(ArrayList<String> instructorEmails, ArrayList<String> studentEmails) {
        this.instructorEmails = instructorEmails;
        this.studentEmails = studentEmails;
    }

    public void addStudenttoGroup(String studentEmail){
        // 1. Check if instructors is null (initialize if it is)
        if (studentEmails == null) {
            studentEmails = new ArrayList<String>();
        }
        // 2. Add the new element to the ArrayList
        studentEmails.add(studentEmail);
    }

    public void addInstructortoGroup(String instructorEmail){
        // 1. Check if instructors is null (initialize if it is)
        if (instructorEmails == null) {
            instructorEmails = new ArrayList<String>();
        }
        // 2. Add the new element to the ArrayList
        instructorEmails.add(instructorEmail);
    }

    public ArrayList<String> getInstructorEmails() {
        return instructorEmails;
    }

    public void setInstructorEmails(ArrayList<String> instructorEmails) {
        this.instructorEmails = instructorEmails;
    }

    public ArrayList<String> getStudentEmails() {
        return studentEmails;
    }

    public void setStudentEmails(ArrayList<String> studentEmails) {
        this.studentEmails = studentEmails;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("groupNumber", groupNumber);
        result.put("instructorEmails", instructorEmails);
        result.put("studentEmails", studentEmails);

        return result;
    }
}
