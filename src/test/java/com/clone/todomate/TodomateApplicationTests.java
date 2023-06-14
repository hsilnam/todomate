package com.clone.todomate;

import com.clone.todomate.entity.User;
import com.clone.todomate.entity.UserSec;
import com.clone.todomate.repository.UserRepository;
import com.clone.todomate.repository.UserSecRepository;
import com.clone.todomate.util.OpenCrypt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SpringBootTest
class TodomateApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSecRepository userSecRepository;


    /*
    User CRUD
     */

    // TODO: 차이점 비교해보기
    @Test
    void validEmail() {
        /*
        - RFC 5322 기준
            /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        - 강화1 (유저네임 제한)
            /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        - 강화2 (숫자 도메인 불가능)
            /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}$/
         */
        String email = "user1@user1.com";

        // 길이
        assertThat(email.length() >= 5 && email.length() <= 324).isEqualTo(true);

        // 패턴
        final String EMAIL_REGEX1 = "(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))";
        final String EMAIL_REGEX2 = "[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))";
        final String EMAIL_REGEX3 = "[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}";

        Pattern pattern1 = Pattern.compile(EMAIL_REGEX1);
        Matcher matcher1 = pattern1.matcher(email);
        assertThat(matcher1.matches()).isEqualTo(true);

        Pattern pattern2 = Pattern.compile(EMAIL_REGEX2);
        Matcher matcher2 = pattern2.matcher(email);
        assertThat(matcher2.matches()).isEqualTo(true);

        Pattern pattern3 = Pattern.compile(EMAIL_REGEX3);
        Matcher matcher3 = pattern3.matcher(email);
        assertThat(matcher3.matches()).isEqualTo(true);
    }

    // TODO: 패턴
    @Test
    void validId() {
        String id = "user1";
        assertThat(id.length() >= 6 && id.length() <= 12).isEqualTo(true);
    }

    // TODO: 적어도 하나 포함하는지
    @Test
    void validPw() {
        String pw = "user1";
        assertThat(pw.length() >= 8 && pw.length() <= 20).isEqualTo(true);
    }

    @Test
    void isEmailExist() {
        String email = "user1@user1.com";

        User user = userRepository.findById(email).get();
        assertThat(user).isNotNull();
    }

    @Test
    // TODO:??? findById는 @id 말하는거지..? column에 id라는게 있으면.. 이건 어떻게 불러오지
    void isIdExist() {
//        String email = "user1";
//
//        User user = userRepository.findb(email).get();
//        assertThat(user).isNotNull();
    }

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
        User user = User.builder().email(email)
                .id(id)
                .pw(pw).build();

        // create salt, hashPw
        String salt = UUID.randomUUID().toString();
        String hashPw = OpenCrypt.getSHA256(user.getPw(), salt);

        // update user pw
        user.setPw(hashPw);

        // save user, userSec
        userRepository.save(user);

        // get user from db
        User actualUser = userRepository.findById(email).get();
        System.out.println(user);

        // check user
        assertThat(user.getEmail()).isEqualTo(actualUser.getEmail());
        assertThat(user.getId()).isEqualTo(actualUser.getId());
        assertThat(user.getPw()).isEqualTo(actualUser.getPw());





        // create userSec
        UserSec userSec = UserSec.builder().user(actualUser) // TODO:???? 왜 user로 찾으면 왜 못찾아...?
                .salt(salt).build();

        // save userSec
        userSecRepository.save(userSec);

        // check usersec
        UserSec actualUserSec = userSecRepository.findById(email).get();
        assertThat(userSec.getEmail()).isEqualTo(actualUserSec.getEmail());
        assertThat(userSec.getSalt()).isEqualTo(actualUserSec.getSalt());
        System.out.println(actualUserSec);
    }


    @Test
    @Transactional // TODO:???? select 할때도 transaction이 필요하네?
    public void getAllUser() {
        System.out.println();
        System.out.println("========= user =========");
        List<User> users = userRepository.findAll();
        users.stream().forEach(System.out::println);
        System.out.println();

        System.out.println("========= userSec =========");
        List<UserSec> userSecs = userSecRepository.findAll();
        userSecs.stream().forEach(System.out::println);
        System.out.println();
    }


    @Test
    void updateUser() {
        String newId = "testuser1";
        String email = "user1@user1.com";

        // get user from db
        User user = userRepository.findById(email).get();

        // set id
        user.setId(newId);

        // update user
        userRepository.save(user);

        // check user
        User actualUser = userRepository.findById(email).get();
        assertThat(user.getEmail()).isEqualTo(actualUser.getEmail());
        assertThat(user.getId()).isEqualTo(actualUser.getId());
        assertThat(user.getPw()).isEqualTo(actualUser.getPw());
    }

    @Test
    @Transactional
//    @Commit
    void updatePassword() throws Exception {
        String email = "user1@user1.com";
        String newPw = "testuser1";

        // get user, userSec from db
        User user = userRepository.findById(email).get();
        UserSec userSec = userSecRepository.findById(user.getEmail()).get();


        // create salt, hashPw
        String salt = userSec.getSalt();
        String newHashPw = OpenCrypt.getSHA256(newPw, salt);

        // update user pw
        user.setPw(newHashPw);

        // save user, userSec
        userRepository.save(user);

        // get user from db
        User actualUser = userRepository.findById(email).get();
        System.out.println(user);

        // check user
        assertThat(user.getEmail()).isEqualTo(actualUser.getEmail());
        assertThat(user.getId()).isEqualTo(actualUser.getId());
        assertThat(user.getPw()).isEqualTo(actualUser.getPw());
    }

    @Test
    @Transactional
