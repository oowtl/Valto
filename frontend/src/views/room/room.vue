<template>
  <div id="main-container" class="container">
    <div id="session" v-if="state.session">
			<div id="session-header">
				<h1 id="session-title">{{ state.nickname }}</h1>
				<input class="btn btn-large btn-danger" type="button" id="buttonLeaveSession" @click="leaveSession" value="Leave session">
			</div>
			<div id="main-video" class="col-md-6">
				<user-video :stream-manager="state.mainStreamManager" />
			</div>
			<div id="video-container" class="col-md-6">
				<user-video :stream-manager="state.publisher" @click="updateMainVideoStreamManager(state.publisher)" />
				<user-video v-for="sub in state.subscribers" :key="sub.stream.connection.connectionId" :stream-manager="sub" @click="updateMainVideoStreamManager(sub)"/>
			</div>
		</div>
  </div>
  <el-container>
    <el-aside width="250px">
      <div id="socket">
        <el-scrollbar height="50pc">
          <div v-for="(item, idx) in state.recvList" :key="idx">
            <span> {{ item.userId }}</span>
            <span> : {{ item.message }}</span>
          </div>
          채팅: <input
            v-model="state.message"
            type="text"
            @keyup="sendMessage"
          >
        </el-scrollbar>
      </div>
    </el-aside>
    </el-container>
</template>

<style scoped>
  #socket {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: left;
    color: #2c3e50;
    margin-top: 60px;
    margin-left: 10px;
  }
  .el-header, .el-footer {
    background-color: #B3C0D1;
    color: #333;
    text-align: center;
    line-height: 60px;
  }

  .el-aside {
    background-color: #D3DCE6;
    color: #333;
    text-align: center;
    line-height: 30px;
  }

  .el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
    line-height: 100px;
  }
  body > .el-container {
    margin-bottom: 100px;
  }

  .el-container:nth-child(5) .el-aside,
  .el-container:nth-child(6) .el-aside {
    line-height: 260px;
  }

  .el-container:nth-child(7) .el-aside {
    line-height: 320px;
  }
</style>

<script>
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
import { OpenVidu } from 'openvidu-browser'
import UserVideo from './components/UserVideo';
import { reactive, computed, onBeforeMount, onBeforeUnmount } from 'vue'
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

// 12: state.publisher, 13:state.sub?

