package com.dntask.userphonebooks.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "records")
public class RecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 5, max = 15)
    @Column(name = "phone_owner")
    private String phoneOwner;
    @NotNull
    @Pattern(regexp = "^\\d{11}$")
    @Column(name = "phone_number")
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public RecordEntity() {
    }

    public RecordEntity(String phoneOwner, String phoneNumber) {
        this.phoneOwner = phoneOwner;
        this.phoneNumber = phoneNumber;
    }

    public RecordEntity(Long id, String phoneOwner, String phoneNumber, UserEntity user) {
        this.id = id;
        this.phoneOwner = phoneOwner;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneOwner() {
        return phoneOwner;
    }

    public void setPhoneOwner(String phoneOwner) {
        this.phoneOwner = phoneOwner;
    }
}
