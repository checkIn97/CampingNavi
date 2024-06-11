package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

// import 생략....

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {


}