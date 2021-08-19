<template>
  <li>
    <el-card :body-style="{ padding: '0px' }">
      <div :class="{ closed: room.start }">
        <div class="cardBody">
          <span class="title" style="display: flex; flex-direction: column;">
              <div style="display: flex; justify-content: flex-start;">
                <span>{{ room.topicAgree }}</span>
                <!-- 15 글자 / 12 글자 -->
                <!-- <span>하하하하하하하하하하하하</span> -->
              </div>
              <div style="display: flex; justify-content: center;">
                <span style="color: #6b6b6b;">VS</span>
              </div>
              <div style="display: flex; justify-content: flex-end;">
                <span>{{ room.topicOpposite }}</span>
                <!-- <span>딱복숭아가 맛있는가</span> -->
              </div>
          </span>
          <hr>
          <div class="bottom">
            <div style="display: flex; justify-content: center;">
              <!-- 15글자 -->
              <span>{{ room.title }}</span>
            </div>
            <div class="roomInfo" style="display: flex; justify-content: space-between;">
              <el-button type="primary" size="mini" v-if="!room.start" style="cursor: default;">Waiting</el-button>
              <el-button type="danger" size="mini" v-else style="cursor: default;">Playing</el-button>
              <div style="display: flex; align-items: center;">
                <div style="display: flex; align-items: center; margin-right: 1rem;">
                  <UserFilled :style="[state.buttonBase]" style="color: grey; opacity: 0.8;"/>
                  <span style="color: grey; opacity: 0.8; font-size: 1rem;">{{ room.userTotalCount }} / {{ room.participants }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </li>
</template>
<style scoped>
.el-card {
  margin: 0 8px;
  margin-bottom: 40px;
}
.el-card .title {
  /* font-weight: bold; */
  font-size: 25px;
}

.el-card .bottom {
  font-weight: bold;
  font-size: 20px;
  /* border-bottom: 1px solid black; */
  text-align: center;
  /* margin-top: 1rem; */
  display:-webkit-box;
  word-wrap:break-word;
  -webkit-box-orient:vertical;
  /* overflow:hidden; */
  text-overflow:ellipsis;
}
div .cardBody {
  text-align: left;
  padding: 14px;
  background-color: #fafafa;
}
div .closed {
  background-color: #000;
  backdrop-filter: blur(8px);
  z-index:990;
}

.el-card .bottom .roomInfo {
  margin-top: 1rem;
}

/* 테블릿, 모바일의 경우 두 줄 말줄임표시 */
@media (max-width: 1269px) {
  .el-card .bottom {
    -webkit-line-clamp: 2;
    /* height:42px; */
  }
}
/* 데스크탑의 경우 세 줄 말줄임표시 */
@media (min-width: 1270px) {
  .el-card .bottom {
    -webkit-line-clamp: 3;
    /* height:60px; */
  }
}

</style>
<script>
import { reactive } from 'vue'
import { Lock, Unlock, UserFilled } from '@element-plus/icons'

export default {
  name: 'Room',
  components: {
    Lock,
    Unlock,
    UserFilled,
  },
  props: {
    room: {
      type: Object
    }
  },
  setup () {
    const state = reactive({
      buttonBase: { width: '1em'},
    })

    return { state }
  }
}
</script>
