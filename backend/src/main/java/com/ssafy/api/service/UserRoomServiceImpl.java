package com.ssafy.api.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.ssafy.db.entity.User;
import com.ssafy.db.entity.User_Room;
import com.ssafy.db.repository.RoomRepository;
import com.ssafy.db.repository.UserRepository;
import com.ssafy.db.repository.UserRoomRepository;

public class UserRoomServiceImpl implements UserRoomService {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoomRepository roomRepository;
	@Autowired
	UserRoomRepository userRoomRepository;
	
	@Override
	public User_Room enterUserRoom(String UserId, Integer RoomId) {
		// TODO Auto-generated method stub
		
		User_Room userRoom = new User_Room();
		
		userRoom.setRoomId(roomRepository.findById(RoomId));
		// 계정 유효성 검사는 createUserRoom 이 실행되기 전에 이미 한다.
		userRoom.setUserId(userRepository.findById(Integer.parseInt(UserId)).get());
		
		return userRoomRepository.save(userRoom);
	}
	
	@Override
	public User getUserByUserId(String UserId) {
		// TODO Auto-generated method stub
		
//		User user = userRepository.findById(Integer.parseInt(UserId)).get();
		
		return userRoomRepository.findByUserId(UserId).orElse(new User());
	}
	
	
	@Override
	public String leaveRoom(String UserId, Integer RoomId) {
		// TODO Auto-generated method stub
		
		
		
		
//		userRoomRepository.delete();
		
		
		return "deleteSucces";
	}
	
}
