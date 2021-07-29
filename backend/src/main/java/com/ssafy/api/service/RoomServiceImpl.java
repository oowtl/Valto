package com.ssafy.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.RoomPostReq;
import com.ssafy.db.entity.Room;
import com.ssafy.db.repository.RoomRepository;

// Room 관련 서비스 구현 정의

@Service("RoomService")
public class RoomServiceImpl implements RoomService {
	
	@Autowired
	RoomRepository roomRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public Room createRoom(RoomPostReq roomPostRequestInfo) {
		Room room = new Room();
		
		// room.setOwnerId(roomPostRequestInfo.getOwnerId());
		// 생각할 것 : 이거... 들어올 때 인트로 들어오던가 할건데 어떻게 해결하지?
		
		room.setParticipants(roomPostRequestInfo.getParticipants());
		room.setObservers(roomPostRequestInfo.getObservers());
		room.setTimes(roomPostRequestInfo.getTimes());
		room.setTitle(roomPostRequestInfo.getTitle());
		room.setTopicAgree(roomPostRequestInfo.getTopicAgree());
		room.setTopicOpposite(roomPostRequestInfo.getTopicOpposite());
		
		
		// room.setRoomPassword(passwordEncoder.encode(roomPostRequestInfo.getRoomPassword()));
		// room password 가 null 인지 아닌지에 대한 검사가 필요하다.
		
		return roomRepository.save(room);
		
	}
	
}
