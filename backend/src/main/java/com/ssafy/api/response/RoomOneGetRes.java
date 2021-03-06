package com.ssafy.api.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.Room;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.User_Room;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("RoomOneGetRes")
public class RoomOneGetRes extends BaseResponseBody{
	@ApiModelProperty(name="room id")
	Long roomId;
	@ApiModelProperty(name="participants")
	Integer participants;
//	@ApiModelProperty(name="observers")
//	Integer observers;
	@ApiModelProperty(name="times")
	Integer times;
	@ApiModelProperty(name="user id")
	String userId;
	@ApiModelProperty(name="title")
	String title;
	@ApiModelProperty(name="topicAgree")
	String topicAgree;
	@ApiModelProperty(name="topicOpposite")
	String topicOpposite;
	@ApiModelProperty(name="privateRoom")
	Boolean privateRoom;
	@ApiModelProperty(name="start")
	Boolean start;
	
	// user side
	@ApiModelProperty(name="AgreeeUsers")
	List<HashMap> agreeUsers;
	@ApiModelProperty(name="OppositeUsers")
	List<HashMap> oppositeUsers;
//	@ApiModelProperty(name="ObserverUsers")
//	List<HashMap> observerUsers;
	@ApiModelProperty(name="TotalCountUsers")
	Integer totalCountUsers;
	
	public static RoomOneGetRes of(Room room, List<User_Room> userRoomList) {
		
		RoomOneGetRes roomInfo = new RoomOneGetRes();
		
		roomInfo.setRoomId(room.getId());
		roomInfo.setParticipants(room.getParticipants());
		roomInfo.setTimes(room.getTimes());
		roomInfo.setUserId(room.getUserId().getUserId());
		roomInfo.setTitle(room.getTitle());
		roomInfo.setTopicAgree(room.getTopicAgree());
		roomInfo.setTopicOpposite(room.getTopicOpposite());
		roomInfo.setStart(room.getStart());
		
		List<HashMap>agreeUserList = new ArrayList<HashMap>();
		List<HashMap>oppositeUserList = new ArrayList<HashMap>();
		
		// user 가 없는 방이라면
		if (userRoomList.size() == 0) {
			roomInfo.setAgreeUsers(agreeUserList);
			roomInfo.setOppositeUsers(oppositeUserList);
			return roomInfo;
		}
		
		for (User_Room userRoom: userRoomList) {
			HashMap<String, String> user = new HashMap<>();
			user.put("userId", userRoom.getUserId().getUserId());
			user.put("name", userRoom.getUserId().getName());
			user.put("nickName", userRoom.getUserId().getNickName());

			if (userRoom.getUserSide().equals("agree")) {
				agreeUserList.add(user);
			} else {
				oppositeUserList.add(user);
			}
		}
		roomInfo.setAgreeUsers(agreeUserList);
		roomInfo.setOppositeUsers(oppositeUserList);
		
		roomInfo.setTotalCountUsers(agreeUserList.size() + oppositeUserList.size());
		
		return roomInfo;
	}
}
