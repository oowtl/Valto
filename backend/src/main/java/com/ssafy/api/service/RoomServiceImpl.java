package com.ssafy.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.RoomPostReq;
import com.ssafy.db.entity.Room;
import com.ssafy.db.repository.RoomRepository;
import com.ssafy.db.repository.UserRepositorySupport;

// Room 관련 서비스 구현 정의

@Service("RoomService")
public class RoomServiceImpl implements RoomService {
	
	@Autowired
	RoomRepository roomRepository;
	
	@Autowired
	UserRepositorySupport userRepositorySupport;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public Room createRoom(RoomPostReq roomPostRequestInfo, String validatedUserId) {
		Room room = new Room();

		
		if (validatedUserId.equals(roomPostRequestInfo.getUserId())) {
			room.setUserId(userRepositorySupport.findUserByUserId(validatedUserId).orElse(null));
		}
		// userId 도 중복검사를 진행할 것이라서 없을리는... 없을 것이다.
		
		room.setParticipants(roomPostRequestInfo.getParticipants());
		room.setObservers(roomPostRequestInfo.getObservers());
		room.setTimes(roomPostRequestInfo.getTimes());
		room.setTitle(roomPostRequestInfo.getTitle());
		room.setTopicAgree(roomPostRequestInfo.getTopicAgree());
		room.setTopicOpposite(roomPostRequestInfo.getTopicOpposite());
		

		// room password 가 null 인지 아닌지에 대한 검사
		if (roomPostRequestInfo.getRoomPassword() == null) {
			room.setRoomPassword("null");
		} else {
			room.setRoomPassword(passwordEncoder.encode(roomPostRequestInfo.getRoomPassword()));
		}
		
		return roomRepository.save(room);
		
	}
	
}
