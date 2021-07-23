package com.ssafy.db.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

// Conference history
@Entity
@Getter
@Setter
public class Room_History extends BaseEntity{
	
	// room id
	@ManyToOne
	@JoinColumn(name = "roomId") // fk
	private Room roomId;
	
	// user id
	@ManyToOne
	@JoinColumn(name = "userId")
	private User userId;
	
	//action
	Short action;
		
	//inserted time
	@Temporal(TemporalType.TIMESTAMP)
	Date insertedTime;
	
}