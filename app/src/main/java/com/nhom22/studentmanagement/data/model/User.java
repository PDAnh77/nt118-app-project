package com.nhom22.studentmanagement.data.model;

public class User {
    private String id;
    private String username;
    private String password;
    private String role = "student";
    private String fullname = null;
    private String brithday = null;
    private Integer academicYear = null;

    public User(String username, String password, String role, String fullname, String brithday, Integer academicYear) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullname = fullname;
        this.brithday = brithday;
        this.academicYear = academicYear;
    }

    public User(User currentUser) {
        this.id = currentUser.get_id();
        this.username = currentUser.getUsername();
        this.password = currentUser.getPassword();
        this.role = currentUser.getRole();
        this.fullname = currentUser.getFullname();
        this.brithday = currentUser.getBrithday();
        this.academicYear = currentUser.getAcademicYear();
    }

    public User getUser() {
        return new User(this);
    }

    // Constructor cho đăng ký
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Constructor cho đăng nhập
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getBrithday() { return brithday; }
    public void setBrithday(String brithday) { this.brithday = brithday; }

    public Integer getAcademicYear() { return academicYear; }
    public void setAcademicYear(Integer academicYear) { this.academicYear = academicYear; }

    public String get_id() { return id; }
    public void set_id(String id) { this.id = id; }
}
