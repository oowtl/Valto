package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

// Room List 반환 API ([GET] /api/v1/rooms) 요청에 필요한 parameter 정의

@Getter
@Setter
@ApiModel("RoomListGetReq")
public class RoomListGetReq {
	@ApiModelProperty(name = "타이틀", example = "타이틀 명")
	String title;
	@ApiModelProperty(name = "주제", example = "주제")
	String topic;
	@ApiModelProperty(name = "정렬", example = "createdAt")
	String sorting;
}
