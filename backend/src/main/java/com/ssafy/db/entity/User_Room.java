package com.ssafy.db.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;

// user_conference
@Entity
@Getter
@Setter
public class User_Conference extends BaseEntity {
	
	// conference
	@ManyToOne
	@JoinColumn(name = "conference_id")
	@NotNull
	private Conference confernce;
	
	// user id
	@ManyToOne
	@JoinColumn(name = "user_id")
	@NotNull
	private User user;
	
	Short action;
	
	@Temporal(TemporalType.TIMESTAMP)
	Date insertedTime;
}
