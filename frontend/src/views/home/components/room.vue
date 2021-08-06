<template>
  <li>
    <el-card :body-style="{ padding: '0px' }">
      <!-- <div class="image-wrapper">
        <el-skeleton style="width: 100%">
          <template #template>
            <el-skeleton-item variant="image" style="width: 100%; height: 190px" />
          </template>
        </el-skeleton>
        </div> -->
<<<<<<< HEAD
      <div style="text-align: left; padding: 14px; ">
=======
      <div class="cardBody">
>>>>>>> origin/week4
        <span class="title" style="display: flex; justify-content: space-between;">
            <span>{{ room.roomId }}</span>
            <span>{{ room.title }}</span>
        </span>
        <hr>
        <div class="bottom">
          <span>
            <span>{{ room.topicAgree }}</span>
            <span style="color: #6b6b6b;"> vs. </span>
            <span>{{ room.topicOpposite }}</span>
          </span>
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
.el-card .image-wrapper {
  width: 100%;
  height: 190px;
}
.el-card .title {
  /* font-weight: bold; */
}
.el-card .bottom {
  font-weight: bold;
  font-size: 20px;
  /* border-bottom: 1px solid black; */
  text-align: center;
  margin-top: 45px;
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
/* 테블릿, 모바일의 경우 두 줄 말줄임표시 */
@media (max-width: 1269px) {
  .el-card .bottom {
    -webkit-line-clamp: 2;
    height:42px;
  }
}
/* 데스크탑의 경우 세 줄 말줄임표시 */
@media (min-width: 1270px) {
  .el-card .bottom {
    -webkit-line-clamp: 3;
    height:60px;
  }
}

</style>
<script>
export default {
  name: 'Room',

  props: {
    room: {
      type: Object
    }
  },

  setup () {}
}
</script>
