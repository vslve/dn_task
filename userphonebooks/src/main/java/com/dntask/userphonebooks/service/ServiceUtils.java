package com.dntask.userphonebooks.service;

import com.dntask.userphonebooks.entity.UserEntity;
import com.dntask.userphonebooks.exception.RecordNotFoundException;
import com.dntask.userphonebooks.exception.UserNotFoundException;
import java.util.Optional;


class ServiceUtils {
    public static UserNotFoundException userNotFoundException = new UserNotFoundException("User not found");
    public static RecordNotFoundException recordNotFoundException = new RecordNotFoundException("Record not found");

    public static boolean isEntityNull(Object entity, RuntimeException e) {
        if (entity == null) {
            throw e;
        }
        return false;
    }

    public static boolean checkIfUserExist(Optional<UserEntity> user) {
        return !isEntityNull(user.orElse(null), userNotFoundException);
    }

}
