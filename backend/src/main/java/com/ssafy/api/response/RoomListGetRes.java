package com.ssafy.api.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ssafy.db.entity.Room;
import com.ssafy.db.entity.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("RoomListGetRes")
public class RoomListGetRes {
	
	@ApiModelProperty(name="content")
	ArrayList<HashMap> content;
	
	public static RoomListGetRes of(List<Room> getRoomsList) {
		
		RoomListGetRes roomList = new RoomListGetRes();
		
		ArrayList<HashMap> roomListArray = new ArrayList<HashMap>();
		
		for (Room room : getRoomsList) {
			HashMap<String, Object> mapRoom = new HashMap<>();
			mapRoom.put("roomId", room.getId());
			mapRoom.put("userId", room.getUserId().getNickName());
			mapRoom.put("participants", room.getParticipants());
			mapRoom.put("observers", room.getObservers());
			mapRoom.put("times", room.getTimes());
			mapRoom.put("title", room.getTitle());
			mapRoom.put("topicAgree", room.getTopicAgree());
			mapRoom.put("topicOpposite", room.getTopicOpposite());
			
			roomListArray.add(mapRoom);
		}
		
		roomList.setContent(roomListArray);
		
		return roomList;
		
	}
	
	
	
}
