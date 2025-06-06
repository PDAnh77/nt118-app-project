package com.nhom22.studentmanagement.data.model;

import com.google.gson.annotations.SerializedName;

public class AcademicTranscript {
    @SerializedName("_id")
    private String id;
    private String studentId;
    private String classId;
    private Grade grade;

    public  AcademicTranscript(String studentId, String classId) {
        this.studentId = studentId;
        this.classId = classId;
    }

    public AcademicTranscript(String studentId, String classId, Grade grade) {
        this.studentId = studentId;
        this.classId = classId;
        this.grade = grade;
    }

    public AcademicTranscript(AcademicTranscript other) {
        this.id = other.getId();
        this.studentId = other.getStudentId();
        this.classId = other.getClassId();
        this.grade = other.getGrade();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId() {
        this.studentId = studentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId() {
        this.classId = classId;
    }

    public Grade getGrade() {
        return grade;
    }

    public  void setGrade(Grade grade) {
        this.grade = grade;
    }
}
