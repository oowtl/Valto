<template>
  <div id="main-container" class="container">
    <h1>test</h1>
    <div id="session" v-if="session">
			<div id="session-header">
				<h1 id="session-title">{{ nickname }}</h1>
				<input class="btn btn-large btn-danger" type="button" id="buttonLeaveSession" @click="leaveSession" value="Leave session">
			</div>
			<div id="main-video" class="col-md-6">
				<user-video :stream-manager="mainStreamManager" />
			</div>
			<div id="video-container" class="col-md-6">
				<user-video :stream-manager="publisher" @click="updateMainVideoStreamManager(publisher)" />
				<user-video v-for="sub in subscribers" :key="sub.stream.connection.connectionId" :stream-manager="sub" @click="updateMainVideoStreamManager(sub)"/>
			</div>
		</div>
  </div>
</template>

<script>
import { OpenVidu } from 'openvidu-browser'
import UserVideo from './components/UserVideo';

// 12: this.publisher, 13:this.sub?

export default{
  name: 'room',

  components: {
    UserVideo
  },

	data () {
		return {
			OV: undefined,
			session: undefined,
			mainStreamManager: undefined,
			publisher: undefined,
			subscribers: [],

      nickname: 'publisher1',
      username: 'participant1',
		}
	},

  beforeMount: function () {
    // OpenVidu 객체 할당
    this.OV = new OpenVidu()
    console.log(this.OV)

    // init session
    this.session = this.OV.initSession()

    // session.on으로 웹소켓 수신에 대한 동작 맵핑
    // On every new Stream received...
    this.session.on('streamCreated', ({ stream }) => {
      const subscriber = this.session.subscribe(stream)
      console.log('room onBeforeMount subscriber : ' + subscriber)
      this.subscribers.push(subscriber)
    })

    // On every Stream destroyed...
    this.session.on('streamDestroyed', ({ stream }) => {
      const index = this.subscribers.indexOf(stream.streamManager, 0)
      if (index >= 0) {
        this.subscribers.splice(index, 1)
      }
    })

    // On every asynchronous exception...
    this.session.on('exception', ({ exception }) => {
      console.warn(exception);
    })

    // 토큰과 클라이언트의 정보를 전달하며 세션에 연결함
    const token = localStorage.getItem('st')
    this.session.connect(token, { clientData: this.nickname })
      .then(() => {
        // --- Get your own camera stream with the desired properties ---
        let publisher = this.OV.initPublisher(undefined, {
          audioSource: undefined, // The source of audio. If undefined default microphone
          videoSource: undefined, // The source of video. If undefined default webcam
          publishAudio: true,  	// Whether you want to start publishing with your audio unmuted or not
          publishVideo: true,  	// Whether you want to start publishing with your video enabled or not
          resolution: '640x480',  // The resolution of your video
          frameRate: 30,			// The frame rate of your video
          insertMode: 'APPEND',	// How the video is inserted in the target element 'video-container'
          mirror: false       	// Whether to mirror your local video or not
        })
        this.mainStreamManager = publisher
        this.publisher = publisher
        console.log(this.publisher)
        this.session.publish(this.publisher)
      })
      .catch(error => {
        console.log('There was an error connecting to the session:', error.code, error.message);
      })
  },

  methods: {
      leaveSession () {
			// --- Leave the session by calling 'disconnect' method over the Session object ---
        if (this.session) this.session.disconnect();

        this.session = undefined;
        this.mainStreamManager = undefined;
        this.publisher = undefined;
        this.subscribers = [];
        this.OV = undefined;
      },

      updateMainVideoStreamManager (stream) {
          if (this.mainStreamManager === stream) return;
          this.mainStreamManager = stream;
        },
    },

  beforeUnmount: function () {
    this.leaveSession()
  }
}
</script>
