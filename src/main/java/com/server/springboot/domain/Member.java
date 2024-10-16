package com.server.springboot.domain;

import jakarta.persistence.*;

@Entity // JPA가 관리해주는 Entity임을 명시
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name") // DB의 해당 column과 매핑해준다.
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
