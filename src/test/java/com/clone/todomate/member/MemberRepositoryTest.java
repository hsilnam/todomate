package com.clone.todomate.member;

import com.clone.todomate.member.entity.Member;
import com.clone.todomate.member.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/*

NOTE
 ----
 DataJpaTest
 - JPA만 테스트하기위한 어노테이션
 - JPA와 관련된 Bean들만 가져옴
    - SpringBootTest에 비해 속도가 빠름
    - source, controller 사용 불가할 수도
 - 자동 Rollback 지원 (코드로 수동으로 transaction commit 가능)
 - @SpringBootTest와의 차이점
    - 분류 | @DataJpaTest | @SpringBootTest
    - -----------------------------------
    - 목적 | JPA Repository 테스트에 사용 |전체 스프링 애플리케이션 테스트에 사용
    - 테스트 대상 | JPA Repository, JPA 엔티티 | 스프링 부트 애플리케이션 전체
    - 의존성 제어 | 테스트용 Bean을 자동으로 구성 | 실제 애플리케이션 컨텍스트를 구성
    - 테스트 환경 설정 | JPA 관련 설정만 로드 | 모든 스프링 구성 요소 로드
    - 데이터베이스 사용 | 내장형 데이터베이스를 사용 | 실제 데이터베이스 연결
    - 트랜잭션 관리 | 각 테스트마다 트랜잭션을 시작하고 롤백 | 테스트 수행 후 트랜잭션 롤백 또는 커밋
    - 외부 종속성 | 실제 외부 시스템과의 연결 없음	외부 시스템과의 연결 가능
    - 실행 시간 | 빠름 | 느림
    - 주요 어노테이션 | @DataJpaTest, @AutoConfigureTestDatabase | @SpringBootTest, @SpringBootTest.WebEnvironment
*/

/*
NOTE
 ----
 Junit
 - nested을 통해 계층화 가능
 - 성공했을 경우, 실패했을 경우도 체크해 줘야한다
 - given-when-then(준비-실행-검증) 테스트
    - given
        - 테스트를 위해 준비
        - 테스트에 사용하는 변수, 입력 값 등을 정의
        - Mock 객체 정의
    - when
        - 실제로 액션 테스트 실행
        - 하나의 메서드만 수행하는 것이 바람직
    - then
        - 테스트 검증
 */

/*
NOTE
 ----
 테스트
 - 커버리지 100프로는 최손한의 양심이다
 - 테스트 시간 줄이는 것도 나중에는 중요 포인트이다
 - 단위테스트 / 통합테스트
    - 단위테스트 (Unit Test)
        - 디버깅
        - 속도 빠름
        - 간단하고 명확해야
    - 통합테스트 (Integration Test)
        - 서로 다른 시스템들의 상호작용이 의도대로 잘 동작하는지 테스트
        - 속도 느림
        - 복잡함
 */

/*
NOTE
 ----
  getter 메소드의 이름은 관례에 따라 결정
 - boolean클래스형: getMethod
    boolean: is 접두사
 - 원시타입:
    그 외: get 접두사
 - JavaBean 명세에 따라 설계


 */
//@SpringBootTest
@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

//    @Autowired
//    private MemberSecRepository memberSecRepository;

    private List<Member> memberList = new ArrayList<>();
    @BeforeEach
    public void insertDummies() {
        for (int i = 0; i < 100; i++) {
            Member member = Member.builder()
                    .email("user"+i+"@user.com")
                    .memberId("user"+i)
                    .pw("user"+i)
                    .build();
            memberList.add(member);
        }
        memberRepository.saveAll(memberList);
    }

    @AfterEach
    void cleanup() {
        memberRepository.deleteAll();
    }

    // QUESTION: 왜 계층이 안보이지?
    @Nested
    @DisplayName("save")
    public class SaveTest {
        @Nested
        @DisplayName("success")
        public class Success {
            @Test
            @DisplayName("save success")
            public void saveSuccess() {
                // given
                long beforeCount = memberRepository.findAll().size();
                Member member = Member.builder()
                        .email("test@test.com")
                        .memberId("test")
                        .pw("test")
                        .build();

                // when
                Member saved = memberRepository.save(member);

                // then
                long afterCount = memberRepository.findAll().size();
                assertThat(afterCount - beforeCount).isEqualTo(1);

                assertThat(saved.getEmail()).isEqualTo(member.getEmail());
                assertThat(saved.getMemberId()).isEqualTo(member.getMemberId());
                assertThat(saved.getPw()).isEqualTo(member.getPw());
                assertThat(saved.getName()).isEqualTo(member.getName());
                assertThat(saved.getDescription()).isEqualTo(member.getDescription());
                assertThat(saved.getProfile()).isEqualTo(member.getProfile());
//                assertThat(saved.getRefreshToken()).isEqualTo(member.getRefreshToken());
//                assertThat(saved.isCertification()).isEqualTo(member.isCertification());

                System.out.println(saved.getCreatedAt());
                assertThat(saved.getCreatedAt()).isNotNull();
                assertThat(saved.getUpdatedAt()).isNotNull();
            }

            @Nested
            @DisplayName("fail")
            public class Fail {

            }
        }
    }

