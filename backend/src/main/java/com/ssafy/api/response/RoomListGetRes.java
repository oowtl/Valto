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
	List<Room> content;
	
	public static RoomListGetRes of(List<Room> getRoomsList) {
		
		ArrayList roomList = new ArrayList<>();
		
		for (Room room : getRoomsList) {
			HashMap<String, ?> mapRoom = new HashMap<>();
			mapRoom.put("userId", room.getUserId());
			mapRoom.put("participants", room.getParticipants());		
			
		}
		
		
	}
	
	
	
}
