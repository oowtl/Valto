package com.ssafy.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.RoomListGetReq;
import com.ssafy.api.request.RoomPostReq;
import com.ssafy.api.request.RoomUpdatePatchReq;
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
	
	@Override
	public List<Room> checkRoomList(RoomListGetReq roomListGetInfo) {
		// TODO Auto-generated method stub
		
		// roomListGetInfo 에 따라서 다르게 넣어준다.
		// only title, only topic, topic and title
				
		
		// topic and title
		if (!(roomListGetInfo.getTitle().equals("null")) && (!roomListGetInfo.getTopic().equals("null"))) {
			
			return roomRepository.findByTopicAgreeOrTopicOppositeOrTitle
					(roomListGetInfo.getTopic(), roomListGetInfo.getTopic(), roomListGetInfo.getTitle());
		}
		
		// only title
		if (!roomListGetInfo.getTitle().equals("null")) {
						
			return roomRepository.findByTitle(roomListGetInfo.getTitle());
		}
		
		// only topic
		if (!roomListGetInfo.getTopic().equals("null")) {		
			
			return roomRepository.findByTopicAgreeOrTopicOpposite(roomListGetInfo.getTopic(), roomListGetInfo.getTopic());
		}
		
		return roomRepository.findAll();
	}
	
	@Override
	public Room getRoomByRoomId(String roomId) {
		// TODO Auto-generated method stub		
		
		return roomRepository.findById(Integer.parseInt(roomId));
	}
	
	@Override
	public Room updateRoom(RoomUpdatePatchReq roomUpdateInfo, String roomId) {
		// TODO Auto-generated method stub
		
		Room room = roomRepository.findById(Integer.parseInt(roomId));
		
		room.setParticipants(roomUpdateInfo.getParticipants());
		room.setObservers(roomUpdateInfo.getObservers());
		room.setTimes(roomUpdateInfo.getTimes());
		room.setTitle(roomUpdateInfo.getTitle());
		room.setTopicAgree(roomUpdateInfo.getTopicAgree());
		room.setTopicOpposite(roomUpdateInfo.getTopicOpposite());
		
		return roomRepository.save(room);
	}
	
	@Override
	public String deleteRoom(String roomId) {
		// TODO Auto-generated method stub
		
		Room room = roomRepository.findById(Integer.parseInt(roomId));
		
		// room 자체 삭제전 다른 행위, 필요하다면 할 것!
		
		
		roomRepository.delete(room);
		
		return "Success";
	}
	
}
