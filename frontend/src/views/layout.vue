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
      </div>


		</div>
    <div class="footer">
      <div class="footer-child session-title">
        <p> 세션 타이틀!! </p>
      </div>
      <div class="footer-child controller">

        <microphone style="width: 2em; height: 2em; color: white; margin-right: 16px;" />
        <mute style="width: 2em; height: 2em; color: grey; margin-right: 16px;" />
        <video-camera style="width: 2em; height: 2em; color: green; margin-right: 16px;" />
        <video-camera style="width: 2em; height: 2em; color: grey; margin-right: 16px;" />
        <phone-filled style="width: 2em; height: 2em; color: red; margin-right: 16px;" />
        <close-bold style="width: 2em; height: 2em; color: red; margin-right: 16px;" />

      </div>
      <div class="footer-child communication">
        <chat-dot-round style="width: 2em; height: 2em; color: white; margin-right: 16px;" />
        <chat-dot-round style="width: 2em; height: 2em; color: grey; margin-right: 16px;" />
        <user style="width: 2em; height: 2em; color: white; margin-right: 8px;" />
        <user style="width: 2em; height: 2em; color: grey; margin-right: 8px;" />
      </div>
    </div>
  </div>
</template>

<script>
import { Mute } from '@element-plus/icons'
import { User } from '@element-plus/icons'
import { CloseBold } from '@element-plus/icons'
import { Microphone } from '@element-plus/icons'
import { VideoCamera } from '@element-plus/icons'
import { PhoneFilled } from '@element-plus/icons'
import { ChatDotRound } from '@element-plus/icons'
export default {
  components: {
    Mute,
    User,
    CloseBold,
    Microphone,
    PhoneFilled,
    VideoCamera,
    ChatDotRound,
  },
  setup() {
  },
}
</script>

<style scoped>
  @import './room.css';
</style>
