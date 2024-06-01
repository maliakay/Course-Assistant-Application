package com.example.courseassistantapplication.model;

import java.util.HashMap;
import java.util.Map;

public class Group {
    private String groupNumber;
    private String instructorEmail;

    public Group() {
    }

    public Group(String groupNumber, String instructorEmail) {
        this.groupNumber = groupNumber;
        this.instructorEmail = instructorEmail;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("groupNumber", groupNumber);
        result.put("instructorEmail", instructorEmail);
        return result;
    }
}
