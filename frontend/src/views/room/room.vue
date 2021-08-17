<template>
  <div class="room-wrapper">
    <div id="session" v-if="state.session">
      <div class="divider">
        <!-- 왼쪽 값을 가져와서 있으면 보여주는 쪽으로?? -->
          <div class="partition left" v-if="state">
            왼쪽
            <div class="container">
              <!-- <div id="main-video" class="col-md-6">
                <user-video :stream-manager="state.mainStreamManager" />
              </div> -->
              <div id="video-container" class="col-md-6">
                <user-video :stream-manager="state.publisher" @click="updateMainVideoStreamManager(state.publisher)" />
                <user-video v-for="sub in state.subscribers" :key="sub.stream.connection.connectionId" :stream-manager="sub" @click="updateMainVideoStreamManager(sub)"/>
              </div>
            </div>
          </div>
          <!-- left end -->
          <div class="partition right">
            오른쪽
            <div class="container">
              <!-- <div id="main-video" class="col-md-6">
                <user-video :stream-manager="state.mainStreamManager" />
              </div> -->
              <div id="video-container" class="col-md-6">
                <user-video :stream-manager="state.publisher" @click="updateMainVideoStreamManager(state.publisher)" />
                <user-video v-for="sub in state.subscribers" :key="sub.stream.connection.connectionId" :stream-manager="sub" @click="updateMainVideoStreamManager(sub)"/>
              </div>
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
<!-- 세션 나가기 버튼  -->
  <!-- <input class="btn btn-large btn-danger" type="button" id="buttonLeaveSession" @click="leaveSession" value="Leave session"> -->
</template>

<script>
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
import { OpenVidu } from 'openvidu-browser'
import UserVideo from './components/UserVideo';
import { reactive, computed, onBeforeMount, onBeforeUnmount } from 'vue'
import { Mic, Mute, User, BellFilled, CloseBold, Microphone, VideoCamera, ChatDotRound, Opportunity } from '@element-plus/icons'

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

    })

    const subsTest = function () {
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
      store.dispatch('root/requestRoomToken', state.roomId)
        .then((result) => {
          state.token = result.data[0]
          console.log('@@@@@@@@@@')
          console.log(result.data)
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
      console.log('test')
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

    //disconnect로 세션 leave

    return {
      state,
      updateMainVideoStreamManager,
      leaveSession,
      connectSession,
      onClickChat,
      onClickMember,
      subsTest,
    }
  }
}
</script>
<style scoped>
  @import '../room.css';
</style>
