package com.clone.todomate.member.dto;

import com.clone.todomate.member.entity.Member;
import lombok.*;

public class MemberJoinDTO {
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String email;
        private String id;
        private String pw;
//        private String name;
//        private String description;
//        private String profile;
//        private String refreshToken;
//        private boolean certification;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private String email;
        private String id;
    }

    public static Member joinDTOToEntity(Request requestDto) {
        return Member.builder()
                .email(requestDto.getEmail())
                .memberId(requestDto.getId())
                .pw(requestDto.getPw())
//                .name(requestDto.getName())
//                .description(requestDto.getDescription())
//                .profile(requestDto.getProfile())
//                .refreshToken(requestDto.getRefreshToken())
//                    .certification(requestDto.isCertification())
                .build();
    }

    public static Response entityToJoinDTO(Member member) {
        return Response.builder()
                .email(member.getEmail())
                .id(member.getMemberId())
                .build();
    }
}
