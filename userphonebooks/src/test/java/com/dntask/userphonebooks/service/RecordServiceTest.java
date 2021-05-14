package com.dntask.userphonebooks.service;

import com.dntask.userphonebooks.entity.RecordEntity;
import com.dntask.userphonebooks.exception.RecordNotFoundException;
import com.dntask.userphonebooks.model.Record;
import com.dntask.userphonebooks.exception.UserNotFoundException;
import com.dntask.userphonebooks.repository.RecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dntask.userphonebooks.service.TestSource.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
class RecordServiceTest {
    @Autowired
    RecordService recordService;
    @MockBean
    UserService userService;
    @MockBean
    RecordRepository recordRepository;


    @ParameterizedTest
    @MethodSource("generateCorrectInRecords")
    void addRecordCorrect(Long recordId, RecordEntity record) {
        when(userService.getEntity(correctUserId))
                .thenReturn(correctOutUsers.get(correctUserId));
        when(recordRepository.save(record))
                .thenReturn(correctOutUserRecords.get(recordId));

        Record addedRecord = recordService.addRecord(record, correctUserId);

        assertEquals(record.getPhoneOwner(), addedRecord.getPhoneOwner());
        assertEquals(record.getPhoneNumber(), addedRecord.getPhoneNumber());
        assertEquals(correctUserId, addedRecord.getUserId());

        Throwable exception = assertThrows(
                UserNotFoundException.class, () -> recordService.addRecord(record, incorrectUserId)
        );
        assertEquals(TestSource.userNotFoundMessage, exception.getMessage());
    }

    @Test
    void getUserRecords() {
        when(userService.getUser(incorrectUserId)).thenThrow(TestSource.userNotFoundException);
        when(recordRepository.findByUser_Id(correctUserId)).thenReturn(new ArrayList<>(correctOutUserRecords.values()));

        List<Record> userRecords = recordService.getUserRecords(correctUserId);

        assertNotNull(userRecords);
        assertEquals(correctOutUserRecords.size(), userRecords.size());
        userRecords.forEach(record -> assertEquals(correctUserId, record.getUserId()));

        Throwable exception = assertThrows(
                UserNotFoundException.class, () -> recordService.getUserRecords(incorrectUserId)
        );
        assertEquals(TestSource.userNotFoundMessage, exception.getMessage());
    }

    @Test
    void getUserRecord() {
        when(userService.getUser(incorrectUserId)).thenThrow(TestSource.userNotFoundException);
        when(recordRepository.findByIdAndUser_Id(correctRecordId, correctUserId))
                .thenReturn(correctOutUserRecords.get(correctRecordId));

        Record record = recordService.getUserRecord(correctRecordId, correctUserId);

        assertNotNull(record);
        assertEquals(correctRecordId, record.getId());
        assertEquals(correctUserId, record.getUserId());

        checkIfExceptionByGetUserRecord(
                correctRecordId, incorrectUserId, UserNotFoundException.class, TestSource.userNotFoundMessage
        );
        checkIfExceptionByGetUserRecord(
                incorrectRecordId, correctUserId, RecordNotFoundException.class, TestSource.recordNotFoundMessage
        );
    }

    @Test
    void updateUserRecord() {
        when(userService.getUser(incorrectUserId)).thenThrow(TestSource.userNotFoundException);
        when(recordRepository.findById(correctRecordId))
                .thenReturn(Optional.of(correctOutUserRecords.get(correctRecordId)));
        when(recordRepository.save(any(RecordEntity.class))).thenReturn(updatedOutRecord);

        Record updatedRecord = recordService.updateUserRecord(correctRecordId, correctUserId, updatedInRecord);

        assertNotNull(updatedRecord);
        assertEquals(updatedInRecord.getPhoneOwner(), updatedRecord.getPhoneOwner());
        assertEquals(updatedInRecord.getPhoneNumber(), updatedRecord.getPhoneNumber());
        assertEquals(correctUserId, updatedRecord.getUserId());
        assertEquals(correctRecordId, updatedRecord.getId());

        checkIfExceptionByUpdateUserRecord(
                correctRecordId, incorrectUserId, updatedInRecord, UserNotFoundException.class, TestSource.userNotFoundMessage
        );
        checkIfExceptionByUpdateUserRecord(
                incorrectRecordId, correctUserId, updatedInRecord, RecordNotFoundException.class, TestSource.recordNotFoundMessage
        );

    }

    @Test
    void deleteRecord() {
        when(userService.getEntity(incorrectUserId)).thenThrow(TestSource.userNotFoundException);
        when(recordRepository.findById(correctRecordId))
                .thenReturn(Optional.of(correctOutUserRecords.get(correctRecordId)));

        Record deletedRecord = recordService.deleteUserRecord(correctRecordId, correctUserId);

        assertNotNull(deletedRecord);
        assertEquals(correctRecordId, deletedRecord.getId());
        assertEquals(correctUserId, deletedRecord.getUserId());

        checkIfExceptionByDeleteUserRecord(
                correctRecordId, incorrectUserId, UserNotFoundException.class, TestSource.userNotFoundMessage
        );
        checkIfExceptionByDeleteUserRecord(
                incorrectRecordId, correctUserId, RecordNotFoundException.class, TestSource.recordNotFoundMessage
        );
    }

    @Test
    void getRecordByPhoneNumber() {
        final String correctPhoneNumber = "01234567891";
        when(recordRepository.findByUser_IdAndPhoneNumber(correctUserId, correctPhoneNumber))
                .thenReturn(new ArrayList<>(correctOutUserRecords.values()));

        List<Record> records = recordService.getUserRecordByPhoneNumber(correctPhoneNumber, correctUserId);

        records.forEach(record ->
                assertEquals(correctPhoneNumber, record.getPhoneNumber())
        );
        records.forEach(record ->
                assertEquals(correctUserId, record.getUserId())
        );
    }

    private static List<Arguments> generateCorrectInRecords() {
        return correctInUserRecords.entrySet().stream().map(
                entry -> Arguments.of(entry.getKey(), entry.getValue())
        ).collect(Collectors.toList());
    }

    private void checkIfExceptionByDeleteUserRecord(
            Long recordId,
            Long userId,
            Class<? extends Exception> expectedException,
            String exceptionMessage
    ) {
        Throwable exception = assertThrows(
                expectedException, () -> recordService.deleteUserRecord(recordId, userId)
        );
        assertEquals(exceptionMessage, exception.getMessage());
    }

    private void checkIfExceptionByGetUserRecord(
            Long recordId,
            Long userId,
            Class<? extends Exception> expectedException,
            String exceptionMessage
    ) {
        Throwable exception = assertThrows(
                expectedException, () -> recordService.getUserRecord(recordId, userId)
        );
        assertEquals(exceptionMessage, exception.getMessage());
    }

    private void checkIfExceptionByUpdateUserRecord(
            Long recordId,
            Long userId,
            RecordEntity updatedEntity,
            Class<? extends Exception> expectedException,
            String exceptionMessage
    ) {
        Throwable exception = assertThrows(
                expectedException, () -> recordService.updateUserRecord(recordId, userId, updatedEntity)
        );
        assertEquals(exceptionMessage, exception.getMessage());
    }
}