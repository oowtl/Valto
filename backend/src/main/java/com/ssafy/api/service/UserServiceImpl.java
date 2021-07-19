package com.ssafy.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.UserModifyPatchReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.db.entity.User;
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
	PasswordEncoder passwordEncoder;
	
	@Override
	public User createUser(UserRegisterPostReq userRegisterInfo) {
		User user = new User();

		user.setUserId(userRegisterInfo.getUser_id());
		// 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
		user.setPassword(passwordEncoder.encode(userRegisterInfo.getPassword()));
		user.setDepartment(userRegisterInfo.getDepartment());
		user.setName(userRegisterInfo.getName());
		user.setPosition(userRegisterInfo.getPosition());
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
	public User modifyUser(UserModifyPatchReq userModifyInfo, String userId) {
		User user = userRepositorySupport.findUserByUserId(userId).get();
//		user.setUserId(userId);
		user.setName(userModifyInfo.getName());
		user.setPosition(userModifyInfo.getPosition());
		user.setDepartment(userModifyInfo.getDepartment());
		
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
}
