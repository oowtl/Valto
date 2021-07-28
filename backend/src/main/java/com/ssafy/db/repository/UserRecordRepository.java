package com.ssafy.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.db.entity.UserRecord;

@Repository
public interface UserRecordRepository extends JpaRepository<UserRecord, Long > {

}
