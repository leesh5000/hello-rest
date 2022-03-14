# rest market

rest market 프로젝트는 여러 상품 리소스를 대상으로 rest 아키텍처를 따르는 api server를 구현한 프로젝트입니다. 대부분의 rest api가 사실 rest 아키텍처 설계 원칙을 만족시키지 못합니다. 이러한 점에 동기가 생겨 "한 번 로이 필딩이 제시한 진짜 rest 아키텍처르 따르는 api 서버를 만들어보자"는 목적으로 시작하게 되었습니다.

## 개발 전 배경지식

### 1. rest 아키텍처


- 로이 필딩의 박사 논문에서 처음으로 소개
- 어떻게 하면 인터넷 상에 존재하는 서로 다른 시스템 간에 독립적인 진화를 보장할 것인가에 대한 문제해결

```
인터넷 상의 시스템 간의 상호 운용성(inter-operatablilty)을 제공하는 방법 중 하나
```

- REST = Representational State Transfer
- 웹을 깨뜨리지 않으면서, HTTP를 진화시키는 방법

#### 1-2. REST API 의 목표

- 구성 요소 상호작용의 규모 확장성(scalability of component interactions)
- 인터페이스의 범용성 (Generality of interfaces)
- 구성 요소의 독립적인 배포(Independent deployment of components)
- 중간적 구성요소를 이용해 응답 지연 감소, 보안을 강화, 레거시 시스템을 인캡슐레이션 (Intermediary components to reduce latency, enforce security and encapsulate legacy systems)

#### 1-3. REST 아키텍처 스타일

- _Client-Server Architecture_ : 분리되고 독립적인 클라/서버 구조
- _Stateless_ : 각 요청간 클라이언트의 콘텍스트가 서버에 저장되어서는 안 된다.
- _Cacheable_ : 클라-서버간의 상호작용을 줄이기위한 캐시 처리 가능
- _Layered System_ : 클라이언트가 요청한 정보를 검색하는 과정에서 관련된 서버들은 계층화 되어있어 시스템의 확장에 유리해야한다
- _Code on demand_ (optional) : 서버에서 클라이언트로 실행시킬 수 있는 로직을 전송하여 기능을 확장할 수 있음
- _Uniform Interface_ : 일관적인 인터페이스로 분리되어야한다.

```
RESTFUL API는 일관적인 인터페이스로 분리되어야 한다. 일관적인 인터페이스를 제공해야 표준적으로 사용할 수 있으므로
다른 5개의 아키텍처들은 http 프로토콜에서도 이미 제공되지만, uniform interface를 위해서는 추가적인 제약사항을 만족하여야한다.
특히, Uniform Interface 중에서도 self-descriptive messages, hypermedia as the engine of application stats를 만족하지 못하기 때문에 인터넷 상의 시스템들의 독립적인 진화를 보장하지 못하게 된다.
(예를들면, API/v1 과 같은 종속적인 일들)
```

#### 1-4. Uniform Interface

- _identification of resources_ :
- _manipulation of resources through representations_ :
- _self-descriptive messages_
    - 메세지 스스로에 대한 자기 설명이 가능해야한다.
    - 서버가 변해서 메세지가 변하더라도, 클라이언트에서는 이 메세지의 자기 설명을 보고 해석하여 독립적으로 사용할 수 있어야한다.
    - 확장 가능한 커뮤니케이션
- _hypermedia as the engine of application stats_
    - 하이퍼미디어를 통해 애플리케이션의 상태 변화가 가능해야한다.

```
클라이언트와 서버가 미리 URL을 정해놓고 거기에 맞춰 통신하는 것이 아니고, REST API 응답에 존재하는 링크를 
통해서 언제든지 다른 상태로 전이가 가능해야한다.
```

- 링크 정보를 동적으로 바꿀 수 있어야한다.
- self-descriptive messages 해결방법

    1. 응답에 대한 미디어 타입을 정의하고, IANA에 등록 후 Content-Type으로 사용 `ex) github api`
    2. profile link 헤더를 추가한다.
        1. 아직 profile header 스펙을 지원하는 브라우저들이 많지 않음
        2. 대안으로 HAL 스펙을 따라서, profile을 응답 본문에 실어서 전달
- hypermedia as the engine of application stats 해결방법

    1. HAL 스펙을 따라서, profile을 응답 본문에 실어서 전달

### 2. Authentication & Authorization

