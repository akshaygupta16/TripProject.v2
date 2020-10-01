package com.example.tripprojectv2;

import  java.util.HashMap;
import java.util.Map;

public class User_local {

    private String firstname;
    private String lastname;
    private String profilePhotoURL;
    private String gender;
    private String uid;

    public User_local() {
        super();
    }

    public User_local(String firstname, String lastname, String profilePhotoURL, String gender, String uid) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.profilePhotoURL = profilePhotoURL;
        this.gender = gender;
        this.uid = uid;
    }

    public User_local(Map userMap) {
        this.setFirstName((String) userMap.get("Firstname"));
        this.setLastName((String) userMap.get("Lastname"));
        this.setGender((String) userMap.get("Gender"));
        this.setPhotoURL((String) userMap.get("Image"));
        this.setUid((String) userMap.get("Uid"));

    }

    public Map toUserMap() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("Firstname", this.firstname);
        objectMap.put("Lastname", this.lastname);
        objectMap.put("Image", this.profilePhotoURL);
        objectMap.put("Gender", this.gender);
        objectMap.put("Uid", this.uid);
        return objectMap;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public String getPhotoURL() {
        return profilePhotoURL;
    }

    public void setPhotoURL(String profilePhotoURL) {
        this.profilePhotoURL = profilePhotoURL;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

}