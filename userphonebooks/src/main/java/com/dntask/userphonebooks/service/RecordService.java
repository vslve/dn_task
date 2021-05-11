package com.dntask.userphonebooks.service;

import com.dntask.userphonebooks.model.Record;
import com.dntask.userphonebooks.repository.RecordRepository;
import com.dntask.userphonebooks.entity.RecordEntity;
import com.dntask.userphonebooks.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordService {
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    UserService userService;

    private Record getRecord(Long id) {
        RecordEntity record = recordRepository.findById(id).orElse(null);
        ServiceUtils.isEntityNull(record, ServiceUtils.recordNotFoundException);
        return Record.toModel(record);
    }

    public Record getUserRecord(Long id, Long userId) {
        userService.getUser(userId);
        RecordEntity record = recordRepository.findByIdAndUser_Id(id, userId);
        ServiceUtils.isEntityNull(record, ServiceUtils.recordNotFoundException);
        return Record.toModel(record);
    }

    public List<Record> getUserRecords(Long userId) {
        userService.getUser(userId);
        return recordRepository.findByUser_Id(userId).stream().map(Record::toModel).collect(Collectors.toList());
    }

    public Record addRecord(RecordEntity record, Long userId) {
        UserEntity user = userService.getEntity(userId);
        ServiceUtils.isEntityNull(user, ServiceUtils.userNotFoundException);
        record.setUser(user);
        return Record.toModel(recordRepository.save(record));
    }

    public Record updateUserRecord(Long id, Long userId, RecordEntity updatedRecord) {
        userService.getUser(userId);
        RecordEntity recordToUpdate = recordRepository.findById(id).orElse(null);
        ServiceUtils.isEntityNull(recordToUpdate, ServiceUtils.recordNotFoundException);
        recordToUpdate.setPhoneNumber(updatedRecord.getPhoneNumber());
        recordToUpdate.setPhoneOwner(updatedRecord.getPhoneOwner());
        return Record.toModel(recordRepository.save(recordToUpdate));
    }

    public Record deleteUserRecord(Long id, Long userId) {
        userService.getEntity(userId);
        Record record = getRecord(id);
        recordRepository.deleteById(id);
        return record;
    }

    public List<Record> getUserRecordByPhoneNumber(String phoneNumber, Long userId) {
        return recordRepository.findByUser_IdAndPhoneNumber(userId, phoneNumber)
                .stream()
                .map(Record::toModel)
                .collect(Collectors.toList());

    }
 }
