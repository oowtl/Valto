# 밸런스 토론! REST API

[TOC]

## API 리스트

| 요청 URL                   | 메서드        | 응답형식 | 설명                            |
| -------------------------- | ------------- | -------- | ------------------------------- |
| `api/v1/`                  | -             | -        | 기본 주소                       |
| `/auth/login`              | POST          | JSON     | 로그인 인증 요청                |
| `/users`                   | POST          | JSON     | 회원가입 요청                   |
| `/users/{userId}`          | PATCH, DELETE | JSON     | 회원 정보 수정 , 회원 정보 삭제 |
| `/users/{userId}/id`       | GET           | JSON     | 아이디 중복 검사 요청           |
| `/users/{nickName}/nick`   | GET           | JSON     | 닉네임 중복 검사 요청           |
| `/users/me`                | GET           | JSON     | 페이지 권한 여부 확인           |
| `/users/myprofile`         | GET           | JSON     | 자기 프로필 확인                |
| `/users/{userId}/profile/` | GET           | JSON     | 타인 프로필 확인                |
| `/room`                    | POST          | JSON     | 방 생성                         |
| `/room`                    | GET           | JSON     | 전체 방 목록 조회               |
| `/room/{roomId}`           | GET           | JSON     | 방 상세 정보 조회               |
| `/room/{roomId}`           | PATCH, DELETE | JSON     | 방 정보 수정, 삭제              |
| `/room/{roomId}/admission` | POST, DELETE  | JSON     | 방 입장, 퇴장                   |



## 로그인

| 요청 URL      | 메서드 | 응답형식 | 설명             |
| ------------- | ------ | -------- | ---------------- |
| `/auth/login` | POST   | JSON     | 로그인 인증 요청 |



### [POST] `/auth/login`

- userId(로그인 시 아이디) 와 Password 를 요청 받아서 인증 및 JWT 토큰을 발급하는 API
- 에러처리
  - 로그인을 시도하려는 회원이 존재하지 않는다.
    - 응답 코드 : 404
    - 응답 메시지 : 존재하지 않는 계정입니다.
  - Password 가 일치하지 않는다.
    - 응답 코드 : 401
    - 응답 메시지 : 잘못된 비밀번호입니다.



## 유저관리

| 요청 URL                  | 메서드        | 응답형식 | 설명                           |
| ------------------------- | ------------- | -------- | ------------------------------ |
| `/users`                  | POST          | JSON     | 회원가입 요청                  |
| `/users/{userId}`         | PATCH, DELETE | JSON     | 회원 정보 수정, 회원 정보 삭제 |
| `/users/id/{userId}`      | GET           | JSON     | 아이디 중복 검사 요청          |
| `/users/nick/{nickName}`  | GET           | JSON     | 닉네임 중복 검사 요청          |
| `/users/me`               | GET           | JSON     | userId 확인                    |
| `/users/myprofile`        | GET           | JSON     | 자기 프로필 확인               |
| `/users/profile/{userId}` | GET           | JSON     | 타인 프로필 확인               |



### [POST] `/users`

- Request

  - Body

    ```json
    {
    	"userId" : "String",
        "name" : "String",
        "nickname" : "String",
    	"passsword" : "String"
    }
    ```

- Response

  - 201

    ```json
    {
        "statusCode" : 201,
        "message" : "Success"
    }
    ```

- 권한

  - 모두 사용가능



### [PATCH] `/users/{String : userId}`

- Request

  - Body

    ```json
    {
        "name" : "String",
        "nickname" : "String",
        "password" : "String"
    }
    ```

- Response

  - 201

    ```json
    {
        "statusCode" : 201,
        "message" : "Success"
    }
    ```
  
- 권한

  - 유저 본인



### [DELETE] `/users/{String : userId}`

- Request

  - 없음

- Response

  - 200

    ```json
    {
        "statusCode" : 200,
        "message" : "success"
    }
    ```

- 권한
  
  - 로그인 한 사용자
- 기능
  - 해당 유저가 생성한 방을 모두 삭제한다.
  - 해당 유저의 지난 회의 이력을 모두 삭제한다.
  - 해당 유저 정보를 삭제한다.
  - 해당 유저 전적 정보를 삭제한다.



### [GET] `/users/{String : userId}/id`

- Request

  - 없음

- Response

  - 200

    ```json
    {
    	"statusCode" : 200,
        "message" : "possible userId"
    }
    ```

  - 409

    ```json
    {
    	"statusCode" : 409,
        "message" : "exist userId"
    }
    ```

- 권한

  - 모두 사용 가능



### [GET] `/users/{String : nickName}/nick`

- Request

  - 없음

