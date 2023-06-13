package com.clone.todomate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(indexes = @Index(name="i_user", columnList = "name, email"))
public class User {
    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 25, nullable = false)
    private String email;

    @Column(name="helloWorld")
    private String helloWorld;

    public User(String name, String email, String helloWorld) {
        this.name = name;
        this.email = email;
        this.helloWorld = helloWorld;
    }
}
