package com.clone.todomate.member.service;

import com.clone.todomate.member.dto.MemberJoinDTO;
import com.clone.todomate.member.dto.MemberLoginDTO;
import com.clone.todomate.member.entity.Authority;
import com.clone.todomate.member.entity.Member;
import com.clone.todomate.member.repository.MemberRepository;
import com.clone.todomate.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final TokenProvider tokenProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /*
    TODO:
     - 약관 동의 체크
     - id, email 중복 검사
     - 데이터 유효성 검사
     - 예외처리하기
     */
    @Transactional // QUESTION: 꼭 붙여야하나?
    public MemberJoinDTO.Response join(MemberJoinDTO.Request requestDto) {

        // 비밀번호 암호화
        requestDto.setPw(passwordEncoder.encode(requestDto.getPw()));


        Member member = MemberJoinDTO.joinDTOToEntity(requestDto);

        // member 일반 유저 설정
        member.setRoles(new HashSet<Authority>(){{
                add(new Authority("ROLE_USER", member));
        }});
        return MemberJoinDTO.entityToJoinDTO(memberRepository.save(member));
    }

    /*
TODO:
 - 데이터 유효성 검사
 - 예외처리하기
 */
    @Override
    public MemberLoginDTO.Response login(MemberLoginDTO.Request requestDTO) throws Exception {

        //아이디로 검색
        Member member = memberRepository.findById(requestDTO.getEmail()).get();
        if(member == null) {
            return null;
//            throw new Exception(); // TODO: TEMP EXP
        }

        if(!passwordEncoder.matches(requestDTO.getPw(), member.getPw())){
            return null;
//            throw new Exception(); // TODO: TEMP EXP
        }


        return MemberLoginDTO.Response.builder()
                .accessToken(tokenProvider.createToken(member.getEmail(), member.getRoles()))
                .build();
//            return null;
    }
}
