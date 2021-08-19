package com.ssafy.api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.RoomListGetReq;
import com.ssafy.api.request.RoomPostReq;
import com.ssafy.api.request.RoomUpdatePatchReq;
import com.ssafy.db.entity.Room;
import com.ssafy.db.repository.RoomRepository;
import com.ssafy.db.repository.UserRepositorySupport;
import com.ssafy.db.repository.UserRoomRepository;

// Room 관련 서비스 구현 정의

@Service("RoomService")
public class RoomServiceImpl implements RoomService {

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	UserRepositorySupport userRepositorySupport;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRoomRepository userRoomRepository;

	@Override
	public Room createRoom(RoomPostReq roomPostRequestInfo, String validatedUserId) {
		Room room = new Room();

//		if (validatedUserId.equals(roomPostRequestInfo.getUserId())) {
//			room.setUserId(userRepositorySupport.findUserByUserId(validatedUserId).orElse(null));
//		}
		room.setUserId(userRepositorySupport.findUserByUserId(validatedUserId).orElse(null));
		// userId 도 중복검사를 진행할 것이라서 없을리는... 없을 것이다.

		room.setParticipants(roomPostRequestInfo.getParticipants());
//		room.setObservers(roomPostRequestInfo.getObservers());
		room.setTimes(roomPostRequestInfo.getTimes());
		room.setTitle(roomPostRequestInfo.getTitle());
		room.setTopicAgree(roomPostRequestInfo.getTopicAgree());
		room.setTopicOpposite(roomPostRequestInfo.getTopicOpposite());
		room.setStart(false);

		// private room 검사
		if (roomPostRequestInfo.getPrivateRoom()) {
			room.setPrivateRoom(true);
			room.setRoomPassword(passwordEncoder.encode(roomPostRequestInfo.getRoomPassword()));
		} else {
			room.setPrivateRoom(false);
		}

		return roomRepository.save(room);
	}

	@Override
	public List<Room> checkRoomList(RoomListGetReq roomListGetInfo, Pageable pageable) {
		// TODO Auto-generated method stub

		// roomListGetInfo 에 따라서 다르게 넣어준다.
		// only title, only topic, topic and title

//		System.out.println();
//		System.out.println(roomListGetInfo.getSorting());
//		System.out.println(roomListGetInfo.getTitle());
//		System.out.println(roomListGetInfo.getTopic());
//		System.out.println();
		
		// sort 기준 : createdAt(생성 시간) - Desc
//		if (roomListGetInfo.getSorting().equals("createdAtDesc")) {
		if ("createdAtDesc".equals(roomListGetInfo.getSorting())) {
			// topic and title
			if (!(roomListGetInfo.getTitle().equals("null")) && (!roomListGetInfo.getTopic().equals("null"))) {
				return roomRepository
						.findByTopicAgreeContainingOrTopicOppositeContainingOrTitleContainingOrderByCreatedAtDesc(
								roomListGetInfo.getTopic(), roomListGetInfo.getTopic(), roomListGetInfo.getTitle(),
								pageable);
			}
			// only title
			if (!roomListGetInfo.getTitle().equals("null")) {
				return roomRepository.findByTitleContainingOrderByCreatedAtDesc(roomListGetInfo.getTitle(), pageable);
			}
			// only topic
			if (!roomListGetInfo.getTopic().equals("null")) {
				return roomRepository.findByTopicAgreeContainingOrTopicOppositeContainingOrderByCreatedAtDesc(
						roomListGetInfo.getTopic(), roomListGetInfo.getTopic(), pageable);
			}
			// findAll
			return roomRepository.findAllByOrderByCreatedAtDesc(pageable);
		}

		// sort 기준 : createdAt(생성 시간) - Asc
		//if (roomListGetInfo.getSorting().equals("createdAtAsc")) {
		if ("createdAtAsc".equals(roomListGetInfo.getSorting())) {
			// topic and title
			if (!(roomListGetInfo.getTitle().equals("null")) && (!roomListGetInfo.getTopic().equals("null"))) {
				return roomRepository
						.findByTopicAgreeContainingOrTopicOppositeContainingOrTitleContainingOrderByCreatedAtAsc(
								roomListGetInfo.getTopic(), roomListGetInfo.getTopic(), roomListGetInfo.getTitle(),
								pageable);
			}
			// only title
			if (!roomListGetInfo.getTitle().equals("null")) {
				return roomRepository.findByTitleContainingOrderByCreatedAtAsc(roomListGetInfo.getTitle(), pageable);
			}
			// only topic
			if (!roomListGetInfo.getTopic().equals("null")) {
				return roomRepository.findByTopicAgreeContainingOrTopicOppositeContainingOrderByCreatedAtAsc(
						roomListGetInfo.getTopic(), roomListGetInfo.getTopic(), pageable);
			}
			// findAll
			return roomRepository.findAllByOrderByCreatedAtAsc(pageable);
		}

		// participants 접속한 유저의 수 기준 정렬
		// 느리지만 할 수 있는 방법 : findAll 로 들고온다. -? 배열에 정보를 넣는다. -> sort 한다.

		List<HashMap<String, Object>> connectedUserRoomList = new ArrayList<HashMap<String, Object>>();

		// topic and title
		if (!(roomListGetInfo.getTitle().equals("null")) && (!roomListGetInfo.getTopic().equals("null"))) {
			roomRepository
					.findByTopicAgreeContainingOrTopicOppositeContainingOrTitleContaining(roomListGetInfo.getTopic(),
							roomListGetInfo.getTopic(), roomListGetInfo.getTitle())
					.forEach((room) -> convertConnectedUserRoom(room, connectedUserRoomList));
		}
		// only title
		if (!roomListGetInfo.getTitle().equals("null")) {
			roomRepository.findByTitleContaining(roomListGetInfo.getTitle())
					.forEach((room) -> convertConnectedUserRoom(room, connectedUserRoomList));
		}
		// only topic
		if (!roomListGetInfo.getTopic().equals("null")) {
			roomRepository
					.findByTopicAgreeContainingOrTopicOppositeContaining(roomListGetInfo.getTopic(),
							roomListGetInfo.getTopic())
					.forEach((room) -> convertConnectedUserRoom(room, connectedUserRoomList));
		}

		// 아무런 조건이 없을 때 해줘야 하는 것
		if ((roomListGetInfo.getTitle().equals("null")) && (roomListGetInfo.getTopic().equals("null"))) {
			roomRepository.findAll().forEach((room) -> convertConnectedUserRoom(room, connectedUserRoomList));
		}

		Collections.sort(connectedUserRoomList, new Comparator<HashMap<String, Object>>() {
			@Override
			public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
				// TODO Auto-generated method stub
				Integer userCount1 = (Integer) o1.get("userCount");
				Integer userCount2 = (Integer) o2.get("userCount");
				// 내림차순
				if (roomListGetInfo.getSorting().equals("participantsDesc")) {
					return userCount2.compareTo(userCount1);
				}
				// 오름차순 정렬 : default 라서 그냥 이걸로 감
				return userCount1.compareTo(userCount2);
			}
		});
		// paging, size
		// pageable 은 0부터 시작한다.
		Integer resultStartIndex = pageable.getPageSize() * pageable.getPageNumber();
		Integer resultEndIndex = (pageable.getPageSize() * (pageable.getPageNumber() + 1));
		Integer resultMaxPageNumber = connectedUserRoomList.size() / pageable.getPageSize();

