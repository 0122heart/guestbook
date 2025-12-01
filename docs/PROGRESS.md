## 진행상황 기록

<2025.11.14>
- dictionary를 기능별로 나눌지 주제별로 나눌지 결정 -> 주제별로 나누기로
- signup 기능과 signin 기능을 합쳐서 user라는 주제로 통합
- user entity설계

<2025.11.17>
- ![전체 ERD 작성](./ERD/ERD_2.png)
- ERD에 맞게 스프링에서 entity 작성

<2025.11.18>
- user dictionary의 비즈니스 로직들 완성
- friend를 새로운 dictionary로 분리

<2025.11.19>
- friend 팩토리 1차 완성

<25.11.20>
- guestbook CRUD 1차 진행

<25.11.21>
- BE 1차 완성

<25.11.24>
- claud로 FE 구현 시작

<25.11.26>
- 여러 api들 RESTful하게 설정
- claud로 FE 구현 중

<25.11.27>
- @repository에서 여러 반환값들을 Optional로 설정
- @SessionScope를 통해 로그인 유지 구현
- h2 db를 메모리 모드가 아닌 파일 모드로 변경

<25.12.01>
- 친구 요청 및 리스트업 BE로직 수정
- BE수정에 맞춰서 FE 로직 수정
- 반환을 위한 여러 DTO 생성

## trouble shooting 기록
<2025.11.20>
- trouble : GetMapping, PostMapping, DeleteMapping은 다 할 줄 아는데 PatchMapping은 어떻게 해야하지?

<2025.11.26>
- trouble : API를 RESTful 원칙에 맞게 수정하려면 어떻게 설정해야하지

<25.11.27>
- trouble : @SessionScope를 설정했는데도 로그인이 유지가 안된다... why?
- solution : LoginPage.js에서 백엔드 통신 성공 후 localStorage에 해당 값을 저장하는 코드가 누락되어 있었음 => 이로 인해 서버 세션은 유효하지만, 클라이언트 라우터는 사용자를 '비로그인' 상태로 인식하여 접근을 차단 => 로그인 로직 수정

<25.12.01>
- trouble : 프론트를 잘 모르니 매번 클로드에게 의존하는 것도 힘드네.. 이번 프로젝트가 끝나면 react를 공부해봐야겠다