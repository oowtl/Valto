package com.ssafy.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import com.ssafy.api.service.UserService;
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

// Room ?????? API ?????? ????????? ?????? ???????????? ??????

@Api(value = "Room Api", tags = { "Room" })
@RestController
@RequestMapping("api/v1/room")
public class RoomController {

	@Autowired
	UserService userService;

	@Autowired
	RoomService roomService;

	@Autowired
	UserRoomService userRoomService;

	// OpenVidu object as entrypoint of the SDK
	private OpenVidu openVidu;

	// Collection to pair session names and OpenVidu Session objects
	// ?????? ????????? OpenVidu ?????? ????????? ??????????????? ?????? ?????????
	private Map<String, Session> mapSessions = new ConcurrentHashMap<>();
	// Collection to pair session names and tokens (the inner Map pairs tokens and
	// role associated)
	// ?????? ????????? ????????? ??????????????? ?????????(?????? ?????? ????????? ????????? ????????? ?????????)
	private Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();

	// URL where our OpenVidu server is listening
	private String OPENVIDU_URL;
	// Secret shared with our OpenVidu server
	private String SECRET;

	public RoomController(@Value("${openvidu.secret}") String secret, @Value("${openvidu.url}") String openviduUrl) {

		this.SECRET = secret;
		this.OPENVIDU_URL = openviduUrl;
		this.openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
	}

	@PostMapping()
	@ApiOperation(value = " ??? ?????? ", notes = " request ?????? ?????? ????????? ????????? ?????? ????????????. ")
	@ApiResponses({ @ApiResponse(code = 201, message = " ??? ?????? ?????? "),
			@ApiResponse(code = 400, message = " no password private room"), })
	public ResponseEntity<? extends BaseResponseBody> createRoom(@ApiIgnore Authentication authentication,
			@RequestBody @ApiParam(value = "??? ?????? ??????", required = true) RoomPostReq roomCreateInfo) {

		/**
		 * ?????? ?????? ????????? ????????? ????????? ???????????? ???????????? ?????? ????????????, ???????????? ?????? ?????? ??????(authentication) ????????? ????????? ??????
		 * ??????. ????????? ????????? ?????? ???????????? ??????, 403 ??????({"error": "Forbidden", "message": "Access
		 * Denied"}) ??????.
		 */
		SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
		String validatedUserId = userDetails.getUsername();

		// ??? ????????????
		Room room = roomService.createRoom(roomCreateInfo, validatedUserId);
		// User_Room userRoom = userRoomService.enterUserRoom(validatedUserId,
		// room.getId(), roomCreateInfo.getUserSide());

		// Session
		String sessionName = Long.toString(room.getId());
		OpenViduRole role = OpenViduRole.PUBLISHER;
		String serverData = "{\"serverData\": \"" + validatedUserId + "\"}";

		ConnectionProperties connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC)
				.data(serverData).role(role).build();

