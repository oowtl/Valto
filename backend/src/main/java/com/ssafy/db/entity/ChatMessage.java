package com.ssafy.db.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatMessage {
	
	public enum MessageType {
	    JOIN, CHAT
	}
	
    private Integer roomId;	//방번호
    private String userId;
    private String message;
    private String nickName;
    private MessageType type;
}