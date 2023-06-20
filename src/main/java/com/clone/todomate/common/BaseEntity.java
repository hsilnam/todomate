package com.clone.todomate.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/*
Note
 ----
 @MappedSuperclass
 - 매핑 정보를 자식 클래스에게 상속하는 추상클래스
 - 이 클래스(부모 클래스)는 실제 테이블과 매핑되지 않음
 - 공통 매핑 정보 필요할 때 사용

Note
 ----
 @EntityListeners(AuditingEntityListener.class)
 - Entity의 변화(생성, 수정)를 감지하는 리스너를 등록하는 어노테이션
 -`@CreatedDate` 및 `@LastModifiedDate`와 함께 자동으로 생성 및 수정시간 기록 가능
    - updateable: Entity 변화 시 자동 시간 기록 업데이트 활성화 유무
 */

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
