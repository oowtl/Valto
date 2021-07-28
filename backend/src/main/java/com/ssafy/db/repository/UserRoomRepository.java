package com.ssafy.db.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.db.entity.Room;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.User_Room;

// User Room model 관련 디비 쿼리 생성을 위한 JPA Query Method 인터페이스 정의

public interface UserRoomRepository extends JpaRepository<User_Room, Long>{
	Optional<User_Room> findByUserId(User user);
	Optional<User_Room> findByRoomId(Room room);
	List<User_Room> findAllByRoomId(Room room);
}
