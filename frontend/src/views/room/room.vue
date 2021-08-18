<template>
  <div class="room-wrapper">
    <div id="session" v-if="state.session">
      <div class="divider">
        <!-- 왼쪽 값을 가져와서 있으면 보여주는 쪽으로?? -->
          <div class="partition-left">
            왼쪽
              <div id="main-video" class="col-md-6">
                <user-video :stream-manager="state.mainStreamManager" />
              </div>

              <div id="video-container" class="col-md-6">
                <!-- <user-video :stream-manager="state.publisher" @click="updateMain+VideoStreamManager(state.publisher)" /> -->
                <user-video class="candidates" v-for="sub in state.subscribers" :key="sub.stream.connection.connectionId" :stream-manager="sub" @click="updateMainVideoStreamManager(sub)"/>
              </div>

          </div>
          <!-- left end -->
          <!--  v-if="state.side === 'right'" -->
          <div class="partition-right">
            오른쪽
              <div id="main-video" class="col-md-6">
                <user-video :stream-manager="state.mainStreamManager" />
              </div>
              <div id="video-container" class="col-md-6">
                <!-- <user-video :stream-manager="state.publisher" @click="updateMainVideoStreamManager(state.publisher)" /> -->
                <user-video  class="candidates" v-for="sub in state.subscribers" :key="sub.stream.connection.connectionId" :stream-manager="sub" @click="updateMainVideoStreamManager(sub)"/>
              </div>
          </div>
          <!-- right end -->
          <!-- chat start -->
          <transition name="slide">
          <div class="panel" v-if="state.openPanel">
            <div class="panelChild chat" v-if="state.openChat">
              <p>채팅창</p>
            </div>
            <div class="panelChild member" v-if="state.openMember">
              <p>멤버</p>
            </div>
          </div>
        </transition>
        <!-- chat end -->
      </div>
		</div>

  </div>
  <!-- footer start -->
  <div class="footer">
    <div class="footer-child head-controller">
      <switchButton :style="[state.buttonBase, {color: 'red'}]" @click="onClickEndGame"/>
    </div>
    <div class="footer-child controller">
      <microphone :style="[state.buttonBase]" />
      <mute :style="[state.buttonBase, {color: 'red'}]" />
      <video-camera :style="[state.buttonBase]" />
      <video-camera :style="[state.buttonBase, {color: 'red'}]" />
      <close-bold :style="[state.buttonBase, {color: 'red'}]" @click="subsTest"/>
    </div>
    <div class="footer-child communication">
      <bell-filled :style="[state.buttonBase]" />
      <opportunity :style="[state.buttonBase]" />
      <mic :style="[state.buttonBase]" />
      <chat-dot-round :style="[state.buttonBase, state.chatButton]" @click="onClickChat" />
      <user :style="[state.buttonBase, state.memberButton]" @click="onClickMember" />
    </div>
  </div>
  <!-- footer end -->
  <!-- chat start -->
  <!-- <el-container>
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
    </el-container> -->
  <!-- chat end -->
</template>



<script>
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
import { OpenVidu } from 'openvidu-browser'
import UserVideo from './components/UserVideo';
import { reactive, computed, onBeforeMount, onBeforeUnmount } from 'vue'
import { Mic, Mute, User, BellFilled, CloseBold, Microphone, VideoCamera, ChatDotRound, Opportunity, SwitchButton } from '@element-plus/icons'
// import Stomp from 'webstomp-client'
// import SockJS from 'sockjs-client'

// 12: state.publisher, 13:state.sub?

