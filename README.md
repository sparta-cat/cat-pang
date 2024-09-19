# 🛠️ MSA 기반 물류 관리 및 배송 시스템

## 📋 목차

1. [프로젝트 개요](#프로젝트-개요)
2. [역할 분담 및 MSA 서비스 설명](#역할-분담-및-msa-서비스-설명)
3. [프로젝트 핵심 사용자 유스케이스](#프로젝트-핵심-사용자-유스케이스)
4. [적용 기술](#적용-기술)
5. [프로젝트 구성](#프로젝트-구성)
    - [인프라 아키텍처](#인프라-아키텍처)
    - [멀티 모듈 구조](#멀티-모듈-구조)
    - [ERD](#erd)
6. [트러블슈팅](#트러블슈팅)
7. [API명세서 및 기술스택](#기능-상세-설명)

---

## 1️⃣ 프로젝트 개요

본 프로젝트에서는 **MSA(Microservices Architecture) 기반 물류 관리 및 배송 시스템**을 설계하고 구현하였습니다. <br>
**유레카 서버를 통해 각 애플리케이션을 관리**하고, **게이트웨이를 통해 모든 인입을 제어**하며, 모든 애플리케이션은 **Docker 컨테이너**로 실행됩니다.

---

## 2️⃣ 역할 분담 및 MSA 서비스 설명

### 역할 분담

| 이름  | 담당 역할                                          |
|-----|------------------------------------------------|
| 박태언 | 멀티 그래들 프로젝트 구조와 인프라 설계 및 구축, 주문 및 배송 관리 서비스 개발 |
| 이재구 | 물류 허브 및 업체 관리 서비스 개발, 상품 관리 서비스 개발             |
| 김남혁 | 인증 및 권한 관리, API Gateway 설정, 사용자 관리             |

### 각 서비스 설명

1. **유레카 서버:** 마이크로서비스 등록 및 탐색.
2. **API Gateway:** 외부 요청을 각 마이크로서비스로 라우팅.
3. **허브 관리 서비스:** 허브 관련 서비스 관리.
4. **주문 관리 서비스:** 주문 처리 관련 서비스 관리.
5. **배송 관리 서비스:** 주문 및 배송 정보 처리 및 최적의 경로 관리.
6. **사용자 관리 서비스:** 배송 담당자 및 업체 사용자 관리. JWT 토큰 관리.

---

## ️3️⃣ 프로젝트 핵심 사용자 유스케이스

### 🛻주문 기능

- **주문 발생**
    - **주문 업체:** A업체는 B업체에 상품을 주문합니다.
    - **생산 업체:** 이에 따라 B업체는 본 서비스에 배송을 요청합니다.

- **물류 처리 및 재고 이동**
    - **B업체 지역을 담당하고 있는 허브에서 A업체 담당 허브로 물품 이동:** 주문을 처리하기 위해 A업체 담당 허브에서 B업체 담당 허브로 상품이 이동됩니다. 이때 주어진 경로에 따라 상품이 이동됩니다.

- **최종 수령**
    - **A업체 담당 허브에서 A업체로 물품 배송:** A업체 담당 허브에 물품이 도착하면, A업체 담당 허브 소속 배송 담당자가 물품을 A업체로 배송합니다.

###     

---

## 4️⃣ 적용 기술

### 🎎**Multie Modules**

본 프로젝트는 MSA와 모놀리식 구조의 장점을 결합한 멀티 모듈 구조를 채택하였습니다.
</br>
모놀리식 구조의 장점을 의존성 관리등 개발시 난이도를 조절하고, 각 모듈을 독립적으로 개발하고 유지보수할 수 있는 MSA의 유연성을 도입하여 운영의 복잡성을 줄였습니다.
</br>
이를 통해 모듈별로 명확한 책임을 부여하고, 기능 확장 및 변경 사항이 발생하더라도 안정적인 운영이 가능하도록 설계되었습니다.

### 🔍**QueryDSL**

- 페이징, 정렬, 검색어 등에 따른 동적 쿼리 작성을 위하여 QueryDSL을 도입하여 활용했습니다.
- 컴파일 시점에 오류를 발견할 수 있어, 런타임 에러를 줄일 수 있었습니다.
- Query DSL을 통해 객체 지향 프로그램의 장점을 활용해 타입 안전성을 보장할 수 있었습니다.

### 📜**Swagger**

- **문서화:** SwaggerUI를 사용하여 각각의 API 문서를 자동으로 생성할 수 있었습니다.
- **시각화:** API엔드포인트, 매개변수, 요청 및 응답 본문 등을 시각적으로 확인할 수 있었습니다.
- **테스트:** 각각의 API를 테스트할 수 있었습니다.
- **팀 간의 협업 용이:** API문서를 중앙 집중식으로 관리하여 팀원 간의 협업을 용이하게 진행할 수 있었습니다.

### 🔐**JWT (JSON Web Token)**

- **무상태 인증:** JWT를 사용하여 세션 상태를 유지하지 않고도 사용자 인증을 처리할 수 있었습니다.
- **Self-contained 토큰:** 토큰 자체에 사용자 정보 및 권한 정보가 포함되어 있어, 서버가 별도로 상태를 저장하지 않고도 인증을 수행할 수 있었습니다.
- **보안성:** JWT는 서명을 통해 토큰의 무결성을 보장하며, 만료 시간을 설정하여 토큰의 유효성을 관리할 수 있었습니다.
- **확장성:** 분산 시스템 환경에서 JWT를 사용함으로써, 서버 간 세션 공유 없이 확장 가능한 인증 구조를 구현할 수 있었습니다.

### 🟥**Redis**

- **토큰 관리**: **Refresh Token**을 Redis에 저장하고 관리함으로써, JWT가 만료되었을 때 효율적으로 토큰을 갱신할 수 있었습니다.
- **캐싱을 통한 빠른 데이터 조회**: 자주 조회되는 데이터(예: 사용자 정보 등)를 Redis에 캐싱하여, 데이터베이스 접근을 줄이고 빠른 응답 시간을 제공하였습니다. 이를 통해 성능을 크게 향상시켰습니다.
- **TTL 설정**: Redis에서 캐싱된 데이터와 토큰에 **TTL**을 설정하여, 만료된 데이터가 자동으로 삭제되도록 함으로써 데이터의 유효성을 유지했습니다.

---

## 5️⃣ 프로젝트 구성

### 3.1 인프라 아키텍처

***➡️사용자 API 요청 흐름***
<br>
로그인 후 사용자가 API요청을 보내면, <br>
Gateway에서 Auth서비스(User서비스)로 토큰 유효성 검사를 요청.<br>
Auth서비스에서 유효한 토큰임이 확인되면, id와 role 값을 반환. <br>
Gateway에서 반환받은 값을 Header에 넣고, 요청 엔드포인트로 라우팅. <br>

### 3.2 멀티 모듈 구조

```bash
프로젝트/
│
├── eureka/
├── gateway/
├── config/
├── core/
├── securitydb/
├── user/
├── auth/
├── order/
├ ├── delivery/
├── hub/
  ├─── company/
  ├─── product/

```

### 3.3 ERD

<img src="">

---

## 6️⃣ 트러블 슈팅

<details>
<summary> 1. 내부 포트 문제</summary>

내부 포트 문제는 두 가지로 나누어집니다:

#### 1.1 **Docker 컨테이너 간 네트워크 통신 문제**

- **문제 상황**: Docker 네트워크에 속한 컨테이너들끼리 통신할 때, `service_name:내부포트` 형식을 사용해야 하지만, 이 부분이 잘못 설정되어 통신이 이루어지지 않았습니다.
- **해결 방법**: 컨테이너 간 올바른 네트워크 통신을 위해, `service_name:내부포트`로 접근하도록 설정을 수정하여 문제를 해결했습니다.

#### 1.2 **Postgres 내부 포트 변경 문제**

- **문제 상황**: Postgres의 기본 내부 포트(5432)를 다른 포트로 변경하면서 데이터베이스가 정상적으로 작동하지 않는 문제가 발생했습니다.
- **해결 방법**: Postgres의 내부 포트를 다시 5432로 복구하여 데이터베이스가 정상적으로 작동하도록 문제를 해결했습니다.

</details>

<details>
<summary> 2. Docker 캐시 문제</summary>

- **문제 상황**: Docker 이미지를 빌드할 때, 이전에 빌드된 이미지가 캐시로 인해 업데이트되지 않는 문제가 발생했습니다.
- **해결 방법**:
    - `no-cache` 옵션을 사용하여 Docker 이미지를 새롭게 빌드함으로써 캐시 문제를 해결했습니다.
    - `docker compose system prune -a` 명령어와 `docker compose down -v` 명령어를 사용하여 캐시를 정리하고 문제를 해결했습니다.

</details>

<details>
<summary> 3. 헬스 체크 문제</summary>

- **문제 상황**: 헬스 체크(Health Check) 기능에서 문제가 발생하여 서비스가 정상적으로 작동하지 않았습니다.
- **해결 방법**: Docker Compose 설정에서 누락된 필드를 추가하여 헬스 체크 문제를 해결했습니다.

</details>

<details>
<summary> 4. Security Context가 유지 되지 않는 문제 </summary>

- **문제 상황**
    - **원 인증, 인가 흐름:**
        1. 로그인 후 API 요청
        2. **Gateway에서 Auth서비스로** 토큰 유효성 검사 및 인가 요청
        3. Auth 서비스에서 토큰을 기반으로 **Authentication객체를 생성하여 SecurityContext생성**
        4. Gateway에서 API요청대로 **라우팅**
           <br></br>
- **해결 방법**
    - **설계 변경**
        1. 로그인 후 API 요청
        2. Gateway에서 **Auth서비스(User서비스)로 토큰 유효성 검사** 요청
        3. 유효성 검사 완료 후 **토큰에서 ID와 Role을 추출**해 Gateway에 반환
        4. Gateway에서 받은 값들을 **헤더에 넣고 라우팅**
        5. SecurityDB모듈(Core모듈)에서 SecurityFilterChain에 추가한 OncePerRequestFilter를 상속한 **AuthorizationFilter를 통해
           SecurityContext를
           생성**
        6. 이후 **MSA서비스의 엔드포인트에 도달**하여 로직이 실행

</details>

---

## 7️⃣ API명세서 및 기술스택

<span>
<a href="https://www.notion.so/teamsparta/API-c6aaf710f30647cfa1e940338f9a5e14" style="text-decoration: none;">
        <img src="https://img.shields.io/badge/Notion-000000?style=flat&logo=Notion&logoColor=white"/>
</a>
<a href="">
        <img src="https://img.shields.io/badge/Swagger-%238DC16F.svg?style=flat&logo=swagger&logoColor=white"/>
</a>
<div>API 명세서</div>
</span>


<br>
---
<p >
    <img src="https://img.shields.io/badge/JAVA-%23007396.svg?style=flat&logo=java&logoColor=white"/>
    <img src="https://img.shields.io/badge/SpringBoot-%236DB33F.svg?style=flat&logo=springboot&logoColor=white"/>
    <img src="https://img.shields.io/badge/SpringSecurity-%236DB33F.svg?style=flat&logo=springsecurity&logoColor=white"/>
    <img src="https://img.shields.io/badge/JSONWebToken-%23000000.svg?style=flat&logo=jsonwebtokens&logoColor=white"/>
    <br/>
    <img src="https://img.shields.io/badge/PostgreSQL-316192?style=flat&logo=postgresql&logoColor=white"/>
    <img src="https://img.shields.io/badge/Swagger-%238DC16F.svg?style=flat&logo=swagger&logoColor=white"/>
    <img src="https://img.shields.io/badge/Gradle-%2302303A.svg?style=flat&logo=gradle&logoColor=white"/>
    <br/>
    <img src="https://img.shields.io/badge/Docker-%232496ED.svg?style=flat&logo=docker&logoColor=white"/>
    <img src="https://img.shields.io/badge/Git-%23F05033.svg?style=flat&logo=git&logoColor=white"/>
    <img src="https://img.shields.io/badge/GitHub-%23181717.svg?style=flat&logo=github&logoColor=white"/>
    <br/>
    <img src="https://img.shields.io/badge/IntelliJ%20IDEA-%23000000.svg?style=flat&logo=intellijidea&logoColor=white"/>
    <img src="https://img.shields.io/badge/Postman-%23FF6C37.svg?style=flat&logo=postman&logoColor=white"/>
    <img src="https://img.shields.io/badge/Notion-%23000000.svg?style=flat&logo=notion&logoColor=white"/>
    <img src="https://img.shields.io/badge/Slack-%234A154B.svg?style=flat&logo=slack&logoColor=white"/>
</p>