export default{
  name: 'room',

  components: {
    UserVideo
  },
  setup () {
    const store = useStore()
    const route = useRoute()
    const getToken = function(){
      store.dispatch('root/requestRoomToken', state.roomId)
        .then((result) => {
          // 임시로 로컬스토리지에 저장
          localStorage.setItem('st', result.data[0])
          console.log(`TOKEN: ${localStorage.getItem('st')})`)
          console.log(`RoomID: ${state.roomId}`)
        })
        .catch((err) => {
          console.log(err)
        })

    }
    const state = reactive({
      OV: undefined,
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],
      //user nickname 으로 수정해야함
      nickname: 'publisher1',
      //username 으로 수정해야함
      username: 'participant1',
      roomId: '',
      token: null,
      userId:'test',
      message: '',
      // leftRecvList: [],
      // RightRecvList: [],
      recvList: [],
      stompClient: '',
    })

    const connectSession = function () {
      state.session.connect(state.token, {})
        .then(() => {
          // --- Get your own camera stream with the desired properties ---
          let publisher = state.OV.initPublisher(undefined, {
            audioSource: undefined, // The source of audio. If undefined default microphone
            videoSource: undefined, // The source of video. If undefined default webcam
            publishAudio: true,  	// Whether you want to start publishing with your audio unmuted or not
            publishVideo: true,  	// Whether you want to start publishing with your video enabled or not
            resolution: '640x480',  // The resolution of your video
            frameRate: 30,			// The frame rate of your video
            insertMode: 'APPEND',	// How the video is inserted in the target element 'video-container'
            mirror: false       	// Whether to mirror your local video or not
          })
          state.mainStreamManager = publisher
          state.publisher = publisher
          state.session.publish(state.publisher)
        })
        .catch(error => {
          console.log('There was an error connecting to the session:', error.code, error.message);
        })
    }

    onBeforeMount(() => {
      state.roomId = computed(() => route.path.split('/')[2])
      //localStorage.setItem('roomId', state.roomId)
      store.dispatch('root/requestRoomToken', state.roomId)
        .then((result) => {
          state.token = result.data[0]
          connectSession()
        })
        .catch((err) => {
          console.log(err)
        })
      // OpenVidu 객체 할당
      state.OV = new OpenVidu()
      // init session
      console.log('before session :' + state.session)
      state.session = state.OV.initSession()
      console.log('room onBeforeMount session : ')
      console.log(state.session)

      // session.on으로 웹소켓 수신에 대한 동작 맵핑
			// On every new Stream received...
			state.session.on('streamCreated', ({ stream }) => {
				const subscriber = state.session.subscribe(stream)
        console.log('room onBeforeMount subscriber : ' + subscriber)
				state.subscribers.push(subscriber)
			})

			// On every Stream destroyed...
			state.session.on('streamDestroyed', ({ stream }) => {
				const index = state.subscribers.indexOf(stream.streamManager, 0)
				if (index >= 0) {
					state.subscribers.splice(index, 1)
				}
			})


			// On every asynchronous exception...
			state.session.on('exception', ({ exception }) => {
        console.warn(exception);
			})

      // 토큰과 클라이언트의 정보를 전달하며 세션에 연결함

      //chat connect
      chatConnect()
    })

    //세션 나가기
      onBeforeUnmount(() => {
      state.session.disconnect();
      state.session = undefined
      state.mainStreamManager = undefined
      state.publisher = undefined
      state.subscribers = []
      state.OV = undefined

      const payload = {
        sessionName: state.roomId,
        token: localStorage.getItem('st')
      }
      localStorage.removeItem('st')
      store.dispatch('root/requestDeleteRoom', payload)
    })
    const updateMainVideoStreamManager = function (stream) {
      if (state.mainStreamManager === stream) return;
      state.mainStreamManager = stream
    }

    const leaveSession = function(){
      console.log('test')
    }

    ///////////////////////// 채팅 관련 ////////////////////////////

    const sendMessage = function (e) {
      console.log('eeeeee', e.keyCode, 'userId', state.userId, 'msg', state.message, 'recvList', state.recvList )
      if(e.keyCode === 13 && state.userId !== '' && state.message !== ''){
        console.log("send!@!@@@@@@@@")
        send()
        state.message = ''
      }
    }

    const send = function() {
      console.log('Send message:' + state.message);
      if (state.stompClient && state.stompClient.connected) {
        const msg = {
          type:'CHAT',
          roomId: state.roomId,
          // userName: state.form.userName,
          message: state.message,
          userId: state.userId
          // recvList: state.form.recvList,
        };
        console.log('메세지확인', msg)
        state.stompClient.send('/pub/chat/message', JSON.stringify(msg), {});
      }
    }

    const chatConnect = function() {
      const serverURL = 'https://localhost:8443/'
      let socket = new SockJS(serverURL);
      state.stompClient = Stomp.over(socket);
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
      state.stompClient.connect(
        {},
        frame => {
          // 소켓 연결 성공
          state.stompClient.connected = true;
          console.log('소켓 연결 성공', frame, 'id', state.roomId);
          // 서버의 메시지 전송 endpoint를 구독합니다.
          // 이런형태를 pub sub 구조라고 합니다.
          state.stompClient.subscribe('/sub/chat/room/' + state.roomId,
          res => {
            console.log('구독으로 받은 메시지 입니다.', res.body);

            // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
            if(state.userSid)
            state.recvList.push(JSON.parse(res.body))
          });
        },
        error => {
          // 소켓 연결 실패
          console.log('소켓 연결 실패', error);
          state.stompClient.connected = false;
        }
      );
    }

    //disconnect로 세션 leave

    return { state, updateMainVideoStreamManager, leaveSession, connectSession, chatConnect, send, sendMessage }
  }

}


</script>