export default{
  name: 'room',

  components: {
    UserVideo,
    Mic,
    Mute,
    User,
    BellFilled,
    CloseBold,
    Microphone,
    VideoCamera,
    ChatDotRound,
    Opportunity,
    SwitchButton,
  },
  setup (){
    const store = useStore()
    const route = useRoute()
    const state = reactive({
      OV: undefined,
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],
      //user nickname 으로 수정해야함
      nickname: '',
      //username 으로 수정해야함
      username: '',
      roomId: '',
      token: null,
      // footer menu setting
      openChat: false,
      openMember: false,
      openPanel: computed(() => state.openChat || state.openMember),
      buttonBase: { width: '2em', height: '3em', color: 'white', marginRight: '18px', cursor: 'pointer' },
      chatButton: { color: 'grey' },
      memberButton: { color: 'grey' },
      userSide: computed(() => store.getters['root/getUserSide']),
      side: '',
      //임의 userId
      userId:'test',
      message: '',
      // leftRecvList: [],
      // RightRecvList: [],
      recvList: [],
      stompClient: '',
    })

    const subsTest = function () {
      console.log(state.userSide)
      console.log(state.subscribers)
    }


    const connectSession = function () {
      // state.session.connect(state.token, {})
      state.session.connect(state.token, { side: state.side })
        .then(() => {
          // --- Get your own camera stream with the desired properties ---
          let publisher = state.OV.initPublisher(undefined, {
            audioSource: undefined, // The source of audio. If undefined default microphone
            videoSource: undefined, // The source of video. If undefined default webcam
            publishAudio: true,  	// Whether you want to start publishing with your audio unmuted or not
            publishVideo: true,  	// Whether you want to start publishing with your video enabled or not
            resolution: '320x240',  // The resolution of your video
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
      state.roomId = route.path.split('/')[2]
      const payload = {
        roomId: state.roomId,
        userSide: state.userSide,
      }
      store.dispatch('root/requestRoomToken', payload)
        .then((result) => {
          state.token = result.data[0]
          state.side = result.data[1]
          connectSession()
        })
        .catch((err) => {
          console.log(err)
        })

      // OpenVidu 객체 할당
      state.OV = new OpenVidu()
      // init session
      state.session = state.OV.initSession()


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
      // chatConnect()
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
        token: state.token,
      }
      store.dispatch('root/requestDeleteRoom', payload)
        .then((res) => {
        })
        .catch((err) => {
          console.log(err)
        })
    })
    const updateMainVideoStreamManager = function (stream) {
      if (state.mainStreamManager === stream) return;
      state.mainStreamManager = stream
    }

    const leaveSession = function(){
      // state.session.disconnect();
      // state.session = undefined
      // state.mainStreamManager = undefined
      // state.publisher = undefined
      // state.subscribers = []
      // state.OV = undefined
    }

    const onClickEndGame = function () {

      /*
      게임 종료 절차
      1. 방장?
      2. 종료가능 alert 같은 것 내어놓기
      3. 종료 시 session을 없앤다.
      4. api 로 요청한다.
      5. route 해준다.
       */


    }

    const onClickMember = function () {
      state.chatButton.color = 'grey'
      state.openChat = false
      state.openMember = !state.openMember
      if (state.openMember) {
        state.memberButton.color = 'white'
      } else {
        state.memberButton.color = 'grey'
      }
    }


    const onClickChat = function () {
      state.memberButton.color = 'grey'
      state.openMember = false
      state.openChat = !state.openChat
      if (state.openChat) {
        state.chatButton.color = 'white'
      } else {
        state.chatButton.color = 'grey'
      }
    }
    ///////////////////////// 채팅 관련 ////////////////////////////

    // const sendMessage = function (e) {
    //   console.log('eeeeee', e.keyCode, 'userId', state.userId, 'msg', state.message, 'recvList', state.recvList )
    //   if(e.keyCode === 13 && state.userId !== '' && state.message !== ''){
    //     console.log("send!@!@@@@@@@@")
    //     send()
    //     state.message = ''
    //   }
    // }

    // const send = function() {
    //   console.log('Send message:' + state.message);
    //   if (state.stompClient && state.stompClient.connected) {
    //     const msg = {
    //       type:'CHAT',
    //       roomId: state.roomId,
    //       // userName: state.form.userName,
    //       message: state.message,
    //       userId: state.userId
    //       // recvList: state.form.recvList,
    //     };
    //     console.log('메세지확인', msg)
    //     state.stompClient.send('/pub/chat/message', JSON.stringify(msg), {});
    //   }
    // }

    // const chatConnect = function() {
    //   const serverURL = 'https://localhost:8443/'
    //   let socket = new SockJS(serverURL);
    //   state.stompClient = Stomp.over(socket);
    //   console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
    //   state.stompClient.connect(
    //     {},
    //     frame => {
    //       // 소켓 연결 성공
    //       state.stompClient.connected = true;
    //       console.log('소켓 연결 성공', frame, 'id', state.roomId);
    //       // 서버의 메시지 전송 endpoint를 구독합니다.
    //       // 이런형태를 pub sub 구조라고 합니다.
    //       state.stompClient.subscribe('/sub/chat/room/' + state.roomId,
    //       res => {
    //         console.log('구독으로 받은 메시지 입니다.', res.body);

    //         // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
    //         if(state.userSid)
    //         state.recvList.push(JSON.parse(res.body))
    //       });
    //     },
    //     error => {
    //       // 소켓 연결 실패
    //       console.log('소켓 연결 실패', error);
    //       state.stompClient.connected = false;
    //     }
    //   );
    // }



    //disconnect로 세션 leave
  // chatConnect, send, sendMessage,
    return { state, updateMainVideoStreamManager, leaveSession, connectSession, onClickChat,onClickMember, subsTest }
  }

}
</script>
<style scoped>
  @import '../room.css';

  /* #socket {
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
  } */
</style>
