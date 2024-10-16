package com.server.springboot.controller;

import com.server.springboot.domain.Member;
import com.server.springboot.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    private final MemberService memberService;

    // 생성자에 @Autowired가 있으면 스프링이 연관 객체를 스프링 컨테이너에 찾아서 넣어준다. - 이렇게 객체 의존 관계를 외부에서 넣어주는 것을 DI(의존성 주입)이라고 한다.
    @Autowired // MemberService를 Spring Container에 있는 MemberService를 가져와서 연결시켜준다.
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);

        return "redirect:/";
    }
}