		// 공통 조건 : page=0 에서 size 가 검색결과 보다 큰 경우를 대비
		// 시작할 곳이 전체 결과보다 더 큰 경우
		if ((resultMaxPageNumber != 0) && (resultStartIndex >= connectedUserRoomList.size())) {
			return new ArrayList<Room>();
		}

		// page 끝
		if (resultMaxPageNumber == pageable.getPageNumber()) {
			if (resultMaxPageNumber >= 1) {
				resultEndIndex = connectedUserRoomList.size() - 1;
			} else {
				resultEndIndex = connectedUserRoomList.size();
			}
		}
		// page 가 더 갈 수 없다.
		if ((resultMaxPageNumber != 0) && (resultMaxPageNumber < pageable.getPageNumber())) {
			return new ArrayList<Room>();
		}

		List<HashMap<String, Object>> resultUserRoomList = new ArrayList<HashMap<String, Object>>();
		resultUserRoomList = connectedUserRoomList.subList(resultStartIndex, resultEndIndex);

		List<Room> result = new ArrayList<Room>();
		resultUserRoomList.forEach((room) -> result.add((Room) room.get("room")));

		return result;
	}

	@Override
	public Room getRoomByRoomId(String roomId) {
		// TODO Auto-generated method stub

		Room room = roomRepository.findById(Long.valueOf(roomId)).orElseGet(() -> new Room());

		if (room.getTitle().isEmpty()) {
			return null;
		}

		return room;
	}

	@Override
	public Room updateRoom(RoomUpdatePatchReq roomUpdateInfo, String roomId) {
		// TODO Auto-generated method stub

		Room room = roomRepository.findById(Long.valueOf(roomId)).get();

		room.setParticipants(roomUpdateInfo.getParticipants());
		room.setObservers(roomUpdateInfo.getObservers());
		room.setTimes(roomUpdateInfo.getTimes());
		room.setTitle(roomUpdateInfo.getTitle());
		room.setTopicAgree(roomUpdateInfo.getTopicAgree());
		room.setTopicOpposite(roomUpdateInfo.getTopicOpposite());

		return roomRepository.save(room);
	}

	@Override
	public String deleteRoom(String roomId) {
		// TODO Auto-generated method stub

		Room room = roomRepository.findById(Long.valueOf(roomId)).get();

		// room 자체 삭제전 다른 행위, 필요하다면 할 것!

		roomRepository.delete(room);

		return "Success";
	}

	public void convertConnectedUserRoom(Room room, List<HashMap<String, Object>> connectedUserRoomList) {
		HashMap<String, Object> roomInfo = new HashMap<String, Object>();
		roomInfo.put("room", room);
		roomInfo.put("userCount", userRoomRepository.findAllByRoomId(room).size());
		connectedUserRoomList.add(roomInfo);
	}

	// 시작여부 변경
	@Override
	public String updateRoomStart(String roomId) {
		Room room = roomRepository.findById(Long.valueOf(roomId)).get();
		Boolean start = room.getStart();
		room.setStart(start? false: true);
		roomRepository.save(room);
		return "Success";
	}

}
