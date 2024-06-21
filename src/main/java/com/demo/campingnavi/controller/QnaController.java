package com.demo.campingnavi.controller;

import com.demo.campingnavi.config.PathConfig;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Qna;
import com.demo.campingnavi.domain.Reply;
import com.demo.campingnavi.domain.Role;
import com.demo.campingnavi.dto.QnaVo;
import com.demo.campingnavi.dto.ReplyVo;
import com.demo.campingnavi.repository.jpa.ReplyRepository;
import com.demo.campingnavi.service.QnaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/qna")
public class QnaController {

    @Autowired
    QnaService qnaService;
    @Autowired
    private ReplyRepository replyRepository;

    @GetMapping("/home")
    public String qnaHomeView(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("loginUser");
        model.addAttribute("member", member);
        return "qna/qnaHome";
    }

    @GetMapping("/faq")
    @ResponseBody
    public Page<Qna> faqPaging(Pageable pageable) {
        return qnaService.findAllByType("FAQ", pageable);
    }

    @GetMapping("/oneByone")
    @ResponseBody
    public Page<Qna> oneByonePaging(Pageable pageable) {
        return qnaService.findAllByType("ONE", pageable);
    }

    @GetMapping("/faq/detail/{qseq}")
    public String faqDetail(@PathVariable("qseq") int qseq, Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("loginUser");

        Qna qna = qnaService.findById(qseq);
        model.addAttribute("member", member);
        model.addAttribute("qna", qna);

        return "qna/qnaDetail";
    }

    @GetMapping("/oneByone/detail/{qseq}")
    public String qnaDetail(@PathVariable("qseq") int qseq, Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("loginUser");

        Qna qna = qnaService.findById(qseq);
        model.addAttribute("member", member);
        model.addAttribute("qna", qna);

        return "qna/qnaDetail";
    }

    @GetMapping("/detail/edit/{qseq}")
    public String editQnaView(@PathVariable("qseq") int qseq, Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("loginUser");

        Qna qna = qnaService.findById(qseq);
        model.addAttribute("member", member);
        model.addAttribute("qna", qna);

        return "qna/qnaEdit";
    }

    @PostMapping("/detail/edit")
    public String editQna(QnaVo vo, HttpSession session, Model model) {

        Member member = (Member) session.getAttribute("loginUser");
        Qna qna = qnaService.findById(vo.getQseq());
        if(qna.getMember().getMseq() == member.getMseq()) {
            qna.setTitle(vo.getTitle());
            qna.setContent(vo.getContent());
            if (!vo.getImg().isEmpty()) {
                String fileName = vo.getImg().getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                String saveName = uuid + "_" + fileName;
                qna.setImage(saveName);
                try {
                    String uploadPath = PathConfig.intelliJPath_QNA + saveName;
                    boolean exists = PathConfig.isExistsPath();
                    if(exists) {
                        vo.getImg().transferTo(new File(PathConfig.realPath(uploadPath)));
                    } else {
                        uploadPath = PathConfig.eclipsePath_QNA + saveName;
                        vo.getImg().transferTo(new File(PathConfig.realPath(uploadPath)));
                    }
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                }
            }

            qnaService.saveQna(qna);
            model.addAttribute("member", member);
            model.addAttribute("qna", qna);
            return "qna/qnaDetail";
        } else {
            model.addAttribute("qna", qna);
            model.addAttribute("msg", "권한이 없습니다.");
            return "qna/qnaDetail";
        }
    }

    @GetMapping("/detail/delete/{qseq}")
    public String deleteQna(@PathVariable("qseq") int qseq, HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("loginUser");
        Qna qna = qnaService.findById(qseq);
        if(qna.getMember().getMseq() == member.getMseq()) {
            qna.setUseyn("n");
            qnaService.saveQna(qna);
            model.addAttribute("member", member);
            return "qna/qnaHome";
        } else {
            model.addAttribute("msg", "권한이 없습니다.");
            if(qna.getType() == "FAQ") {
                return "redirect:/faq/detail/" + qseq;
            } else {
                return "redirect:/oneByone/detail/" + qseq;
            }

        }
    }

    @GetMapping("/faq/writeView")
    public String faqWriteView(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("loginUser");
        model.addAttribute("member", member);

        return "qna/writeFAQ";
    }

    @GetMapping("/oneByone/writeView")
    public String qnaWriteView(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("loginUser");
        model.addAttribute("member", member);

        return "qna/writeQNA";
    }

