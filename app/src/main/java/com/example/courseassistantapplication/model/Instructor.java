package com.example.courseassistantapplication.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Instructor {
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String contactEmailAddress;
    private String password;
    private ArrayList<String> courses;
    private String userId;

    public Instructor(){ }

    public Instructor(String name, String surname, String phone, String email, ArrayList<String> courses) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.courses = courses;
    }

    public void addCourse(String courseId){
        // 1. Check if instructors is null (initialize if it is)
        if (courses == null) {
            courses = new ArrayList<String>();
        }
        // 2. Add the new element to the ArrayList
        courses.add(courseId);
    }

    public ArrayList<String> getCourses() {
        return courses;
    }
    public String getContactEmailAddress() {
        return contactEmailAddress;
    }

    public void setContactEmailAddress(String contactEmailAddress) {
        this.contactEmailAddress = contactEmailAddress;
    }
    public void setCourses(ArrayList<String> courses) {
        this.courses = courses;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> update = new HashMap<>();
        update.put("name", name);
        update.put("surname", surname);
        update.put("contactEmailAddress", contactEmailAddress);
        update.put("courses", courses);
        update.put("email", email);
        update.put("phone", phone);
        update.put("password", password);
        update.put("userId", userId);

        return update;
    }
}
