# OpenVidu Java Js example

- 설치하기

  ```bash
  git clone https://github.com/OpenVidu/openvidu-tutorials.git -b v2.19.0
  cd openvidu-tutorials/openvidu-js-java
  mvn package exec:java
  ```

  ```ubuntu
  docker run -p 4443:4443 --rm -e OPENVIDU_SECRET=MY_SECRET openvidu/openvidu-server-kms:2.19.0
  ```

- `https://localhost:5000` 에서 확인해보기





## 사용자 로그인

1. app.js > httpPostRequest

   ```js
   // 사용자 로그인
   // url  POST 작업의 경로
   // body 데이터로 보낼 객체
   // errorMsg 에러 출력 메시지
   //callback 성공 시 실행할 함수
   function httpPostRequest(url, body, errorMsg, callback) {
   	var http = new XMLHttpRequest();
   	http.open('POST', url, true);
   	http.setRequestHeader('Content-type', 'application/json');
   	http.addEventListener('readystatechange', processRequest, false);
   	http.send(JSON.stringify(body));
   
   	function processRequest() {
   		if (http.readyState == 4) {
   			if (http.status == 200) {
   				try {
   					callback(JSON.parse(http.responseText));
   				} catch (e) {
   					callback();
   				}
   			} else {
   				console.warn(errorMsg);
   				console.warn(http.responseText);
   			}
   		}
   	}
   }
   ```

2. app.js > logIn()  : 로그인 처리함수

   ```js
   function logIn() {
   	var user = $("#user").val(); // Username
   	var pass = $("#pass").val(); // Password
   
   	httpPostRequest(
   		'api-login/login',
   		{user: user, pass: pass},
   		'Login WRONG',
   		(response) => {
   			$("#name-user").text(user);
   			$("#not-logged").hide();
   			$("#logged").show();
   			// Random nickName and session
   			$("#sessionName").val("Session " + Math.floor(Math.random() * 10));
   			$("#nickName").val("Participant " + Math.floor(Math.random() * 100));
   		}
   	);
   }
   ```

   - 로그인하는 함수...



## 화상통화 연결

1. app.js > getToken : token을 가져와야한다.

   ```js
   function getToken(callback) {
   	sessionName = $("#sessionName").val(); // Video-call chosen by the user
   
   	httpPostRequest(
   		// token 을 가져올 수 있는 API 필요
   		'api-sessions/get-token',
   		{sessionName: sessionName},
   		'Request of TOKEN gone WRONG:',
   		(response) => {
   			token = response[0]; // Get token from response
   			console.warn('Request of TOKEN gone WELL (TOKEN:' + token + ')');
   			callback(token); // Continue the join operation
   		}
   	);
   }
   ```

   - token 을 가져올 수 있는 API 가 필요하다.
   - 백엔드에서 OpenVidu 토큰을 검색해서 준다...?







## HttpSession

- Http 프로토콜은 stateless 라서 연속적으로 정보를 보관할 수 없다.
  그것을 해결하기 위해서 별도의 수단을 통해서 가각의 클라이언트를 구분한다.
  또한 클라이언트 별로 해당 정보를 유지할 방법이 바로 쿠키와 세션이다.

- Session
  - 사용자 정보를 서버에 저장한다.
  - 클라이언트의 최초 접속 시에 새로운 세션을 생성하고 세션 ID 를 전송한다.
  - 이후 접속마다 클라이언트가 세션 ID 를 전송하낟.
  - 서버는 세션 ID 에 해당하는 세션 정보를 획득한다.
  - 세션 ID 전송 수단으로 쿠키를 사용할 수 있다.

