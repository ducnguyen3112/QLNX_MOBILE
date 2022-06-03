package com.example.quanlynhapxuat.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class KhachHang implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("address")
    private String address;
    @SerializedName("email")
    private String email;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("deliveryDockets")
    List<DeliveryDocket> deliveryDockets;

    public KhachHang(int id, String fullName, String phoneNumber, String address, String email) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
    }

    public KhachHang(int id, String fullName, String phoneNumber, String address, String email, String avatar) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.avatar = avatar;
    }

    public KhachHang(String fullName, String phoneNumber, String address, String email) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
    }

    public KhachHang(String fullName, String phoneNumber, String address, String email, String avatar) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<DeliveryDocket> getDeliveryDockets() {
        return deliveryDockets;
    }

    public void setDeliveryDockets(List<DeliveryDocket> deliveryDockets) {
        this.deliveryDockets = deliveryDockets;
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", deliveryDockets=" + deliveryDockets +
                '}';
    }
}
