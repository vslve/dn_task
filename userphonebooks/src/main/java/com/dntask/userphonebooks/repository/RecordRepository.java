package com.dntask.userphonebooks.repository;

import com.dntask.userphonebooks.entity.RecordEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecordRepository extends CrudRepository<RecordEntity, Long> {
    List<RecordEntity> findByUser_Id(Long UserId);
    RecordEntity findByIdAndUser_Id(Long id, Long userId);
    List<RecordEntity> findByUser_IdAndPhoneNumber(Long UserId, String PhoneNumber);
}
