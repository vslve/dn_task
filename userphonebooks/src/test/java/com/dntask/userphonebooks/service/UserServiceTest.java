package com.dntask.userphonebooks.service;

import com.dntask.userphonebooks.entity.UserEntity;
import com.dntask.userphonebooks.exception.UserNotFoundException;
import com.dntask.userphonebooks.model.User;
import com.dntask.userphonebooks.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.dntask.userphonebooks.service.TestSource.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @ParameterizedTest
    @MethodSource("generateCorrectInUsers")
    void addUser(Long userId, UserEntity user) {
        when(userRepository.save(user)).thenReturn(correctOutUsers.get(userId));

        User addedUser = userService.addUser(user);

        assertEquals(user.getName(), addedUser.getName());
    }

    @Test
    void getUsers() {
        when(userRepository.findAll()).thenReturn(correctOutUsers.values());

        List<User> users = userService.getUsers();

        assertNotNull(users);
        assertEquals(correctOutUsers.values().size(), users.size());
    }

    @Test
    void updateUser() {
        when(userRepository.findById(correctUserId))
                .thenReturn(Optional.of(correctOutUsers.get(correctUserId)));
        when(userRepository.save(any(UserEntity.class))).thenReturn(updatedOutUser);

        User updatedUser = userService.updateUser(correctUserId, updatedInUser);

        assertNotNull(updatedUser);
        assertEquals(correctUserId, updatedUser.getId());
        assertEquals(updatedInUser.getName(), updatedUser.getName());
        checkIfUserNoFoundExceptionByGetById(incorrectUserId);
    }

    @Test
    void getUser() {
        when(userRepository.findById(correctUserId))
                .thenReturn(Optional.of(correctOutUsers.get(correctUserId)));

        User user = userService.getUser(correctUserId);

        assertNotNull(user);
        assertEquals(correctUserId, user.getId());
        checkIfUserNoFoundExceptionByGetById(incorrectUserId);
    }

    @Test
    void deleteUser() {
        when(userRepository.findById(correctUserId))
                .thenReturn(Optional.of(correctOutUsers.get(correctUserId)));

        User deletedUser = userService.deleteUser(correctUserId);

        assertNotNull(deletedUser);
        assertEquals(correctUserId, deletedUser.getId());
        checkIfUserNoFoundExceptionByGetById(incorrectUserId);
    }

    @ParameterizedTest
    @MethodSource("generateCorrectNames")
    void getUserByName(String nameSubstring) {
        when(userRepository.findByNameContainingIgnoreCase(nameSubstring)).thenReturn(
                correctOutUsers.values().stream()
                        .filter(user -> user.getName().contains(nameSubstring)).collect(Collectors.toList())
        );

        List<User> users = userService.getUserByName(nameSubstring);

        users.forEach(user -> assertTrue(user.getName().contains(nameSubstring)));
    }

    private static List<Arguments> generateCorrectInUsers() {
        return correctInUsers.entrySet().stream().map(
                entry -> Arguments.of(entry.getKey(), entry.getValue())
        ).collect(Collectors.toList());
    }

    private static List<Arguments> generateCorrectNames() {
        return correctUserNames.stream().map(Arguments::of).collect(Collectors.toList());
    }

    private void checkIfUserNoFoundExceptionByGetById(Long userId) {
        Throwable exception = assertThrows(UserNotFoundException.class, () -> userService.getUser(userId));
        assertEquals(TestSource.userNotFoundMessage, exception.getMessage());
    }


}