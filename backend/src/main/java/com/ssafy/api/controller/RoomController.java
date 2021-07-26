package com.ssafy.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.request.RoomListGetReq;
import com.ssafy.api.request.RoomPostReq;
import com.ssafy.api.request.RoomUpdatePatchReq;
import com.ssafy.api.response.RoomListGetRes;
import com.ssafy.api.response.RoomOneErrorRes;
import com.ssafy.api.response.RoomOneGetRes;
import com.ssafy.api.response.RoomPostRes;
import com.ssafy.api.service.RoomService;
import com.ssafy.common.auth.SsafyUserDetails;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.Room;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

// Room 관련 API 요청 처리를 위한 컨트롤러 정의

@Api(value = "Room Api", tags = {"Room"})
@RestController
@RequestMapping("api/v1/room")
public class RoomController {
	
	@Autowired
	RoomService roomService;
	
	@PostMapping()
	@ApiOperation(value = " 방 생성 ", notes =" request 에서 받은 정보를 통해서 방을 생성한다. ")
	@ApiResponses({
		@ApiResponse(code = 201, message = " 방 생성 성공 ")
	})
	public ResponseEntity<RoomPostRes> createRoom(
			@ApiIgnore Authentication authentication,
			@RequestBody @ApiParam(value = "방 생성 정보", required = true) RoomPostReq roomCreateInfo) {
		
		/**
		 * 요청 헤더 액세스 토큰이 포함된 경우에만 실행되는 인증 처리이후, 리턴되는 인증 정보 객체(authentication) 통해서 요청한 유저 식별.
		 * 액세스 토큰이 없이 요청하는 경우, 403 에러({"error": "Forbidden", "message": "Access Denied"}) 발생.
		 */
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String validatedUserId = userDetails.getUsername();
				
		Room room = roomService.createRoom(roomCreateInfo, validatedUserId);
		
		return ResponseEntity.status(201).body(RoomPostRes.of(room));
	}
	
	@GetMapping()
	@ApiOperation(value = "전체 방목록 조회", notes = "방 전체 목록을 조회한다.")
	@ApiResponses({
		@ApiResponse(code = 200, message ="Success")
	})
	public ResponseEntity<RoomListGetRes> checkRoomList (
			// 파라미터를 string 으로 받아서 판단
			@RequestParam(value ="title", defaultValue ="null", required =false) @ApiParam( value = "제목으로 조회") String IN_title ,
			@RequestParam(value ="topic", defaultValue ="null", required =false) @ApiParam( value = "주제로 조회") String IN_topic) {
		
		RoomListGetReq roomListGetInfo = new RoomListGetReq();
		roomListGetInfo.setTitle(IN_title);
		roomListGetInfo.setTopic(IN_topic);
		
		List<Room> getRoomsList = roomService.checkRoomList(roomListGetInfo);
		
		return ResponseEntity.status(200).body(RoomListGetRes.of(getRoomsList));
		
	}
	
	@GetMapping("/{roomId}")
	@ApiOperation(value="상세 방 목록 조회(개별)", notes="방 id 를 통해서 방 상세 정보를 조회한다.")
	@ApiResponses({
		@ApiResponse(code=200, message="Success"),
		@ApiResponse(code=400, message="noExist roomId")
	})
	public ResponseEntity<RoomOneGetRes> checkOneRoom (
			@PathVariable("roomId") String roomId) {
			
		Room room = roomService.getRoomByRoomId(roomId);
		
		if (room == null) {
			return ResponseEntity.status(400).body(RoomOneErrorRes.of(400, "noExist roomId"));
		}
		return ResponseEntity.status(200).body(RoomOneGetRes.of(room));
	}
	
	@PatchMapping("/{roomId}")
	@ApiOperation(value="방 정보 변경", notes="방 id 를 통해서 방 정보를 변경한다.")
	@ApiResponses({
		@ApiResponse(code=200, message="Success")
	})
	public ResponseEntity<RoomOneGetRes> updateRoom(
			@ApiIgnore Authentication authentication,
			@RequestBody @ApiParam(value="방 수정 정보", required=true) RoomUpdatePatchReq roomUpdateInfo,
			@PathVariable("roomId") String roomId) {
	
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String userId = userDetails.getUsername();
		
		Room room = roomService.getRoomByRoomId(roomId);
		
		// room 이 없을 경우
		if (room == null) {
			return ResponseEntity.status(400).body(RoomOneErrorRes.of(400, "no Exist Room"));
		}
		
		// 방장과 유저 아이디가 다를 경우
		if (!userId.equals(room.getUserId().getUserId())) {
			return ResponseEntity.status(400).body(RoomOneErrorRes.of(403, "Bang Jang Na Wa"));
		}
		
		// 검사를 마치면 수정을 해보자.
		Room resRoom = roomService.updateRoom(roomUpdateInfo, roomId);
		return ResponseEntity.status(200).body(RoomOneGetRes.of(resRoom));
	}
	
	
	
}