    @PostMapping("/faq/write")
    public String writeFAQ(QnaVo vo, HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("loginUser");

        Qna qna = Qna.builder()
                .member(member)
                .title(vo.getTitle())
                .type("FAQ")
                .content(vo.getContent())
                .checkyn("n")
                .useyn("y")
                .build();

        if (!vo.getImg().isEmpty()) {
            String fileName = vo.getImg().getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String saveName = uuid + "_" + fileName;
            qna.setImage(saveName);
            try {
                String uploadPath = PathConfig.intelliJPath_QNA + saveName;
                boolean exists = PathConfig.isExistsPath();
                if(exists) {
                    vo.getImg().transferTo(new File(PathConfig.realPath(uploadPath)));
                } else {
                    uploadPath = PathConfig.eclipsePath_QNA + saveName;
                    vo.getImg().transferTo(new File(PathConfig.realPath(uploadPath)));
                }
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        }

        qnaService.saveQna(qna);
        model.addAttribute("member", member);
        return "qna/qnaHome";
    }

    @PostMapping("/oneByone/write")
    public String writeQNA(QnaVo vo, HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("loginUser");
        Qna qna = Qna.builder()
                .member(member)
                .title(vo.getTitle())
                .type("ONE")
                .content(vo.getContent())
                .checkyn("n")
                .useyn("y")
                .build();

        if (!vo.getImg().isEmpty()) {
            String fileName = vo.getImg().getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String saveName = uuid + "_" + fileName;
            qna.setImage(saveName);
            try {
                String uploadPath = PathConfig.intelliJPath_QNA + saveName;
                boolean exists = PathConfig.isExistsPath();
                if(exists) {
                    vo.getImg().transferTo(new File(PathConfig.realPath(uploadPath)));
                } else {
                    uploadPath = PathConfig.eclipsePath_QNA + saveName;
                    vo.getImg().transferTo(new File(PathConfig.realPath(uploadPath)));
                }
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        }

        qnaService.saveQna(qna);
        model.addAttribute("member", member);
        return "qna/qnaHome";
    }

    @GetMapping("/reply/write/{qseq}")
    public String replyView(@PathVariable("qseq") int qseq, HttpSession session, Model model) {

        Member member = (Member) session.getAttribute("loginUser");
        Qna qna = qnaService.findById(qseq);
        model.addAttribute("qna", qna);
        model.addAttribute("member", member);

        return "qna/replyWrite";
    }

    @PostMapping("/reply/answer")
    public String replyAnswer(ReplyVo vo, Model model, HttpSession session,
                              @RequestParam("qseq") int qseq) {
        Member member = (Member) session.getAttribute("loginUser");
        Qna qna = qnaService.findById(qseq);
        qna.setCheckyn("y");
        qnaService.saveQna(qna);

        Reply reply = Reply.builder()
                .content(vo.getContent())
                .qna(qna)
                .useyn("y")
                .member(member)
                .build();

        if (!vo.getImg().isEmpty()) {
            String fileName = vo.getImg().getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String saveName = uuid + "_" + fileName;
            reply.setImg(saveName);
            try {
                String uploadPath = PathConfig.intelliJPath_REPLY + saveName;
                boolean exists = PathConfig.isExistsPath();
                if(exists) {
                    vo.getImg().transferTo(new File(PathConfig.realPath(uploadPath)));
                } else {
                    uploadPath = PathConfig.eclipsePath_REPLY + saveName;
                    vo.getImg().transferTo(new File(PathConfig.realPath(uploadPath)));
                }
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        }

        replyRepository.save(reply);
        model.addAttribute("member", member);
        model.addAttribute("qna", qna);

        return "redirect:/qna/oneByone/detail/" + qna.getQseq();
    }

    @GetMapping("/reply/paging")
    @ResponseBody
    public Page<Reply> replyPaging(Pageable pageable, @RequestParam(value = "qseq") int qseq) {

        return replyRepository.findAllByQna(qseq, pageable);
    }

    @GetMapping("/reply/edit/{reply_id}")
    public String replyEditView(@PathVariable("reply_id") int reply_id, Model model) {
        Reply reply = replyRepository.findById(reply_id);

        model.addAttribute("reply", reply);
        return "qna/replyEdit";
    }

    @PostMapping("/reply/edit")
    public String replyEdit(ReplyVo vo, Model model) {
        Reply reply = replyRepository.findById(vo.getReply_id());
        reply.setContent(vo.getContent());

        if(!vo.getImg().isEmpty()) {
            String fileName = vo.getImg().getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String saveName = uuid + "_" + fileName;
            reply.setImg(saveName);
            try {
                String uploadPath = PathConfig.intelliJPath_REPLY + saveName;
                boolean exists = PathConfig.isExistsPath();
                if(exists) {
                    vo.getImg().transferTo(new File(PathConfig.realPath(uploadPath)));
                } else {
                    uploadPath = PathConfig.eclipsePath_REPLY + saveName;
                    vo.getImg().transferTo(new File(PathConfig.realPath(uploadPath)));
                }
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        }
        replyRepository.save(reply);
        return "redirect:/qna/oneByone/detail/" + reply.getQna().getQseq();
    }

    @GetMapping("/reply/delete/{reply_id}")
    public String replyDelete(@PathVariable("reply_id") int reply_id) {
        Reply reply = replyRepository.findById(reply_id);
        reply.setUseyn("n");
        replyRepository.save(reply);

        return "redirect:/qna/oneByone/detail/" + reply.getQna().getQseq();
    }
}
