# WebRTC Signaling Server

- 시그널링 ( signaling)
  - 서로 다른 네트워크에 있는 2개의 디바이스들을 서로 위치시키기 위해서는, 각 디바이스들의 위치를 발견하는 방법과 미디어 포맷 협의 프로세스
  - 각 디바이스들을 상호가넹 동의된 서버(socket.io, websocket 을 이용한 서버)
    이 서버는 각 디바이스들이 negotiation (협의) 메세지들을 교활할 수 있도록한다.



https://developer.mozilla.org/ko/docs/Web/API/WebRTC_API/Signaling_and_video_calling





## Signaling Server

- 인터넷 네트워크에서 연결시키는 작업을 해줄 서버가 필요하다.
- 중요점
  - 시그널링 서버는 시그널링 데이터 내용을 몰라도 된다.
    - 몰라도 큰 문제가 되지 않는다.
  - 메세지의 내용들은 그저 시그널링 서버를 통해서 상대편으로 가기만 하면된다.











https://lovejaco.github.io/posts/webrtc-connectivity-and-nat-traversal/

https://webrtclab.herokuapp.com/intro

