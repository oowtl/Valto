package com.ssafy.db.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.db.entity.Room;

// Room model 관련 디비 쿼리 생성을 위한 JPA Query Method 인터페이스 정의.

public interface RoomRepository extends JpaRepository<Room, Long>{
	
	List<Room> findByTopicAgreeContainingOrTopicOppositeContainingOrTitleContaining(String topicAgree, String topicOpposite, String title);
	List<Room> findByTopicAgreeContainingOrTopicOppositeContaining(String topicAgree, String topicOpposite);
	List<Room> findByTitleContaining(String title);
	
	// 접속 유저 기준 정렬
	
	
	// 생성 시간 기준 Desc
	List<Room> findByTopicAgreeContainingOrTopicOppositeContainingOrTitleContainingOrderByCreatedAtDesc(String topicAgree, String topicOpposite, String title, Pageable pageable);
	List<Room> findByTopicAgreeContainingOrTopicOppositeContainingOrderByCreatedAtDesc(String topicAgree, String topicOpposite, Pageable pageable);
	List<Room> findByTitleContainingOrderByCreatedAtDesc(String title, Pageable pageable);
	// findAll 에서 localdata type 은 그냥 정렬하려고 하면 작동하지 않는다. 그래서 findAll By OrderBy 로 진행하면 된다.
	List<Room> findAllByOrderByCreatedAtDesc(Pageable pageable);
	
	// 생성 시간 기준 Asc
	List<Room> findByTopicAgreeContainingOrTopicOppositeContainingOrTitleContainingOrderByCreatedAtAsc(String topicAgree, String topicOpposite, String title, Pageable pageable);
	List<Room> findByTopicAgreeContainingOrTopicOppositeContainingOrderByCreatedAtAsc(String topicAgree, String topicOpposite, Pageable pageable);
	List<Room> findByTitleContainingOrderByCreatedAtAsc(String title, Pageable pageable);
	List<Room> findAllByOrderByCreatedAtAsc(Pageable pageable);
	
	Optional<Room> findById(Long roomId);
	
}
