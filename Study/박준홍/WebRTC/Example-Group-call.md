# Example Kurento group-call



- 시작하기

  ```bash
  git clone https://github.com/Kurento/kurento-tutorial-java.git
  cd kurento-tutorial-java/kurento-group-call
  git checkout master
  
  mvn -U clean spring-boot:run \
      -Dspring-boot.run.jvmArguments="-Dkms.url=ws://{KMS_HOST}:8888/kurento"
  ```

  - `{KMS_HOST}`
    - AWS 환경이기 때문에 EC2 의 public IP 로 사용한다.

- 설명
  - 방의 개념으로 작업을 하는 것을 보여준다.
  - 각 방은 다른 방과 격리된 자체 파이프 라인을 생성한다.
  - 특정 방에 접속한 클라이언트는 같은 방에 있는 클라리언트와만 미디어를 교환할 수 있다.
  - 각 클라이언트는 자체 미디어를 보내고 차례로 다른 모든 참가자로부터 미디어를 받는다.
    - 각 방에 n*n 개의 webrtc 엔드 포인트가 있다. (n = 클라이언트 수)
  - 방에 접속할 때
    1. 새로운 webrtc 가 생성이 되고, 서버에서 미디어를 수신하도록 협상한다.
    2. 모든 참가자는 새 사용자가 연결되었음을 확인한다.
    3. 모든 참가자는 서버에 새 참가자의 미디어를 수신하도록 요청한다.
  - 방에서 나갈 때
    1. 모든 클라이언트는 서버에 나가는 것을 알린다.
    2. 클라이언트 측 코드는 서버에 남아있는 클라이언트와 관련된 모든 미디어 요소를 취소하도록 요청한다.
  - 클라이언트-서버 아키텍처
    - 클라이언트 측 로직 : js
    - 서버 측 로직 : Kurento Media Server 기능을 제어하기 위한 Kurento Java Client API 를 사용하는 Spring-Boot 기반 애플리케이션 서버 사용
    - 엔터티 통신을 위해서 두 개의 WebSocket 을 사용
      1. 클라이언트와 응용 프로그램 서버 사이에 WebSocket
         - 사용자 지정 신호 프로토콜 구현을 위해서
      2. Kurento Java 클라이언트와 Kurento Media Server 간의 통신 수행
         - Kurento 프로토콜 사용



## GroupCallApp



```java
@SpringBootApplication
@EnableWebSocket
public class GroupCallApp implements WebSocketConfigurer {

  @Bean
  public UserRegistry registry() {
    return new UserRegistry();
  }

  @Bean
  public RoomManager roomManager() {
    return new RoomManager();
  }

  @Bean
  public CallHandler groupCallHandler() {
    return new CallHandler();
  }

  @Bean
  public KurentoClient kurentoClient() {
    return KurentoClient.create();
  }

  @Bean
  public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
    ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
    container.setMaxTextMessageBufferSize(32768);
    return container;
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(GroupCallApp.class, args);
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(groupCallHandler(), "/groupcall");
  }
}

```

- ```java
  @Bean
    public KurentoClient kurentoClient() {
      return KurentoClient.create();
    }
  ```

  - 애플리케이션에 미디어 기능을 추가하는 데 사용되는 Kurento 미디어 파이프라인을 만드는데 사용된다.
    - 미디어 파이프라인이라는 것을 정확하게는 알 수 없지만, 여기에서 webrtc endpoint 가 작동하며, 미러필터나 녹화 등이 움직인다.
  - 이 인스턴스화에서 Kurento Media Server 의 위치를 클라이언트 라이브러리에 지정해야한다.

- ```java
  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(groupCallHandler(), "/groupcall");
  }
  ```

  - `groupCallHandler()` 핸들러는 `"/groupcall"` endpoint 로 handshake 를 완료한 후에 맺어진 connection 을 관리한다.



## CallHandler

