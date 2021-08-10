<template>
 <div class="container">
  <div id="room">
    <h2 id="room-header"></h2>
    <div id="participants"></div>
    <input type="button" id="button-leave" @click="leaveRoom"
      value="Leave room">
	</div>
 </div>
</template>
<script>
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
import { reactive, onBeforeMount, onBeforeUnmount, onMounted, computed } from 'vue'
import Participant from '@/views/room/js/participants'

export default{
  setup() {
    const route = useRoute()
    const store = useStore()
    const state = reactive({
      // 로딩 시간에 따라 name이 전달되지 않는 경우가 있음!
      room: 1,
      name: 'test',
      participants:{},
    })




    // 자바 백엔드와 연결되는 웹소켓
    // maven 소켓 연결
    const ws = new WebSocket('wss://localhost:8443/groupcall')
    ws.onmessage = function(message) {
      var parsedMessage = JSON.parse(message.data)
      console.info('Received message: ' + message.data)

      switch (parsedMessage.id) {
      case 'existingParticipants':
        onExistingParticipants(parsedMessage)
        break
      case 'newParticipantArrived':
        onNewParticipant(parsedMessage)
        break
      case 'participantLeft':
        onParticipantLeft(parsedMessage)
        break
      case 'receiveVideoAnswer':
        receiveVideoResponse(parsedMessage)
        break
      case 'iceCandidate':
        state.participants[parsedMessage.name].rtcPeer.addIceCandidate(parsedMessage.candidate, function (error) {
              if (error) {
              console.error("Error adding candidate: " + error)
              return
              }
          })
          break
      default:
        console.error('Unrecognized message', parsedMessage)
      }
    }

    const register = function(){

    }

    onBeforeMount(()=> {
      const message = {
        id : 'joinRoom',
        name : state.name,
        room : state.room,
      }
      console.log(message)
      sendMessage(message)
    })

    onBeforeUnmount(()=>{
      ws.close()
    })

    const onNewParticipant = function(request){
      receiveVideo(request.name)
    }

    const receiveVideo = function(sender) {
      var participant = new Participant(sender)
      state.participants[sender] = participant
      var video = participant.getVideoElement()

      var options = {
          remoteVideo: video,
          onicecandidate: participant.onIceCandidate.bind(participant)
        }

      participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(options,
          function (error) {
            if(error) {
              return console.error(error)
            }
            this.generateOffer(participant.offerToReceiveVideo.bind(participant))
      })
    }

    const receiveVideoResponse = function(result){
      state.participants[result.name].rtcPeer.processAnswer (result.sdpAnswer, function (error) {
          if (error) return console.error (error)
        })
    }

    const callResponse = function(message){
      if (message.response != 'accepted') {
        console.info('Call not accepted by peer. Closing call');
        stop()
      } else {
        webRtcPeer.processAnswer(message.sdpAnswer, function (error) {
          if (error) return console.error (error)
        })
      }
    }

    const onExistingParticipants = function(msg) {
      var constraints = {
        audio : true,
        video : {
          mandatory : {
            maxWidth : 320,
            maxFrameRate : 15,
            minFrameRate : 15
          }
        }
      }
      console.log(state.name + " registered in room " + state.room)
      var participant = new Participant(state.name)
      participants[state.name] = participant
      var video = participant.getVideoElement()

      var options = {
            localVideo: video,
            mediaConstraints: constraints,
            onicecandidate: participant.onIceCandidate.bind(participant)
          }
      participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendonly(options,
        function (error) {
          if(error) {
            return console.error(error)
          }
          this.generateOffer(participant.offerToReceiveVideo.bind(participant))
      })

      msg.data.forEach(receiveVideo)
    }

    const leaveRoom = function() {
      alert("leave")
      sendMessage({
        id : 'leaveRoom'
      });

      for (var key in state.participants) {
        state.participants[key].dispose()
      }
      ws.close()
    }

    const onParticipantLeft = function(request) {
      console.log('Participant ' + request.name + ' left')
      var participant = participants[request.name]
      participant.dispose()
      delete participants[request.name]
    }

    const sendMessage = function(message) {
      var jsonMessage = JSON.stringify(message)
      console.log('Sending message: ' + jsonMessage)

      waitForConnection(function(){
        ws.send(jsonMessage)

        console.log(ws);
        if (typeof callback !== 'undefined') {
          callback();
        }
      }, 1000)
    }

    const waitForConnection = function (callback, interval) {
      if (ws.readyState === 1) {
          callback();
      } else {
          var that = this;
          // optional: implement backoff for interval here
          setTimeout(function () {
              console.log("websocket connecting...")
              waitForConnection(callback, interval);
          }, interval);
      }
  }

    return {state, waitForConnection, onNewParticipant, receiveVideo, receiveVideoResponse, callResponse, onExistingParticipants, leaveRoom, onParticipantLeft, sendMessage}
  }
}
</script>
