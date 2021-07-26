package com.ssafy.api.request;

import lombok.Setter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

// 방 정보 수정 ([PATCH] /api/v1/room/{roomId}) 요청에 필요한 리퀘스트 바디 정의

@Getter
@Setter
@ApiModel("RoomUpdatePatchReq")
public class RoomUpdatePatchReq {
	@ApiModelProperty(name="참가인원 수", example="2")
	Integer participants;
	@ApiModelProperty(name="관전자 수", example="3")
	Integer observers;
	@ApiModelProperty(name="토론 발언 시간", example="2")
	Integer times;
	@ApiModelProperty(name="토론방 이름", example="물딱복숭아")
	String title;
	@ApiModelProperty(name="토론 주제 A", example="물복숭아")
	String TopicAgree;
	@ApiModelProperty(name="토론 주제 A", example="물복숭아")
	String TopicOpposite;

}