- Authentication : 자격증명, 사용자의 신원을 확인하는 프로세스
- Authorization : 권한부여, 누가 무엇을 할 수 있는지 권한을 부여하는 프로세스
- http의 stateless 특성으로 인해 클라이언트 요청을 인증/인가하기 위한 방법이 필요함
- 인증/인가를 위한 방법으로 크게 다음과 같이 3가지가 있음
- #### 2-1. Cookie
- 웹 브라우저의 쿠키 저장소에 쿠키를 저장해놓고 이 쿠키를 통해 인증
- 웹 브라우저는 서버에 요청을 할 때, 자동으로 쿠키 저장소의 쿠키를 http 헤더에 넣어서 전달

```
http 쿠키 관련 헤더
- Set-Cookie : 서버에서 클라이언트로 쿠키 전달 (응답)
- Cookie : 클라이언트가 서버에서 받은 쿠키를 저장하고, HTTP 요청 시 서버로 전달
- Secure : https인 경우에만 전송 (기본 동작은 http, https 구분 안 함)
- HttpOnly : XSS 공격 방지, 자바스크립트에서 이 쿠키에 접근 불가, http 전송에만 사용
- SameSite : XSRF 공격 방지, 요청 도메인과 쿠키에 설정된 도메인이 같은 경우에만 쿠키 전송
```

- 장/단점

    - 쿠키 정보는 항상 서버에 전송이 되므로 네트워크 추가 트래픽을 유발한다. `최소한의 정보만 사용 예) 세션 ID, 인증 토큰 등`
    - 항상 서버에 전송하지 않고, 웹 브라우저 내부에 데이터를 저장하고 싶으면 웹 스토리지 `localStorage, sessionStorage`
    - 탈취, 조작 가능성이 있어 보안에 취약함

2. Cookie & Session

- 쿠키를 이용하되, 쿠키 정보를 웹 브라우저가 아닌 서버에 저장하는 방법
- 서버는 클라이언트 요청 시, 유저 인증 정보를 서버에 저장하고 이 값을 식별할 수 있는 ID를 쿠키로 내보낸다.
- 이후, 클라이언트에서는 요청을 보낼 때마다 ID를 쿠키 헤더에 담아서 요청한다.
- 장/단점

    - 세션 ID는 유의미한 정보를 가지고 있지 않기 때문에 유출에 상대적으로 안전하다.
    - 서버 측에서 정보를 관리하므로 데이터 유출/변조에 대한 걱정이 없다.
    - 세션 저장을 서버에서 하므로 요청이 많아지면 서버 부하가 심해진다.
    - 확장성이 좋지 않다.

```
세션은 서버에 저장되므로, 요청이 많아지면 서버에 과부하가 걸릴 수 있다.
-> 과부하를 줄이기 위해 서버를 여러대 둔다.
-> 서버마다 세션 정보를 동기화하는 데에 따른 추가 오버헤드가 발생한다.
```

3. Token

- 주로 JWT(Json Web Token)을 사용하여 구현
- 서버는 인증 토큰만 발급할 뿐, 어떠한 유저 정보를 저장하지 않는다.
- 확장성이 좋기 때문에 최근 대부분의 웹 서비스가 토큰 방식을 사용하고 있다.

```
flow
1. 최초 http 요청 시, 서버는 Access Token을 발급하여 헤더에 담아 응답으로 내보낸다.
2. 클라이언트는 발급된 토큰을 저장한다.
3. 클라이언트는 http 요청 시, token을 헤더에 포함시켜 보낸다.
4. 요청을 받은 서버는 헤더에 토큰을 검증하고, 해당 유저에 권한을 인가한다.
```

- 장/단점

    - 클라이언트에 저장되기 때문에, 서버에 데이터를 저장하는데에 따른 오버헤드가 없다.
    - Scale 확장성이 높다. `헤더에 저장된 Token으로 인증/인가를 처리하기 때문에 서버 확장에 대한 부담이 없다.`
    - 클라이언트에 저장되기 때문에, 공격에 노출될 가능성이 있다

### 3. Json Web Token

- JWT란 인증에 필요한 정보들을 암호화시킨 토큰을 의미
- JWT 기반 인증 방법은 JWT 토큰(Access Token)을 HTTP 헤더에 실어 서버가 클라이언트 정보를 인증/인가
- jwt 사용 시 주의할 점 https://www.kabisa.nl/tech/where-to-put-json-web-tokens-in-2019/
- 일반적으로 Access Token, Refresh Token을 함께 헤더에 저장하여 사용함

