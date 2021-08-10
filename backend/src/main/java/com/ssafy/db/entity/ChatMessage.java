package com.ssafy.db.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
	
	// 메시지 타입 : 입장, 채팅
	public enum MessageType{
		JOIN, TALK
	}
	
	private MessageType type; // 메세지 타입
	private String roomId; // 방번호
	private String sender; // 메시지 보낸 사람
	private String message; // 메시지
}
