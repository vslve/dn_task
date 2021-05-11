package com.dntask.userphonebooks.service;

import com.dntask.userphonebooks.entity.UserEntity;
import com.dntask.userphonebooks.model.User;
import com.dntask.userphonebooks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false).
                map(User::toModel).
                collect(Collectors.toList());
    }

    public User getUser(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        ServiceUtils.isEntityNull(user, ServiceUtils.userNotFoundException);
        return User.toModel(user);
    }

    public UserEntity getEntity(Long id) {
        UserEntity entity =  userRepository.findById(id).orElse(null);
        ServiceUtils.isEntityNull(entity, ServiceUtils.userNotFoundException);
        return entity;
    }

    public User addUser(UserEntity user) {
        if (user.getRecords() == null) {
            user.setRecords(new ArrayList<>());
        }
        return User.toModel(userRepository.save(user));
    }

    public User updateUser(Long id, UserEntity updatedUser) {
        UserEntity userToUpdate = getEntity(id);
        userToUpdate.setName(updatedUser.getName());
        userRepository.save(userToUpdate);
        return User.toModel(userToUpdate);
    }

    public User deleteUser(Long id) {
        User user = getUser(id);
        userRepository.deleteById(id);
        return user;
    }

    public List<User> getUserByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name).
                stream().
                map(User::toModel).
                collect(Collectors.toList());
    }
}
