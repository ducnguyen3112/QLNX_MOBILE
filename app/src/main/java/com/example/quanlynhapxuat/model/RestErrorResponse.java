package com.example.quanlynhapxuat.model;

import java.time.LocalDateTime;

public class RestErrorResponse {
    private String status;
    private String timeStamp;
    private String message;
    private String detail;
    public RestErrorResponse() {
        // TODO Auto-generated constructor stub
    }

    public RestErrorResponse(String message, String status, String timeStamp, String detail) {
        super();
        this.message = message;
        this.status = status;
        this.timeStamp = timeStamp;
        this.detail = detail;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}
