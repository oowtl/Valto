package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

// Room 입장 APi ([POST] /api/vi/room/{roomId}/admission) 요청에 필요한 리퀘스트 바디 정의

@Getter
@Setter
@ApiModel("UserRoomPostReq")
public class UserRoomPostReq {
	@ApiModelProperty(name = "비밀방 여부", example = "True")
	Boolean privateRoom;
	@ApiModelProperty(name = "비밀방 비밀번호", example = "password")
	String roomPassword;
	@ApiModelProperty(name = "user 팀 정보", example = "Agree")
	String userSide;
	@ApiModelProperty(name = "room Id", example = "1")
	String roomId;
	
	@Override
	public String toString() {
		return "UserRoomPostReq [privateRoom=" + privateRoom + ", roomPassword=" + roomPassword + ", userSide="
				+ userSide + ", roomId=" + roomId + "]";
	}
	
	
}
