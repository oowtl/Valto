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
</template>

<script>
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
import { OpenVidu } from 'openvidu-browser'
import UserVideo from './components/UserVideo';
import { reactive, computed, onBeforeMount, onBeforeUnmount } from 'vue'

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

    //disconnect로 세션 leave

    return { state, updateMainVideoStreamManager, leaveSession, connectSession }
  }

}


</script>
