package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

// Room 삭제 API ([DELETE] /api/v1/room/roomId)

@Getter
@Setter
@ApiModel("RoomDeleteRequest")
public class RoomDeleteReq {
	@ApiModelProperty(name="방 id", example ="1")
	Integer roomId;
	@ApiModelProperty(name="방 생성자의 Id", example ="nick")
	String userId;
	@ApiModelProperty(name="방 종료", example ="false")
	Boolean surrender;
}
