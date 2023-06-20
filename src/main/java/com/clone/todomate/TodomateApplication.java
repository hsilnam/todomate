package com.clone.todomate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/*
NOTE
 ----
 @EntityListeners(AuditingEntityListener.class)를 사용하기 위해서는
 application단에서 @EnableJpaAuditing를 설정해줘야한다
 */
@SpringBootApplication
@EnableJpaAuditing
public class TodomateApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodomateApplication.class, args);
    }

}
