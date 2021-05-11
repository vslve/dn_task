package com.dntask.userphonebooks.service;

import com.dntask.userphonebooks.entity.UserEntity;
import com.dntask.userphonebooks.model.User;
import com.dntask.userphonebooks.exception.UserNotFoundException;
import com.dntask.userphonebooks.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    private final long incorrectUserId = 0;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void addUser() {
        final UserEntity[] correctUserEntities = {
                new UserEntity("abcde"),
                new UserEntity("abcdefghijklmno")
        };

        for (UserEntity user : correctUserEntities) {
            User addedUser = userService.addUser(user);

            assertEquals(user.getName(), addedUser.getName());
        }
    }

    @Test
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    void getUsers() {
        final long correctUsersCount = userRepository.findAll().spliterator().estimateSize();
        List<User> users = userService.getUsers();

        assertNotNull(users);
        assertEquals(correctUsersCount, users.size());
    }

    @Test
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    void updateUser() {
        final long correctUserId = 1;
        UserEntity updatedEntity = new UserEntity("updated");
        User updatedUser = userService.updateUser(correctUserId, updatedEntity);

        assertNotNull(updatedUser);
        assertEquals(correctUserId, updatedUser.getId());
        assertEquals(updatedEntity.getName(), updatedUser.getName());
        checkUserNoFoundExceptionByGetById(incorrectUserId);
    }

    @Test
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    void getUser() {
        final long correctUserId = 1;
        User user = userService.getUser(correctUserId);

        assertNotNull(user);
        assertEquals(correctUserId, user.getId());
        checkUserNoFoundExceptionByGetById(incorrectUserId);
    }

    @Test
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    void deleteUser() {
        final long correctUserId = 1;
        User deletedUser = userService.deleteUser(correctUserId);

        assertNotNull(deletedUser);
        assertEquals(correctUserId, deletedUser.getId());
        checkUserNoFoundExceptionByGetById(incorrectUserId);
    }

    @Test
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    void getUserByName() {
        final String[] correctNames = {"abc", "ghijklm", "abcdefghijklmno"};

        for (String nameSubstring : correctNames) {
            List<User> users = userService.getUserByName(nameSubstring);

            users.forEach(user -> assertTrue(user.getName().contains(nameSubstring)));
        }
    }

    private void checkUserNoFoundExceptionByGetById(Long userId) {
        Throwable exception = assertThrows(UserNotFoundException.class, () -> userService.getUser(userId));
        assertEquals(TestUtils.userNotFoundMessage, exception.getMessage());
    }
}