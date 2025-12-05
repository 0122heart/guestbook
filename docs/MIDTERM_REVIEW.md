# SNS 프로젝트 중간 점검 (Midterm Review)

> **작성자:** 박의진  
> **프로젝트 주제:** Spring Boot 기반의 SNS 프로토타입 (like 싸이월드, 페이스북)  
> **작성일:** 2025.12.05

---

## 1. 프로젝트 개요 (Overview)
* **기획 의도:** SNS의 핵심 기능인 **CRUD**를 직접 구현하며 Spring Boot 학습.
* **개발 목표:** 친구들과 교류할 수 있는 SNS 프로토타입 완성.
* **타겟 유저:** 지인들과 소통하고 일상을 공유하고 싶은 사용자.

## 2. 기술 스택 (Tech Stack)
* **Frontend:** React.js (Claude AI 활용), Axios
* **Backend:** Spring Boot, Spring Security (Password Encryption), SessionScope
* **Database:** H2 Database (초기 MySQL 고려하였으나 개발 편의성을 위해 변경)
* **Tools:** H2 Console, ERDCloud

## 3. 핵심 기능 (Key Features)

### 회원 관리 (User)
- **회원가입/로그인:** 비밀번호 암호화 적용 (Spring Security), 세션 기반 로그인 유지 (SessionScope).
- **프로필:** 프로필 조회 및 정보 변경.
- **이메일 인증:** 가입 시 본인 인증 기능 (미구현).

### 방명록 (Guestbook)
- **CRUD:** 방명록 작성, 조회, 수정, 삭제.
- **페이징:** Spring Framework의 `Page` 인터페이스를 활용한 목록 조회.
- **댓글:** 방명록 게시글에 대한 댓글 작성/삭제.

### 친구 관리 (Friend)
- **친구 요청 흐름:** 요청 -> 수락/거절 -> 친구 맺기.
- **친구 목록:** 조회 및 삭제 기능.

## 4. 아키텍처 및 설계 (Architecture)
* **패키지 구조:** 계층형(Layered)이 아닌 **기능별(Feature-based)** 패키징 적용.
    * `friend/`, `user/`, `guestbook/` 내부에서 Controller, Service, Repository 분리.
* **ERD 설계:**
    * Entity: `User`, `Guestbook`, `FriendRequest`, `Friend`, `Comment`
* **API 연동:** Spring Boot(8080) <-> React(3000) 연동 설정 완료.

## 5. 트러블 슈팅 및 한계점 (Issues & Limitations)
개발 과정에서 마주친 문제점과 추후 보완해야 할 기술적 과제입니다.

### 🚧 Backend
1.  **Spring Security:** 현재 비밀번호 암호화 외에는 `permitAll` 상태. 인가(Authorization) 로직 고도화 필요.
2.  **Transaction 관리:** `@Transactional`의 정확한 적용 범위와 시점에 대한 학습 필요.
3.  **DTO & Entity:** DTO와 Entity 간의 변환 로직 및 의존성 점검 필요.
4.  **Logging:** 체계적인 로그 전략 부재. (System.out 지양 및 Slf4j 활용 학습 필요)
5.  **JPA:** 심도 있는 이해 부족. (향후 김영한님 강의/서적을 통해 영속성 컨텍스트 등 학습 예정)

### Frontend
1.  **AI 의존도:** UI 로직의 95%를 AI(Claude)에 의존. 기본적인 React 동작 원리 학습의 필요성을 느낌.

## 6. 향후 계획 (Next Step)
* 이메일 인증 기능 추가 구현.(미정)
* 트랜잭션 및 예외 처리(Exception Handling) 로직 리팩토링.
* JPA 심화 학습을 통한 쿼리 최적화.

---
**Review Conclusion:** 백엔드 로직 구현에 집중하였으나, 시각적인 결과 확인을 위해 AI를 활용해 프론트엔드를 빠르게 구축한 점이 효율적이었음. 남은 기간 동안 '돌아가기만 하는 코드'에서 '견고한 코드'로 리팩토링하는 데 집중할 예정.