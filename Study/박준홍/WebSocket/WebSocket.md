# WebSocket

- 하나의 TCP 접속에 전이중 통신 채널을 제공하는 컴퓨터 통신 프로토콜



## TCP

> 전송 제어 프로토콜
>
> Transmission Control Protocol

- TCP는 근거리 통신망이나 인트라넷, 인터넷에 연결된 컴퓨터에서 실행되는 프로그램 간의 일련의 옥텟을 안적적으로 순서대로 에러없이 교환할 수 있도록 한다.

- 옥텟
  - 옛날 컴퓨터에서는 1바이트가 안정적이지 못했다. 그래서 8비트에 대한 확실한 값이 필요했다. 그래서 생긴 것이 옥텟.
    지금은 1바이트와 동일한 말이 되었다.



## Working Flow

1. 클라이언트와 서버간에 통신 채널을 설정하기 위해서 클라리언트가 서버로 HTTP 업그레이드 요청을 보낸다.
   - 이 것을 WebSocket 프로토콜 핸드 셰이크라고 한다.
2. 서버가 연결을 업그레이드 할 수 있다면 HTTP 101 응답을 요청한 클라이언트에게 보낸다.
   - 이 시점에서 핸드 셰이크가 성공한 것으로 간주한다.
   - 서버와 클라이언트 간의 연결이 WebSocket 프로토콜로 업그레이드 된다.
   - 클라이언트가 응답받는 즉시, 연결은 더 이상 HTTP 와 연결되었다고 간주하지 않는다.
3. WebSocket 연결을 통해서 서버와 클라이언트 간에 양방향으로 메세지가 흐른다.
4. 데이터 교환에 참여한 모든 참가자는 다른 참가자에게 닫기 요청을 전송해서 WebSocket 연결을 종료하도록 요청할 수 있다.



## WebSocket Server 를 운용할 때의 유의사항

- HTTP에서 동작하나, 그 방식이 HTTP와는 상이하다.
  - REST 한 방식의 HTTP 통신에서는 많은 URI 를 통해서 application 이 설계된다.
  - WebSocket은 하나의 URL 을 통해서 Connection이 맺어지고, 후에는 해당 Connection 으로만 통신한다.
- Handshake 가 완료되고 Connection 을 유지한다.
  - 전통적인 HTTP 통신은 요청-응답이 완료되면 Connection 을 close 한다.
    - 이론 상 하나의 서버는 포트 수의 한계를 넘는 클라이언트의 요청을 처리할 수 있다.
  - WebSocket은 Connection을 유지하고 있으므로, 가용 Port 수만큼의 클라이언트와 통신할 수 있다.







## 간단한 예제 1 - WebSocket

- 간단한 예제를 통해서 몇 가지 알고자 한다.
- 어떤 것을 import 하는지도 중요하기 때문에 통짜로 올리겠다.



### WebSocket end-point 및 message broker 구성하기

- config(package) / WebSocketConfig

  ```java
  package com.example.springbootwebsocketdemo.config;
  
  import org.springframework.context.annotation.Configuration;
  import org.springframework.messaging.simp.config.MessageBrokerRegistry;
  import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
  import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
  import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
  
  @Configuration
  @EnableWebSocketMessageBroker
  public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  
  
      @Override
      public void registerStompEndpoints(StompEndpointRegistry registry) {
          registry.addEndpoint("/ws").withSockJS();
      }
  
  
      @Override
      public void configureMessageBroker(MessageBrokerRegistry registry) {
          registry.setApplicationDestinationPrefixes("/app");
          registry.enableSimpleBroker("/topic");
      }
  }
  ```

  - `@EnableWebSocketMessageBroker`
    - WebSocket 서버를 활성화하는데 사용한다.
  - `implements WebSocketMessageBrokerConfigurer`
    - 즉, 웹 소켓 연결을 구성하기 위한 메서드를 구현하고 제공한다.
  - `registerStompEndpoints` 메서드
    - 클라이언트가 웹 소켓 서버를 연결하는데 사용할 웹 소켓 엔드 포인트를 등록한다.
    - 엔드포인트 구성에 `withSockJS()` 를 사용한다.
    - SockJS() 는 웹 소켓을 지원하지 않는 브라우저에서 폴백 옵션을 활성화하는데 사용한다.
  - `configureMessageBroker`메서드
    - 한 클라이언트에서 다른 클라이언트로 메시지를 라우팅하는데 사용될 메시지 브로커
  - `registry.setApplicationDestinationPrefixes("/app");`
    - "/app" 으로 시작되는 메시지가 message-handling methods 로 라우팅 되어야 한다는 것을 명시한다.
  - `registry.enableSimpleBroker("/topic");`
    - "/topic" 으로 시작되는 메시지가 메시지 브로커로 라우팅 되도록 정의한다.
      메시지 브로커는 특정 주체를 구독한 연결된 모든 클라이언트에게 메시지를 broadcast 한다.

