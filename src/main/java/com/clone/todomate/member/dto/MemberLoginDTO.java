package com.clone.todomate.member.dto;

import com.clone.todomate.member.entity.Member;
import lombok.*;

public class MemberLoginDTO {
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String email;
        private String pw;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private String accessToken;
    }

    public static Member loginDTOToEntity(Request requestDto) {
        return Member.builder()
                .email(requestDto.getEmail())
                .pw(requestDto.getPw())
                .build();
    }

    public static Response entityToLoginDTO(String accessToken) {
        return Response.builder()
                .accessToken(accessToken)
                .build();
    }
}