//
//
//
//    /*
//    User CRUD
//     */
//
//    // TODO: 차이점 비교해보기
//    @Test
//    void validEmail() {
//        /*
//        - RFC 5322 기준
//            /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
//        - 강화1 (유저네임 제한)
//            /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
//        - 강화2 (숫자 도메인 불가능)
//            /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}$/
//         */
//        String email = "user1@user1.com";
//
//        // 길이
//        assertThat(email.length() >= 5 && email.length() <= 324).isEqualTo(true);
//
//        // 패턴
//        final String EMAIL_REGEX1 = "(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))";
//        final String EMAIL_REGEX2 = "[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))";
//        final String EMAIL_REGEX3 = "[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}";
//
//        Pattern pattern1 = Pattern.compile(EMAIL_REGEX1);
//        Matcher matcher1 = pattern1.matcher(email);
//        assertThat(matcher1.matches()).isEqualTo(true);
//
//        Pattern pattern2 = Pattern.compile(EMAIL_REGEX2);
//        Matcher matcher2 = pattern2.matcher(email);
//        assertThat(matcher2.matches()).isEqualTo(true);
//
//        Pattern pattern3 = Pattern.compile(EMAIL_REGEX3);
//        Matcher matcher3 = pattern3.matcher(email);
//        assertThat(matcher3.matches()).isEqualTo(true);
//    }
//
//    // TODO: 패턴
//    @Test
//    void validId() {
//        String id = "user123";
//        assertThat(id.length() >= 6 && id.length() <= 12).isEqualTo(true);
//    }
//
//    // TODO: 적어도 하나 포함하는지
//    @Test
//    void validPw() {
//        String pw = "user1";
//        assertThat(pw.length() >= 8 && pw.length() <= 20).isEqualTo(true);
//    }
//
//    @Test
//    void isEmailExist() {
//        String email = "user1@user.com";
//
//        Member member = memberRepository.findById(email).get();
//        assertThat(member).isNotNull();
//    }
//
//    @Test
//    // QUESTION findById는 @id 말하는거지..? column에 id라는게 있으면.. 이건 어떻게 불러오지
//    void isIdExist() {
////        String email = "user1";
////
////        User user = userRepository.findb(email).get();
////        assertThat(user).isNotNull();
//    }