```
Aceess Token
1. 클라이언트 정보를 인증/인가 하는데 사용
2. 짧은 수명을 가지며, 만료기간을 가짐

Refresh Token
1. Access Token을 발급하기 위한 정보
2. 클라이언트의 Access Token이 없거나 만료되었을 때, Refresh Token으로 요청하여 Access Token을 발급 받음
3. 긴 수명
```

- Jwt는 .을 구분자로 나누어지는 세 가지 문자열의 조합이며, 각 파트는 다음과 같은 역할을 한다.

![jwt.png](etc/images/jwt.png)

1. Header

- alg : 해싱 알고리즘을 지정
- tye : 토큰의 타입

2. Payload

- 토큰에 담을 정보
- key-value로 이루어진 한 쌍의 정보를 Claim이라고 부르며, registered, public, private 세 종류의 클레임이 존재

```
registerd
- iss : 토큰 발급자
- sub : 토큰 제목
- aud : 토큰 대상자
- exp : 토큰 만료시간 (Numeric date)
- iat : 토큰이 발급된 시간

private
- 유저가 커스텀하게 생성하한 클레임 ex) "username" : "helloC"
```

3. Signature

- Header와 Payload의 base 64 인코딩 값을 합친 후, 서버의 비밀 키를 해쉬로 하여 암호화


### 4. Layered Architecture

#### 4-1. Layered Architecture 란 무엇인가?

- 복잡한 어플리케이션을 단순화하기 위해 기능 및 역할에 따라 계층화 한 구조를 말한다. (추상화)
- 계층화의 장점
    - 역할과 책임이 명확해짐 -> 유지보수 용이
    - 각 계층은 다른 곳에서 일어나는 일을 신경쓰지 않고, 자신이 맡은 역할에만 집중할 수 있음

> 예를들면, 표현 계층에서는 화면을 나타내는 데에만 집중할 수 있으며 데이터 저장 계층에서는 UI를 신경쓰지 않고 데이터를 저장하는데에만 집중할 수 있다.
>
> 마찬가지로 비즈니스 계층에서는 이 둘을 모두 몰라도 비즈니스 로직에만 집중할 수 있다.

- 본 프로젝트에서는 애플리케이션을 web - service - repository 로 계층화하였으며, 이 들 사이에 Dto, Domain Model이 존재한다.

![spring-web-layer.png](etc/images/spring-web-layer.png)

#### 4-2. Web Layer

- 이 계층에서의 역할은 다음과 같다.
    - 외부 요청/응답 핸들링 및 예외처리 (Filter, Interceptor, ControllerAdvice, ...)
    - Json 파라미터를 객체로 변환
    - 모델/뷰 생성 및 응답
- Controller 등 전반적인 외부 요청/응답에 대한 처리를 담당한다.
- 일반적으로 말하는 Presentation Layer와 유사하다.

> 서버사이드 렌더링의 경우 jsp, thymeleaf 등과 같은 템플릿 엔진을 통해서 View를 구성할 수 있지만 클라이언트 사이드 렌더링의 경우 별도의 view가 존재하지 않으므로 Data Transfer Object(데이터 전송용 모델)을 만들어 응답한다.


#### 4-3. Service Layer

- 이 계층에서의 역할은 다음과 같다.
    - 약한 비즈니스 로직
    - 트랜잭션과 도메인 로직 간의 순서를 조정
- 본 프로젝트에서는 서비스 계층이 모든 비즈니스 로직을 담당하지 않는다. 비즈니스 로직은 실세계를 사상한 객체에서 처리하며 서비스 계층에서는 해당 도메인들 간의 순서를 조정해주는 임무를 맡는다.

> 서비스 계층에서 모든 비즈니스 로직을 담당해버리면, 객체란 단순히 데이터 덩어리 역할만 담당하게 되며 객체지향적인 코드라 할 수 없다.
>
> 객체란 실세계에 존재하는 물체를 추상화하여 가상세계에 사상한 것을 말하며, 속성과 행위를 가진다. 예를들어, 실제 사물 '자동차'를 객체화 해보면 자동차의 속도, 이름 등이 속성이 될 수 있으며 시동, 브레이크 등이 행위가 될 수 있다.
>
> 따라서, 객체란 단순히 데이터 덩어리 역할만 해서는 안되며 "행위"를 가져야 하고 이 행위가 비즈니스 로직이라 할 수 있다.


