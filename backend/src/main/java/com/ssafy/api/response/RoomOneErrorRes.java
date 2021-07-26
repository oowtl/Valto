package com.ssafy.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("RoomOneErrorRes")
public class RoomOneErrorRes extends RoomOneGetRes{

	@ApiModelProperty(name="응답 메시지", example="실패")
	String message = null;
	@ApiModelProperty(name="응답 코드", example="400")
	Integer statusCode = null;
	
	@JsonIgnore
	Integer roomId;
	@JsonIgnore
	Integer prticipants;
	@JsonIgnore
	Integer observers;
	
	
	public RoomOneErrorRes(Integer statusCode, String message) {
		this.message = message;
		this.statusCode = statusCode;
	}
	
	public static RoomOneErrorRes of(Integer statusCode, String message) {
		RoomOneErrorRes body = new RoomOneErrorRes(statusCode, message);
		body.message = message;
		body.statusCode = statusCode;
		
		
		
		
		return body;
	}
}
