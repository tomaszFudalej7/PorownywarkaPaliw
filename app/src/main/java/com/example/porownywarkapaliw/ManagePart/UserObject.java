package com.example.porownywarkapaliw.ManagePart;

public class UserObject {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String town;
    private String phoneNumber;
    private String permission;
    private String creationData;
    private String blockStatus;

    public UserObject(int id, String name, String surname, String email, String town, String phoneNumber,
                      String permission, String creationData, String blockStatus) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.town = town;
        this.phoneNumber = phoneNumber;
        this.permission = permission;
        this.creationData = creationData;
        this.blockStatus = blockStatus;
    }
    public  UserObject(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getCreationData() {
        return creationData;
    }

    public void setCreationData(String creationData) {
        this.creationData = creationData;
    }

    public String getBlockStatus() {
        return blockStatus;
    }

    public void setBlockStatus(String blockStatus) {
        this.blockStatus = blockStatus;
    }
}