- 추가 용어
  - Fallback
    - 어떤 기능이 약해지거나 제대로 동작하지 않을 때, 이에 대처하는 기능 또는 동작
  - 소켓
    - 네트워크 상에서 돌아가는 두 개의 프로그램 간 양방향 통신의 하나의 엔드 포인트
    - 포트 번호에 바인딩되어 TCP 레이어에서 데이터가 전달되야하는 어플리케이션을 식별할 수 있게 한다.
  - 엔드 포인트
    - 아이피 주소와 포트 번호의 조합
    - 모든 TCP 연결은 2개의 엔드 포인트로 유일하게 식별되어질 수 있다.
    - 클라이언트와 서버 간 여러개의 연결이 맺어질 수 있다.



### Message Payload

- ```java
  package com.example.example.model;
  
  
  public class ChatMessage {
  
      private MessageType type;
      private String content;
      private String sender;
  
      public MessageType getType() {
          return type;
      }
  
      public void setType(MessageType type) {
          this.type = type;
      }
  
      public String getContent() {
          return content;
      }
  
      public void setContent(String content) {
          this.content = content;
      }
  
      public String getSender() {
          return sender;
      }
  
      public void setSender(String sender) {
          this.sender = sender;
      }
  }
  ```

- ```java
  package com.example.example.model;
  
  public enum MessageType {
      CHAT,
      JOIN,
      LEAVE
  }
  
  ```

- 정리
  - spring 에서는 json 으로 보내주는 것을 이용한 것이다.



### Controller



#### chatController

- message handling methods

  - 한 Client 에게서 message 를 수신하고 다른 Client 에게 broadcast 한다.

- ```java
  package com.example.example.controller;
  
  import com.example.example.model.ChatMessage;
  import org.springframework.messaging.handler.annotation.MessageMapping;
  import org.springframework.messaging.handler.annotation.Payload;
  import org.springframework.messaging.handler.annotation.SendTo;
  import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
  import org.springframework.stereotype.Controller;
  
  @Controller
  public class chatController {
  
      @MessageMapping("/chat.sendMessage")
      @SendTo("/topic/public")
      public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
          return chatMessage;
      }
  
      @MessageMapping("/chat.addUser")
      @SendTo("/topic/public")
      public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
          headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
          return chatMessage;
      }
  }
  ```

  - `@MessageMapping("/chat.sendMessage")`
    - config 에서 "/app" 으로 시작하는 대상이 있는 클라이언트에서 보낸 메시지가 라우팅되도록 설정한다.
    - 즉, `"/app/chat.sendMessage"` 인 메세지는 `sendMessage()`로 라우팅 된다.
      `"/app/chat.addUser"`인 메세지는 `addUser()`로 라우팅 된다.

- 용어
  - payload
    - 전송되는 데이터
    - 보내고자 하는 데이터 자체를 의미한다.
    - 오로지 그 내용 자체만을 의미하고 그 밖에 있는 헤더와 같은 것들은 제외한다.
      나머지 내용들은 프로토콜 오버헤드(protocol overhead)라고 한다.







- https://github.com/sockjs/sockjs-client

- https://velog.io/@koseungbin/WebSocket









## [Error] Intelli J build.gradle 설정

- 상황

  - ```console
    java: warning: source release 11 requires target release 11
    ```

    - 이런 에러가 뜬다.

- 이유

  - gradle 이나 프로젝트 설정에서 java 버전이 맞지 않아서 나오는 문제이다.

- 해결

  - build.gradle 파일 변경

    ```pro
    변경 전
    sourceCompatibility = '11'
    
    변경 후
    sourceCompatibility = '1.8'
    ```

    - 버전을 바꿔준다.

  - intelliJ 의 설정에서 지금 사용하는 java 로 바꿔준다.