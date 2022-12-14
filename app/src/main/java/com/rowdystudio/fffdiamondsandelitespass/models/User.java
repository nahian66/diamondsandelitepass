package com.rowdystudio.fffdiamondsandelitespass.models;

public class User {
    private String name;
    private String number;
    private String email;
    private String points;
    private String isBLocked;
    private String referCode;
    private String userReferCode;

    public User() {
    }

    public User(String name, String number, String email, String points, String referCode, String isBLocked, String userReferCode) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.points = points;
        this.referCode = referCode;
        this.isBLocked = isBLocked;
        this.userReferCode = userReferCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getIsBLocked() {
        return isBLocked;
    }

    public void setIsBLocked(String isBLocked) {
        this.isBLocked = isBLocked;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public String getUserReferCode() {
        return userReferCode;
    }

    public void setUserReferCode(String userReferCode) {
        this.userReferCode = userReferCode;
    }
}
