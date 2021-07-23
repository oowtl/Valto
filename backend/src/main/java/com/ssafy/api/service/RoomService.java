package com.ssafy.api.service;

import com.ssafy.api.request.RoomPostReq;
import com.ssafy.db.entity.Room;

// Room 관련 서비스 인터페이스 정의
public interface RoomService {
	Room createRoom(RoomPostReq roomPostRequestInfo);
}