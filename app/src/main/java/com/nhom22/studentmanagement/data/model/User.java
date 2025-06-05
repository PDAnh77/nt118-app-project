package com.nhom22.studentmanagement.data.model;

public class User {
    private String id;
    private String username;
    private String password;
    private String role = "student";
    private String fullname = null;
    private String email = null;
    private String birthday = null;
    private Integer academicYear = null;

    public User(String email, String fullname, String birthday, Integer academicYear) {
        this.email = email;
        this.fullname = fullname;
        this.birthday = birthday;
        this.academicYear = academicYear;
    }

    public User(User currentUser) {
        this.id = currentUser.getId();
        this.username = currentUser.getUsername();
        this.password = currentUser.getPassword();
        this.role = currentUser.getRole();
        this.fullname = currentUser.getFullname();
        this.birthday = currentUser.getBirthday();
        this.academicYear = currentUser.getAcademicYear();
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

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String brithday) { this.birthday = brithday; }

    public Integer getAcademicYear() { return academicYear; }
    public void setAcademicYear(Integer academicYear) { this.academicYear = academicYear; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
