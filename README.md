# 커뮤니티 [BE]

## 🛠️ 기술 스택
- Java 21
- Spring Boot 3.4.3
- MySQL 9.2.0
- Spring Data JPA
- Gradle

## 📁 프로젝트 구조
```
ktb
 ├── auth
 │   ├── config
 │   │   ├── JwtAuthenticationFilter
 │   │   ├── JwtProvider
 │   │   ├── SecurityConfig
 │   ├── dto
 │   │   ├── LoginRequestDto
 │   │   ├── LoginResponseDto
 │   ├── AuthController
 │   ├── AuthService
 │
 ├── comment
 │   ├── dto
 │   │   ├── CommentListResponseDto
 │   │   ├── CommentRequestDto
 │   │   ├── CommentResponseDto
 │   ├── Comment
 │   ├── CommentController
 │   ├── CommentRepository
 │   ├── CommentService
 │   ├── CommentServiceImpl
 │
 ├── common
 │   ├── config
 │   │   ├── CommonProperties
 │   │   ├── WebConfig
 │   ├── dto
 │   │   ├── ApiResponseDto
 │   ├── exception
 │   │   ├── CustomException
 │   │   ├── ErrorMessage
 │   │   ├── GlobalExceptionHandler
 │   ├── FileService
 │
 ├── post
 │   ├── dto
 │   │   ├── PostListResponseDto
 │   │   ├── PostRequestDto
 │   │   ├── PostResponseDto
 │   ├── Post
 │   ├── PostController
 │   ├── PostService
 │   ├── PostServiceImpl
 │
 ├── postRecommendation
 │   ├── PostRecommendation
 │   ├── PostRecommendationRepository
 │   ├── PostRecommendationService
 │
 ├── user
 │   ├── dto
 │   │   ├── UserRequestDto
 │   │   ├── UserResponseDto
 │   ├── User
 │   ├── UserController
 │   ├── UserRepository
 │   ├── UserService
 │   ├── UserServiceImpl
 │
 ├── KtbApplication

```
기본적인 `Entity`, `Controller`, `Service`, `Repository`는 도메인형으로 두어 한 눈에 보기 좋게 구별하였고   
`dto`나 `config`, `exception` 같은 부분은 주요 파일들(MVC)과 섞이지 않도록 따로 폴더를 두었습니다.   

## 🎯 ERD 설계
<img width="949" alt="image" src="https://github.com/user-attachments/assets/d16253e8-d63e-43ec-86af-c161e18dc127" />



## 📌 API 엔드포인트 정리
### 회원 (User)
| 기능                | 메서드 | 엔드포인트             | 설명                          |
|-------------------|--------|----------------------|-----------------------------|
| 회원 가입           | `POST` | `/users`             | 신규 회원 등록              |
| 회원 탈퇴         | `DELETE` | `/users`               | 회원 탈퇴              |
| 이메일 중복 체크       | `GET` | `/users/check-email`     | 이메일 중복 방지용 체크      |
| 닉네임 중복 체크       | `GET` | `/users/check-nickname`   | 닉네임 중복 방지용 체크   |
| 로그인              | `POST` | `/auth/sessions`     | 로그인 처리            |
| 로그아웃             | `DELETE` | `/auth/sessions`   | 로그아웃 처리            |
| 회원정보 조회         | `GET` | `/users/{userId}`     | 회원 정보 조회              |
| 회원정보 수정         | `PATCH` | `/users/{userId}`    | 회원 정보 수정              |
| 비밀번호 수정         | `PATCH` | `/users/{userId}/password` | 회원 비밀번호 수정       |

### 게시물 (Post)
| 기능                | 메서드 | 엔드포인트             | 설명                          |
|-------------------|--------|----------------------|-----------------------------|
| 게시물 작성       | `POST` | `/posts`             | 새로운 게시물 등록            |
| 게시물 목록 조회       | `GET`  | `/posts`        | 게시물 목록 전체 조회         |
| 게시물 단일 조회       | `GET`  | `/posts/{postId}`        | 특정 게시물 상세 조회         |
| 게시물 수정       | `PATCH`  | `/posts/{postId}`        | 특정 게시물 수정              |
| 게시물 삭제       | `DELETE` | `/posts/{postId}`      | 특정 게시물 삭제              |
| 게시물 추천       | `POST` | `/posts/{postId}/recommendation`      | 특정 게시물 추천              |

### 댓글 (Comment)
| 기능                | 메서드 | 엔드포인트             | 설명                          |
|-------------------|--------|----------------------|-----------------------------|
| 댓글 작성        | `POST` | `/posts/{postId}/comments`          | 게시물에 댓글 추가            |
| 댓글 목록 조회    | `GET`  | `/posts/{postId}/comments` | 특정 게시물의 댓글 리스트 조회 |
| 댓글 단일 조회    | `GET`  | `/posts/{postId}/comments/{commentsId}` | 특정 게시물의 특정 댓글 리스트 조회 |
| 댓글 수정    | `PATCH`  | `/posts/{postId}/comments/{commentsId}` | 특정 게시물의 특정 댓글 수정 |
| 댓글 삭제        | `DELETE` | `/posts/{postId}/comments/{commentsId}`    | 특정 댓글 삭제                |

- Soft Delete
  - 모든 DELETE 메서드 API에 대하여 `Soft Delete` 를 적용하여 실제 운영 시 발생할 수 있을 '데이터 복구'에 대응하였습니다.
  - 해당 서비스는 물론 개인 프로젝트로 마무리 짓겠지만, 추후 팀 프로젝트에서 `Soft Delete`를 사용할 부분이 반드시 발생할거라 생각하여 이번 기회에 시도해보았습니다.
- JWT
  - 사용자 로그인/로그아웃 시에 JWT를 활용하여 보안을 강화하였습니다.
  - `OncePerRequestFilter` 클래스를 상속받아 매 요청마다 JWT를 확인하였고,
 
## 보완할 부분
- [ ] JWT Refresh Token 관련 처리
- [ ] Exception에 대한 ENUM 처리
- [ ] 중복 코드 리팩터링
- [ ] 고도화 진행 (동시성 관련)

## 회고