- Response

  - 200

    ```json
    {
    	"statusCode" : 200,
        "message" : "possible nickName"
    }
    ```

  - 409

    ```json
    {
    	"statusCode" : 409,
        "message" : "exist nickName"
    }
    ```

- 권한

  - 모두 사용 가능



### [GET] `/users/me`

- Request

  - 없음

- Response

  - Body

    - 200

      ```json
      {
          "userId" : "String"
      }
      ```

- 권한

  - 로그인 한 사용자



### [GET] `/users/myprofile`

- Request

  - 없음

- Response

  - 200

    ```json
    {
    	"userId" : "String",
        "nickName" : "String",
        "password" : "String",
        "name" : "String",
        "point" : Integer,
        "userRecord" : {
            "win" : integer,
            "lose" : integer,
            "draw" : integer
    	}
    }
    ```

- 권한

  - 로그인 한 사용자



### [GET] `/users/{String : userId}/profile`

- Request
  
- 없음
  
- Response

  - 200

    ```json
    {
        "nickName" : "String",,
        "point" : Integer,
        "userRecord" : {
            "win" : integer,
            "lose" : integer,
            "draw" : integer
    	}
    }
    ```
    
  - 400

    ```json
    {
    	"statusCode" : 400,
        "message" : "noExist userId or inaccuracy userId"
    }
    ```





## 방 관리

| 요청 URL                   | 메서드        | 응답형식 | 설명              |
| -------------------------- | ------------- | -------- | ----------------- |
| `/room`                    | POST          | JSON     | 방 생성           |
| `/room`                    | GET           | JSON     | 전체 방 목록 조회 |
| `/room/{roomId}`           | GET           | JSON     | 방 상세 정보 조회 |
| `/room/{roomId}`           | PATCH, DELETE | JSON     | 방 정보수정, 삭제 |
| `/room/{roomId}/admission` | POST, DELETE  | JSON     | 방 입장, 퇴장     |



### [POST] `/room`

- Request

  - Body

    ```json
    {
    	"userId" : "String",   
        "participants" : integer,
        "observers" : integer,
    	"times" : integer,
        "title" : "String",
        "privateRoom" : Boolean,
        "roomPassword" : "String",
        "topicAgree" : "String",
        "topicOpposite" : "String",
        "userSide" : "String"
    }
    ```
    
    - 설명 (`topicAgree`, `topicOpposite`)
    
      - `topicAgree`
          - 찬성 측 주제
      - `topicOpposite`
        - 반대 측 주제
    
      - 예시
    
      ```json
      {
          "topicAgree" : "물복숭아",
          "topicOpposite" : "딱복숭아"
      }
      ```
    
    - 설명 (`privateRoom`, `roomPassword`)
    
      - `privateRoom`
        - Boolean 으로서 비공개방일 시 `True`
      - `roomPassword`
        - `privateRoom`의 값이 True 일 때 값이 존재하는 것으로 한다.
      
    - 설명 (`userSide`)
    
      - `agree` = Agree
      - `opposite` = Opposite

- Response

  - 201

    ```json
    {
        "userId" : "String",
        "participants" : integer,
        "observers" : integer,
        "times" : integer,
        "title" : "String",
        "privateRoom" : Boolean,
        "topicAgree" : "String",
        "topicOpposite" : "String",
        "userSide" : "String"
    }
    ```

  - 400

    ```json
    {
        "StatusCode" : 400,
        "message" : "no password private room"
    }
    ```

    - `"privateRoom" : True"`, `roomPassword : ''`
      비공개방 o, 비밀번호 없음

- 권한

  - 로그인 한 사용자



### [GET] `/room`

- Request

  - Query Parameter
    - title = String
      - title 으로 검색하는 기능
        필요하지 않다면 미입력
    - topic = String
      - topic 으로 검색하는 기능
        필요하지 않다면 미입력
    - sorting={String}
      - `sorting=participantsAsc` (Default)
        - 비고 : sorting을 입력하지 않거나 다른 query를 입력할 경우 해당 정렬을 실행함.
      - `sorting=participantsDesc`
      - `sorting=createdAtAsc`
      - `sorting=createdAtDesc`
    - page={Integer}
      - 페이징 처리
    - size={Integer}
      - 몇 개의 데이터를 들고 올지 정하는 것

- Response

  - 200

    ```json
    {
        "RoomCount" : Integer,
    	"content": [
            {
                "roomdId" : Integer,
                "userId" : "String",   
                "participants" : integer,
                "times" : integer,
                "title" : "String",
                "privateRoom" : Boolean,
                "topicAgree" : "String",
                "topicOpposite" : "String",
                "currentParticipants" : Integer,
                "userTotalCount": Integer,
                "userAgreeCount": Integer,
                "userOppositeCount": Integer,
    		},
            {
                "roomId" : Integer
                ...
            }
        ],
    }
    ```
    
    - 설명
      - `participants` : 방 생성 시 설정한 참가 인원 제한
      - `currentParticipants` : 현재 참가자 숫자
      - `userTotalCount` : 현재 방에 몇명이 있는지 총 숫자를 내어준다.
    
  - 204

    ```json
    {
        "StatusCode" : 204,
        "message" : "no Room"
    }
    ```

    

