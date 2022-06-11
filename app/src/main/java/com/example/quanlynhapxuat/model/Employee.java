package com.example.quanlynhapxuat.model;

import java.io.Serializable;
import java.util.List;

public class Employee implements Serializable {
    private int id;
    private String fullName;
    private String address;
    private String dateOfBirth;
    private String phoneNumber;
    private int role;
    private String password;
    private int status;
    private String avatar;

    List<DeliveryDocket> deliveryDockets;

    public Employee() {
    }

    public Employee(String fullName, String address, String dateOfBirth, String phoneNumber, int role, String password, int status, String avatar) {
        this.fullName = fullName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.password = password;
        this.status = status;
        this.avatar = avatar;
    }

    public Employee(int id, String fullName, String address, String dateOfBirth, String phoneNumber, int role, String password, int status, String avatar) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.password = password;
        this.status = status;
        this.avatar = avatar;
    }

    public Employee(String fullName, String address, String dateOfBirth, String phoneNumber, int role, String password, int status) {
        this.fullName = fullName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.password = password;
        this.status = status;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
        return "Employee{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", avatar='" + avatar + '\'' +
                ", deliveryDockets=" + deliveryDockets +
                '}';
    }
}
