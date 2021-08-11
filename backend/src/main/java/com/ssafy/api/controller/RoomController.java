package com.ssafy.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

import io.openvidu.java.client.ConnectionProperties;
import io.openvidu.java.client.ConnectionType;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.OpenViduRole;
import io.openvidu.java.client.Session;
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
	
	private OpenVidu openVidu;
	private Map<String, Session> mapSessions = new ConcurrentHashMap<>();
	private Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();
	private String OPENVIDU_URL;
	private String SECRET;
	
	public RoomController(@Value("${openvidu.secret}") String secret, @Value("${openvidu.url}") String openviduUrl) {
		this.SECRET = secret;
		this.OPENVIDU_URL = openviduUrl;
		this.openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
	}
	
	@PostMapping()
	@ApiOperation(value = " 방 생성 ", notes =" request 에서 받은 정보를 통해서 방을 생성한다. ")
	@ApiResponses({
		@ApiResponse(code = 201, message = " 방 생성 성공 "),
		@ApiResponse(code = 400, message = " no password private room"),
	})
	public ResponseEntity<?> createRoom(
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
		System.out.println(room.getId());
		// User_Room 생성하기
		User_Room userRoom = userRoomService.enterUserRoom(validatedUserId, room.getId(), roomCreateInfo.getUserSide());
		String sessionName = Long.toString(room.getId());
		
		//JSONObject sessionJSON = (JSONObject) new JSONParser().parse(sessionName);
		
//		// New session
		System.out.println("New session " + sessionName);
		
		OpenViduRole role = OpenViduRole.PUBLISHER;

		// Optional data to be passed to other users when this user connects to the
		// video-call. In this case, a JSON with the value we stored in the HttpSession
		// object on login
		String serverData = "{\"serverData\": \"" + validatedUserId + "\"}";

		// Build connectionProperties object with the serverData and the role
		ConnectionProperties connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC).data(serverData).role(role).build();

		//JSONObject responseJson = new JSONObject();
		
		try {

			// Create a new OpenVidu Session
			Session session = this.openVidu.createSession();
			// Generate a new Connection with the recently created connectionProperties
			String token = session.createConnection(connectionProperties).getToken();

			// Store the session and the token in our collections
			this.mapSessions.put(sessionName, session);
			this.mapSessionNamesTokens.put(sessionName, new ConcurrentHashMap<>());
			//this.mapSessionNamesTokens.get(sessionName).put(token, role);
			
			// Prepare the response with the token
			//responseJson.put(0, token);

			// Return the response to the client
			return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));

		} catch (Exception e) {
			// If error generate an error message and return it to client
			return getErrorResponse(e);
		}
	}
	
	@GetMapping()
	@ApiOperation(value = "전체 방목록 조회", notes = "방 전체 목록을 조회한다.")
	@ApiResponses({
		@ApiResponse(code = 200, message ="Success"),
		@ApiResponse(code = 204, message ="no Room")
	})
	public ResponseEntity<? extends BaseResponseBody> checkRoomList (
			// 파라미터를 string 으로 받아서 판단
			@RequestParam(value ="title", defaultValue ="null", required =false) @ApiParam( value = "제목으로 조회") String IN_title ,
			@RequestParam(value ="topic", defaultValue ="null", required =false) @ApiParam( value = "주제로 조회") String IN_topic) {
		
		RoomListGetReq roomListGetInfo = new RoomListGetReq();
		roomListGetInfo.setTitle(IN_title);
		roomListGetInfo.setTopic(IN_topic);
		
		List<Room> getRoomsList = roomService.checkRoomList(roomListGetInfo);
		
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
	public ResponseEntity<?> enterRoom(
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
		
		// 만약에 비밀번호 방이라면??

		User_Room userRoom = userRoomService.enterUserRoom(userId, room.getId(), userRoomPostReq.getUserSide());
		
		JSONObject sessionJSON = null;
		try {
			sessionJSON = (JSONObject) new JSONParser().parse(Long.toString(room.getId()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sessionName = (String) sessionJSON.get("sessionName");
		System.out.println("Existing session " + sessionName);
		
		OpenViduRole role = OpenViduRole.PUBLISHER;
		
		String serverData = "{\"serverData\": \"" + userId + "\"}";

		// Build connectionProperties object with the serverData and the role
		ConnectionProperties connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC).data(serverData).role(role).build();

		JSONObject responseJson = new JSONObject();
		
		try {

			// Generate a new Connection with the recently created connectionProperties
			String token = this.mapSessions.get(sessionName).createConnection(connectionProperties).getToken();

			// Update our collection storing the new token
			this.mapSessionNamesTokens.get(sessionName).put(token, role);

			// Prepare the response with the token
			responseJson.put(0, token);

			// Return the response to the client
			return new ResponseEntity<>(responseJson, HttpStatus.OK);
		} catch (OpenViduJavaClientException e1) {
			// If internal error generate an error message and return it to client
			return getErrorResponse(e1);
		} catch (OpenViduHttpException e2) {
			if (404 == e2.getStatus()) {
				// Invalid sessionId (user left unexpectedly). Session object is not valid
				// anymore. Clean collections and continue as new session
				this.mapSessions.remove(sessionName);
				this.mapSessionNamesTokens.remove(sessionName);
			}
		}
		
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
	
	private ResponseEntity<JSONObject> getErrorResponse(Exception e) {
		JSONObject json = new JSONObject();
		json.put("cause", e.getCause());
		json.put("error", e.getMessage());
		json.put("exception", e.getClass());
		return new ResponseEntity<>(json, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
