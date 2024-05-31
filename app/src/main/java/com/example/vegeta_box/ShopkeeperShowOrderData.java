package com.example.vegeta_box;

// ShopkeeperShowOrderData.java
public class ShopkeeperShowOrderData {
    private String image;
    private String title;
    private String price;
    private String totalItem;

    public ShopkeeperShowOrderData(String image, String title, String price, String totalItem) {
        this.image = image;
        this.title = title;
        this.price = price;
        this.totalItem = totalItem;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(String totalItem) {
        this.totalItem = totalItem;
    }
}
