package com.ssafy.db.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.db.entity.QUser;
import com.ssafy.db.entity.UserRecord;
/**
 * UserRecord 모델 관련 디비 쿼리 생성을 위한 구현 정의.
 */
@Repository
public class UserRecordRepositorySupport{
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
//	Quser qUser = QUser.user;
	
	public Optional<UserRecord> findAll(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
