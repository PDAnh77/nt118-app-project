package com.nhom22.studentmanagement.data.model;

import java.util.ArrayList;
import java.util.List;

public class Class {
    private String id;
    private String className;
    private String classCode;
    private String teacherId;

    private String description = null;
    private List<String> students = null;

    public Class(String className, String classCode, String teacherId, String description, List<User> students) {
        this.className = className;
        this.classCode = classCode;
        this.teacherId = teacherId;
        this.description = description;
        this.students = new ArrayList<>();
    }

    public Class(Class otherClass) {
        this.id = otherClass.getId();
        this.className = otherClass.getClassName();
        this.classCode = otherClass.getClassCode();
        this.teacherId = otherClass.getTeacherId();
        this.description = otherClass.getDescription();
        this.students = otherClass.getStudents();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }

    public void addStudent(User student) {
        if(student != null && !students.contains(student.getId())) {
            students.add(student.getId());
        }
    }

    public void removeStudent(User student) {
        if (students != null) {
            students.remove(student);
        }
    }

}
