package com.example.courseassistantapplication.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Student {
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String password;
    private String studentId;
    private String contactEmailAddress;
    private String ongoingEducation;
    private String userId;
    private ArrayList<String> courses;

    public Student(String name, String surname, String email, String studentId) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.studentId = studentId;
    }

    public Student() {
    }

    public void addCourse(String courseId){
        // 1. Check if instructors is null (initialize if it is)
        if (courses == null) {
            courses = new ArrayList<String>();
        }
        // 2. Add the new element to the ArrayList
        courses.add(courseId);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getContactEmailAddress() {
        return contactEmailAddress;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setContactEmailAddress(String contactEmailAddress) {
        this.contactEmailAddress = contactEmailAddress;
    }

    public String getOngoingEducation() {
        return ongoingEducation;
    }

    public void setOngoingEducation(String ongoingEducation) {
        this.ongoingEducation = ongoingEducation;
    }

    public ArrayList<String> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<String> courses) {
        this.courses = courses;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> update = new HashMap<>();
        update.put("name", name);
        update.put("surname", surname);
        update.put("studentId", studentId);
        update.put("contactEmailAddress", contactEmailAddress);
        update.put("courses", courses);
        update.put("ongoingEducation", ongoingEducation);
        update.put("email", email);
        update.put("phone", phone);
        update.put("password", password);
        update.put("userId", userId);

        return update;
    }

}
