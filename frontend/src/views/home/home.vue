<template>
  <h1>{{ state.query }}</h1>
    <button class="el-button el-button--primary" type="button">
      <i class="el-icon-sort"></i>
      <span>개발중</span>
    </button>
  <div class="carousel">
    <el-carousel class="carousel-container" :interval="40000" type="card" height="300px">
      <el-carousel-item v-for="item in 5" :key="item">
        <h3 @click="clickRoom(item)" class="medium">{{ item }}</h3>
      </el-carousel-item>
    </el-carousel>
  </div>
  <ul class="room-list">
    <li v-for="room in state.rooms" :key="room.roomId" @click="clickRoom(room.roomId)" class="room-list-item">
      <room :room="room"/>
    </li>
  </ul>


</template>
<style>
.carousel {
  /* max-width: 100%; */
  margin-top: 30px;
  padding: 24px;
}

.carousel-container {
  /* max-width: calc(100% - 48px); */
  /* margin: 5rem; */
  max-width: 100%;
}

.carousel .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 300px;
}
.carousel .el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}
.carousel .el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
.room-list {
  padding-left: 0;
  max-height: calc(100% - 35px);
  list-style:none;
  margin-top: 20px;
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
      rooms: [],
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
      console.log('query updated')
      if (Object.keys(route.query).length !== 0) {
        // sort 키가 없으면 query를 수정해서 넘기기: default 값은 participantsAsc
        getRoomList()
      }
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
