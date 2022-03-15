# hello rest

hello rest 프로젝트는 rest 아키텍처를 따르는 api server를 구현한 프로젝트입니다. 대부분의 rest api가 사실 rest 아키텍처 설계 원칙을 만족시키지 못합니다. 
이러한 점에 동기가 생겨 "한 번 로이 필딩이 제시한 진짜 rest 아키텍처르 따르는 api 서버를 만들어보자"는 목적으로 시작하게 되었습니다.

## 개발 전 배경지식

### 1. rest 아키텍처

#### 1-1. REST API 의 목표

- 구성 요소 상호작용의 규모 확장성(scalability of component interactions)
- 인터페이스의 범용성 (Generality of interfaces)
- 구성 요소의 독립적인 배포(Independent deployment of components)
- 중간적 구성요소를 이용해 응답 지연 감소, 보안을 강화, 레거시 시스템을 인캡슐레이션 (Intermediary components to reduce latency, enforce security and encapsulate legacy systems)

#### 1-2. REST 아키텍처 스타일

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

#### 1-3. Uniform Interface

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

