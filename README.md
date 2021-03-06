# 밸런스토론 Valto!

토론하면 떠오르는게 있나요? 아무래도 100분 토론이 떠오를 것입니다.
100분 토론에서 다루는 주제들은 정치, 경제, 외교 등 다소 무거운 주제가 많습니다.
Valto는 토론에 대한 무거운 이미지를 깨기 위해서 가벼운 주제로 가볍게 이야기할 수 있는 서비스 입니다.
WebRTC, WebSocket 을 사용해서 화상회의, 채팅기능을 구현했습니다.



## 특이사항

- 프로젝트가 실행되지 않습니다.
  프로젝트를 진행할 때, 삼성 청년 소프트웨어 아카데미에서 제공한 AWS 환경에서 진행했습니다.
  교육기간이 끝난 후 ( 21.12.31 ) AWS 환경을 사용할 수 없게 되었습니다.



## 개발 환경

### Tech Stack

| Category           | Name                            |
| ------------------ | ------------------------------- |
| Language           | JAVA / JavaScript / html / css  |
| Front-end          | Vue.js / VUEX / Node            |
| Back-end           | Spring Boot, Gradle             |
| Database           | MYSQL / JPA                     |
| Develop tool       | VS Code / Eclipse               |
| Collaboration tool | Git / JIRA                      |
| DevOps             | Jenkins / Docker / Nginx        |
| Design             | Vuetify, ElementPlus            |
| ETC                | OPENVIDU / Kurento-media-server |



### Version

- JAVA : openjdk:8-jdk-alpineV
- Vue.js : 3.0.11
- Node : lts-alpine
- Spring : 3.9.13 RELEASE
- MYSQL : 5.7
- OPENVIDU : 2.19.0
- Kurento-media-server : 6.16.0



### ETC

