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
    @Column(name="user_email", length = 324)
    private String email;

    @OneToOne(fetch=FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_email")
    private User user;

    @Column(length = 45)
    private String salt;
}
