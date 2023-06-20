package com.clone.todomate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/*
QUESTION: 이걸 왜 해주지?
 */
@Getter
@AllArgsConstructor
public class BaseResponse {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
