<template>
  <div id="container">
		<div id="wrapper">
			<div id="join" class="animate join">
				<h1>Join a Room</h1>
				<form accept-charset="UTF-8">
					<p>
						<input v-model="state.name">
					</p>
					<p>
						<input v-model="state.room">
					</p>
					<p class="submit">
						<!-- <input name="commit" value="Join!"> -->
            <button @click="register">Join!</button>
					</p>
				</form>
			</div>
			<div id="room">
				<h2 id="room-header"></h2>
				<div id="participants"></div>
				<input type="button" id="button-leave"
					value="Leave room">
			</div>
		</div>
	</div>
  <button @click="testtest">TESTTESTTESTTESTTESTTEST</button>
</template>

<script>
import { reactive, onMounted } from 'vue'



export default {
  name: 'test',
  setup() {
    const state = reactive({
      name: 'asd',
      room: '123',
    })
    // var ws = new WebSocket('wss://localhost:8083/groupcall')
    var ws = new SockJS('https://localhost:8443/groupcall')


    console.log(ws)
    ws.onmessage = function(message) {
      // json 데이터를 unserialize
      var parsedMessage = JSON.parse(message.data);
      console.info('Received message: ' + message.data);
      switch (parsedMessage.id) {
      case 'existingParticipants':
        console.log('existingParticipants')
        break;
      case 'newParticipantArrived':
        console.log('newParticipantArrived')
        break;
      case 'participantLeft':
        break;
      case 'receiveVideoAnswer':
        break;
      case 'iceCandidate':
        console.log('iceCandidate')
        break;
      default:
        console.error('Unrecognized message', parsedMessage);
      }
    }






    const testtest = function () {
      console.log(ws)
    }


    const register = function () {
      var message = {
        id : 'joinRoom',
        name : state.name,
        room : state.room,
      }
    }
    onMounted (() => {
      console.log('hi')
    })
    return { state, register, testtest }
  },
}
</script>
