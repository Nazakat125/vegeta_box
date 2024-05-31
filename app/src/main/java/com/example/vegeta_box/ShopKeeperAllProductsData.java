package com.example.vegeta_box;

public class ShopKeeperAllProductsData {
    private String image;
    private String name;
    private String price;
    private String detail;

    public ShopKeeperAllProductsData() {
        // Default constructor required for Firebase
    }

    public ShopKeeperAllProductsData(String image, String name, String price, String detail) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.detail = detail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
