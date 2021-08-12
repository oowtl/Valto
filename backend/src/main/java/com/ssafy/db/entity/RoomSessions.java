package com.ssafy.db.entity;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

//@Entity
@Getter
@Setter
public class RoomSessions extends BaseEntity{
	
	private String sessionId;
	
	private Object session;
}
