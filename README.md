# 프로젝트 명

## 목차

- [프로젝트 소개](#프로젝트-소개)   
- [텍스트 ... ](#프로젝트-소개)   
- [프로젝트 명세](#프로젝트-명세)
  - [배포 환경](#배포-환경)
  - [개발 환경](#개발-환경)
  - [Design Resources](#design-resources)
  - [핵심 라이브러리](#핵심-라이브러리)
    <br>

## 프로젝트 소개

webRTC API 를 이용한 그룹 비디오 컨퍼런스 서비스
<br>

## 프로젝트 명세

### 배포 환경

- __URL__ : 
- __배포 여부__ : O / X
- __접속 가능__ : 접속 가능 / 수정 중
- __HTTPS 적용__ : O / X
- __PORT__ : // 3rd Party에서 사용하는 포트가 있다면 기입해주세요. <- 기입 후 해당 주석 삭제
  <br>

### 개발 환경

#### Front-end / Android

- __Framework__ : Vue.js 
- __지원 환경__ : Web / Web App
- __담당자__ : 최정훈, 김동윤, 김병찬
  <br>

#### Back-end

- __Framework__ : Spring boot / Node.js /
- __Database__ : MySQL / JPA
- __담당자__ : 하지훈, 박준홍
  <br>

#### Design

- __Framework 사용__ : O
  - [Vuetify](https://vuetifyjs.com/)
  - [Element Plus](https://element-plus.org/)
- __Design Tool 사용__ :
- __담당자__ :
  <br>

### Design Resources

__외부 템플릿 또는 에셋__ (이미지 또는 링크 첨부)

- 무료 이미지, 아이콘, 폰트 등은 제외
- [Vue Argon Design System](https://www.creative-tim.com/product/vue-argon-design-system?affiliate_id=116187) (무료)
  - __사용 기능__ : 디자인 전반 적용
- [Vue Black Dashboard Pro](https://www.creative-tim.com/product/vue-black-dashboard-pro?affiliate_id=116187) (유료)
  - __사용 기능__ : 캘린더 컴포넌트 사용
    <br>

__자체 제작 산출물__ (필요시 이미지 또는 설명 첨부)

- LOGO
- CardView
- Button
- Calendar
  <br>

### 핵심 라이브러리

기본 제공하는 라이브러리 외 핵심 기능 구현에 사용한 라이브러리가 있다면 작성해주세요.   
예시 ) VR/AR 라이브러리, 애니메이션 라이브러리, 텍스트/사진/동영상 지원, 편집 라이브러리 등

- __AR CORE__

  - __링크__ : https://developers.google.com/ar
  - __소개__ : 구글에서 제공하는 AR 지원 라이브러리
  - __사용 기능__ : 이미지 인식, 이미지 위 영상 표시
  - __담당자__ : 팀원 A, 

- __COLOR THIEF__

  - __링크__ : https://lokeshdhakar.com/projects/color-thief/
  - __소개__ : 이미지에서 색상을 추출해주는 라이브러리
  - __사용 기능__ : 커버 사진에서 색상 추출 -> 배경 색상 변경
  - __담당자__ : 팀원 A,

- __Animate.css__

  - __링크__ : https://animate.style/
  - __소개__ : css 애니메이션 지원 라이브러리
  - __사용 기능__ : 메인 페이지 진입 시 애니메이션 사용
  - __담당자__ : 팀원 A,

  

### TIL

- 20-07-12
  - build
    - 소스 코드 파일을 컴퓨터나 휴대폰에서 실행할 수 있는 독립 소프트웨어 가공물로 변환하는 과정 또는 결과물을 말함.
    - build vs compile
      - 빌드는 소스코드 파일을 실행가능한 소프트웨어 산출물로 만드는 일련의 작업
      - 컴파일은 개발자가 작성한 소스코드를 기계어로 변환하는 작업
      - 빌드 단계 중 컴파일이 포함
  - Gradle
    - Groovy 기반 오픈소스 빌드 도구
    - Ant + Maven의 장점
    - Ant 처럼 유연한 범용 빌드 도구 , Maven을 사용할 수 있는 변환 가능 컨벤션 프레임 워크
    - 멀티 프로젝트에 사용하기 좋음
    - apache Ivy에 기반한 강력한 의존성 관리
    - Maven과 Ivy 레포 완전 지원
    - 원격 저장소나, pom, ivy파일 없이 연결되는 의존성 관리 지원
    - 빌드를 설명하는 풍부한 도메인 모델
- 20-7-13
  - JWT
- 20-07-14
  - ORM
    - Object-relational mapping(객체 관계 매핑)
      - 객체는 객체대로 설계
      - 관계형 데이터베이스는 관계형 데이터베이스대로 설계
    - 대중적인 언어에는 대부분 ORM 기술 존재
    - ORM은 객체와 RDB 두 기둥 위에 있는 기술
  - JPA
    - 자바의 ORM 기술 표준, 인터페이스의 모음
      - 실제로 동작하는 것은 아님
      - JPA 인터페이스를 구현한 대표적인 오픈소스가 Hibernate
    - 동작과정
      - java application 과 JDBC 사이에서 동작
        - 개발자가 JPA를 사용하면, JPA 내부에서 JDBC API 사용 -> SQL 호출 -> DB 통신
        - 즉, 개발자가 직접 JDBC API 사용하는 것은 아님
- 20-07-15
  - Optional
    - Java 8에 추가
    - NullpointerException 방지할 수 있도록 도와줌
    - null 값이 올 수 있는 객체를 감싸는 Wrapper 클래스
    - NPE를 유발할 수 있는 null을 직접 다루지 않아도 되고 null 체크를 직접 하지 않아도 됨
      - 불필요한 방어 로직을 줄 일 수 있음



### 진척률

90%

- JPA - Entity , Repository / 삭제기능 미구현
  - 유저 관리 기능 구현이 목표라 유저 관리 이외 기능은 구현하지않음



### 어려웠던 점

2. DB ERD를 보고 DB 구현하였지만 잘 만든 것인지 잘 모르겠음

