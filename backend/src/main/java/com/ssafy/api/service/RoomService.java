package com.ssafy.api.service;

import java.util.ArrayList;
import java.util.List;

import com.ssafy.api.request.RoomListGetReq;
import com.ssafy.api.request.RoomPostReq;
import com.ssafy.api.request.RoomUpdatePatchReq;
import com.ssafy.db.entity.Room;

// Room 관련 서비스 인터페이스 정의
public interface RoomService {
	Room createRoom(RoomPostReq roomPostRequestInfo ,String validatedUserId);
	List<Room> checkRoomList(RoomListGetReq roomListGetInfo);
	Room getRoomByRoomId(String roomId);
	Room updateRoom(RoomUpdatePatchReq roomUpdateInfo, String roomId);
	String deleteRoom(String roomId);
}
