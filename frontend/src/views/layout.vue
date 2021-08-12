<template>
  <div class="room-wrapper">
    <div class="session">
      <div class="divider">
        <div class="partition left">
          왼쪽
          <div class="container">
            <div id="main-video" class="col-md-6">
              <div>main video</div>
            </div>
            <div id="video-container" class="col-md-6">
              <div>videos_vfor</div>
            </div>
          </div>
        </div>
        <div class="partition right">
          오른쪽
          <div class="container">
          </div>
        </div>
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
      </div>
		</div>

    <div class="footer">
      <div class="footer-child session-title">
        <p> 세션 타이틀!! </p>
      </div>
      <div class="footer-child controller">

        <microphone :style="[state.buttonBase]" />
        <mute :style="[state.buttonBase, {color: 'red'}]" />
        <video-camera :style="[state.buttonBase]" />
        <video-camera :style="[state.buttonBase, {color: 'red'}]" />
        <close-bold :style="[state.buttonBase, {color: 'red'}]" />

      </div>
      <div class="footer-child communication">
        <bell-filled :style="[state.buttonBase]" />
        <opportunity :style="[state.buttonBase]" />
        <mic :style="[state.buttonBase]" />
        <chat-dot-round :style="[state.buttonBase, state.chatButton]" @click="onClickChat" />
        <user :style="[state.buttonBase, state.memberButton]" @click="onClickMember" />
      </div>
    </div>
  </div>
</template>

<script>
import { reactive, computed } from 'vue'
import { Mic, Mute, User, BellFilled, CloseBold, Microphone, VideoCamera, ChatDotRound, Opportunity } from '@element-plus/icons'

export default {
  components: {
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
  setup() {
    const state = reactive({
      openChat: false,
      openMember: false,
      openPanel: computed(() => state.openChat || state.openMember),
      buttonBase: { width: '2em', height: '2.2em', color: 'white', marginRight: '18px', cursor: 'pointer' },
      chatButton: { color: 'grey' },
      memberButton: { color: 'grey' },
    })

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
    return { state, onClickChat, onClickMember }
  },
}
</script>

<style scoped>
  @import './room.css';
</style>
