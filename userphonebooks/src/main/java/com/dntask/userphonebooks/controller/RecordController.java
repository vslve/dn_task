package com.dntask.userphonebooks.controller;

import com.dntask.userphonebooks.entity.RecordEntity;
import com.dntask.userphonebooks.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/users/{userId}/records")
public class RecordController {
    @Autowired
    RecordService recordService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserRecord(@PathVariable Long id, @PathVariable Long userId) {
        return ResponseEntity.ok().body(recordService.getUserRecord(id, userId));
    }

    @GetMapping
    public ResponseEntity<?> getUserRecords(@PathVariable Long userId) {
        return ResponseEntity.ok().body(recordService.getUserRecords(userId));
    }

    @GetMapping(params = {"phone"})
    public ResponseEntity<?> getRecordByPhoneNumber(
            @RequestParam @Pattern (regexp = "^\\d{11}$") String phone,
            @PathVariable Long userId
    ) {
        System.out.println(phone);
        return ResponseEntity.ok().body(recordService.getUserRecordByPhoneNumber(phone, userId));
    }

    @PostMapping
    public ResponseEntity<?> addRecord(@Valid @RequestBody RecordEntity record, @PathVariable Long userId) {
        return ResponseEntity.ok().body(recordService.addRecord(record, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserRecord(
            @Valid @RequestBody RecordEntity record,
            @PathVariable Long id,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok().body(recordService.updateUserRecord(id, userId, record));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserRecord(@PathVariable Long id, @PathVariable Long userId) {
        return ResponseEntity.ok().body(recordService.deleteUserRecord(id, userId));
    }
}
