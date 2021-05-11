package com.dntask.userphonebooks.model;

import com.dntask.userphonebooks.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class User {
    private Long id;
    private String name;
    private List<Record> records;

    public User(Long id, String name, List<Record> records) {
        this.id = id;
        this.name = name;
        this.records = records;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public static User toModel(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getRecords().stream().map(Record::toModel).collect(Collectors.toList())
        );
    }
}
