package com.dntask.userphonebooks.service;

import com.dntask.userphonebooks.entity.RecordEntity;
import com.dntask.userphonebooks.exception.RecordNotFoundException;
import com.dntask.userphonebooks.model.Record;
import com.dntask.userphonebooks.exception.UserNotFoundException;
import com.dntask.userphonebooks.repository.RecordRepository;
import com.dntask.userphonebooks.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecordServiceTest {
    final long correctUserId = 1;
    final long incorrectUserId = 0;
    final long incorrectRecordId = 0;


    @Autowired
    RecordService recordService;
    @Autowired
    UserRepository userRepository;

    @Test
    void addRecord() {
        final Map<RecordEntity, Long> correctUsersRecords= Map.of(
                new RecordEntity("abcde", "01234567891"), correctUserId,
                new RecordEntity("abcdefghijklmno", "12345678901"), correctUserId
        );
        final Map<RecordEntity, Long> incorrectUsersRecords= Map.of(
                new RecordEntity("abcde", "01234567891"), incorrectUserId
        );

        addRecord(correctUsersRecords, true);
        addRecord(incorrectUsersRecords, false);
    }

    @Test
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    void getUserRecords() {
        int correctUserRecordCount = userRepository.findById(correctUserId).get().getRecords().size();
        List<Record> userRecords = recordService.getUserRecords(correctUserId);

        assertNotNull(userRecords);
        assertEquals(correctUserRecordCount, userRecords.size());
        userRecords.forEach(record -> assertEquals(correctUserId, record.getUserId()));

        Throwable exception = assertThrows(
                UserNotFoundException.class, () -> recordService.getUserRecords(incorrectUserId)
        );
        assertEquals(TestUtils.userNotFoundMessage, exception.getMessage());
    }

    @Test
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    void getUserRecord() {
        final long correctRecordId = 1;
        Record record = recordService.getUserRecord(correctRecordId, correctUserId);

        assertNotNull(record);
        assertEquals(correctRecordId, record.getId());
        assertEquals(correctUserId, record.getUserId());

        checkIfExceptionByGetOrDeleteUserRecord(
                correctRecordId, incorrectUserId, UserNotFoundException.class, TestUtils.userNotFoundMessage
        );
        checkIfExceptionByGetOrDeleteUserRecord(
                incorrectRecordId, correctUserId, RecordNotFoundException.class, TestUtils.recordNotFoundMessage
        );
    }

    @Test
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    void updateUserRecord() {
        final long correctRecordId = 1;
        final RecordEntity updatedEntity = new RecordEntity("updated", "00000000000");
        Record updatedRecord = recordService.updateUserRecord(correctRecordId, correctUserId, updatedEntity);

        assertNotNull(updatedRecord);
        assertEquals(updatedEntity.getPhoneOwner(), updatedRecord.getPhoneOwner());
        assertEquals(updatedEntity.getPhoneNumber(), updatedRecord.getPhoneNumber());
        assertEquals(correctUserId, updatedRecord.getUserId());
        assertEquals(correctRecordId, updatedRecord.getId());

        checkIfExceptionByUpdateUserRecord(correctRecordId, incorrectUserId, updatedEntity, UserNotFoundException.class, TestUtils.userNotFoundMessage);
        checkIfExceptionByUpdateUserRecord(incorrectRecordId, correctUserId, updatedEntity, RecordNotFoundException.class, TestUtils.recordNotFoundMessage);

    }

    @Test
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    void deleteRecord() {
        final long correctRecordId = 1;
        Record deletedRecord = recordService.deleteUserRecord(correctRecordId, correctUserId);

        assertNotNull(deletedRecord);
        assertEquals(correctRecordId, deletedRecord.getId());
        assertEquals(correctUserId, deletedRecord.getUserId());
        checkIfExceptionByGetOrDeleteUserRecord(
                correctRecordId, incorrectUserId, UserNotFoundException.class, TestUtils.userNotFoundMessage
        );
        checkIfExceptionByGetOrDeleteUserRecord(
                incorrectRecordId, correctUserId, RecordNotFoundException.class, TestUtils.recordNotFoundMessage
        );
    }

    @Test
    void getRecordByPhoneNumber() {
        final long correctUserId = 1;
        final String correctPhoneNumber = "01234567891";
        List<Record> records = recordService.getUserRecordByPhoneNumber(correctPhoneNumber, correctUserId);

        records.forEach(record -> assertEquals(correctPhoneNumber, record.getPhoneNumber()));
        records.forEach(record -> assertEquals(correctUserId, record.getUserId()));
    }

    private void addRecord(Map<RecordEntity, Long> records, boolean correct) {
        for (Map.Entry<RecordEntity, Long> record : records.entrySet()) {
            RecordEntity recordEntity = record.getKey();
            long userId = record.getValue();
            if (correct) {
                Record addedRecord = recordService.addRecord(recordEntity,userId);

                assertEquals(recordEntity.getPhoneOwner(), addedRecord.getPhoneOwner());
                assertEquals(recordEntity.getPhoneNumber(), addedRecord.getPhoneNumber());
                assertEquals(userId, addedRecord.getUserId());
            } else {
                Throwable exception = assertThrows(
                        UserNotFoundException.class, () -> recordService.addRecord(recordEntity,userId)
                );
                assertEquals(TestUtils.userNotFoundMessage, exception.getMessage());
            }

        }
    }

    private void checkIfExceptionByGetOrDeleteUserRecord(
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