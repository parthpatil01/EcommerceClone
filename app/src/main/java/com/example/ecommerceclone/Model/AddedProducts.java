package com.example.ecommerceclone.Model;

public class AddedProducts {

    private String date,discount,pid,productDes,productName,productPrice,productSize,time,url,sizepid;
    private int quantity;

    AddedProducts(){}

    public AddedProducts(String date, String discount, String pid, String productDes, String productName, String productPrice, String productSize, String time, int quantity,String url,String sizepid) {
        this.date = date;
        this.discount = discount;
        this.pid = pid;
        this.productDes = productDes;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productSize = productSize;
        this.time = time;
        this.quantity = quantity;
        this.url=url;
        this.sizepid=sizepid;

    }

    public String getSizepid() {
        return sizepid;
    }

    public void setSizepid(String sizepid) {
        this.sizepid = sizepid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProductDes() {
        return productDes;
    }

    public void setProductDes(String productDes) {
        this.productDes = productDes;
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

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
