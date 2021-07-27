<template>
  <li>
    <el-card :body-style="{ padding: '0px' }">
      <div class="image-wrapper">
        <el-skeleton style="width: 100%">
          <template #template>
            <el-skeleton-item variant="image" style="width: 100%; height: 190px" />
          </template>
        </el-skeleton>
        </div>
      <div style="text-align: left; padding: 14px;">
        <span class="title">{{ room.roomId }}</span>
        <div class="bottom">
          <span>{{ room.title }}</span>
        </div>
      </div>
    </el-card>
  </li>
</template>
<style>
.el-card {
  margin: 0 8px;
  margin-bottom: 40px;
}
.el-card .image-wrapper {
  width: 100%;
  height: 190px;
}
.el-card .title {
  font-weight: bold;
}
.el-card .bottom {
  margin-top: 5px;
  display:-webkit-box;
  word-wrap:break-word;
  -webkit-box-orient:vertical;
  overflow:hidden;
  text-overflow:ellipsis;
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
  name: 'Home',

  props: {
    room: {
      type: Object
    }
  },

  setup () {}
}
</script>
