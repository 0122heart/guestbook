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

## trouble shooting 기록
<2025.11.20>
- trouble : GetMapping, PostMapping, DeleteMapping은 다 할 줄 아는데 PatchMapping은 어떻게 해야하지?