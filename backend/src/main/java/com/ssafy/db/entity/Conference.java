package com.ssafy.db.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * 컨퍼런스 모델 정의
 */
@Entity
@Getter
@Setter
public class Conference extends BaseEntity{

	// owner_id
	@ManyToOne
	@JoinColumn(name = "owner_id") // fk
	private User user;
	
	// conference_category
	@ManyToOne
	@JoinColumn(name = "conference_category") // fk
	private Conference_category conferenceCategory; 
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp // 생성 시 시간을 저장해준다.
	private Date callStartTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date callEndTime;
	
	private String thumbnailUrl;
	private String title;
	private String description;
	private Boolean isActive;
	

}