- 권한
  
  - 모든 사용자



### [GET] `/room/{String : roomId}`

- Request

  - 없음

- Reponse

  - 200

    ```json
    {
        "roomId" : integer,
       	"participants" : integer,
        "observers" : integer,
    	"times" : integer,
        "userId" : "String",
        "title" : "String",
        "privateRoom" : Boolean,
        "topicAgree" : "String",
        "topicOpposite" : "String",
        "AgreeUsers" : [
            {
                "userId" : "String",
                "name" : "String",
                "nickName" : "String"
            },
            {
                "userId" : "String",
                "name" : "String",
                "nickName" : "String"
    		},
        ],
        "OppositeUsers": [
            {
                "userId" : "String",
                "name" : "String",
                "nickName" : "String"
            },
            {
                "userId" : "String",
                "name" : "String",
                "nickName" : "String"
    		},
        ]
    }
    ```
    
  - 400
  
    ```json
    {
        "StatusCode" : 400,
        "message" : "noExist roomId",
    }
    ```

- 권한

  - 모든 사용자



### [PATCH] `/room/{String : roomId}`

- Request

  - Body

    ```json
    {
       	"participants" : integer,
        "observers" : integer,
    	"times" : integer,
        "title" : "String",
        "topicAgree" : "String",
        "topicOpposite" : "String",
    }
    ```

- Response

  - 200

    ```json
    {
        "roomId" : integer,
       	"participants" : integer,
        "observers" : integer,
        "privateRoom" : Boolean,
    	"times" : integer,
        "userId" : "String",
        "title" : "String",
        "topicAgree" : "String",
        "topicOpposite" : "String",
        "currentParticipants" : integer
    }
    ```
    
  - 400

    ```json
    {
        "StatusCode" : 400,
        "message" : "noExist roomId",
    }
    ```

  - 403

    ```json
    {
        "StatusCode" : 403,
        "message" : "no Room Creater",
    }
    ```

    - userId != ownerId

- 권한

  - 로그인 한 사용자
  - 방을 만든 사용자 (userId == ownerId)



### [DELETE] `/room/{String : roomId}`

- Request

  - Body

    ```json
    {
        "roomId" : integer,
        "userId" : "String",
        "surrender" : false
    }
    ```

    - `surrender`
      - true : 방장이 아니더라도 방을 종료시키는 조건

- Response

  - 200

    ```json
    {
        "StatusCode" : 200,
        "message" : "Success"
    }
    ```

  - 400

    ```json
    {
        "StatusCode" : 400,
        "message" : "no Exist Room"
    }
    ```

  - 403
  
    ```json
    {
        "StatusCode" : 403,
        "message" : "no Room Creater"
    }
    ```
  
    - ownerId != userId



### [POST] `/room/{String : roomId}/admission` 

- 부가설명

  - 방 입장하기
  
- Request

  ```json
  {
      "privateRoom" : Boolean,
      "roomPassword" : "String",
      "userSide" : "String"
  }
  ```

  - 설명(`privateRoom`)
    - 비밀번호가 있다면, `True`
  - 설명(`userSide`)
    - `agree` = Agree
    - `opposite`  = Opposite

- Response

  - 200

    ```json
    {
        "StatusCode" : 200,
        "message" : "Success"
    }
    ```
    
  - 400

    ```json
    {
        "StatusCode" : 400,
        "message" : "no Exist Room"
    }
    ```

    ```json
    {
        "StatusCode" : 400,
        "message" : "already enter room user"
    }
    ```
    
    ```json
    {
        "StatusCode" : 400,
        "message" : "don't enter Room : maximum userside"
    }
    ```

- 권한

  - 로그인한 유저
  - 방에 접속 하지 않은 유저
  
  

### [DELETE] `/room/{String : roomId}/admission`  

- 부가설명

  - 방에서 퇴장하는 API

- Request

  - 없음

- Reponse

  - 204

    ```json
    {
        "StatusCode" : 204,
        "message" : "Success"
    }
    ```
    
  - 400

    ```json
    {
        "StatusCode" : 400,
        "message" : "no Exist Room"
    }
    ```

    ```json
    {
        "StatusCode" : 400,
        "message" : "already enter room user"
    }
    ```

- 권한

  - 로그인 한 유저
  - 방에 들어가 있는 유저

