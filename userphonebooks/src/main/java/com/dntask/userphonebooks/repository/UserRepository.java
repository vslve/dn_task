package com.dntask.userphonebooks.repository;

import com.dntask.userphonebooks.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    List<UserEntity> findByNameContainingIgnoreCase(String name);
}