#### 4-4. Repository Layer

- 이 계층에서의 역할은 다음과 같다.
    - 데이터베이스에 접근하여 리소스를 다룸
- 일반적으로 말하는 Persistance Layer 이며, DAO 영역이라 할 수 있다.


#### 4-5. Dto

- 데이터의 전송을 위한 객체
- Dto <-> Entity 간에는 서로 변환될 수 있다.
- 본 프로젝트에서 Dto는 외부 요청/응답에 사용되며 서비스 계층까지 들어오지 못하도록 설계되었지만, 예외는 존재할 수 있다.

> 왜 Dto와 Entity를 구분하여야 하는가?
>
> 1. Entity 와 api 사이에 의존관계를 막기 위해
>
> - 만약 Dto를 사용하지 않는다면, Entity의 변경에 의해 api 스펙이 변경되어 버릴 수가 있다. api는 프론트 서버나 혹은 다른 서버간에 사용되고 있기 때문에 치명적이게 된다.
> - 각각의 api 마다 요구하는 엔티티의 데이터가 모두 다르기 때문에 dto를 통해서 필요한 부분만 가져올 수 있어야한다.


#### 4-6. Domain Model

- 소프트웨어로 해결하고자 하는 문제 영역으로서, 개발자 모두가 동일한 관점에서 이해할 수 있고 공유될 수 있어야한다.
- 실제 세계의 속성과 행위를 추상화한 객체를 문제 해결의 관점에서 재구성한 것이다.

> 도메인이란 무엇인가?
>
> 어떤 개발자들이 수동으로 이루어지는 버스 예약 프로세스를 소프트웨어로 해결한다고 가정해보자.
>
> 직면한 문제는 다음과 같다. : **어떻게 수동 예약을 자동화할 것인가?**
>
> 그렇다면, 문제해결을 위해 당면한 문제에 존재하는 실제 사물들을 추상화하여 도메인 객체로 만들 필요가 있다. 문제해결의 관점에서 본 도메인 모델은 다음과 같다.
>
> ```
> 고객
> - 속성 : 이름, 돈
> - 행위 : 예약한다, 취소한다,
>
> 버스
> - 속성 : 탑승인원,
> - 행위 : 출발한다, 도착한다
>
> 티켓
> - 속성 : 출발시간, 도착시간, 예약날짜
> - 행위 : 
>
> ```
>
> 즉, 도메인 모델은 반드시



## 출처

- REST API

    - https://ko.wikipedia.org/wiki/REST
- Hypertext Application Language

    - https://en.wikipedia.org/wiki/Hypertext_Application_Language
    - https://stateless.group/hal_specification.html
- Authentication & Authorization

    - https://tecoble.techcourse.co.kr/post/2021-05-22-cookie-session-jwt/
- Json Web Token

    - https://velopert.com/2389
    - https://jwt.io/
    - https://www.kabisa.nl/tech/where-to-put-json-web-tokens-in-2019/
    - https://velog.io/@kyeongsoo5196/JWT%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%ED%94%8C%EB%A1%9C%EC%9A%B0-%EC%97%B0%EA%B5%AC
    - https://cotak.tistory.com/102
- Layered Architecture

    - https://martinfowler.com/bliki/PresentationDomainDataLayering.html
    - https://www.javatpoint.com/spring-boot-architecture
    - https://martinfowler.com/eaaCatalog/serviceLayer.html
    - https://books.google.co.kr/books?id=acG8ywEACAAJ&dq=spring+boot+web+service&hl=ko&sa=X&ved=2ahUKEwjk38_wq9z0AhWpsVYBHTlbCzIQ6AF6BAgDEAE

# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.5/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.5/gradle-plugin/reference/html/#build-image)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.5.5/reference/htmlsingle/#boot-features-security)
* [Spring HATEOAS](https://docs.spring.io/spring-boot/docs/2.5.5/reference/htmlsingle/#boot-features-spring-hateoas)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.5.5/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.5.5/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.5.5/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides

The following guides illustrate how to use some features concretely:

* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Building a Hypermedia-Driven RESTful Web Service](https://spring.io/guides/gs/rest-hateoas/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project&#39;s build](https://scans.gradle.com#gradle)
