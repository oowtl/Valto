package com.ssafy.db.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;


/**
 * 컨퍼런스 모델 정의
 */
@Entity
@Getter
@Setter
public class Room extends BaseEntity{

	// owner_id
	@ManyToOne
	@JoinColumn(name = "ownerId") // fk
	private User userId;
	
	private Date localDateTime;
	
	@NotNull
	private String title; // 방 제목
	@NotNull
	private Integer participants; // 참가자 수
	@NotNull
	private Integer observers; // 관전자 수
	@NotNull
	private Integer times; // 발언 시간 제한
	@NotNull
	private String topicAgree; // 주제 1
	@NotNull
	private String topicOpposite; // 주제 2
	
	@NotNull
	private Boolean privateRoom; // 비밀번호 방 True 이면 비밀번호검사
	@JsonIgnore
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String roomPassword; // 방 비밀번호 (null 이면 없음)
	
}