/*
    @Test
    @Transactional
    @Commit
        // 안붙이면 test환경에서 rollback 시켜버림
        // n+1 쿼리 문제: select하고, 각각 삭제되는 쿼리 생성
    void register() throws Exception {
        String email = "user1@user1.com";
        String id = "user1";
        String pw = "user1";

        // create user
        Member member = Member.builder().email(email)
                .id(id)
                .pw(pw).build();

        // create salt, hashPw
        String salt = UUID.randomUUID().toString();
        String hashPw = OpenCrypt.getSHA256(member.getPw(), salt);

        // update user pw
        member.setPw(hashPw);

        // save user, userSec
        memberRepository.save(member);

        // get user from db
        Member actualMember = memberRepository.findById(email).get();
        System.out.println(member);

        // check user
        assertThat(member.getEmail()).isEqualTo(actualMember.getEmail());
        assertThat(member.getId()).isEqualTo(actualMember.getId());
        assertThat(member.getPw()).isEqualTo(actualMember.getPw());





        // create userSec
        MemberSec memberSec = MemberSec.builder().member(actualMember) // QUESTION: 왜 user로 찾으면 왜 못찾아...?
                .salt(salt).build();

        // save userSec
        memberSecRepository.save(memberSec);

        // check usersec
        MemberSec actualMemberSec = memberSecRepository.findById(email).get();
        assertThat(memberSec.getEmail()).isEqualTo(actualMemberSec.getEmail());
        assertThat(memberSec.getSalt()).isEqualTo(actualMemberSec.getSalt());
        System.out.println(actualMemberSec);
    }


    @Test
    @Transactional // QUESTION: select 할때도 transaction이 필요하네?
    public void getAllUser() {
        System.out.println();
        System.out.println("========= user =========");
        List<Member> members = memberRepository.findAll();
        members.stream().forEach(System.out::println);
        System.out.println();

        System.out.println("========= userSec =========");
        List<MemberSec> memberSecs = memberSecRepository.findAll();
        memberSecs.stream().forEach(System.out::println);
        System.out.println();
    }


    @Test
    void updateUser() {
        String newId = "testuser1";
        String email = "user1@user1.com";

        // get user from db
        Member member = memberRepository.findById(email).get();

        // set id
        member.setId(newId);

        // update user
        memberRepository.save(member);

        // check user
        Member actualMember = memberRepository.findById(email).get();
        assertThat(member.getEmail()).isEqualTo(actualMember.getEmail());
        assertThat(member.getId()).isEqualTo(actualMember.getId());
        assertThat(member.getPw()).isEqualTo(actualMember.getPw());
    }

    @Test
    @Transactional
//    @Commit
    void updatePassword() throws Exception {
        String email = "user1@user1.com";
        String newPw = "testuser1";

        // get user, userSec from db
        Member member = memberRepository.findById(email).get();
        MemberSec memberSec = memberSecRepository.findById(member.getEmail()).get();


        // create salt, hashPw
        String salt = memberSec.getSalt();
        String newHashPw = OpenCrypt.getSHA256(newPw, salt);

        // update user pw
        member.setPw(newHashPw);

        // save user, userSec
        memberRepository.save(member);

        // get user from db
        Member actualMember = memberRepository.findById(email).get();
        System.out.println(member);

        // check user
        assertThat(member.getEmail()).isEqualTo(actualMember.getEmail());
        assertThat(member.getId()).isEqualTo(actualMember.getId());
        assertThat(member.getPw()).isEqualTo(actualMember.getPw());
    }

    @Test
    @Transactional
//    @Commit
    void delete() {
        String email = "user1@user1.com";

        // delete userSec
        MemberSec memberSec = memberSecRepository.findById(email).get();
        memberSecRepository.delete(memberSec);

        // check delete userSec
        Optional<MemberSec> optionalUserSec = memberSecRepository.findById(email);
        assertThat(optionalUserSec.isPresent()).isEqualTo(false);



        // delete user
        Member member = memberRepository.findById(email).get();
        memberRepository.delete(member);

        // check delete user
        Optional<Member> optionalUser = memberRepository.findById(email);
        assertThat(optionalUser.isPresent()).isEqualTo(false);
    }
 */

    /*
    user login
     */
    @Test
    void login() throws Exception {
        String email = "user1@user1.com";
        String pw = "user1";

        // get salt
//        MemberSec memberSec = memberSecRepository.findById(email).get();
//        String salt = memberSec.getSalt();

        // create salt, hashPw
//        String hashPw = OpenCrypt.getSHA256(pw, salt);

//        assertThat(memberRepository.findByEmailAndPw(email, hashPw)).isNotNull();


        /*
        if(userRepository.findUserByEmailExistsAndPwExists(email, hashPw) != null) {
            String accessToken = jwtService.createAccessToken("userid", loginUser.getUserid());// key, data
            String refreshToken = jwtService.createRefreshToken("userid", loginUser.getUserid());// key, data
            memberService.saveRefreshToken(memberDto.getUserid(), refreshToken);
            //AES 대칭키 암호화
            String accessTokenEncript = AES256.encrypt(accessToken);
            String refreshTokenEncript = AES256.encrypt(refreshToken);
            resultMap.put("access-token", accessTokenEncript);
            resultMap.put("refresh-token", refreshTokenEncript);
            resultMap.put("message", SUCCESS);
        }
         */
    }

    @Test
    void logout() {
        String email = "user1@user1.com";

        /*
            memberService.deleRefreshToken(userid);
            resultMap.put("message", SUCCESS);
            status = HttpStatus.ACCEPTED;
         */
    }
}


