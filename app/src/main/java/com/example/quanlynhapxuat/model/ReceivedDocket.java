package com.example.quanlynhapxuat.model;

import java.util.ArrayList;

public class ReceivedDocket {
    private int id;
    private String createdAt;
    private int employeeId;
    private int status;
    private String supplierName;
    public ArrayList<ReceivedDocketDetail> receivedDocketDetails;

    public ReceivedDocket() {
    }

    public ReceivedDocket(int id, String created_at, int employee_id, int status, String supplier_name, ArrayList<ReceivedDocketDetail> receivedDocketDetails) {
        this.id = id;
        this.createdAt = created_at;
        this.employeeId = employee_id;
        this.status = status;
        this.supplierName = supplier_name;
        this.receivedDocketDetails = receivedDocketDetails;
    }

    public ReceivedDocket(int id, String createdAt, int employeeId, int status, String supplierName) {
        this.id = id;
        this.createdAt = createdAt;
        this.employeeId = employeeId;
        this.status = status;
        this.supplierName = supplierName;
    }

    public ReceivedDocket(String createdAt, int employeeId, int status, String supplierName) {
        this.createdAt = createdAt;
        this.employeeId = employeeId;
        this.status = status;
        this.supplierName = supplierName;
    }

    @Override
    public String toString() {
        return "ReceivedDocket{" +
                "id=" + id +
                ", createdAt='" + createdAt + '\'' +
                ", employeeId=" + employeeId +
                ", status=" + status +
                ", supplierName='" + supplierName + '\'' +
                ", receivedDocketDetails=" + receivedDocketDetails +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getEmployee_id() {
        return employeeId;
    }

    public void setEmployee_id(int employee_id) {
        this.employeeId = employee_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSupplier_name() {
        return supplierName;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplierName = supplier_name;
    }

    public ArrayList<ReceivedDocketDetail> getReceivedDocketDetails() {
        return receivedDocketDetails;
    }

    public void setReceivedDocketDetails(ArrayList<ReceivedDocketDetail> receivedDocketDetails) {
        this.receivedDocketDetails = receivedDocketDetails;
    }
}
