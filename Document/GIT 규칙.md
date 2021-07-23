# GIT 규칙



## Branch 규칙

- `features /  (front or back) / 기능`

  - 기능 이름 짓기
    - 명사+동사 ( roomcreate, roomdelete)

- 버전은 명시하지 않는다.

- 예시

  - ```text
    features
    	front
    		roomcreate
    		roomdelete
    	back
    		roomdelete
    ```



## commit 메세지 규칙

- `[ (jira issue key) ] (front or back) | 상태 | 기능 `

  - `jira issue key`

    - ```text
      [S05P12D103-66]
      ```

  - 상태

    - `working` : 진행 중
    - `done` : 끝

  - 기능

    - jira issue key 에 대응하는 작업 명

    - ```text
      방 목록 없음 띄우기
      ```

  - 예시

    - ```text
      [S05P12D103-66] front | working | 방 목록 없음 띄우기 
      ```



