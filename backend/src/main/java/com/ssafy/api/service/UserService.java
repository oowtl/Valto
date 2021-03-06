package com.ssafy.api.service;

import java.util.List;

import com.ssafy.api.request.UserModifyPatchReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserRecord;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface UserService {
	User createUser(UserRegisterPostReq userRegisterInfo);
	User getUserByUserId(String userId);
	User getUserByNickName(String nickName);
	User modifyUser(UserModifyPatchReq userModifyInfo, String userId);
//	UserRecord getUserByUserRecord(String userId);
	int deleteUser(String userId);
	
	// rank
	List<User> getRankList();
	User addRankPoint(String userId);
}
