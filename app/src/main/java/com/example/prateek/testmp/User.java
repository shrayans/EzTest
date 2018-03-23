package com.example.prateek.testmp;

public class User {

    public String email;
    public String Full_Name;
    public String UID;
    public String profileImage="";
    public String userType="";

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(com.example.prateek.testmp.User.class)
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getEmail() {
        return email;

    }

    public User(String email, String Full_Name, String UID, String userType) {
        this.email = email;
        this.Full_Name = Full_Name;
        this.UID=UID;
        this.userType=userType;
    }


}
