package com.clone.todomate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class UserSec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /*
    FetchType.EAGER: 조회할 때 관련 모든 엔티티 전부 가져옴 (default)
     */
    @OneToOne(fetch=FetchType.LAZY)
    private User user;

    private String salt;
}
