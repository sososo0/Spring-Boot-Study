package com.server.springboot;

import com.server.springboot.repository.*;
import com.server.springboot.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }

//    @Bean
//    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//        return new JDBCMemberRepository(dataSource);
//        return new JDBCTemplateMemberRepository(dataSource);
//        return new JpaMemberRepository(entityManager);

//    }
}
