package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

// Room 생성 API ([POST] /api/v1/room ) 요청에 필요한 리퀘스트 바디 정의

@Getter
@Setter
@ApiModel("RoomPostRequest")
public class RoomPostReq {
	@ApiModelProperty(name = "방 생성자", example = "test1")
	String userId;
	@ApiModelProperty(name = "토론방 제목", example = "내가 최고다")
	String title;
	@ApiModelProperty(name = "토론주제 1", example = "물복숭아")
	String topicAgree;
	@ApiModelProperty(name = "토론주제 2", example = "딱복숭아")
	String topicOpposite;
	
	
	@ApiModelProperty(name = "토론방 비밀번호방 여부", example = "True")
	Boolean privateRoom;
	@ApiModelProperty(name = "토론방 비밀번호", example = "123zxc")
	String roomPassword;

	@ApiModelProperty(name = "토론 참가자 수", example = "6")
	Integer participants;
	@ApiModelProperty(name = "토론 관전자 수", example = "3")
	Integer observers;
	@ApiModelProperty(name = "토론 발언 시간 제한", example = "15")
	Integer times;
	
	@ApiModelProperty(name = "방장의 토론 팀", example = "Agree")
	String userSide;
		
}	
