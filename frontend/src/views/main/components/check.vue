<template>
  <el-container>
    <h1>asd</h1>
    <h1>asd</h1>
    <h1>asd</h1>
    <h1>asd</h1>
    <h1>asd</h1>
    <el-container class="zxc">
      <h1>zxc</h1>
      <h1>zxc</h1>
      <h1>zxc</h1>
      <h1>zxc</h1>
      <h1>zxc</h1>
    </el-container>
  </el-container>

</template>

<script>
import { ElLoading } from 'element-plus';

export default {
  name: 'check',

  setup() {
    var el = document.querySelector('el-container.zxc')
    let loadingInstance1 = ElLoading.service({ target: el })

    return { loadingInstance1 }
  }
}
</script>


