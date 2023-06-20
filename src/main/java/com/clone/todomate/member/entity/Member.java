package com.clone.todomate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
//@Table(indexes = @Index(name="i_user", columnList = "name, email"))
public class Member extends BaseEntity {
    @Id // pk
    @Column(length = 324)
    private String email;

    @Column(length = 12, unique = true, nullable = false)
    private String id;

    @Column(length = 64, nullable = false)
    private String pw;

    @Column(length = 16)
    private String name;

    @Column(length = 255)
    private String description;

    // TODO: 이미지는 보통 DB에 저장안하지 않나?
    @Column(length = 45)
    private String profile;

    // TODO: 다양한 브라우저에서 동시 접속 문제
    @Column(length = 32)
    private String refresh_token;

    @Column(columnDefinition="TINYINT(1) default 0")
    private boolean certification;
}
