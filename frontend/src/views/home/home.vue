<template>
    <!-- <el-dropdown trigger="click">
      <span class="el-dropdown-link">
        Dropdown List<i class="el-icon-arrow-down el-icon--right"></i>
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>Action 1</el-dropdown-item>
        <el-dropdown-item>Action 2</el-dropdown-item>
        <el-dropdown-item>Action 3</el-dropdown-item>
        <el-dropdown-item disabled>Action 4</el-dropdown-item>
        <el-dropdown-item divided>Action 5</el-dropdown-item>
      </el-dropdown-menu>
  </el-dropdown> -->

  <!-- 정렬하는 방법 -->
  <button class="el-button el-button--primary" type="button">

    <i class="el-icon-sort"></i>
    <span>제목</span>
  </button>

  <ul class="infinite-list" v-infinite-scroll="load" style="overflow:auto">
    <li v-for="i in state.count" @click="clickRoom(i)" class="infinite-list-item" :key="i" >
    <!-- <li v-for="i in state.count" @click="clickConference(i)" class="infinite-list-item" :key="i" > -->
      <conference />
    </li>
  </ul>
</template>

<script>
import Conference from './components/conference'
import { reactive } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'Home',

  components: {
    Conference
  },

  setup (props, { emit }) {
    const router = useRouter()

    const state = reactive({
      count: 12
    })

    const load = function () {
      state.count += 4
    }

    const clickRoom = function (id) {
      emit('openDetailDialog', id)
    }

    return { state, load, clickRoom }
  }
}
</script>

<style>
.infinite-list {
  padding-left: 0;
  max-height: calc(100% - 35px);
}

@media (min-width: 701px) and (max-width: 1269px) {
  .infinite-list {
    min-width: 700px;
  }
}

@media (min-width: 1270px) {
  .infinite-list {
    min-width: 1021px;
  }
}

.infinite-list .infinite-list-item {
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

