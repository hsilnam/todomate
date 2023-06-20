package com.clone.todomate.member.entity;

import com.clone.todomate.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
/*
NOTE
 ----
 entity는 set 메서드를 사용하는 것을 지양
 - 데이터의 영속성을 지켜주기 위해
 - 만약 사용한다면 setId가아닌 changeId깥이 예측 불가능한 함수로 작성해야한다

 NOTE
  ----
  변경: user -> member
  - Spring Security에도 user라는 객체가 있어 혼동 방지를 위해
 */
//@Table(indexes = @Index(name="i_user", columnList = "name, email"))
public class Member extends BaseEntity {
    @Id // pk
    @Column(length = 324)
    private String email;

    @Column(length = 12, unique = true, nullable = false, name="id")
    private String memberId;

    @Column(length = 64, nullable = false)
    private String pw;

    @Column(length = 16)
    private String name;

    @Column(length = 255)
    private String description;

    // QUESTION: 이미지는 보통 DB에 저장안하지 않나?
    @Column(length = 45)
    private String profile;

//    // TODO: 다양한 브라우저에서 동시 접속 문제
//    @Column(length = 32, name="refresh_token")
//    private String refreshToken;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Authority> roles = new HashSet<>();
    public void setRoles(HashSet<Authority> roles) {
        this.roles = roles;
        roles.forEach(r -> r.setMember(this));
    }

//    @ManyToMany
//    @JoinTable(
//            name = "user_authority",
//            joinColumns = {@JoinColumn(name = "id", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
//    private Set<Authority> authorities;


    // QUESTION: org.hibernate.tool.schema.spi.CommandAcceptanceException: Error executing DDL에러 발생
    //  -> 예약어 충돌?
//    @Column(name="certification")
    @Column
    @ColumnDefault("0") // NOTE: command + p
//    columnDefinition="TINYINT(1) default 0"
    private boolean certification;
}
