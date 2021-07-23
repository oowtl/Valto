package com.ssafy.api.response;

import com.ssafy.db.entity.Room;
import com.ssafy.db.entity.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

// 방 생성 API ([POST] /api/v1/rooms) 요청에 대한 응답값 정의

@Getter
@Setter
@ApiModel("RoomPostResponse")
public class RoomPostRes {
	@ApiModelProperty(name = "userId (ownerId)")
	String userId;
	@ApiModelProperty(name = "participants")
	Integer participants;
	@ApiModelProperty(name = "observers")
	Integer observers;
	@ApiModelProperty(name = "times")
	Integer times;
	
	@ApiModelProperty(name = "title")
	String title;
	@ApiModelProperty(name = "topicAgree")
	String topicAgree;
	@ApiModelProperty(name = "topicOpposite")
	String topicOpposite;
	
	public static RoomPostRes of(Room room) {
		RoomPostRes res = new RoomPostRes();
		res.setUserId(room.getUserId().getUserId());
		res.setParticipants(room.getParticipants());
		res.setObservers(room.getObservers());
		res.setTimes(room.getTimes());
		res.setTitle(room.getTitle());
		res.setTopicAgree(room.getTopicAgree());
		res.setTopicOpposite(room.getTopicOpposite());
		return res;
	}
	
	
}
