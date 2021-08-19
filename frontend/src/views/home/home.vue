<template>
  <div ref="homeContainer">
    <el-carousel :interval="2000" arrow="always" height="200px">
      <el-carousel-item v-for="carousel in state.carousels" :key="carousel.roomId">
        <h3 @click="clickRoom(carousel.roomId)" class="small">
          {{ carousel.topicAgree }} vs {{ carousel.topicOpposite }}
        </h3>
      </el-carousel-item>
    </el-carousel>
    <div style="display: inline-block; text-align: center; width: 80%;">
      <div class="sortPanel">
        <span>정렬: </span>
        <el-dropdown @command="sortChange" style="margin-right: 15px;">
          <el-button type="text" style="font-size: 16px;">
            {{ state.sortName }}
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item :command="'participantsAsc'">빈 자리 많은 순</el-dropdown-item>
              <el-dropdown-item :command="'participantsDesc'">빈 자리 적은 순</el-dropdown-item>
              <el-dropdown-item :command="'createdAtDesc'">최신 순</el-dropdown-item>
              <el-dropdown-item :command="'createdAtAsc'">오래된 순</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <span>방 갯수: </span>
        <el-dropdown @command="pageSizeChange" style="margin-right: 15px;">
          <el-button type="text" style="font-size: 16px;">
            {{ state.pageSize }}개
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item :command="10">10개</el-dropdown-item>
              <el-dropdown-item :command="20">20개</el-dropdown-item>
              <el-dropdown-item :command="40">40개</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <el-input
          placeholder="밸런스 토론 검색"
          v-model="state.searchValue"
          @keyup.enter="searchRoom"
          style="width: 40%; max-width: 350px;">
          <template #prepend>
            <el-select v-model="state.searchCategory" placeholder="제목" style="width: 85px;">
              <el-option label="제목" value="title"></el-option>
              <el-option label="주제" value="topic"></el-option>
            </el-select>
          </template>
        </el-input>
      </div>
    </div>

    <ul class="room-list">
      <li v-for="room in state.rooms" :key="room.roomId" @click="clickRoom(room.roomId)" class="room-list-item" :class="{ closed: room.start }">
        <room :room="room" />
      </li>
    </ul>

    <el-pagination
      :default-current-page="1"
      :current-page="state.currentPage"
      @current-change="handlePageChange"
      :page-size="state.pageSize"
      :pager-count="11"
      :hide-on-single-page="false"
      layout="prev, pager, next"
      :total="state.totalSize">
    </el-pagination>
  </div>

</template>
<style>
.el-carousel {
  margin: 0% auto 0%;
  width: 80%;
  z-index: 1;
}
.el-carousel__item h3 {
  color: #1b1b1b;
  font-size: 30px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}
.el-carousel__item:nth-child(2n) {
  background-color: #c0c0c0;
}
.el-carousel__item:nth-child(2n+1) {
  background-color: #c0c0c0;
}

.room-list {
  /* display: inline-block;
  text-align: center;
  width: 80%; */
  max-height: calc(100% - 35px);
  list-style: none;
  margin: 20px 0px 0px 0px;
  padding: 0px 0px 0px 0px;
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
.sortPanel {
  width: 100%;
  /* display: flex; */
  /* justify-content: space-between; */
  display: inline-block;
  text-align: right;
  z-index: 1;
}
.room-list .closed {
  cursor: not-allowed;
}

</style>
<script>
import Room from './components/room'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
import { ElLoading } from 'element-plus'
import { reactive, onMounted, computed, watch, ref } from 'vue'


export default {
  name: 'Home',

  components: {
    Room,
  },

  setup (props, { emit }) {
    const homeContainer = ref(null)
    const store = useStore()
    const route = useRoute()
    const state = reactive({
      searchCategory: 'title',
      searchValue: '',
      query: computed(() => route.query),
      rooms: [],
      carousels: [],
      currentPage: 1,
      pageSize: 10,
      totalSize: 1,
      sort: '',
      sortNameArray: {
        'participantsAsc': '빈 자리 많은 순',
        'participantsDesc': '빈 자리 적은 순',
        'createdAtDesc': '최신 순',
        'createdAtAsc': '오래된 순',
      },
      sortName: computed(function () {
        return Object.values(state.sortNameArray)[Object.keys(state.sortNameArray).indexOf(state.sort)]
      })
    })

    // watch(() => route.query, (newVal, oldVal) => {
    //   state.query = newVal
    // })

    // 방 상세보기 dialog 호출
    const clickRoom = function (roomId) {
      const room = state.rooms.find(room => room.roomId === roomId)
      if (room.start === false) {
        emit('openDetailDialog', roomId)
      } else {
        console.log('already started room')
      }
    }

    // 방 목록 받아오는 함수
    const getRoomList = function () {
      let loadingInstance = ElLoading.service({ target: homeContainer.value })
      setTimeout(() => {

        console.log(state.query)

        let query = {
          ...state.query,
          page: state.currentPage,
          size: state.pageSize,
          sorting: state.sort,
        }
        store.dispatch('root/requestRoomList', query)
          .then((result) => {
            state.rooms = result.data.content
            state.totalSize = result.data.roomCount
            carouselRoom()
          })
          .catch((err) => {
          })
          .finally(() => {
            loadingInstance.close()
          })
      }, 1000);
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

    // 페이지 버튼
    const handlePageChange = function (currentPage) {
      state.currentPage = currentPage
      onChangePageOption()
    }

    // 정렬 변경
    const sortChange = function (sort) {
      localStorage.setItem('sort', sort)
      state.sort = sort
      onChangePageOption()
    }

    // 페이지 사이즈 변경
    const pageSizeChange = function (pageSize) {
      localStorage.setItem('pageSize', pageSize)
      state.pageSize = pageSize
      onChangePageOption()
    }

    // url 바꿔주기 (push)
    const onChangePageOption = function () {
      let query = {
        ...state.query,
        page: state.currentPage,
        size: state.pageSize,
        sorting: state.sort,
      }
      store.dispatch('root/queryUpdate', query)
    }

    // 검색버튼: main-header에서 home.vue로 이동
    const searchRoom = function () {
      let query = {
        page: 1,
      }
      if (state.searchCategory === 'title') {
        query = { title: state.searchValue }
        } else if (state.searchCategory === 'topic') {
        query = { topic: state.searchValue }
      }
      store.dispatch('root/queryUpdate', query)
    }

    // 검색시 방 목록 업데이트
    watch (() => route.query, () => {
      if (Object.keys(route.query).length !== 0) {
        getRoomList()
      }
    })

    // 초기 데이터 로딩
    onMounted (() => {
      state.pageSize = localStorage.getItem('pageSize') ? localStorage.getItem('pageSize') : 10
      state.sort = localStorage.getItem('sort') ? localStorage.getItem('sort') : 'participantsAsc'
      getRoomList()
    })

    return {
      state,
      getRoomList,
      clickRoom,
      handlePageChange,
      homeContainer,
      pageSizeChange,
      sortChange,
      onChangePageOption,
      searchRoom,
    }
  }
}


</script>
