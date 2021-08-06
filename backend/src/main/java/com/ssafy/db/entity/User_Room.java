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
public class User_Room extends BaseEntity {
	
	// conference
	@ManyToOne
	@JoinColumn(name = "roomId")
	@NotNull
	private Room roomId;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	@NotNull
	private User userId;
	
	// user 의 팀
	// True = Agree
	// False = Opposite
	@NotNull
	Boolean userSide;
}
