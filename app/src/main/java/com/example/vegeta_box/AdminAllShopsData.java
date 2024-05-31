package com.example.vegeta_box;

public class AdminAllShopsData {
    private String name;
    private String email;
    private String province;
    private String district;
    private String address;

    public AdminAllShopsData() {
        // Default constructor required for Firebase
    }

    public AdminAllShopsData(String name, String email, String province, String district, String address) {
        this.name = name;
        this.email = email;
        this.province = province;
        this.district = district;
        this.address = address;
    }

    // Getters and setters
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
