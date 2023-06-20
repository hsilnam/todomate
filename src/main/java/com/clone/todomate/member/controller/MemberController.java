package com.clone.todomate.member.controller;

import com.clone.todomate.member.dto.MemberJoinDTO;
import com.clone.todomate.member.dto.MemberLoginDTO;
import com.clone.todomate.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@Controller
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    // TODO: JWT
    @PostMapping("/join")
    public ResponseEntity<MemberJoinDTO.Response> join(@RequestBody MemberJoinDTO.Request requestDTO) throws Exception {
        try {
            return new ResponseEntity<>(memberService.join(requestDTO), HttpStatus.CREATED);

        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<MemberLoginDTO.Response> login(@RequestBody MemberLoginDTO.Request requestDTO) throws Exception {
        try {
            return new ResponseEntity<>(memberService.login(requestDTO), HttpStatus.OK);
        }catch(Exception e){
            throw e;
        }
    }

    // TODO: Error Handler
    /*
    @ExceptionHandler({AlreadyExistException.class,ValidationException.class,SaveException.class})
    public ResponseEntity<ErrorMessageDTO> badRequestErrorHandler(Exception e){
        return new ResponseEntity<>(ErrorMessageDTO.builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({LoginException.class,UserInfoNotFoundException.class})
    public ResponseEntity<ErrorMessageDTO> unauthorizedErrorHandler(Exception e){
        return new ResponseEntity<>(ErrorMessageDTO.builder()
                .message(e.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .build(),HttpStatus.UNAUTHORIZED);
    }
     */
}
