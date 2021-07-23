package com.ssafy.db.entity;

import javax.persistence.Entity;

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
	String userId;
	int win;
	int draw;
	int lose;
}
