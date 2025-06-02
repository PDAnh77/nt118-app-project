package com.nhom22.studentmanagement.data.model;

public class AcademicTranscripts {
    private String id;
    private String studentId;
    private String classId;
    private Grade grade;

    public AcademicTranscripts(String studentId, String classId, Grade grade) {
        this.studentId = studentId;
        this.classId = classId;
        this.grade = grade;
    }

    public AcademicTranscripts(AcademicTranscripts other) {
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
