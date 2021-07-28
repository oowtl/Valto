package com.ssafy.db.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.db.entity.Room;

// Room model 관련 디비 쿼리 생성을 위한 JPA Query Method 인터페이스 정의.

public interface RoomRepository extends JpaRepository<Room, Long>{
	
	List<Room> findByTopicAgreeContainingOrTopicOppositeContainingOrTitleContaining(String topicAgree, String topicOpposite, String title);
	List<Room> findByTopicAgreeContainingOrTopicOppositeContaining(String topicAgree, String topicOpposite);
	List<Room> findByTitleContaining(String title);
	
	Optional<Room> findById(Long roomId);
	
}
