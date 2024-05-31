package com.example.vegeta_box;

public class CustomerShowAllShopsData {
    private String name;
    private String userName;

    public CustomerShowAllShopsData(String name,String userName) {
        this.name = name;
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
