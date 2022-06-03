package com.example.quanlynhapxuat.model;

public class ReceivedDocketDetail {
    private int id;
    private int receivedDocketId;
    private int productId;
    private int quantity;
    private int price;

    public ReceivedDocketDetail() {
    }

    public ReceivedDocketDetail(int id, int received_docket_id, int product_id, int quantity, int price) {
        this.id = id;
        this.receivedDocketId = received_docket_id;
        this.productId = product_id;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceivedDocketId() {
        return receivedDocketId;
    }

    public void setReceivedDocketId(int receivedDocketId) {
        this.receivedDocketId = receivedDocketId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int product_id) {
        this.productId = product_id;
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

    @Override
    public String toString() {
        return "ReceivedDocketDetail{" +
                "id=" + id +
                ", receivedDocketId=" + receivedDocketId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
