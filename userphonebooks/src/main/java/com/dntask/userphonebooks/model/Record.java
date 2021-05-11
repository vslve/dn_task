package com.dntask.userphonebooks.model;

import com.dntask.userphonebooks.entity.RecordEntity;

public class Record {
    private Long id;
    private Long userId;
    private String phoneOwner;
    private String phoneNumber;

    public Record(Long id,Long userId, String phoneOwner, String phoneNumber) {
        this.id = id;
        this.userId = userId;
        this.phoneOwner = phoneOwner;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneOwner() {
        return phoneOwner;
    }

    public void setPhoneOwner(String phoneOwner) {
        this.phoneOwner = phoneOwner;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static Record toModel(RecordEntity entity) {
        return new Record(
                entity.getId(),
                entity.getUser().getId(),
                entity.getPhoneOwner(),
                entity.getPhoneNumber()
        );
    }
}
