package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// import 생략....

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    @Query("SELECT c FROM ChatRoom c WHERE c.campName LIKE %:campName% OR c.name LIKE %:campName%")
    List<ChatRoom> findByCampNameContaining(String campName);
}