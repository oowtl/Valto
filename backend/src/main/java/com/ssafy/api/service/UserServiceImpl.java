package com.ssafy.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.UserModifyPatchReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserRecord;
import com.ssafy.db.repository.UserRecordRepository;
import com.ssafy.db.repository.UserRepository;
import com.ssafy.db.repository.UserRepositorySupport;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserRepositorySupport userRepositorySupport;
	
	@Autowired
	UserRecordRepository userRecordRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public User createUser(UserRegisterPostReq userRegisterInfo) {
		
		UserRecord userRecord = new UserRecord();
		userRecord.setWin(0);
		userRecord.setDraw(0);
		userRecord.setLose(0);
		
		userRecordRepository.save(userRecord);
		
		User user = new User();
		user.setUserId(userRegisterInfo.getUserId());
		// 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
		user.setPassword(passwordEncoder.encode(userRegisterInfo.getPassword()));
		user.setNickName(userRegisterInfo.getNickName());
		user.setName(userRegisterInfo.getName());
		user.setPoint(0);
		user.setUserRecord(userRecord);
		return userRepository.save(user);
	}

	@Override
	public User getUserByUserId(String userId) {
		// 디비에 유저 정보 조회 (userId 를 통한 조회).
		//System.out.println(userRepositorySupport.findUserByUserId(userId).get());
		User empty = new User();
		empty.setName("");
		
		return userRepositorySupport.findUserByUserId(userId).orElse(new User());
	}
	
	@Override
	public User getUserByNickName(String nickName) {
		// 닉네임 조회
		User empty = new User();
		empty.setNickName("");
		
		return userRepositorySupport.findUserByNickName(nickName).orElse(new User());
	}

	@Override
	public User modifyUser(UserModifyPatchReq userModifyInfo, String userId) {
		User user = userRepositorySupport.findUserByUserId(userId).get();
//		user.setUserId(userId);
		user.setName(userModifyInfo.getName());
		user.setNickName(userModifyInfo.getNickName());
		user.setPassword(userModifyInfo.getPassword());
		
		return userRepository.save(user);
	}

	@Override
	public int deleteUser(String userId) {
		// user 정보 삭제
		User user = userRepositorySupport.findUserByUserId(userId).get();
		userRepository.delete(user);
		//add
		// user conference history delete
		// user conference delete
		
		return 1;
	}
	
	@Override
	public List<User> getRankList() {
		// TODO Auto-generated method stub
		
		List<User> rankList = userRepository.findTop10ByOrderByPointDesc();
		
		return rankList;
	}
	
}
