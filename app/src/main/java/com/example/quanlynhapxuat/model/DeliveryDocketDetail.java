package com.example.quanlynhapxuat.model;

import java.io.Serializable;

public class DeliveryDocketDetail implements Serializable {

    private int id;
    private int quantity;
    private int price;
    private int deliveryDocketId;
    private int productId;

    public DeliveryDocketDetail() {
    }

    public DeliveryDocketDetail(int quantity, int price, int productId) {
        this.quantity = quantity;
        this.price = price;
        this.productId = productId;
    }
    public DeliveryDocketDetail(int quantity, int price, int productId,int deliveryDocketId) {
        this.quantity = quantity;
        this.price = price;
        this.productId = productId;
        this.deliveryDocketId=deliveryDocketId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDeliveryDocketId() {
        return deliveryDocketId;
    }

    public void setDeliveryDocketId(int deliveryDocketId) {
        this.deliveryDocketId = deliveryDocketId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "DeliveryDocketDetail{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", price=" + price +
                ", deliveryDocketId=" + deliveryDocketId +
                ", productId=" + productId +
                '}';
    }
}
