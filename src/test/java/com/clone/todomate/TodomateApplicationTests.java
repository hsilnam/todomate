package com.clone.todomate;

import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


/*
NOTE
 ----
 테스트 시 중요한 것
 - 속도
 - 환경 상관 없이 동일한 값이 나와야 함
 */
@SpringBootTest
class TodomateApplicationTests {
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


