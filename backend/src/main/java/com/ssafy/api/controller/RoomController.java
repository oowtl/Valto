package com.ssafy.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.request.RoomDeleteReq;
import com.ssafy.api.request.RoomListGetReq;
import com.ssafy.api.request.RoomPostReq;
import com.ssafy.api.request.RoomUpdatePatchReq;
import com.ssafy.api.request.UserRoomPostReq;
import com.ssafy.api.response.RoomListGetRes;
import com.ssafy.api.response.RoomOneGetRes;
import com.ssafy.api.response.RoomPostRes;
import com.ssafy.api.service.RoomService;
import com.ssafy.api.service.UserRoomService;
import com.ssafy.common.auth.SsafyUserDetails;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.Room;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.User_Room;

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
	
	@Autowired
	UserRoomService userRoomService;
	
	@PostMapping()
	@ApiOperation(value = " 방 생성 ", notes =" request 에서 받은 정보를 통해서 방을 생성한다. ")
	@ApiResponses({
		@ApiResponse(code = 201, message = " 방 생성 성공 "),
		@ApiResponse(code = 400, message = " no password private room"),
	})
	public ResponseEntity<? extends BaseResponseBody> createRoom(
			@ApiIgnore Authentication authentication,
			@RequestBody @ApiParam(value = "방 생성 정보", required = true) RoomPostReq roomCreateInfo) {
		
		/**
		 * 요청 헤더 액세스 토큰이 포함된 경우에만 실행되는 인증 처리이후, 리턴되는 인증 정보 객체(authentication) 통해서 요청한 유저 식별.
		 * 액세스 토큰이 없이 요청하는 경우, 403 에러({"error": "Forbidden", "message": "Access Denied"}) 발생.
		 */
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String validatedUserId = userDetails.getUsername();
		
		
		// 비밀번호 방을 설정했는데 비밀번호를 안쳤다면?
		if (roomCreateInfo.getPrivateRoom() && roomCreateInfo.getRoomPassword().isEmpty()) {
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "no password private room"));
		}
		
		// 방 생성하기
		Room room = roomService.createRoom(roomCreateInfo, validatedUserId);
		
		System.out.println(roomCreateInfo.getUserSide().equals("opposite"));
		
		// userSide 유효성 검사
		if (roomCreateInfo.getUserSide()==null || !(roomCreateInfo.getUserSide().equals("agree") || roomCreateInfo.getUserSide().equals("opposite") || roomCreateInfo.getUserSide().equals("observer"))) {
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "incorrect userSide"));
		}
		
		// 방에 접속한 유저 정보 / User_Room 생성하기
		User_Room userRoom = userRoomService.enterUserRoom(validatedUserId, room.getId(), roomCreateInfo.getUserSide());
		
		return ResponseEntity.status(201).body(RoomPostRes.of(room, userRoom));
	}
	
	@GetMapping()
	@ApiOperation(value = "전체 방목록 조회", notes = "방 전체 목록을 조회한다.")
	@ApiResponses({
		@ApiResponse(code = 200, message ="Success"),
		@ApiResponse(code = 204, message ="no Room")
	})
	public ResponseEntity<? extends BaseResponseBody> checkRoomList (
			// 파라미터를 string 으로 받아서 판단
			@PageableDefault(size = 20, page = 0) Pageable pageable,
			@RequestParam(value ="title", defaultValue ="null", required =false) @ApiParam( value = "제목으로 조회") String IN_title ,
			@RequestParam(value ="topic", defaultValue ="null", required =false) @ApiParam( value = "주제로 조회") String IN_topic,
			@RequestParam(value="sorting", defaultValue="null", required=false) @ApiParam( value = "정렬 파라미터") String IN_sort
			) {
	
		RoomListGetReq roomListGetInfo = new RoomListGetReq();
		roomListGetInfo.setTitle(IN_title);
		roomListGetInfo.setTopic(IN_topic);
		roomListGetInfo.setSorting(IN_sort);
		
		List<Room> getRoomsList = roomService.checkRoomList(roomListGetInfo, pageable);
		
		// room 이 없을 때
		if (getRoomsList.isEmpty()) {
			return ResponseEntity.status(204).body(BaseResponseBody.of(204, "no Room"));
		}
		
		// room list 에 각 방에 몇명이 있는지 추가해줌
		ArrayList<HashMap> getRoomListAddCount = userRoomService.getUserRoomTotalCount(getRoomsList);
		
		return ResponseEntity.status(200).body(RoomListGetRes.of(getRoomListAddCount));
		
	}
	
	@GetMapping("/{roomId}")
	@ApiOperation(value="상세 방 목록 조회(개별)", notes="방 id 를 통해서 방 상세 정보를 조회한다.")
	@ApiResponses({
		@ApiResponse(code=200, message="Success"),
		@ApiResponse(code=400, message="noExist roomId")
	})
	public ResponseEntity<? extends BaseResponseBody> checkOneRoom (
			@PathVariable("roomId") String roomId) {
			
		Room room = roomService.getRoomByRoomId(roomId);
		
		if (room == null) {
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "noExist roomId"));
		}
		
		List<User_Room> userRoomList = userRoomService.getUserRoomByRoomId(roomId);
		
		return ResponseEntity.status(200).body(RoomOneGetRes.of(room, userRoomList));
	}
	
	@PatchMapping("/{roomId}")
	@ApiOperation(value="방 정보 변경", notes="방 id 를 통해서 방 정보를 변경한다.")
	@ApiResponses({
		@ApiResponse(code=200, message="Success"),
		@ApiResponse(code = 400, message ="no Exist Room"),
		@ApiResponse(code = 403, message ="no owner")
	})
	public ResponseEntity<? extends BaseResponseBody> updateRoom(
			@ApiIgnore Authentication authentication,
			@RequestBody @ApiParam(value="방 수정 정보", required=true) RoomUpdatePatchReq roomUpdateInfo,
			@PathVariable("roomId") String roomId) {
	
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String userId = userDetails.getUsername();
		
		Room room = roomService.getRoomByRoomId(roomId);
		
		// room 이 없을 경우
		if (room == null) {
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "no Exist Room"));
		}
		
		// 방장과 유저 아이디가 다를 경우
		if (!userId.equals(room.getUserId().getUserId())) {
			return ResponseEntity.status(403).body(BaseResponseBody.of(403, "no Room Creater"));
		}
		
		// 검사를 마치면 수정을 해보자.
		Room resRoom = roomService.updateRoom(roomUpdateInfo, roomId);
		List<User_Room> userRoomList = userRoomService.getUserRoomByRoomId(roomId);
		return ResponseEntity.status(200).body(RoomOneGetRes.of(resRoom, userRoomList));
	}
	
	
	@DeleteMapping("/{roomId}")
	@ApiOperation(value="방 삭제", notes="해당 방을 삭제한다.")
	@ApiResponses({
		@ApiResponse(code = 200, message ="success"),
		@ApiResponse(code = 400, message ="no Exist Room"),
		@ApiResponse(code = 403, message ="no owner")
	})
	public ResponseEntity<? extends BaseResponseBody> deleteRoom(
			@ApiIgnore Authentication authentication,
			@RequestBody @ApiParam(value = "방 삭제 정보", required= true) RoomDeleteReq roomDeleteInfo,
			@PathVariable("roomId") String roomId){
		
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String userId = userDetails.getUsername();
		
		Room room = roomService.getRoomByRoomId(roomId);
		
		// room 이 없을 경우
		if (room == null) {
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "no Exist Room"));
		}
		
		// 방장과 유저 아이디가 다를 경우
		if (!userId.equals(room.getUserId().getUserId())) {
			return ResponseEntity.status(403).body(BaseResponseBody.of(403, "no owner"));
		}
		
		// userRoom 삭제
		User_Room existUserRoom = userRoomService.getUserByUserId(userId);
		String userRoomMessage = userRoomService.deleteUserRoom(existUserRoom);
		
		
		//room 삭제
		String roomMessage = roomService.deleteRoom(roomId);
		
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}
	
	@PostMapping("/{roomId}/admission")
	@ApiOperation(value="방 입장하기", notes="유저가 방에 입장한다.")
	@ApiResponses({
		@ApiResponse(code=200, message="Success"),
		@ApiResponse(code=400, message="no Exist Room"),
		@ApiResponse(code=400, message="already enter room user")
	})
	public ResponseEntity<? extends BaseResponseBody> enterRoom(
			@ApiIgnore Authentication authentication,
			@RequestBody @ApiParam(value = "방 입장 정보", required= true) UserRoomPostReq userRoomPostReq,
			@PathVariable("roomId") String roomId){
		
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String userId = userDetails.getUsername();
		
		Room room = roomService.getRoomByRoomId(roomId);
		
		// 방이 없을 경우
		if (room == null) {
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "no Exist Room"));
		}
		
		// user 가 이미 하나의 방에 접속한 경우
		User_Room existUserRoom = userRoomService.getUserByUserId(userId);
		
		if (existUserRoom.getUserId() != null) {
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "already enter room user"));
		}
		
		// userSide 유효성 검사
		if (userRoomPostReq.getUserSide()==null || !(userRoomPostReq.getUserSide().equals("agree") || userRoomPostReq.getUserSide().equals("opposite") || userRoomPostReq.getUserSide().equals("observer"))) {
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "incorrect userSide"));
		}
		
		// 만약에 비밀번호 방이라면??

		User_Room userRoom = userRoomService.enterUserRoom(userId, room.getId(), userRoomPostReq.getUserSide());
		
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}
	
	@DeleteMapping("{roomId}/admission")
	@ApiOperation(value="방 나가기", notes="유저가 방에서 나간다.")
	@ApiResponses({
		@ApiResponse(code=204, message="success"),
		@ApiResponse(code=400, message="no Exist Room"),
		@ApiResponse(code=400, message="no Room User"),
		@ApiResponse(code=403, message="no entered User")
	})
	public ResponseEntity<?extends BaseResponseBody> leaveRoom (
			@ApiIgnore Authentication authentication,
			@PathVariable("roomId") String roomId) {
		
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String userId = userDetails.getUsername();
		
		Room room = roomService.getRoomByRoomId(roomId);
		
		// 방 없을 때
		if (room == null ) {
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "no Exist Room"));
		}
		
		// 접속한 유저가 아니라면
		User_Room existUserRoom = userRoomService.getUserByUserId(userId);
		
//		if (existUserRoom.getUserId() == null) {
//			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "already enter room user"));
//		}
		
		// 방에 속한 유저가 아니라면
		// userRoom 에서 찾고, existUserRoom 를 가진 room id 를 비교한다.
		if (!existUserRoom.getRoomId().equals(room)) {
			return ResponseEntity.status(403).body(BaseResponseBody.of(403, "no entered User"));
		}
		
		// User_Room 삭제
		String message = userRoomService.leaveRoom(existUserRoom);
		
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}
}
