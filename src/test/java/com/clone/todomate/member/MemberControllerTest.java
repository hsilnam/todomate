package com.clone.todomate.member;

import com.clone.todomate.member.dto.MemberJoinDTO;
import com.clone.todomate.member.dto.MemberLoginDTO;
import com.clone.todomate.member.entity.Member;
import com.clone.todomate.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
NOTE
 ----
 무거운 라이브러리(ex-mail)는 Mocking을 활용해서 테스트 시간을 줄여라
 */
/*
TODO: MOCK을 이용해서 테스트 짜보기
 */
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberRepository memberRepository;

    /*
    NOTE
     ----
     ObjectMapper
     - 객체를 직렬화 시켜서 json.stringify한 것같이 만들어 줄 수 있음 (반대도 가능)
     */
    @Autowired
    private ObjectMapper mapper;


//    @Autowired
//    private MemberSecRepository memberSecRepository;

//    protected MockHttpSession session;
//
//    @MockBean
//    private MemberService memberService;


    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private JavaMailSender javaMailSender;

/*
    @BeforeEach
    public void setUp() {

        Member member = Member.builder()
                .email("testuser@testuser.com")
                .id("testuser")
                .pw("testuser")
                .build();
        memberRepository.save(member);
    }

    @AfterEach
    void cleanup() {
        memberRepository.deleteAll();
    }
*/

    @Nested
    @DisplayName("[POST] /member/join")
    public class JoinTest {
        final String requestURL = "/member/join";

        @Nested
        @DisplayName("success test")
        public class Success {

            @Test
            @DisplayName("정상 요청")
            public void saveSuccess() throws Exception {
                // given
                int beforeMemberSize = memberRepository.findAll().size();

                MemberJoinDTO.Request requestDTO = MemberJoinDTO.Request.builder()
                        .email("hsilnam2@gmail.com")
                        .id("hstest")
                        .pw("hstest")
                        .build();

                String stringify = mapper.writeValueAsString(requestDTO);
                System.out.println(stringify);

                // when
                mvc.perform(post(requestURL).content(stringify).contentType("application/json;charset=utf-8"))
                        //then
                        // 1. Response 기댓값 검증
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.email", is(requestDTO.getEmail()))) // NOTE: 여기서 $는 json의 root
                        .andExpect(jsonPath("$.id", is(requestDTO.getId())));



                // 2. Member DB 저장 검증
                assertThat(memberRepository.findAll().size()).isEqualTo(beforeMemberSize + 1);


                // 3. 암호화된 PW 검사
                Member savedMember = memberRepository.findById(requestDTO.getEmail()).get();


                /*
                NOTE
                 ----
                 PasswordEncoder로 암호화된 비밀번호
                 - 같은 비밀번호로 추가해도 다른 값으로 암호화되기 때문에 equals()로는 비교가 불가능
                 - PasswordEncoder의 matches()를 통해 검사해야함
                 */
                assertThat(passwordEncoder.matches(requestDTO.getPw(), savedMember.getPw()));


                /*
                RequestBuilder request = MockMvcRequestBuilders.post("/member/join")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO));

                MemberJoinDTO.Response stubResponse = MemberJoinDTO.entityToJoinDTO();
                given(userService.insert(any())).willReturn(stubResponse);
                */
                // then
                /*
                long afterCount = memberRepository.findAll().size();
                assertThat(afterCount - beforeCount).isEqualTo(1);

                assertThat(saved.getEmail()).isEqualTo(member.getEmail());
                assertThat(saved.getId()).isEqualTo(member.getId());
                assertThat(saved.getPw()).isEqualTo(member.getPw());
                assertThat(saved.getName()).isEqualTo(member.getName());
                assertThat(saved.getDescription()).isEqualTo(member.getDescription());
                assertThat(saved.getProfile()).isEqualTo(member.getProfile());
                assertThat(saved.getRefreshToken()).isEqualTo(member.getRefreshToken());
                assertThat(saved.isCertification()).isEqualTo(member.isCertification());

                System.out.println(saved.getCreatedAt());
                assertThat(saved.getCreatedAt()).isNotNull();
                assertThat(saved.getUpdatedAt()).isNotNull();
                 */
            }

            @Nested
            @DisplayName("fail")
            public class Fail {

            }
        }
    }


    @Nested
    @DisplayName("[POST] /member/login")
    public class LoginTest {
        final String requestURL = "/member/login";

        @BeforeEach
        public void setUp() throws Exception {

            MemberJoinDTO.Request requestDTO = MemberJoinDTO.Request.builder()
                    .email("hsilnam2@gmail.com")
                    .id("hstest")
                    .pw("hstest")
                    .build();

            String stringify = mapper.writeValueAsString(requestDTO);
            System.out.println(stringify);

            // when
            mvc.perform(post("/member/join").content(stringify).contentType("application/json;charset=utf-8"));
        }

        @AfterEach
        void cleanup() {
            memberRepository.deleteAll();
        }

        @Nested
        @DisplayName("success test")
        public class Success {

            @Test
            @DisplayName("정상 요청")
            public void loginSuccess() throws Exception {

                MemberLoginDTO.Request requestDTO = MemberLoginDTO.Request.builder()
                        .email("hsilnam2@gmail.com")
                        .pw("hstest")
                        .build();

                String stringify = mapper.writeValueAsString(requestDTO);
                System.out.println(stringify);

                // when
                mvc.perform(post(requestURL).content(stringify).contentType("application/json;charset=utf-8"))
                        //then
                        // 1. Response 기댓값 검증
                        .andExpect(status().isOk())
                        .andDo(print());
//                        .andExpect(jsonPath("$.accessToken", is(requestDTO.getEmail()))) // NOTE: 여기서 $는 json의 root
//                        .andExpect(jsonPath("$.id", is(requestDTO.getId())));

//
//
//                // 2. Member DB 저장 검증
//                assertThat(memberRepository.findAll().size()).isEqualTo(beforeMemberSize + 1);
//
//
//                // 3. 암호화된 PW 검사
//                Member savedMember = memberRepository.findById(requestDTO.getEmail()).get();


                /*
                NOTE
                 ----
                 PasswordEncoder로 암호화된 비밀번호
                 - 같은 비밀번호로 추가해도 다른 값으로 암호화되기 때문에 equals()로는 비교가 불가능
                 - PasswordEncoder의 matches()를 통해 검사해야함
                 */
//                assertThat(passwordEncoder.matches(requestDTO.getPw(), savedMember.getPw()));


                /*
                RequestBuilder request = MockMvcRequestBuilders.post("/member/join")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO));

                MemberJoinDTO.Response stubResponse = MemberJoinDTO.entityToJoinDTO();
                given(userService.insert(any())).willReturn(stubResponse);
                */
                // then
                /*
                long afterCount = memberRepository.findAll().size();
                assertThat(afterCount - beforeCount).isEqualTo(1);

                assertThat(saved.getEmail()).isEqualTo(member.getEmail());
                assertThat(saved.getId()).isEqualTo(member.getId());
                assertThat(saved.getPw()).isEqualTo(member.getPw());
                assertThat(saved.getName()).isEqualTo(member.getName());
                assertThat(saved.getDescription()).isEqualTo(member.getDescription());
                assertThat(saved.getProfile()).isEqualTo(member.getProfile());
                assertThat(saved.getRefreshToken()).isEqualTo(member.getRefreshToken());
                assertThat(saved.isCertification()).isEqualTo(member.isCertification());

                System.out.println(saved.getCreatedAt());
                assertThat(saved.getCreatedAt()).isNotNull();
                assertThat(saved.getUpdatedAt()).isNotNull();
                 */
            }

            @Nested
            @DisplayName("fail")
            public class Fail {

            }
        }
    }

    @Test
    void sendEmail() {
        System.out.println(javaMailSender);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        try {
            // 1. 메일 수신자 설정
            String[] receiveList = {"hsilnam2@gmail.com"};


            // ArrayList의 경우 배열로 변환이 필요함
//            ArrayList<String> receiveList = new ArrayList<>();
//            receiveList.add("test@naver.com");
//            receiveList.add("test@gmail.com");
//            String[] receiveList = (String[]) receiveList.toArray(new String[receiveList.size()]);


            simpleMailMessage.setTo(receiveList);


            // 2. 메일 제목 설정
            simpleMailMessage.setSubject("test_title");

            // 3. 메일 내용 설정
            simpleMailMessage.setText("test_content");

            // 4. 메일 전송
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
        }
    }
}