//    @Commit
    void delete() {
        String email = "user1@user1.com";

        // delete userSec
        UserSec userSec = userSecRepository.findById(email).get();
        userSecRepository.delete(userSec);

        // check delete userSec
        Optional<UserSec> optionalUserSec = userSecRepository.findById(email);
        assertThat(optionalUserSec.isPresent()).isEqualTo(false);



        // delete user
        User user = userRepository.findById(email).get();
        userRepository.delete(user);

        // check delete user
        Optional<User> optionalUser = userRepository.findById(email);
        assertThat(optionalUser.isPresent()).isEqualTo(false);
    }

    /*
    user login
     */
    @Test
    void login() throws Exception {
        String email = "user1@user1.com";
        String pw = "user1";

        // get salt
        UserSec userSec = userSecRepository.findById(email).get();
        String salt = userSec.getSalt();

        // create salt, hashPw
        String hashPw = OpenCrypt.getSHA256(pw, salt);

        assertThat(userRepository.findByEmailAndPw(email, hashPw)).isNotNull();


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







    /*
    @Test
    void create() {
        User test1 = new User("user1", "user1@user1.com", "hello");
        User test2 = new User("user2", "user2@user2.com", "hello222");

//        userRepository.save(test1);
//        userRepository.save(test2);

        List<User> users = new ArrayList<>();
        users.add(test1);
        users.add(test2);
        userRepository.saveAll(users);



        //CRUD
        //----
        //save: insert, update
    }

    @Test
    void update() {
        User test1 = new User(1L, "testuser1", "user1@user1.com", "hello");
        // 알아서 해당 id가 있는지 찾고 있으면 업데이트 씌워줌 (문법은 create와 동일)
        // id 중복검사는 알아서
    }

    @Test
    void delete() {
        Optional<User> opTest1 = userRepository.findById(Long.valueOf(1));
        User test1 = opTest1.get();
        userRepository.delete(test1);
    }

    @Test
    @Transactional
    @Commit // 안붙이면 test환경에서 rollback 시켜버림
    // n+1 쿼리 문제: select하고, 각각 삭제되는 쿼리 생성
    void delete2() {
        userRepository.deleteByEmailContaining("user");
    }

    @Test
    void count() {
        System.out.println(userRepository.count());
    }

    @Test
    public void findTest() {
        System.out.println("========= find all =====");
        List<User> all = userRepository.findAll();
        all.stream().forEach(System.out::println);

        System.out.println("========= find id =====");
        Optional<User> opTest1 = userRepository.findById(Long.valueOf(1));
        User test1 = opTest1.get();
        System.out.println(test1);
    }

    @Test
    public void dumiesInsert() {
        User test1 = new User("1", "user1@user1.com", "hello");
        User test2 = new User("2", "user2@user2.com", "hello222");
        User test3 = new User("3", "user2@user2.com", "hello222");
        User test4 = new User("4", "user2@user2.com", "hello222");
        User test5 = new User("5", "user2@user2.com", "hello222");
        User test6 = new User("6", "user2@user2.com", "hello222");
        User test7 = new User("6", "user2@user2.com", "hello222");

        List<User> users = new ArrayList<>();
        users.add(test1);
        users.add(test2);
        users.add(test3);
        users.add(test4);
        users.add(test5);
        users.add(test6);
        users.add(test7);
        userRepository.saveAll(users);
    }

    @Test
    public void findTestWithPage() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("email").descending()); // 10개로 나뉜 페이지 중 첫번째 페이지 가져오기
        Page<User> all = userRepository.findAll(pageable);

        System.out.println("isLast = " + all.isLast()); // 마지막 페이지냐
        System.out.println("isFirst = " + all.isFirst()); // 첫번째 페이지냐
        System.out.println("getTotalElements = " + all.getTotalElements()); // 총 데이터 개수
        System.out.println("getTotalPages = " + all.getTotalPages()); // 총 페이지 수
        System.out.println("getNumber = " + all.getNumber()); // 현재 페이지가 몇번째 페이지인가
        List<User> content = all.getContent();
        content.stream().forEach(System.out::println); // 현재 페이지 데이터 출력
    }


    @Test
    @Transactional
    @Commit
    public void deleteTestWithCustom() {
        userRepository.deleteByEmailContainingCustom("user");
    }


    @Test
    public void saveSec() {
        User test = userRepository.findById(12L).get();

        UserSec testsec = UserSec.builder().user(test).salt("12341234").build();
        System.out.println(testsec);;

        userSecRepository.save(testsec);

    }

    @Test
    public void findSecTestWithPage() {

        Pageable pageable = PageRequest.of(0, 10); // 10개로 나뉜 페이지 중 첫번째 페이지 가져오기
        Page<UserSec> all = userSecRepository.findAll(pageable);

        System.out.println("isLast = " + all.isLast()); // 마지막 페이지냐
        System.out.println("isFirst = " + all.isFirst()); // 첫번째 페이지냐
        System.out.println("getTotalElements = " + all.getTotalElements()); // 총 데이터 개수
        System.out.println("getTotalPages = " + all.getTotalPages()); // 총 페이지 수
        System.out.println("getNumber = " + all.getNumber()); // 현재 페이지가 몇번째 페이지인가
        List<UserSec> content = all.getContent();
        content.stream().forEach(System.out::println); // 현재 페이지 데이터 출력
    }


    @Test
    @Transactional // lazy라서 메서드 끝날 때까지 session 붙들고 있음
    // fetch.lazy의 no-session 에러에 대해서 찾아오기
    // eager: n+1 문제
    public void fetchLazy() {
        // get forgien key를 날려야 그때 가져옴

    }


    @Test
    public void leftjoin() {
        Optional<UserSec> test = userSecRepository.getUserSecWithId(12L);
    }
    */
}


