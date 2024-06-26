package com.demo.campingnavi.controller;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.ChatRoom;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.dto.*;
import com.demo.campingnavi.repository.jpa.CampRepository;
import com.demo.campingnavi.repository.jpa.ChatRoomRepository;
import com.demo.campingnavi.repository.jpa.MemberRepository;
import com.demo.campingnavi.repository.jpa.ReviewRepository;
import com.demo.campingnavi.repository.mongo.MongoChatMessageRepository;
import com.demo.campingnavi.service.CampService;
import com.demo.campingnavi.service.ChatRoomService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MongoChatMessageRepository mongoChatMessageRepository;
    private final MemberRepository memberRepository;
    private final CampService campService;
    private final ChatRoomRepository chatRoomRepository;
    private final CampRepository campRepository;
    private final ReviewRepository reviewRepository;

    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model,
                        HttpSession session,
                        @RequestParam(defaultValue = "") String campName,
                        @RequestParam(defaultValue = "") List<String> purpose) {
        // 인증 객체 생성
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        CustomSecurityUserDetails securityUserDetails;
        CustomOauth2UserDetails oauth2UserDetails;
        // 객체의 아이디를 얻기 위해서 타입 변환
        if (authentication.getPrincipal() instanceof CustomSecurityUserDetails) { // 사이트 회원
            securityUserDetails = (CustomSecurityUserDetails) authentication.getPrincipal();
            username = securityUserDetails.getUsername();
        } else { // SNS 로그인 회원
            oauth2UserDetails = (CustomOauth2UserDetails) authentication.getPrincipal();
            username = oauth2UserDetails.getUsername();
        }

        // 추출된 아이디로 회원 객체 생성
        Member member = memberRepository.findByUsername(username);

        // 뷰에 전송
        model.addAttribute("member", member);
        model.addAttribute("comuCamp", campName);
        model.addAttribute("purpose", purpose);
        session.setAttribute("loginUser", member);
        System.out.println(purpose + " 를 room 뷰로 전송");
        System.out.println(campName + "을 room 뷰로 전송");

        return "/chat/room";

    }

    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoomVo> room(@RequestParam(defaultValue = "") String campName,
                               HttpSession session) {
        System.out.println(campName + "으로 찾기실행");
        List<ChatRoom> chatRoomList = chatRoomService.findByCampNameContaining(campName);
        List<ChatRoomVo> chatRoomVoList = new ArrayList<>();
        Member member = (Member) session.getAttribute("loginUser");
        CampRecommendVo campRecommendVo = new CampRecommendVo();
        List<Camp> campList = chatRoomService.getCampListExistingChatRoom();
        campService.saveCampRecommendList(campList, member, campRecommendVo);

        Map<Integer, Float> scoreList = new HashMap<>();
        for (CampVo campVo : campRecommendVo.getCampRecommendListAll()) {
            scoreList.put(campVo.getCamp().getCseq(), campVo.getScore());
        }

        for (ChatRoom chatRoom : chatRoomList) {
            long reviewCount = reviewRepository.countByCampCseq(chatRoom.getCamp().getCseq());
            chatRoomVoList.add(new ChatRoomVo(chatRoom, scoreList.get(chatRoom.getCamp().getCseq()), reviewCount));
        }

        return chatRoomVoList;
    }

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name,
                               @RequestParam String startDate,
                               @RequestParam String endDate,
                               @RequestParam int maxMem,
                               @RequestParam String[] purpose,
                               @RequestParam String campName) {

        Camp camp = campRepository.findByName(campName);
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(UUID.randomUUID().toString());
        chatRoom.setName(name);
        chatRoom.setStartDate(startDate);
        chatRoom.setEndDate(endDate);
        chatRoom.setMaxMem(maxMem);
        chatRoom.setPurpose(List.of(purpose));
        chatRoom.setCampName(campName);
        chatRoom.setCamp(camp);
        System.out.println(campName);
        
        chatRoomService.createChatRoom(chatRoom);

        return chatRoomService.createChatRoom(chatRoom);
    }

    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);

        return "/chat/roomdetail";
    }
    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        System.out.println("특정채팅방 조회 메서드 : " + chatRoomService.findRoomById(roomId));
        return chatRoomService.findRoomById(roomId);
    }

    // 특정 채팅방의 모든 메시지 조회
    @GetMapping("/room/{roomId}/messages")
    @ResponseBody
    public List<ChatMessage> roomMessages(@PathVariable String roomId) {
        return mongoChatMessageRepository.findByRoomId(roomId);
    }

    @RequestMapping("/create")
    public String chatCreate(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("loginUser");
        System.out.println(member);
        model.addAttribute("member", member);
        return "chat/create";
    }

    @GetMapping("/chatCampingSearch")
    public String chatCampingSearch(Model model){
        List<Camp> campList = campRepository.findAll();
        model.addAttribute("campList", campList);
        return "chat/chatCampingSearch";
    }

    @GetMapping("/saerch")
    public String searchRoom(){
        return "chat/room";
    }

    @GetMapping("/userList/{roomId}")
    @ResponseBody
    public List<String> userList(@PathVariable String roomId){

        System.out.println("룸아이디로 유저리스트 찾기 " + roomId);
        System.out.println("이 방의 참여자는" + chatRoomService.findRoomById(roomId));

        List<String> mseqList = chatRoomService.getUserList(roomId);

        List<String> chatRoomUserList = new ArrayList<>();
        for (String mseq : mseqList) {
            chatRoomUserList.add(String.valueOf(memberRepository.findById(Integer.valueOf(mseq)).get().getName()));
        }
        System.out.println(chatRoomUserList);

        return chatRoomUserList;
    }

    @GetMapping("/myList/{memberMseq}")
    @ResponseBody
    public List<ChatRoom> findmyList(HttpSession session,
                                     @PathVariable String memberMseq){
        Member member = (Member) session.getAttribute("loginUser");
        List<ChatRoom> myChatRooms = chatRoomService.findMyChatRooms(String.valueOf(memberMseq));
        return myChatRooms;

    }
    @GetMapping("/chatSearch")
    @ResponseBody
    public List<Camp> searchItems(@RequestParam String keyword) {
        return campService.searchItems(keyword);

    }
}
