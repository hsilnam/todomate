package com.clone.todomate.member.service;

import com.clone.todomate.member.dto.MemberJoinDTO;
import com.clone.todomate.member.dto.MemberLoginDTO;

public interface MemberService {
    public MemberJoinDTO.Response join(MemberJoinDTO.Request requestDTO) throws Exception;

    public MemberLoginDTO.Response login(MemberLoginDTO.Request requestDTO) throws Exception;
}
