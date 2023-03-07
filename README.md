# web-service-practice
웹 서비스 연습
- - -

### 단위 테스트를 해야 하는 이유
새로운 코드를 추가할 때마다 톰캣 서버를 실행해서 확인하는 것은 번거롭다. 

기존 코드에 코드를 추가했을 때 오류가 발생할 수도 있으며

직접 눈으로 확인하는 것보다 코드를 추가할 때마다 단위 테스트

코드를 만들면 어플리케이션 실행 전에 문제를 발견해서 해결할 수 있다.


### 엔티티에 setter 를 생성하지 말라
setter를 무분별하게 생성하면 해당 클래스의 인스턴스 값들이

언제 어디서 변해야하는지 코드상으로 명확하게 구분할 수 없다. 

해당 필드의 값 변경이 필요하다면 그 목적과 의도를 나타내는 메서드를 추가해야 한다.

```
잘못 사용한 예

public void setStatus(boolean status){
this.status = stauts;
}  

필드 값 변경 메서드를 만들어서 의도를 나타낸 예시

public void cancelOrder(){
this.status = false;
}
```

### 롬복을 이용해서 게시글 생성 빌더 만들기  97
생성자에 매개변수가 많다면 빌더를 고려할 수 있다.

롬복에서 제공하는 @Builder 애노테이션을 이용하면 빌더 패턴을 쉽게 만들 수 있다.

```
@Builder(builderMethodName = "createPosts")
    private Posts(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }
    
postsRepository.save(Posts.createPosts()
.title(title).content(content).autohr(author).build());
```

### Service는 트랜잭션, 도메인 간 순서 보장의 역할만 한다. 

* 아이템을 만들고 주문을 만들고 주문 아이템이 되는 로직
```
Item -> Order -> OrderItem
    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        //엔티티 조회 (멤버, 아이템)
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송 정보 생성
        Delivery delivery = Delivery.createDelivery(member.getAddress());

        //주문 만들기
        Order order = Order.createOrder(member, delivery);
        delivery.setOrder(order); // <- 이거 좀 불편하네;;, set 대신 의미 있는 메서드로 리팩토링 하기

        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(order ,item, item.getPrice(), count);

        orderRepository.save(order);

        return order.getId();
    }
```    

Dto 로 받은 값을 Entity 로 바꿔서 저장

```
 @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity())  // 엔티티로 변경
                .getId();
    }
```

## 엔티티를 외부로 노출하지 말라!
서비스 클래스, 비즈니스 로직들은 Entity 클래스를 기준으로 동작한다.

Entity 를 외부로 노출하면 API 스펙이 변경될 수 있다.

즉 Entity 를 외부로 노출했다가 변경된다면 여러 클래스에 영향을 끼치게 된다.

Request , Response 용 DTO 는 View를 위한 클래스라 변경이 잦다.

View Layer 와 DB Layer 를 철저하게 분리하자.

Controller에서 결괏값으로 여러 테이블을 조인해서 보여줘야하는 경우가 많고

이런 경우 Entity 클래스로만 표현하기 어려움.

결론 : 꼭 Entity 클래스와 Controller 에서 사용할 Dto 를 분리하자.

restApi는 Dto 로 만들고 Template 엔진으로 보여줄 화면은 Form 객체로 만듦.

Dto, Form 객체는 Web 계층에 두고 사용


### API 테스트 (게시글 저장)
```
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest

@LocalServerPort 
private int port;

@Autowired
private TestRestTemplate restTemplate;

String url = "http://localhost:" + port + "/api/v1/posts";

ResponseEntity<Long> responseEntity = restTemplate
                .postForEntity(url, requestDto, Long.class);

```
ResponseEntity 는 Http 메시지 헤더, 바디, Http 응답 코드를 가지고 있다.

webEnvironment 설정과 @LocalServerPort 를 사용해서 랜덤 포트를 받고 URI 를 만든다

"http://localhost:" + port + "/api/v1/posts" ApiController 에 postService.save 를 요청하는 URL 생성 후 

RestTemplate 으로 URL, DTO, 요청 반환값을 넣어주면 ResponseEntity 를 반환해준다.


* 참고로 API 예외처리시 @ExceptionHandler 로 일괄 처리할 수 있다.

### API 테스트 (게시글 수정)
```
PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.updatePosts()
                .title(expectedTitle)
                .content(expectedContent)
                .content(expectedContent)
                .build();

HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

ResponseEntity<Long> responseEntity =
                restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
```
업데이트 Dto 를 만들고 HttpEntity 에 요청 바디를 넣고 PUT 수행 후 응답을 받는다 위와 비슷한 메커니즘에서

PUT 을 이용한 수정.

### H2 데이터베이스 yml 설정
```
spring: 띄어쓰기 없음
  datasource: 2칸
    url: 4칸
    username:
    password:
    driver-class-name: org.h2.Driver

  jpa: 2칸
    hibernate: 4칸
      ddl-auto: create 6칸
    properties: 4칸
      hibernate: 6칸
#콘솔 로그        show_sql: 8칸
        format_sql: true 8칸


logging.level: 띄어쓰기 없음
  org.hibernate.sql: debug 2칸
```

* 띄어쓰기를 조심하자!

yml 파일은 띄어쓰기 2 칸으로 계층을 만든다
 
예를 들면 datasource는 spring: 하위에 있고 앞에 띄어쓰기 2 칸이 있으니 spring.datasource가 된다.

