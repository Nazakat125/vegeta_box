// AdminShopsAddData.java
package com.example.vegeta_box;

public class AdminShopsAddData {
    private String key; // Key from Firebase database
    private String userName; // User name
    private String name; // Shop name
    private String email;
    private String province;
    private String district;
    private String address;

    public AdminShopsAddData() {
        // Default constructor required for Firebase
    }

    public AdminShopsAddData(String key, String userName, String name, String email, String province, String district, String address) {
        this.key = key;
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.province = province;
        this.district = district;
        this.address = address;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
