package com.example.tripprojectv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Trip {

    String title,location,coverPhotoUrl,startDate,endDate,adminID;
    ArrayList<String> members;

    public Trip(){
        super();
    }

    public Trip(String title, String location, String coverPhotoUrl, String startDate, String endDate,String adminID) {
        this.title = title;
        this.location = location;
        this.coverPhotoUrl = coverPhotoUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.adminID=adminID;
    }

    public Map toUserMap() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("Title", this.title);
        objectMap.put("Location", this.location);
        objectMap.put("Image", this.coverPhotoUrl);
        objectMap.put("StartDate", this.startDate);
        objectMap.put("EndDate", this.endDate);
        objectMap.put("AdminID",this.adminID);
        objectMap.put("Members",this.members);
        return objectMap;
    }

    public Trip(Map tripMap){
        this.setTitle((String) tripMap.get("Title"));
        this.setLocation((String) tripMap.get("Location"));
        this.setCoverPhotoUrl((String) tripMap.get("Image"));
        this.setStartDate((String) tripMap.get("StartDate"));
        this.setEndDate((String) tripMap.get("EndDate"));
        this.setAdminID((String) tripMap.get("AdminID"));
        this.members = (ArrayList<String>) tripMap.get("Members");


    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCoverPhotoUrl() {
        return coverPhotoUrl;
    }

    public void setCoverPhotoUrl(String coverPhotoUrl) {
        this.coverPhotoUrl = coverPhotoUrl;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}


