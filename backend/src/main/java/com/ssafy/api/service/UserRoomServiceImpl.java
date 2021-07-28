package com.ssafy.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.db.entity.Room;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.User_Room;
import com.ssafy.db.repository.RoomRepository;
import com.ssafy.db.repository.UserRepository;
import com.ssafy.db.repository.UserRoomRepository;

@Service("UserRoomService")
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
		userRoom.setUserId(userRepository.findByUserId(UserId).get());
		
		return userRoomRepository.save(userRoom);
	}
	
	@Override
	public User_Room getUserByUserId(String UserId) {
		// TODO Auto-generated method stub
		 
		User user = userRepository.findByUserId(UserId).get();
		
		return userRoomRepository.findByUserId(user).orElseGet(() -> new User_Room());
	}
	
	@Override
	public List<User_Room> getUserRoomByRoomId(String RoomId) {
		// TODO Auto-generated method stub
		
		Room room = roomRepository.findById(Integer.parseInt(RoomId));
		
		return userRoomRepository.findAllByRoomId(room);
	}
	
	@Override
	public String leaveRoom(User_Room userRoom) {
		// TODO Auto-generated method stub
		
		userRoomRepository.delete(userRoom);
		
		return	"Success";
	}
	
	@Override
	public String deleteUserRoom(User_Room userRoom) {
		// TODO Auto-generated method stub
		
		List<User_Room> deleteUserRoom = userRoomRepository.findAllByRoomId(userRoom.getRoomId());
		
		for (User_Room user : deleteUserRoom) {
			userRoomRepository.delete(user);
		}
		return "Success";
	}
	
}
