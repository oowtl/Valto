<template>
  <h1>{{ state.query }}</h1>
    <button class="el-button el-button--primary" type="button">
      <i class="el-icon-sort"></i>
      <span>제목</span>
    </button>
  <ul class="room-list">
    <li v-for="room in state.rooms" :key="room.roomId" @click="clickRoom(room.roomId)" class="room-list-item">
      <room :room="room"/>
    </li>
  </ul>


</template>
<style>
.room-list {
  padding-left: 0;
  max-height: calc(100% - 35px);
  list-style:none;
}

@media (min-width: 701px) and (max-width: 1269px) {
  .room-list {
    min-width: 700px;
  }
}

@media (min-width: 1270px) {
  .room-list {
    min-width: 1021px;
  }
}

.room-list .room-list-item {
  min-width: 335px;
  max-width: 25%;
  display: inline-block;
  cursor: pointer;
}
.el-dropdown-link {
    cursor: pointer;
    color: #409EFF;
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
</style>
<script>
  // <ul>
    // <room
    //   v-for="room in state.rooms"
    //   :key="room.roomId"
    //   :room="room"
    //   @click="clickRoom(room.roomId)"
    // />
  // </ul>
import Room from './components/room'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
import { reactive, onMounted, computed, watch } from 'vue'

export default {
  name: 'Home',

  components: {
    Room
  },

  setup (props, { emit }) {
    const store = useStore()
    const route = useRoute()
    const state = reactive({
      query: computed(() => route.query),
      rooms: [
        {
          roomId: 1,
          userId: 'aasdasd',
          participants: 2,
          observers: 2,
          times: 20,
          title: 't1',
          roomPassword: '',
          topicAgree: 'asdasdasd',
          topicOpposite: 'asdasdasda',
        },
        {
          roomId: 2,
          userId: 'aasdasd',
          participants: 2,
          observers: 2,
          times: 20,
          title: 't2',
          roomPassword: '',
          topicAgree: 'asdasdasd',
          topicOpposite: 'asdasdasda',
        },
        {
          roomId: 3,
          userId: 'aasdasd',
          participants: 2,
          observers: 2,
          times: 20,
          title: 't3',
          roomPassword: '',
          topicAgree: 'asdasdasd',
          topicOpposite: 'asdasdasda',
        },
        {
          roomId: 4,
          userId: 'aasdasd',
          participants: 2,
          observers: 2,
          times: 20,
          title: 't4',
          roomPassword: '',
          topicAgree: 'asdasdasd',
          topicOpposite: 'asdasdasda',
        },
        {
          roomId: 5,
          userId: 'aasdasd',
          participants: 2,
          observers: 2,
          times: 20,
          title: 't5',
          roomPassword: '',
          topicAgree: 'asdasdasd',
          topicOpposite: 'asdasdasda',
        },
        {
          roomId: 6,
          userId: 'aasdasd',
          participants: 2,
          observers: 2,
          times: 20,
          title: 't6',
          roomPassword: '',
          topicAgree: 'asdasdasd',
          topicOpposite: 'asdasdasda',
        },
    ],
    })

    // watch(() => route.query, (newVal, oldVal) => {
    //   state.query = newVal
    // })

    // 방 상세보기 dialog 호출
    const clickRoom = function (roomId) {
      console.log(roomId)
      emit('openDetailDialog', roomId)
    }

    // 방 목록 받아오는 함수
    const getRoomList = function () {
      // pagination 미구현 상태
      store.dispatch('root/requestRoomList', state.query)
        .then((result) => {
          state.rooms = result.data.content
        })
        .catch((err) => {
          console.log('room list request failed')
          console.log(err)
        })
    }

    // 검색시 방 목록 업데이트
    watch (() => route.query, () => {
      console.log('user searched')
      getRoomList()
    })


    // 초기 데이터 로딩
    onMounted (() => {
      console.log('initial room list loading')
      getRoomList()
    })

    return { state, getRoomList, clickRoom }
  }
}
</script>
