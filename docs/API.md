- main
/main : 초기 화면

- user
POST /api/sign-in : 로그인 버튼
POST /api/sign-up : 회원가입 버튼
POST /api/sign-up/duplicate-check/login-id : 로그인 아이디 중복체크
POST /api/sign-up/duplicate-check/nickname : 닉네임 중복 체크

- guestbook
GET /api/guestbook/{user-nickname}
POST /api/guestbook : 글 작성 버튼
PATCH /api/guestbook : 글 수정 버튼
DELETE /api/guestbook : 글 삭제 버튼
/guestbook/post_comment : 댓글 작성 버튼
/guestbook/patch_comment : 댓글 수정 버튼
/guestbook/delete_comment : 댓글 삭제 버튼

- friend
POST /api/friend/accept-or-reject : 친구 수락/거절 버튼
POST /api/friend-list : 유저의 친구목록 보기 버튼
POST /api/friend-request : 친구 요청 버튼