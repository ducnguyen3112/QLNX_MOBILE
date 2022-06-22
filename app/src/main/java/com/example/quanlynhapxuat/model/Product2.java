package com.example.quanlynhapxuat.model;

public class Product2 {
    private int id;
    private String name;
    private String createdAt;
    private int status;
    private String image;
    private float price;
    private int inventory;

    public Product2() {
    }

    public Product2(int id, String name, String createdAt, int status, String image, float price, int inventory) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.status = status;
        this.image = image;
        this.price = price;
        this.inventory = inventory;
    }

    public Product2(int id, String name, int status, String image, String createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.status = status;
        this.image = image;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    @Override
    public String toString() {
        return "Product2{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", status=" + status +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", inventory=" + inventory +
                '}';
    }
}
