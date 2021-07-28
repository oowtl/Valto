package com.ssafy.api.service;

import java.util.List;

import com.ssafy.db.entity.User_Room;

// UserRoom 관련 서비스 인터페이스 정의
public interface UserRoomService {
	User_Room enterUserRoom(String UserId, Long RoomId);
	User_Room getUserByUserId(String UserId);
	List<User_Room> getUserRoomByRoomId(String RoomId);
	String leaveRoom(User_Room userRoom);
	String deleteUserRoom(User_Room userRoom);
}