package com.ssafy.api.service;

import com.ssafy.db.entity.User;
import com.ssafy.db.entity.User_Room;

// UserRoom 관련 서비스 인터페이스 정의
public interface UserRoomService {
	User_Room enterUserRoom(String UserId, Integer RoomId);
	User getUserByUserId(String UserId);
	
	String leaveRoom(String UserId, Integer RoomId);
}