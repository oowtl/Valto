package com.ssafy.db.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

// Conference history
@Entity
@Getter
@Setter
public class Conference_history extends BaseEntity{
	
	// conference id
	@ManyToOne
	@JoinColumn(name = "conference_id") // fk
	private Conference conference;
	
	// user id
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	//action
	
	
	//inserted time
	
}