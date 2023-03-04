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

## 엔티티를 외부로 노출하지 말라!
서비스 클래스, 비즈니스 로직들은 Entity 클래스를 기준으로 동작한다.

Entity 를 외부로 노출하면 API 스펙이 변경될 수 있다.

즉 Entity 를 외부로 노출했다가 변경된다면 여러 클래스에 영향을 끼치게 된다.

Request , Response 용 DTO 는 View를 위한 클래스라 변경이 잦다.

View Layer 와 DB Layer 를 철저하게 분리하자.

Controller에서 결괏값으로 여러 테이블을 조인해서 보여줘야하는 경우가 많고

이런 경우 Entity 크랠스로만 표현하기 어려움.

결론 : 꼭 Entity 클래스와 Controller 에서 사용할 Dto 를 분리하자.


