<template>
  <div class="room-wrapper">
    <div id="session" v-if="state.publisher">
      <div class="divider">
          <div class="partition partition-left">
            <div class="main-video">
              <user-video :stream-manager="state.mainStreamManagerLeft" v-if="state.mainStreamManagerLeft" />
            </div>
            <div class="sub-video">
              <user-video v-for="sub in state.leftSubs" :key="sub.stream.connection.connectionId" :stream-manager="sub" @click="updateMainVideoStreamManagerLeft(sub)"/>
            </div>
          </div>
          <div class="partition partition-right">
            <div class="main-video">
              <user-video :stream-manager="state.mainStreamManagerRight" v-if="state.mainStreamManagerRight" />
            </div>
            <div class="sub-video">
              <user-video v-for="sub in state.rightSubs" :key="sub.stream.connection.connectionId" :stream-manager="sub" @click="updateMainVideoStreamManagerRight(sub)"/>
            </div>
          </div>
          <!-- chat start -->
          <transition name="slide">
          <div class="panel" v-if="state.openPanel">
            <div class="panelChild" v-if="state.openChat">
              <p>채팅창</p>
                <div v-if="state.side === 'left'" class="chat">
                  <el-scrollbar>
                    <div class="chat-log">
                      <span v-for="(item, idx) in state.leftRecvList" :key="idx" style="display: block;">
                        {{ item.userId }} : {{ item.message }}
                      </span>
                    </div>
                  </el-scrollbar>
                  <div class="chat-input">
                    채팅: <input v-model="state.message" type="text" @keyup="sendMessage">
                  </div>
                </div>
                <div v-else class="chat">
                  <el-scrollbar>
                    <div class="chat-log">
                      <span v-for="(item, idx) in state.rightRecvList" :key="idx" style="display: block;">
                        {{ item.nickName}} ({{ item.userId }}) : {{ item.message }}
                      </span>
                    </div>
                  </el-scrollbar>
                  <div class="chat-input">
                    채팅: <input v-model="state.message" type="text" @keyup="sendMessage" class="chat-input-window">
                  </div>
                </div>
            </div>
            <div class="panelChild member" v-if="state.openMember">
              <p>멤버</p>
            </div>
          </div>
        </transition>
      </div>
		</div>

  </div>
  <!-- footer start -->
  <div class="footer">
    <div class="footer-child head-controller" style="fontSize: 30px;">
      {{ state.topicL}} <span style="color: red; margin: 0px 10px 0px 10px;"> vs </span> {{ state.topicR}}
    </div>
    <div class="footer-child controller">
      <div v-if="state.start">
        <switchButton :style="[state.buttonBase, {color: 'red'}, state.endButton]" @click="onClickEndGame"  v-if="state.userId === state.ownerId" />
      </div>
      <div v-else>
        <video-play :style="[state.buttonBase, {color: 'red'}]" @click="onClickStart" v-if="state.ownerId === state.userId" />
      </div>
      <microphone :style="[state.buttonBase]" />
      <video-camera :style="[state.buttonBase]" />
      <!-- <video-camera :style="[state.buttonBase, {color: 'red'}]" /> -->
      <close-bold :style="[state.buttonBase, {color: 'red'}]" @click="onClickLeave"/>
    </div>
    <div class="footer-child communication">
      <chat-dot-round :style="[state.buttonBase, state.chatButton]" @click="onClickChat" />
    </div>
  </div>
  <!-- footer end -->
  <!-- chat start -->
  <!-- <el-container>
    <el-aside width="250px">
      <div id="socket">

      </div>
    </el-aside>
    </el-container> -->
  <!-- chat end -->
</template>



