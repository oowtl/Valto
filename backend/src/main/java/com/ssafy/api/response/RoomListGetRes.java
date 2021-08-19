package com.ssafy.api.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.Room;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("RoomListGetRes")
public class RoomListGetRes extends BaseResponseBody{
	
	@ApiModelProperty(name="RoomCount")
	Integer roomCount;
	
	@ApiModelProperty(name="content")
	ArrayList<HashMap> content;	
	
	public static RoomListGetRes of(ArrayList<HashMap> getRoomListAddCount) {
		
		RoomListGetRes roomList = new RoomListGetRes();
		
		ArrayList<HashMap> roomListArray = new ArrayList<HashMap>();
		
		
		for (HashMap room : getRoomListAddCount) {
			
			HashMap<String, Object> mapRoom = new HashMap<>();
			Room curRoom = (Room) room.get("room");
			
			mapRoom.put("roomId", curRoom.getId());
			mapRoom.put("userId", curRoom.getUserId().getNickName());
			mapRoom.put("participants", curRoom.getParticipants());
			mapRoom.put("times", curRoom.getTimes());
			mapRoom.put("title", curRoom.getTitle());
			mapRoom.put("topicAgree", curRoom.getTopicAgree());
			mapRoom.put("topicOpposite", curRoom.getTopicOpposite());
			
			mapRoom.put("userTotalCount", room.get("userCount"));
			mapRoom.put("userAgreeCount", room.get("userAgreeCount"));
			mapRoom.put("userOppositeCount", room.get("userOppositeCount"));
			
			mapRoom.put("start", curRoom.getStart());
			
			roomListArray.add(mapRoom);
		}
		
		roomList.setContent(roomListArray);
		roomList.setRoomCount(getRoomListAddCount.size());
		
		return roomList;
		
	}
}
