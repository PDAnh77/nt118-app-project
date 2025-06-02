package com.nhom22.studentmanagement.data.model;

public class Notification {
    private String id;
    private String studentId;
    private String classId;
    private String teacherId;
    private String message = null;
    private String type;
    private Integer status = 0;

    public Notification(String studentId, String classId, String teacherId, String message, String type, Integer status) {
        this.studentId = studentId;
        this.classId = classId;
        this.teacherId = teacherId;
        this.message = message;
        this.type = type;
        this.status = status;
    }

    public Notification(Notification other) {
        this.id = other.id;
        this.studentId = other.studentId;
        this.classId = other.classId;
        this.teacherId = other.teacherId;
        this.message = other.message;
        this.type = other.type;
        this.status = other.status;
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

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
