
### Folder Structure
### 공통 폴더 구조
- `common`: 메인 로직은 아니지만 `src` 에서 필요한 부차적인 파일들을 모아놓은 폴더
  - `config`: 설정 파일 관련 폴더
  - `entity`: 공통 Entity 관리 폴더
  - `exceptions`: 예외처리 관리 폴더
  - `oauth`: Oauth 인증에 필요한 파일 관리 폴더
  - `response`: baseResponse를 관리하는 폴더
  - `secret`: 보안과 관련된 파일 관리 폴더(차후 환경 변수로 분리 추천)


### 도메인 폴더 구조
> Controller - Service - Repository

- `Controller`: Request를 처리하고 Response 해주는 곳. (Service에 넘겨주고 다시 받아온 결과값을 형식화), 형식적 Validation
- `Service`: 비즈니스 로직 처리, 논리적 Validation
- `Repository`: Spring Data JPA Interface를 상속받아 DB 처리를 가능하게 해준다.


## ✨Structure
```text
api-server-spring-boot
  > gradle
  > src.main.java.com.example.demo
    > common
      > config
        | RestTemplateConfig.java // HTTP get,post 요청을 날릴때 일정한 형식에 맞춰주는 template
        | SwaggerConfig.java // Swagger 관련 설정
        | WebConfig.java // Web 관련 설정(CORS 설정 포함)
        | JasyptConfig.java // 프로퍼티 설정 암호화 설정
        | SecurityConfig.java // spring seucurity 설정 
       
      > entity
        | BaseEntity.java // create, update, state 등 Entity에 공통적으로 정의되는 변수를 정의한 BaseEntity
      > exceptions
        | BaseException.java // Controller, Service에서 Response 용으로 공통적으로 사용 될 익셉션 클래스
        | ExceptionAdvice.java // ExceptionHandler를 활용하여 정의해놓은 예외처리를 통합 관리하는 클래스
      > jpa
        | AESConverter.java // Entity 데이터의 일부 필드들을 AES256 방식의 암호화 및 복호화를 적용하여 변환하는 클래스
      > oauth
        | GoogleOauth.java // Google OAuth 처리 클래스
        | KaKaoOauth.java // Kakao OAuth 처리 클래스
        | OAuthService.java // OAuth 공통 처리 서비스 클래스
        | SocialOauth.java // OAuth 공통 메소드 정의 인터페이스
      > response
        | BaseResponse.java // Controller 에서 Response 용으로 공통적으로 사용되는 구조를 위한 모델 클래스
        | BaseResponseStatus.java // Controller, Service에서 사용할 Response Status 관리 클래스 
      > 
      | 기타 enum 및 공통 상수 클래스
    > src
      > log - 로그 관련 패키지 - 미완
      > payment - 결제 관련 패키지 -미완
      > subscription - 구독 관련 패키지 -미진행
      > test
        > entity
        > model
      > token
        > controller
          | TokenController.java - access 토큰 발급용 컨트롤러
        > entity
          | RefreshToken.java
        > model
        > repository
          | RefreshTokenRepository.java
        > service
          | RefreshTokenService.java - refresh token 검증용 서비스
          | TokenService.java
        | TokenAuthenticationFilter.java - 토큰 인증 필터
      > user
        > controller
          | OauthLoginController - 소셜 로그인 버튼 및 redirect 처리용 컨트롤러
          | UserController 
        > entity
          | User.java // User Entity
          | SocialUser.java // 소셜 로그인 용 유저 Entity
        > model
          | ..
          | SecurityUser - UserDetails 구현체 
        > repository
          | SocialUserRepository
          | UserRepository
        > UserService
          | OauthUserService
          | UserService
        | CustomAuthenticationSuccessHandler - oauth와 일반 로그인 성공 후처리 핸들러
        | SecurityUserDetailsService - UserDetailsService 구현체
        | ServiceTermsScheduler - 사용자 약관 동의 약관 기관 만료시 동의 항목을 제거하는 스케쥴러
    > utils
      | AES256.java // AES 암,복호화 클래스
      | JwtService.java // JWT 관련 클래스
      | SHA256.java // 암호화 알고리즘 클래스
      | ValidateRegex.java // 정규표현식 관련 클래스
    | DemoApplication // SpringBootApplication 서버 시작 지점
  > resources
    | application.yml // 애플리케이션 전체적인 설정
    | logback-spring.xml // logback 설정 xml 파일
build.gradle // gradle 빌드시에 필요한 dependency 설정하는 곳
.gitignore // git 에 포함되지 않아야 하는 폴더, 파일들을 작성 해놓는 곳
.gitmodules // git 서브 모듈 - git public 레파지토리에 공유되어선 안될 설정 파일들을 관리하기 위해 설정된 모듈을 세팅해둔 곳

```

## ✨실행
`Jasypt`설정에 의해 대부분의 노출되어선 안될 민감 정보들이 암호화 되었으나
그 암호화의 키가될 Jasypt  암호화 키만은 노출되어 있으므로 주의해서 실행할 필요가 있다.

환경변수로 jasypt.encryptor.password={암호키}