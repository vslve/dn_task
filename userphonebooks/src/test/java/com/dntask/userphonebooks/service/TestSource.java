package com.dntask.userphonebooks.service;

import com.dntask.userphonebooks.entity.RecordEntity;
import com.dntask.userphonebooks.entity.UserEntity;
import com.dntask.userphonebooks.exception.RecordNotFoundException;
import com.dntask.userphonebooks.exception.UserNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TestSource {
    final static String userNotFoundMessage = "User not found";
    final static String recordNotFoundMessage = "Record not found";
    final static UserNotFoundException  userNotFoundException = new UserNotFoundException(userNotFoundMessage);
    final static RecordNotFoundException recordNotFoundException = new RecordNotFoundException(recordNotFoundMessage);

    final static long correctUserId = 1;
    final static long incorrectUserId = 0;
    final static long correctRecordId = 1;
    final static long incorrectRecordId = 0;

    final static List<String> correctUserNames = List.of("abcdefghijklmno", "abc");

    final static Map<Long, UserEntity> correctInUsers = Map.of(
            correctRecordId, new UserEntity( "abcde"),
            correctUserId + 1, new UserEntity( "abcdefghijklmno")
    );
    final static Map<Long, UserEntity> correctOutUsers = Map.of(
            correctUserId, new UserEntity(
                    correctUserId,
                    "abcde",
                    new ArrayList<>()
            ),
            correctUserId + 1, new UserEntity(
                    correctUserId + 1,
                    "abcdefghijklmno",
                    new ArrayList<>()
            )
    );
    final static Map<Long, RecordEntity> correctInUserRecords = Map.of(
            correctRecordId, new RecordEntity("abcde", "01234567891"),
            correctRecordId + 1, new RecordEntity("abcdefghijklmno", "01234567891")
    );
    final static Map<Long, RecordEntity> correctOutUserRecords = Map.of(
            correctRecordId, new RecordEntity(
                    correctRecordId,
                    "abcde",
                    "01234567891",
                    correctOutUsers.get(correctUserId)
            ),
            correctRecordId + 1, new RecordEntity(
                    correctRecordId + 1,
                    "abcdefghijklmno",
                    "01234567891",
                    correctOutUsers.get(correctUserId)
            )
    );

    final static UserEntity updatedInUser = new UserEntity("updated");
    final static UserEntity updatedOutUser = new UserEntity(
            correctUserId,
            updatedInUser.getName(),
            new ArrayList<>()
    );
    final static RecordEntity updatedInRecord = new RecordEntity("updated", "00000000000");
    final  static RecordEntity updatedOutRecord = new RecordEntity(
            correctRecordId,
            updatedInRecord.getPhoneOwner(),
            updatedInRecord.getPhoneNumber(),
            correctOutUsers.get(correctUserId)
    );
}