		try {

			// Create a new OpenVidu Session
			Session session = this.openVidu.createSession();
			// Generate a new Connection with the recently created connectionProperties
			String token = session.createConnection(connectionProperties).getToken();

			// Store the session and the token in our collections
			this.mapSessions.put(sessionName, session);
			this.mapSessionNamesTokens.put(sessionName, new ConcurrentHashMap<>());
			

			System.out.println("createRoom mapSession" + this.mapSessions);
			System.out.println("createRoom mapSessionNamesTokens" + this.mapSessionNamesTokens);
			
			return ResponseEntity.status(201).body(RoomPostRes.of(room));

		} catch (Exception e) {
			// If error generate an error message and return it to client
			System.out.println(e);
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "session failed"));
		}

	}

	@GetMapping()
	@ApiOperation(value = "?????? ????????? ??????", notes = "??? ?????? ????????? ????????????.")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 204, message = "no Room") })
	public ResponseEntity<? extends BaseResponseBody> checkRoomList(
			// ??????????????? string ?????? ????????? ??????
			@PageableDefault(size = 20, page = 0) Pageable pageable,
			@RequestParam(value = "title", defaultValue = "null", required = false) @ApiParam(value = "???????????? ??????") String IN_title,
			@RequestParam(value = "topic", defaultValue = "null", required = false) @ApiParam(value = "????????? ??????") String IN_topic,
			@RequestParam(value = "sorting", defaultValue = "participant", required = false) @ApiParam(value = "?????? ????????????") String IN_sort) {

		RoomListGetReq roomListGetInfo = new RoomListGetReq();
		roomListGetInfo.setTitle(IN_title);
		roomListGetInfo.setTopic(IN_topic);
		roomListGetInfo.setSorting(IN_sort);

		List<Room> getRoomsList = roomService.checkRoomList(roomListGetInfo, pageable);

		// room ??? ?????? ???
		if (getRoomsList.isEmpty()) {
			System.out.println("no room");
			return ResponseEntity.status(204).body(BaseResponseBody.of(204, "no Room"));
		}

		// room list ??? ??? ?????? ????????? ????????? ????????????
		ArrayList<HashMap> getRoomListAddCount = userRoomService.getUserRoomTotalCount(getRoomsList);

		return ResponseEntity.status(200).body(RoomListGetRes.of(getRoomListAddCount));

	}

	@GetMapping("/{roomId}")
	@ApiOperation(value = "?????? ??? ?????? ??????(??????)", notes = "??? id ??? ????????? ??? ?????? ????????? ????????????.")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "noExist roomId") })
	public ResponseEntity<? extends BaseResponseBody> checkOneRoom(@PathVariable("roomId") String roomId) {

		Room room = roomService.getRoomByRoomId(roomId);

		if (room == null) {
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "noExist roomId"));
		}

		List<User_Room> userRoomList = userRoomService.getUserRoomByRoomId(roomId);

		return ResponseEntity.status(200).body(RoomOneGetRes.of(room, userRoomList));
	}

	@PatchMapping("/{roomId}")
	@ApiOperation(value = "??? ?????? ??????", notes = "??? id ??? ????????? ??? ????????? ????????????.")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 400, message = "no Exist Room"),
			@ApiResponse(code = 403, message = "no owner") })
	public ResponseEntity<? extends BaseResponseBody> updateRoom(@ApiIgnore Authentication authentication,
			@RequestBody @ApiParam(value = "??? ?????? ??????", required = true) RoomUpdatePatchReq roomUpdateInfo,
			@PathVariable("roomId") String roomId) {

		SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
		String userId = userDetails.getUsername();

		Room room = roomService.getRoomByRoomId(roomId);

		// room ??? ?????? ??????
		if (room == null) {
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "no Exist Room"));
		}

		// ????????? ?????? ???????????? ?????? ??????
		if (!userId.equals(room.getUserId().getUserId())) {
			return ResponseEntity.status(403).body(BaseResponseBody.of(403, "no Room Creater"));
		}

		// ????????? ????????? ????????? ?????????.
		Room resRoom = roomService.updateRoom(roomUpdateInfo, roomId);
		List<User_Room> userRoomList = userRoomService.getUserRoomByRoomId(roomId);
		return ResponseEntity.status(200).body(RoomOneGetRes.of(resRoom, userRoomList));
	}

	@DeleteMapping("/{roomId}")
	@ApiOperation(value = "??? ??????", notes = "?????? ?????? ????????????.")
	@ApiResponses({ @ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "no Exist Room"),
			@ApiResponse(code = 403, message = "no owner") })
	public ResponseEntity<? extends BaseResponseBody> deleteRoom(@ApiIgnore Authentication authentication,
			@RequestBody @ApiParam(value = "??? ?????? ??????", required = true) RoomDeleteReq roomDeleteInfo,
			@PathVariable("roomId") String roomId) {

		SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
		String userId = userDetails.getUsername();

		Room room = roomService.getRoomByRoomId(roomId);

		// room ??? ?????? ??????
		if (room == null) {
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "no Exist Room"));
		}

		// ????????? ?????? ???????????? ?????? ??????
		if (!userId.equals(room.getUserId().getUserId())) {
			return ResponseEntity.status(403).body(BaseResponseBody.of(403, "no owner"));
		}

		// userRoom ??????
		User_Room existUserRoom = userRoomService.getUserByUserId(userId);
		String userRoomMessage = userRoomService.deleteUserRoom(existUserRoom);

		// room ??????
		String roomMessage = roomService.deleteRoom(roomId);

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}

	@PostMapping("/{roomId}/admission")
	@ApiOperation(value = "??? ????????????", notes = "????????? ?????? ????????????.")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 400, message = "no Exist Room"),
			@ApiResponse(code = 400, message = "already enter room user"),
			@ApiResponse(code = 400, message = "don't enter Room : maximum userside") })
	public ResponseEntity<?> enterRoom(@ApiIgnore Authentication authentication,
			@RequestBody @ApiParam(value = "??? ?????? ??????", required = true) UserRoomPostReq userRoomPostReq,
			@PathVariable("roomId") String roomId) {



		System.out.println(roomId);
		System.out.println(userRoomPostReq.toString());

		SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
		String userId = userDetails.getUsername();

		Room room = roomService.getRoomByRoomId(roomId);
		// System.out.println("Enter Room!");
		// ?????? ?????? ??????
		if (room == null) {
			System.out.println("room");
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "no Exist Room"));
		}


		if (!userRoomService.checkLimitRoom(room, userRoomPostReq.getUserSide())) {
			System.out.println("check");
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "don't enter Room : maximum userside"));
		}

		// user ??? ?????? ????????? ?????? ????????? ??????
		User_Room existUserRoom = userRoomService.getUserByUserId(userId); 

		if (existUserRoom.getUserId() != null) {
			userRoomService.deleteUserRoom(existUserRoom);
		}

		User_Room userRoom = userRoomService.enterUserRoom(userId, room.getId(), userRoomPostReq.getUserSide());

		// Session
		String sessionName = Long.toString(room.getId());
		OpenViduRole role = OpenViduRole.PUBLISHER;
		String serverData = "{\"serverData\": \"" + userId + "\"}";
		JSONObject responseJson = new JSONObject();
		ConnectionProperties connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC)
				.data(serverData).role(role).build();

		System.out.println("Existing session " + sessionName);
		try {

			// Generate a new Connection with the recently created connectionProperties
			System.out.println("mapsession : " + this.mapSessions);
			String token = this.mapSessions.get(sessionName).createConnection(connectionProperties).getToken();

			System.out.println("token : " + token);

			// Update our collection storing the new token
			this.mapSessionNamesTokens.get(sessionName).put(token, role);
			// Prepare the response with the token

			String side = userRoom.getUserSide().equals("agree") ? "left" : "right";
			System.out.println(room.getTopicAgree());
			System.out.println(room.getTopicOpposite());
			
			
			responseJson.put(0, token);
			responseJson.put(1, side);
			responseJson.put(2, room.getUserId().getNickName());
			responseJson.put(3, room.getUserId().getUserId());
			responseJson.put(4, userId);
			responseJson.put(5, room.getTopicAgree());
			responseJson.put(6, room.getTopicOpposite());
			// Return the response to the client
			return new ResponseEntity<>(responseJson, HttpStatus.OK);
		} catch (OpenViduJavaClientException e1) {
			// If internal error generate an error message and return it to client
			return getErrorResponse(e1);
		} catch (OpenViduHttpException e2) {
			if (404 == e2.getStatus()) {
				
				this.mapSessions.remove(sessionName);
				this.mapSessionNamesTokens.remove(sessionName);
			}
		}

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}

	@DeleteMapping("{roomId}/admission")
	@ApiOperation(value = "??? ?????????", notes = "????????? ????????? ?????????.")
	@ApiResponses({ @ApiResponse(code = 204, message = "success"), @ApiResponse(code = 400, message = "no Exist Room"),
			@ApiResponse(code = 400, message = "no Room User"), @ApiResponse(code = 403, message = "no entered User") })
	public ResponseEntity<? extends BaseResponseBody> leaveRoom(@ApiIgnore Authentication authentication,
			@PathVariable("roomId") String roomId, @RequestBody String sessionNameToken) throws ParseException {

		SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
		String userId = userDetails.getUsername();
		System.out.println("Removing user | {sessionName, token}=" + sessionNameToken);
		System.out.println(sessionNameToken);
		Room room = roomService.getRoomByRoomId(roomId);

		JSONObject sessionNameTokenJSON = (JSONObject) new JSONParser().parse(sessionNameToken);
		String sessionName = (String) sessionNameTokenJSON.get("sessionName");
		String token = (String) sessionNameTokenJSON.get("token");

		// ??? ?????? ???
		if (room == null) {
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "no Exist Room"));
		}
		// If the session exists

		User_Room existUserRoom = userRoomService.getUserByUserId(userId);

		// if (existUserRoom.getUserId() == null) {
		// return ResponseEntity.status(400).body(BaseResponseBody.of(400, "already
		// enter room user"));
		// }

		// ?????? ?????? ????????? ????????????
		// userRoom ?????? ??????, existUserRoom ??? ?????? room id ??? ????????????.
		// if (!existUserRoom.getRoomId().equals(room)) {
		// return ResponseEntity.status(403).body(BaseResponseBody.of(403, "no entered
		// User"));
		// }
		//
		// User_Room ??????

		if (this.mapSessions.get(sessionName) != null && this.mapSessionNamesTokens.get(sessionName) != null) {

			// If the token exists
			if (this.mapSessionNamesTokens.get(sessionName).remove(token) != null) {
				// User left the session

				// userRoom ??????
				String message = userRoomService.leaveRoom(existUserRoom);

				// rank point ????????????
				User addPointUser = userService.addRankPoint(userId);
				if (this.mapSessionNamesTokens.get(sessionName).isEmpty()) {
					// Last user left: session must be removed
					this.mapSessions.remove(sessionName);

					// room ??????
					String roomMessage = roomService.deleteRoom(roomId);

				}
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				// The TOKEN wasn't valid
				System.out.println("Problems in the app server: the TOKEN wasn't valid");
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} else {
			// The SESSION does not exist
			System.out.println("Problems in the app server: the SESSION does not exist");
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}

	@PostMapping("{roomId}/start")
	@ApiOperation(value = "?????? ??????", notes = "????????? ?????????????????? ??????????????? ???????????????.")
	public ResponseEntity<?> startRoom(@ApiIgnore Authentication authentication,
			@PathVariable("roomId") String roomId) {

		SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
		String userId = userDetails.getUsername();

		String msg = roomService.updateRoomStart(roomId);

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, msg));
	}

	private ResponseEntity<JSONObject> getErrorResponse(Exception e) {
		JSONObject json = new JSONObject();
		json.put("cause", e.getCause());
		json.put("error", e.getMessage());
		json.put("exception", e.getClass());
		return new ResponseEntity<>(json, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void checkUserLogged(HttpSession httpSession) throws Exception {
		if (httpSession == null) {
			throw new Exception("not httpSession");
		}
		if (httpSession.getAttribute("loggedUser") == null) {
			throw new Exception("not loggedUser");
		}
		// if (httpSession == null || httpSession.getAttribute("loggedUser") == null) {
		// throw new Exception("User not logged");
		// }
	}
}
