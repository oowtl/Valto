<template>
  <div id="main-container" class="container">
    <div id="session" v-if="session">
			<div id="session-header">
				<h1 id="session-title">{{ mySessionId }}</h1>
				<input class="btn btn-large btn-danger" type="button" id="buttonLeaveSession" @click="leaveSession" value="Leave session">
			</div>
			<div id="main-video" class="col-md-6">
				<user-video :stream-manager="mainStreamManager"/>
			</div>
			<div id="video-container" class="col-md-6">
				<user-video :stream-manager="publisher" @click="updateMainVideoStreamManager(publisher)"/>
				<user-video v-for="sub in subscribers" :key="sub.stream.connection.connectionId" :stream-manager="sub" @click="updateMainVideoStreamManager(sub)"/>
			</div>
		</div>
  </div>
</template>

<script>
import { reactive, onBeforeMount, onBeforeUnmount } from 'vue'
import { OpenVidu } from 'openvidu-browser'
// 12: state.publisher, 13:state.sub?

export default{
  name: 'room',

  setup () {
    const state = reactive({
      OV: undefined,
			session: undefined,
			mainStreamManager: undefined,
			publisher: undefined,
			subscribers: [],
      nickname: 'publisher1',
      username: 'participant1',
    })
    onBeforeMount(() => {
      // OpenVidu 객체 할당
      state.OV = new OpenVidu()
      // init session
      state.session = state.OV.initSession()

      // session.on으로 웹소켓 수신에 대한 동작 맵핑
			// On every new Stream received...
			state.session.on('streamCreated', ({ stream }) => {
				const subscriber = state.session.subscribe(stream)
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
      const token = localStorage.getItem('st')
          state.session.connect(token, { clientData: state.nickname })
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
            // When our HTML video has been added to DOM... 부분(jsjava예제 85줄 >>> 생략됨. nickname과 username부분 확인 필요)
            // 8. Publish your stream
            state.session.publish(state.publisher);
          })
          .catch(error => {
            console.log('There was an error connecting to the session:', error.code, error.message);
					})
    })

    //세션 나가기
    onBeforeUnmount(() => {
      state.session.disconnect();
      state.session = undefined
      state.mainStreamManager = undefined
      state.publisher = undefined
      state.subscribers = []
      state.OV = undefined
    })

    const updateMainVideoStreamManager = function (stream) {
      if (state.mainStreamManager === stream) return
      state.mainStreamManager = stream
    }

    //disconnect로 세션 leave


    return { state, updateMainVideoStreamManager }
  }

}


</script>
