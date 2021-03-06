package com.ssafy.api.service;

import java.util.ArrayList;
import java.util.HashMap;
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
	public User_Room enterUserRoom(String UserId, Long RoomId, String userSide) {
		
		User_Room userRoom = new User_Room();

		userRoom.setRoomId(roomRepository.findById(RoomId).get());
		// 계정 유효성 검사는 createUserRoom 이 실행되기 전에 이미 한다.
		userRoom.setUserId(userRepository.findByUserId(UserId).get());

		// userSide 의 기본값 agree
		// userSide F ||    F && T
		
		System.out.println("userSide : " + userSide);
		
		//if ((userSide == null) || (!(userSide.equals("agree")) && !(userSide.equals("opposite")))) {
		if ((userSide == null) || (!("agree".equals(userSide)) && !("opposite".equals(userSide)))) {
			userRoom.setUserSide("agree");
		} else {
//			System.out.println(userSide + "@@@@!!!!!");
			userRoom.setUserSide(userSide);
		}
		
		return userRoomRepository.save(userRoom);
	}

	@Override
	public Boolean checkLimitRoom(Room room, String userSide) {
		// TODO Auto-generated method stub
			
		Integer userSideCount = 0;
		String convertUserSide = "agree";
		
		// userSide 기본값 opposite
		if ((userSide == null) || (!(userSide.equals("agree")) && !(userSide.equals("opposite")))) {
			convertUserSide = "agree";
		} else {
			convertUserSide = "opposite";
		}
		
		List<User_Room> checkUserRoom = userRoomRepository.findByRoomIdAndUserSide(room, convertUserSide);
			
		// 입장가능 = true
		if (checkUserRoom.size() + 1 <= room.getParticipants()) {
			return true;
		}
		return false;
	}
	
	@Override
	public User_Room getUserByUserId(String UserId) {
		// TODO Auto-generated method stub

		List<User> getExistUser = userRepository.findAllByUserId(UserId);
		
		// 없을 때
		if (getExistUser.size() == 0) {
			return new User_Room();
		}
		// 있을 때
		return userRoomRepository.findByUserId(getExistUser.get(0)).orElseGet(() -> new User_Room());
	}

	@Override
	public List<User_Room> getUserRoomByRoomId(String RoomId) {
		// TODO Auto-generated method stub

		Room room = roomRepository.findById(Long.valueOf(RoomId)).get();

		return userRoomRepository.findAllByRoomId(room);
	}

	@Override
	public String leaveRoom(User_Room userRoom) {
		// TODO Auto-generated method stub

		userRoomRepository.delete(userRoom);

		return "Success";
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

	@Override
	public ArrayList<HashMap> getUserRoomTotalCount(List<Room> getRoomsList) {
		// TODO Auto-generated method stub
		ArrayList<HashMap> roomListArray = new ArrayList<HashMap>();

		for (Room room : getRoomsList) {
			HashMap<String, Object> mapRoom = new HashMap<>();
			mapRoom.put("room", room);

			List<User_Room> checkUserRoomUser = userRoomRepository.findAllByRoomId(room);
			// 총 유저 수
			mapRoom.put("userCount", checkUserRoomUser.size());
			// 찬성 측 유저 수
			mapRoom.put("userAgreeCount", userRoomRepository.findByRoomIdAndUserSide(room, "agree").size());
			// 반대 측 유저 수
			mapRoom.put("userOppositeCount", userRoomRepository.findByRoomIdAndUserSide(room, "opposite").size());
			// 관전 측 유저 수
//			mapRoom.put("userObserverCount", userRoomRepository.findByRoomIdAndUserSide(room, "observer").size());

			roomListArray.add(mapRoom);
		}

		return roomListArray;
	}

}
