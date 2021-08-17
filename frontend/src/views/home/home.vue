<template>
  <el-carousel :interval="2000" arrow="always">
    <el-carousel-item v-for="carousel in state.carousels" :key="carousel.roomId">
      <h3 @click="clickRoom(carousel.roomId)" class="small">
        {{ carousel.topicAgree }} vs {{ carousel.topicOpposite }}
      </h3>
    </el-carousel-item>
  </el-carousel>

  <ul class="room-list">
    <li v-for="room in state.rooms" :key="room.roomId" @click="clickRoom(room.roomId)" class="room-list-item">
      <room :room="room"/>
    </li>
  </ul>

</template>
<style>
.el-carousel {
  margin: 5% 10%;
  width: 80%;

}
.el-carousel__item h3 {
  color: #475669;
  font-size: 20px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}
.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}
.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}

.room-list {
  padding-left: 0;
  max-height: calc(100% - 35px);
  list-style: none;
  margin-top: 20px;
}

@media (min-width: 701px) and (max-width: 1269px) {
  .room-list  {
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
    Room,
  },

  setup (props, { emit }) {
    const store = useStore()
    const route = useRoute()
    const state = reactive({
      query: computed(() => route.query),
      rooms: [],
      carousels: [],
    })

    // watch(() => route.query, (newVal, oldVal) => {
    //   state.query = newVal
    // })

    // 방 상세보기 dialog 호출
    const clickRoom = function (roomId) {
      emit('openDetailDialog', roomId)
    }

    // 방 목록 받아오는 함수
    const getRoomList = function () {
      store.dispatch('root/requestRoomList', state.query)
        .then((result) => {
          state.rooms = result.data.content
          carouselRoom()
        })
        .catch((err) => {
        })
    }

    const carouselRoom = function () {
      let randomIndexArray = []
      if (state.rooms.length > 6) {
        for (let i=0; i<6; i++) {   //check if there is any duplicate index
          let randomNum = Math.floor(Math.random() * state.rooms.length)
          if (randomIndexArray.indexOf(randomNum) === -1) {
            randomIndexArray.push(randomNum)
          } else {
            //if the randomNum is already in the array retry
            i--
          }
        }
        for (let i=0; i<6; i++) {
          state.carousels.push(state.rooms[randomIndexArray[i]])
        }
      } else {
        state.carousels = state.rooms
      }
    }

    // 검색시 방 목록 업데이트
    watch (() => route.query, () => {
      if (Object.keys(route.query).length !== 0) {
        // sort 키가 없으면 query를 수정해서 넘기기?: default 값은 participantsAsc
        getRoomList(route.query)
      }
    })

    // 초기 데이터 로딩
    onMounted (() => {
      getRoomList()
    })

    return {
      state,
      getRoomList,
      clickRoom,
    }
  }
}


</script>
