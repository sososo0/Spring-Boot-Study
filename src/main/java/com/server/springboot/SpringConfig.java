package com.server.springboot;

import com.server.springboot.repository.JDBCMemberRepository;
import com.server.springboot.repository.MemoryMemberRepository;
import com.server.springboot.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    private final DataSource dataSource;

    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public JDBCMemberRepository memberRepository() {
//        return new MemoryMemberRepository();
        return new JDBCMemberRepository(dataSource);
    }
}