```java
public class CallHandler extends TextWebSocketHandler {

  private static final Logger log = LoggerFactory.getLogger(CallHandler.class);

  private static final Gson gson = new GsonBuilder().create();

  @Autowired
  private RoomManager roomManager;

  @Autowired
  private UserRegistry registry;

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    final JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);

    final UserSession user = registry.getBySession(session);

    if (user != null) {
      log.debug("Incoming message from user '{}': {}", user.getName(), jsonMessage);
    } else {
      log.debug("Incoming message from new user: {}", jsonMessage);
    }

    switch (jsonMessage.get("id").getAsString()) {
      case "joinRoom":
        joinRoom(jsonMessage, session);
        break;
      case "receiveVideoFrom":
        final String senderName = jsonMessage.get("sender").getAsString();
        final UserSession sender = registry.getByName(senderName);
        final String sdpOffer = jsonMessage.get("sdpOffer").getAsString();
        user.receiveVideoFrom(sender, sdpOffer);
        break;
      case "leaveRoom":
        leaveRoom(user);
        break;
      case "onIceCandidate":
        JsonObject candidate = jsonMessage.get("candidate").getAsJsonObject();

        if (user != null) {
          IceCandidate cand = new IceCandidate(candidate.get("candidate").getAsString(),
              candidate.get("sdpMid").getAsString(), candidate.get("sdpMLineIndex").getAsInt());
          user.addCandidate(cand, jsonMessage.get("name").getAsString());
        }
        break;
      default:
        break;
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    UserSession user = registry.removeBySession(session);
    roomManager.getRoom(user.getRoomName()).leave(user);
  }

  private void joinRoom(JsonObject params, WebSocketSession session) throws IOException {
    final String roomName = params.get("room").getAsString();
    final String name = params.get("name").getAsString();
    log.info("PARTICIPANT {}: trying to join room {}", name, roomName);

    Room room = roomManager.getRoom(roomName);
    final UserSession user = room.join(name, session);
    registry.register(user);
  }

  private void leaveRoom(UserSession user) throws IOException {
    final Room room = roomManager.getRoom(user.getRoomName());
    room.leave(user);
    if (room.getParticipants().isEmpty()) {
      roomManager.removeRoom(room);
    }
  }
}

```

- ```java
  public class CallHandler extends TextWebSocketHandler
  ```

  - `extends TextWebSocketHandler`
    - 텍스트 WebSocket 요청을 처리하기 위해서 구현한다.

- `handleTextMessage` method

  - 요청에 대한 작업을 구현하고 WebSocket 을 통해서 응답을 반환한다.
    시그널링 프로콜의 서버 부분을 구현한다.
  - 메시지의 종류는 `joinRoom`, `receiveVideoFrom`, `leaveRoom`, `onIceCandidate` 이며, switch 절에서 처리가 된다.

- ```java
  @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
      UserSession user = registry.removeBySession(session);
      roomManager.getRoom(user.getRoomName()).leave(user);
    }
  ```

  - registry 에 있는 userSession 을 제거하고 사용자를 방에서 내보낸다.

- ```java
  private void joinRoom(JsonObject params, WebSocketSession session) throws IOException {
      final String roomName = params.get("room").getAsString();
      final String name = params.get("name").getAsString();
      log.info("PARTICIPANT {}: trying to join room {}", name, roomName);
  
      Room room = roomManager.getRoom(roomName);
      final UserSession user = room.join(name, session);
      registry.register(user);
    }
  ```

  - 서버는 지정된 이름으로 등록된 방이 있는지 확인하고 이 방에 사용자를 추가하고 사용자를 등록한다.

- ```java
  private void leaveRoom(UserSession user) throws IOException {
    final Room room = roomManager.getRoom(user.getRoomName());
    room.leave(user);
    if (room.getParticipants().isEmpty()) {
      roomManager.removeRoom(room);
    }
  }
  ```

  - 사용자의 화상 통화를 종료한다.





https://doc-kurento.readthedocs.io/en/latest/tutorials/java/tutorial-groupcall.html

https://supawer0728.github.io/2018/03/30/spring-websocket/