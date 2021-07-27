package com.ssafy.api.response;

import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.Room;
import com.ssafy.db.entity.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("RoomOneGetRes")
public class RoomOneGetRes extends BaseResponseBody{
	@ApiModelProperty(name="room id")
	Integer roomId;
	@ApiModelProperty(name="participants")
	Integer participants;
	@ApiModelProperty(name="observers")
	Integer times;
	@ApiModelProperty(name="user id")
	String userId;
	@ApiModelProperty(name="title")
	String title;
	@ApiModelProperty(name="topicAgree")
	String topicAgree;
	@ApiModelProperty(name="topicOpposite")
	String topicOpposite;
	@ApiModelProperty(name="Users")
	User users;
	
	public static RoomOneGetRes of(Room room) {
		
		RoomOneGetRes roomInfo = new RoomOneGetRes();
		
		roomInfo.setRoomId(room.getId());
		roomInfo.setParticipants(room.getParticipants());
		roomInfo.setTimes(room.getTimes());
		roomInfo.setUserId(room.getUserId().getUserId());
		roomInfo.setTitle(room.getTitle());
		roomInfo.setTopicAgree(room.getTopicAgree());
		roomInfo.setTopicOpposite(room.getTopicOpposite());
		
		// users 를 어떻게 넣어야 할지 모르겠다...
		
		
		return roomInfo;
	}
}
