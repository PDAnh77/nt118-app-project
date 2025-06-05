package com.nhom22.studentmanagement.data.model;

public class StudentIdRequest {
    private String studentId;

    public StudentIdRequest(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
