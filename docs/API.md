



## 백엔드 API

- user
POST /api/sign-in : 로그인 버튼
POST /api/sign-up : 회원가입 버튼
POST /api/sign-up/duplicate-check/login-id : 로그인 아이디 중복체크
POST /api/sign-up/duplicate-check/nickname : 닉네임 중복 체크

- guestbook
GET /api/guestbook/{nickname}
POST /api/guestbook : 글 작성 버튼
PATCH /api/guestbook/{guestbook-id} : 글 수정 버튼
DELETE /api/guestbook/{guestbook-id} : 글 삭제 버튼
POST /api/guestbook/{guestbook-id}/comment : 댓글 작성 버튼
PATCH /api/guestbook/comment/{comment-id} : 댓글 수정 버튼
DELETE /api/guestbook/comment/{comment-id} : 댓글 삭제 버튼

- friend
POST /api/friend/{accept} : 친구 수락/거절 버튼
GET /api/friend/{nickname} : 유저의 친구목록 보기 버튼
POST /api/friend/request : 친구 요청 버튼