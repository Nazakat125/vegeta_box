package com.example.vegeta_box;

public class CustomerCartData {
    private String productImage;
    private String productName;
    private String productPrice;
    private String shopName;
    private int totalItem;
    private int sub;
    private int totalBill;

    public CustomerCartData(String productImage, String productName, String productPrice, String shopName,int totalItem,int sub , int totalBill) {
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.shopName = shopName;
        this.totalItem = totalItem;
        this.sub = sub;
        this.totalBill = totalBill;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }
    public int getSub() {
        return sub;
    }

    public void setSub(int sub) {
        this.sub = sub;
    }
    public int getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(int totalBill) {
        this.totalBill = totalBill;
    }
}
