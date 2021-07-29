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
	List<HashMap> users;
	
	public static RoomOneGetRes of(Room room, List<User_Room> userRoomList) {
		
		RoomOneGetRes roomInfo = new RoomOneGetRes();
		
		roomInfo.setRoomId(room.getId());
		roomInfo.setParticipants(room.getParticipants());
		roomInfo.setTimes(room.getTimes());
		roomInfo.setUserId(room.getUserId().getUserId());
		roomInfo.setTitle(room.getTitle());
		roomInfo.setTopicAgree(room.getTopicAgree());
		roomInfo.setTopicOpposite(room.getTopicOpposite());
		
		// users 를 어떻게 넣어야 할지 모르겠다...
		
		List<HashMap>userList = new ArrayList<HashMap>();
		
		for (User_Room userRoom: userRoomList) {
			HashMap<String, String> user = new HashMap<>();
			user.put("userId", userRoom.getUserId().getUserId());
			user.put("name", userRoom.getUserId().getName());
			user.put("nickName", userRoom.getUserId().getNickName());
			
			userList.add(user);
		}
			
		roomInfo.setUsers(userList);
		
		return roomInfo;
	}
}