- __Design__
  - [Vuetify](https://vuetifyjs.com/)
  - [Element Plus](https://element-plus.org/)
- __Framwork__
  - __Openvidu__
    - **링크** : https://openvidu.io/index
    - **소개** : WebRTC 를 사용한 화상회의 지원 플랫폼
    - **사용 기능** : 화상회의 기능
  
  - __Kurento-media-server__
    - 링크 : https://www.kurento.org/
    - **소개** : WebRTC 를 사용한 화상회의 지원 플랫폼
    - **사용 기능** : media-server 사용
  



## 프로젝트 설치 안내

- 로컬에서 프론트엔드 실행 시 명령어
  ```bash
  # front-end directory
  cd ./frontend
  
  cd src/main/resources 
  # requirements setting
  npm install
  # run
  npm run serve
  ```

- 프론트엔드 빌드 시 명령어
  ```bash
  # front-end directory
  cd ./frontend
  
  cd src/main/resources 
  # front-end build
  npm run build
  ```

- 백엔드 + 프론트엔드 전체 빌드
  ```bash
  # back-end directory
  cd ./backend
  
  gradle clean build
  ```



## 프로젝트 디렉토리 구조

### Back-end

```bash
.
└── main
    ├── generated
    ├── java
    │   └── com
    │       └── ssafy
    │           ├── GroupCallApplication.java
    │           ├── api  /* REST API 요청관련 컨트롤러, 서비스, 요청/응답 모델 정의*/
    │           │   ├── controller
    │           │   │   ├── AuthController.java
    │           │   │   └── UserController.java
    │           │   ├── request
    │           │   │   ├── UserLoginPostReq.java
    │           │   │   └── UserRegisterPostReq.java
    │           │   ├── response
    │           │   │   ├── UserLoginPostRes.java
    │           │   │   └── UserRes.java
    │           │   └── service
    │           │       ├── UserService.java
    │           │       └── UserServiceImpl.java
    │           ├── common /* 공용 유틸, 응답 모델, 인증, 예외처리 관련 정의*/
    │           │   ├── auth
    │           │   │   ├── JwtAuthenticationFilter.java
    │           │   │   ├── SsafyUserDetailService.java
    │           │   │   └── SsafyUserDetails.java
    │           │   ├── exception
    │           │   │   └── handler
    │           │   │       └── NotFoundHandler.java
    │           │   ├── model
    │           │   │   └── response
    │           │   │       └── BaseResponseBody.java
    │           │   └── util
    │           │       ├── JwtTokenUtil.java
    │           │       └── ResponseBodyWriteUtil.java
    │           ├── config /* WebMvc 및 JPA, Security, Swagger 등의 추가 플러그인 설정 정의*/
    │           │   ├── JpaConfig.java
    │           │   ├── SecurityConfig.java
    │           │   ├── SwaggerConfig.java
    │           │   └── WebMvcConfig.java
    │           └── db /* 디비에 저장될 모델 정의 및 쿼리 구현 */
    │               ├── entity
    │               │   ├── BaseEntity.java
    │               │   └── User.java
    │               └── repository
    │                   ├── UserRepository.java
    │                   └── UserRepositorySupport.java
    └── resources
        ├── README.md
        ├── application.properties /* 웹 리소스(서버 host/port, 디비 host/port/계정/패스워드) 관련 설정 정의 */
        ├── babel.config.js
        ├── dist
        ├── package-lock.json
        ├── package.json
        ├── public
```



### Front-end

```bash
.
└── main
    ├── generated
    ├── java
    └── resources
        ├── README.md
        ├── application.properties
        ├── babel.config.js
        ├── dist
        │   ├── css
        │   │   ├── app.22fde46a.css
        │   │   └── chunk-vendors.bfd3c584.css
        │   ├── favicon.ico
        │   ├── fonts
        │   │   ├── element-icons.abe71f7d.ttf
        │   │   └── element-icons.d9491be2.woff
        │   ├── img
        │   │   └── ssafy-logo.74eec4f3.png
        │   ├── index.html
        │   └── js
        │       ├── app.2a195a37.js
        │       ├── app.2a195a37.js.map
        │       ├── chunk-vendors.184466aa.js
        │       └── chunk-vendors.184466aa.js.map
        ├── package-lock.json
        ├── package.json /* 필요 플러그인, 모듈 목록 */
        ├── public
        │   ├── favicon.ico
        │   └── index.html
        ├── src
        │   ├── App.vue /* 진입점 */
        │   ├── assets /* 리소스 저장용 폴더 */
        │   │   ├── images
        │   │   |   ├── sample-image.png
        │   │   |   └── ssafy-logo.png
        |   |   └── fonts /* 폰트 저장 폴더 */
        │   ├── common
        │   │   ├── css
        │   │   │   ├── common.css /* 공통 요소 (html, body, font), 공통 반응형 .hide-on-small 등 */
        │   │   │   └── element-plus.css /* Element Plus Overriding CSS */
        │   │   ├── lib
        │   │   │   ├── axios.js
        │   │   │   ├── element-plus.js
        │   │   │   ├── i18n.js
        │   │   │   ├── store.js
        │   │   │   └── vue-router.js
        │   │   ├── config.js /* 공용 설정 관련 함수 정의 */
        │   │   └── util.js  /* 공용 유틸 관련 함수 정의 */
        │   ├── main.js
        │   └── views
        │       ├── conferences /* 방 정보 페이지 */
        │       │   └── conference-detail.vue /* 방 상세 정보 페이지 */
        │       ├── history /* 이력 페이지 */
        │       │   └── history.vue
        │       ├── home
        │       │   ├── components /* 홈 메뉴 컴포넌트 */
        │       │   │   └── conference.vue /* 방 카드 */
        │       │   └── home.vue
        │       └── main
        │           ├── components /* 메인 관련 컴포넌트(사이드바, 헤더, 푸터, 로그인 다이얼로그) */
        │           │   ├── login-dialog.vue
        │           │   ├── main-footer.vue
        │           │   ├── main-header.vue
        │           │   └── main-sidebar.vue
        │           ├── main.css
        │           ├── main.vue
        |           ├── menu.json
        │           └── store
        │               ├── actions.js
        │               ├── getters.js
        │               ├── index.js
        │               ├── mutations.js
        │               └── state.js
        ├── vue.config.js /* Vue3 관련 설정 파일(프록시, 기타 옵션) */
        └── webpack.config.js /* stylus 파일 확장자(.styl) 관련 설정 */
```



## MockUp

- [Valto 와이어 프레임](https://github.com/oowtl/Valto/blob/master/Document/2%EC%B0%A8_%EC%99%80%EC%9D%B4%EC%96%B4%ED%94%84%EB%A0%88%EC%9E%84.pdf)



## API Document

- [Valto API Document](https://github.com/oowtl/Valto/blob/master/Document/REST_API_v2.1.4.4.md)



## Example

- 메인페이지 ( 방 목록 / 주제 / 방 검색 )
  ![1_mainPage](README.assets/1_mainPage.jpg)

- 로그인 페이지
  ![3_loginPage](README.assets/3_loginPage.jpg)
  
- 방 정보
  ![2_detailPage](README.assets/2_detailPage.jpg)

  ![1-2](README.assets/1-2.png)

- 토론방
  ![4_roomPage](README.assets/4_roomPage.jpg)



