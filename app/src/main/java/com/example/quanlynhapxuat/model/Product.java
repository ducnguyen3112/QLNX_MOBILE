package com.example.quanlynhapxuat.model;

public class Product {
    private int id;
    private String name;
    private int status;
    private String image;
    private String createdAt;

    public Product() {
    }

    public Product(int id, String name, int status, String image, String created_at) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.image = image;
        this.createdAt = created_at;
    }

    public Product(String name, int status, String image, String created_at) {
        this.name = name;
        this.status = status;
        this.image = image;
        this.createdAt = created_at;
    }

    public Product(String name, int status, String created_at) {
        this.name = name;
        this.status = status;
        this.createdAt = created_at;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", image='" + image + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
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

    public String getCreated_at() {
        return createdAt;
    }

    public void setCreated_at(String created_at) {
        this.createdAt = created_at;
    }
}
