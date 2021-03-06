package com.ssafy.api.response;

import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.Room;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.User_Room;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

// 방 생성 API ([POST] /api/v1/rooms) 요청에 대한 응답값 정의

@Getter
@Setter
@ApiModel("RoomPostResponse")
public class RoomPostRes extends BaseResponseBody {
	@ApiModelProperty(name = "roomId")
	Long roomId;
	@ApiModelProperty(name = "userId (ownerId)")
	String userId;
	@ApiModelProperty(name = "participants")
	Integer participants;

	@ApiModelProperty(name = "times")
	Integer times;

	@ApiModelProperty(name = "title")
	String title;
	@ApiModelProperty(name = "topicAgree")
	String topicAgree;
	@ApiModelProperty(name = "topicOpposite")
	String topicOpposite;


	 @ApiModelProperty(name = "userSide")
	 String userSide;


	public static RoomPostRes of(Room room) {
		RoomPostRes res = new RoomPostRes();
		
		res.setRoomId(room.getId());
		res.setUserId(room.getUserId().getUserId());
		res.setParticipants(room.getParticipants());
		res.setTimes(room.getTimes());
		res.setTitle(room.getTitle());
		res.setTopicAgree(room.getTopicAgree());
		res.setTopicOpposite(room.getTopicOpposite());
		
		return res;
	}

}
