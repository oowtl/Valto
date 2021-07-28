package com.ssafy.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 유저 모델 정의.
 */
@Entity
@Getter
@Setter
public class User extends BaseEntity{
	String userId; //아이디
    String name; // 이름
    @Column(name="nickname")
    String nickName; // 별명
    int point; // 포인트
    
    
    @OneToOne
    @JoinColumn(name="record_id")
    UserRecord userRecord;
    
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password; // 비밀번호
    
    @Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", nickName=" + nickName + ", point=" + point
				+ ", password=" + password + "]";
	}

	

    
    
}
