package com.Entities;

public class Admin extends User{

    private int adminID;
    private String adminPrivileges;
    private int userId; // Add userId field to resolve the issue
    

    public Admin(int adminID,int userid, String name, String email, String password, String adminPrivileges) {
        super(userid, name, email, password);
        this.adminID = adminID;
        this.adminPrivileges = adminPrivileges;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public String getAdminPrivileges() {
        return adminPrivileges;
    }

    public void setAdminPrivileges(String adminPrivileges) {
        this.adminPrivileges = adminPrivileges;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String toString() {
        return  "Admin adminID=" + adminID + ", admin Privileges" + adminPrivileges;
    }
}