* 주의사항
    
    * create : 기존 테이블 삭제 후 다시 생성 
    
    * create-drop: create와 같으나 종료 시점에 테이블 드랍
    
    * update: 변경분만 반영(운영db에서는 사용 x) , 추가만 되고 지우는건 안대 
    
    * validate: 엔티티와 테이블이 정상 매핑 됐는지 확인 
    
    * none: 스키마를 사용하지 않음 


JPA 는 어플리케이션 로딩시점에 테이블을 만들어주는 AUTO DDL 기능을 제공한다 

운영 장비에서는 절대 create, create-drop, update 를 사용하면 안된다.

시스템이 직접 테이블을 만들어주는 것은 위험하다.

스테이징과 운영 서버에서는 validate 또는 none,

초기 단계에서 create, update를 고려할 수 있지만 개발 개발 서버나 테스트도 가급적 validate만을 사용하자.


### Auditing
테이블을 생성할 때는 등록일과 수정일을 꼭 남겨야 한다. 보통 모든 테이블에 다 등록일,수정일을 기록함

Auditing 기능을 사용하면 엔티티에 등록일 수정일 필드를 만들지 않고 속성값만 내려받아서 사용할 수 있다.

```
@MappedSuerclass // 클래스 상속시 필드값 인식
@EntityListners(AuditingEntityListner.class) // 클래스에 Auditing 기능 포함
public abstract class BaseTimeEntity { // Auditing 클래스는 생성할 일이 없으니 추상 클래스로 만들어준다.

@CreatedDate
@Column(updatable = false) // 생성 시간 수정 불가
private LocalDateTime createdDate;

@LastModifedDate
private LocalDateTime modifiedDate;

}

+ @EnableJpaAuditing 애노테이션을 Application 클래스에 추가
```

Auditing 클래스를 만들고 필요한 테이블에서 상속하면 컬럼 값에 등록일 수정일을 쉽게 추가해서 사용할 수 있다.


## 타임리프로 동적 게시판 만들기
```
<head th:replace="~{/fragment/head :: head}">
<div th:replace="fragment/footer :: footer"/>
```
템플릿 조각을 이용해서 공통으로 사용할 head.html 와 footer.html 을 가지고 온다.(레이아웃 리팩토링 예정)

그리고 th:inline 을 이용해서 자바스크립트로 등록 버튼 클릭시 간단한 alert 메시지가 나오게끔 만듦

```
<footer th:fragment="footer">
    <link rel="stylesheet" th:href="@{/js/jquery-3.6.3.min.js}">

    <script th:inline="javascript">
        function posts(){
         alert("등록 완료")
        }
    </script>
</footer>

<button type="submit" class="btn btn-primary" id="btn-save"
th:onclick="posts()">등록</button>
```
등록하면 등록 완료라는 작은 박스 메시지가 나오게됨.

createPostsForm.html , footer.html, head.html 을 합쳐서 게시판 + alert 메시지 만듦

https://www.baeldung.com/thymeleaf-js-function-call 참고.

## 게시글 수정과 주의사항!!
PostsController 에서 게시글 수정시 먼저 게시판 아이디 값을 넘겨주고 저장된 게시글을 찾는다

서비스에서 **트랜잭션** 시작 후 리포지토리에서 저장된 게시글을 찾고 Dto 로 감싸서 html 호출을 호출해서 

타임리프로 동적 화면을 랜더링한다. 이때 주의해야 할 점은 조회 후 수정할 때 이다.

수정은 컨트롤러에서 update 메서드 호출시 Service 의 update 메서드를 호출하는데 

컨트롤러에서 반한된 **디티오를 엔티티로 변환해서** 업데이트 하면 **안된다!!** 이렇게 하면 데이터베이스에 있던 PK 값으로

다시 엔티티를 만드는 격이되는데 이 상태를 준영속 상태라고 하며 영속성 컨텍스트에서 관리하지 않게된다.

영속성 컨텍스트에서 엔티티를 관리하지 않으면 더티체킹이 아닌 병합이 일어나고 병합은 수정시 모든 값을 변경해버리기 떄문에

일부 값을 수정하지 않은 것들도 모두 null 값으로 만들어버려서 의도하지 않는 수정이 일어난다.

그렇기 때문에 값을 수정하려면 먼저 서비스 계층에서 트랜잭션 시작 후 수정한 게시글의 아이디 값을 통해서 리포지토리를 조회한 후 

넘겨준 DTO 객체를 이용해서 값을 수정해야 한다.

```
@Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id =" + id));

        // 도메인 메서드
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;

    }
```    
    
**한줄 요약: 어설프게 컨트롤러에서 디티오를 엔티티로 변환하지 말자!! 엔티티는 외부로 노출하지 말자!!**


### 자바스크립트 alert 를 th:onclick 에서 사용하기
간단한 메시지 박스를 띄우고 싶은 경우
```
<div class="row">
  <div class="col">
     <button type="submit" class="btn btn-primary" id="btn-edit"
        th:onclick="postEdit()">수정</button>
         </div>
         
...
function postEdit() {
        alert("수정 완료")
        }
```
템플릿 조각에 만들어둔 자바스크립트 alert 를 가져와서 th:onclick"postEdit()" 으로 수정 완료 메시지 박스를 만들 수 있다.


메시지 박스 + 하이퍼링크
```
<div class="col">
 <button type="button" class="btn btn-danger"
    th:onclick="|location.href='@{/post/edit/{id}/delete(id=${editPost.id})}', postDelete()|">삭제</button>
    </div>
```

삭제 버튼에 하이퍼링크로 삭제 API 를 호출할 수 있게 만듦 그리고 postDelete() 를 호출해서 삭제 메시지 박스까지 추가!!

리터럴 대체 안에서 자바스크립트를 호출해야 하는 점을 주의하자!!


리다이렉트 추가 예정.