<script>
import { useStore } from 'vuex'
import { useRoute, useRouter } from 'vue-router'
import { OpenVidu } from 'openvidu-browser'
import UserVideo from './components/UserVideo';
import { reactive, computed, onBeforeMount, onBeforeUnmount } from 'vue'
import { Mic, Mute, User, BellFilled, CloseBold, Microphone, VideoCamera, ChatDotRound, Opportunity, VideoPlay, DArrowRight, SwitchButton } from '@element-plus/icons'
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

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
    VideoPlay,
    DArrowRight,
  },
  setup (){
    const store = useStore()
    const route = useRoute()
    const router = useRouter()
    const state = reactive({
      OV: undefined,
      session: undefined,
      mainStreamManagerLeft: undefined,
      mainStreamManagerRight: undefined,
      publisher: undefined,
      subscribers: [],
      leftSubs: [],
      rightSubs: [],
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
      buttonBase: { width: '2em', height: '3em', color: 'white', marginRight: '18px', cursor: 'pointer'},
      chatButton: { color: 'grey' },
      memberButton: { color: 'grey' },
      userSide: computed(() => store.getters['root/getUserSide']),
      side: '',
      userId:'',
      message: '',
      leftRecvList: [],
      RightRecvList: [],
      recvList: [],
      stompClient: '',
      ownerId:'',
      start:false,
      end:false,
      topicL: '',
      topicR: '',
    })

    const subsTest = function () {
      console.log(state.userSide)
      console.log(state.leftSubs)
      console.log(state.rightSubs)
    }
    const connectSession = function () {
      // state.session.connect(state.token, {})
      const side = localStorage.getItem('userSide')
      console.log('@@@@@룸 connectsession userside@@@@@')
      console.log(side)
      state.session.connect(state.token, { side: side })
        .then(() => {
          // --- Get your own camera stream with the desired properties ---
          let publisher = state.OV.initPublisher('publisherStartSpeaking', {
            audioSource: undefined, // The source of audio. If undefined default microphone
            videoSource: undefined, // The source of video. If undefined default webcam
            publishAudio: true,  	// Whether you want to start publishing with your audio unmuted or not
            publishVideo: true,  	// Whether you want to start publishing with your video enabled or not
            resolution: '320x240',  // The resolution of your video
            frameRate: 30,			// The frame rate of your video
            insertMode: 'APPEND',	// How the video is inserted in the target element 'video-container'
            mirror: false       	// Whether to mirror your local video or not
          })
          if (side === 'agree') {
            state.mainStreamManagerLeft = publisher
          } else if (side === 'opposite') {
            state.mainStreamManagerRight = publisher
          }
          state.publisher = publisher
          state.session.publish(state.publisher)
        })
        .catch(error => {
          console.log('There was an error connecting to the session:', error.code, error.message);
        })
    }

    const updateMainVideoStreamManager = function (stream) {
      if (state.mainStreamManager === stream) return;
      state.mainStreamManager = stream
    }




    onBeforeMount(() => {
      state.roomId = route.path.split('/')[2]
      const side = localStorage.getItem('userSide')
      console.log('@@@@@ 룸 입장 userside@@@@@')
      console.log(side)
      const payload = {
        roomId: state.roomId,
        // userSide: state.userSide,
        userSide: localStorage.getItem('userSide')
      }
      store.dispatch('root/requestRoomToken', payload)
        .then((result) => {
          console.log(result)
          state.token = result.data[0]
          state.side = result.data[1]
          state.nickname = result.data[2]
          state.ownerId = result.data[3]
          state.userId = result.data[4]
          state.topicL = result.data[5]
          state.topicR = result.data[6]
          connectSession()
            .then(() => {

            })

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
        let side = JSON.parse(subscriber.stream.connection.data.split('%')[0]).side
        if (side === 'agree') {
          state.leftSubs.push(subscriber)
        } else if (side === 'opposite') {
          state.rightSubs.push(subscriber)
        }
			})

			// On every Stream destroyed...
			state.session.on('streamDestroyed', ({ stream }) => {
				const leftIndex = state.leftSubs.indexOf(stream.streamManager, 0)
				const rightIndex = state.rightSubs.indexOf(stream.streamManager, 0)

				if (leftIndex >= 0) {
					state.leftSubs.splice(leftIndex, 1)
				} else if (rightIndex >= 0) {
          state.rightSubs.splice(rightIndex, 1)
        }
        // 강퇴기능
        const userData = stream.connection.data
        const userDataStartIndex = userData.indexOf('serverData')
        const disconnectedUser = userData.slice(userDataStartIndex+14, userData.length - 2)
        // 방장이 나가면 다 나가는 걸로!
        if (state.ownerId == disconnectedUser && state.end) {
          // 방 나가기 요청
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

          state.session.disconnect();
          router.push({
            name : 'home'
          })
        }
      })

      // 음성감지
      state.session.on('publisherStartSpeaking' , (event) => {
        // state.speech = event.connection.connectionId
        // console.log(state.publisher)
        // console.log(state.subscribers)
        // let sub = state.subscribers.find(function (s) {
        //   return s.stream.connection.connectionId === event.connection.connectionId
        // })
        // console.log(sub)
        // // updateMainVideoStreamManager 이 함수가 subscribers 배열에서 sub 이름으로 객체 하나를 뽑아서 실행되니까
        // // state.subscribers 배열에서 connectionId가 위의 event.connection.connectionId 랑 같은 걸 찾아서
        // // 아래에서 updateMainVideoStreamManager 함수에 해당하는 sub를 넣어주면 메인 비디오가 전환됨!
        // updateMainVideoStreamManager(sub)
        // state.speech = event.connection.connectionId
        let leftSub = state.subscribers.find(function (s) {
          return s.stream.connection.connectionId === event.connection.connectionId
        })
        let rightSub = state.subscribers.find(function (s) {
          return s.stream.connection.connectionId === event.connection.connectionId
        })

        if (leftSub) {
          updateMainVideoStreamManagerLeft(leftSub)
        } else if (rightSub) {
          updateMainVideoStreamManagerRight(rightSub)
        }
        console.log('User ' + event.connection.connectionId + ' start speaking');
      })
      state.session.on('publisherStopSpeaking', (event) => {
        state.speech = '';
        console.log('User ' + event.connection.connectionId + ' stop speaking');
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
      if(state.ownerId === state.userId) state.end = true;

      state.session.disconnect();
      state.session = undefined
      state.mainStreamManagerLeft = undefined
      state.mainStreamManagerRight = undefined
      state.publisher = undefined
      state.leftSubs = []
      state.rightSubs = []
      state.OV = undefined

      const payload = {
        sessionName: state.roomId,
        token: state.token,
      }
      store.dispatch('root/requestDeleteRoom', payload)
        .then(() => {
        })
        .catch((err) => {
          console.log(err)
        })
    })

    const updateMainVideoStreamManagerLeft = function (stream) {
      if (state.mainStreamManagerLeft === stream) return
      // const idx = state.leftSubs.find(sub => sub === stream)
      const idx = state.leftSubs.indexOf(stream)
      state.leftSubs.splice(idx, 1)
      state.leftSubs.push(state.mainStreamManagerLeft)
      state.mainStreamManagerLeft = stream
    }

    const updateMainVideoStreamManagerRight = function (stream) {
      if (state.mainStreamManagerRight === stream) return
      const idx = state.leftSubs.indexOf(stream)
      state.rightSubs.splice(idx, 1)
      state.rightSubs.push(state.mainStreamManagerRight)
      state.mainStreamManagerRight = stream
    }
    // const updateMainVideoStreamManagerLeft = function (stream) {
    //   if (state.mainStreamManagerLeft === stream) return;
    //   state.mainStreamManagerLeft = stream
    // }
    // const updateMainVideoStreamManagerRight = function (stream) {
    //   if (state.mainStreamManagerRight === stream) return;
    //   state.mainStreamManagerRight = stream
    // }

    const onClickLeave = function(){
        router.push({
          name : 'home'
        })
    }

    const onClickEndGame = function () {
      if (state.userId === state.ownerId) {
        if (confirm('게임을 종료하시겠습니까?') === true) {
          alert('종료하겠습니다.')
          state.end = true
        } else {
          return
        }
        // leavesession 함수화 및 호출 (아래3줄및 onBeforeUnmount 대체)
        state.session.disconnect()
        router.push({
          name : 'home'
        })
      }
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

    //  토론 시작
    const onClickStart = function(){

      store.dispatch('root/startDebate', state.roomId)
        .then((result) => {
          console.log(result)
          if(state.start) state.start = false;
          else state.start = true;
          alert('토론 시작하였습니다.')
        }).catch((err) =>{
          console.log(err)
        })
    }

    ///////////////////////// 채팅 관련 ////////////////////////////
    const sendMessage = function (e) {
      if(e.keyCode === 13 && state.userId !== '' && state.message !== '') {
        console.log(state.message)
        send()
        state.message = ''
      }
    }

    const send = function () {
      if (state.stompClient && state.stompClient.connected) {
        const msg = {
          type:'CHAT',
          roomId: state.roomId,
          message: state.message,
          userId: state.userId,
          nickName: state.nickname
        }
        console.log(msg)
        state.stompClient.send('/pub/chat/message', JSON.stringify(msg), {})
      }
    }

    const chatConnect = function () {
      // 배포때봐야함
      const serverURL = 'https://i5d103.p.ssafy.io:8443/api'
      let socket = new SockJS(serverURL)
      state.stompClient = Stomp.over(socket)
      state.stompClient.connect(
        {},
        frame => {
          // 소켓 연결 성공
          state.stompClient.connected = true
          console.log('소켓 연결 성공', frame, 'id', state.roomId)
          // 서버의 메시지 전송 endpoint를 구독합니다.
          // 이런형태를 pub sub 구조라고 합니다.
          state.stompClient.subscribe('/sub/chat/room/' + state.roomId,
          res => {
            console.log('구독으로 받은 메시지 입니다.', res.body)

            // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
            if (state.side === 'left') {
              console.log('left chat push')
              state.leftRecvList.push(JSON.parse(res.body))
            } else {
              console.log('right chat push')
              state.rightRecvList.push(JSON.parse(res.body))
            }

          })
        },
        error => {
          // 소켓 연결 실패
          console.log('소켓 연결 실패', error)
          state.stompClient.connected = false
        }
      )
    }



    //disconnect로 세션 leave
  // chatConnect, send, sendMessage,
    return {
      state,
      updateMainVideoStreamManager,
      updateMainVideoStreamManagerLeft,
      updateMainVideoStreamManagerRight,
      onClickLeave,
      connectSession,
      onClickChat,
      onClickMember,
      subsTest,
      onClickStart,
      chatConnect,
      send,
      sendMessage,
      onClickEndGame,
    }
  }

}
</script>
<style scoped>
  @import '../room.css';
</style>
