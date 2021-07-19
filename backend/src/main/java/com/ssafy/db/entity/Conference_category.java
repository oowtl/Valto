package com.ssafy.db.entity;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * 컨퍼런스 카테고리 정의.
 */
@Entity
@Getter
@Setter
public class Conference_category extends BaseEntity{
	
	String name;
	
}

