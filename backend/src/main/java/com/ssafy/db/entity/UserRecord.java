package com.ssafy.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * 유저 전적 모델
 *
 */
@Entity
@Getter
@Setter
public class UserRecord extends BaseEntity{
	
	Long point;
	
	int win;
	int draw;
	int lose;
}
