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
	@ApiModelProperty(name="privateRoom")
	Boolean privateRoom;
	

	
	
	@ApiModelProperty(name="AgreeeUsers")
	List<HashMap> agrreUsers;
	@ApiModelProperty(name="OppositeUsers")
	List<HashMap> oppositeUsers;
	@ApiModelProperty(name="ObserverUsers")
    List<HashMap> observerUsers;
	
	public static RoomOneGetRes of(Room room, List<User_Room> userRoomList) {
		
		RoomOneGetRes roomInfo = new RoomOneGetRes();
		
		roomInfo.setRoomId(room.getId());
		roomInfo.setParticipants(room.getParticipants());
		roomInfo.setTimes(room.getTimes());
		roomInfo.setUserId(room.getUserId().getUserId());
		roomInfo.setTitle(room.getTitle());
		roomInfo.setTopicAgree(room.getTopicAgree());
		roomInfo.setTopicOpposite(room.getTopicOpposite());
		roomInfo.setPrivateRoom(room.getPrivateRoom());
		
		// users 를 어떻게 넣어야 할지 모르겠다...
		
		List<HashMap>agreeUserList = new ArrayList<HashMap>();
		List<HashMap>oppositeUserList = new ArrayList<HashMap>();
		List<HashMap>observerUserList = new ArrayList<HashMap>();
		
		for (User_Room userRoom: userRoomList) {
			HashMap<String, String> user = new HashMap<>();
			user.put("userId", userRoom.getUserId().getUserId());
			user.put("name", userRoom.getUserId().getName());
			user.put("nickName", userRoom.getUserId().getNickName());

			if (userRoom.getUserSide().equals("agree")) {
                agreeUserList.add(user);
            } else if (userRoom.getUserSide().equals("opposite")) {
                oppositeUserList.add(user);
            } else {
//                 위 두개가 아닌 다른 것은 전부 observer 로 추가된다.
                observerUserList.add(user);
            }
			
			
		}
			
		roomInfo.setAgrreUsers(agreeUserList);
		roomInfo.setOppositeUsers(oppositeUserList);
		roomInfo.setObserverUsers(observerUserList);
		return roomInfo;
	}
}
