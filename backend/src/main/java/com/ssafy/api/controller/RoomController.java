package com.ssafy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.request.RoomPostReq;
import com.ssafy.api.response.RoomPostRes;
import com.ssafy.api.service.RoomService;
import com.ssafy.common.auth.SsafyUserDetails;
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
		
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String userId = userDetails.getUsername();
				
		Room room = roomService.createRoom(roomCreateInfo);
		
		return ResponseEntity.status(201).body(RoomPostRes.of(room));
	}
	
	
}
