<template>
  <el-container class="main-wrapper">
    <main-header
      :height="`70px`"
      @openLoginDialog="onOpenLoginDialog"
      @openJoinDialog="onOpenJoinDialog"
      />



    <el-container class="main-container">
      <el-aside class="hide-on-small" width="240px">
        <main-sidebar
          :width="`240px`"/>
      </el-aside>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
    <main-footer :height="`110px`"/>
  </el-container>
  <login-dialog
    :open="loginDialogOpen"
    @closeLoginDialog="onCloseLoginDialog"/>
  <join-dialog
    :open="joinDialogOpen"
    @closeJoinDialog="onCloseJoinDialog"/>
</template>
<style>
  @import "https://unpkg.com/element-plus/lib/theme-chalk/index.css";
  @import './main.css';
  @import '../../common/css/common.css';
  @import '../../common/css/element-plus.css';

</style>
<script>
import LoginDialog from './components/login-dialog'
import MainHeader from './components/main-header'
import MainSidebar from './components/main-sidebar'
import MainFooter from './components/main-footer'
import JoinDialog from './components/join-dialog'

export default {
  name: 'Main',
  components: {
    MainHeader,
    MainSidebar,
    MainFooter,
    LoginDialog,
    JoinDialog
  },
  data () {
    return {
      loginDialogOpen: false,
      joinDialogOpen: false,
      localLoginFlag: false
    }
  },
  methods: {
    onOpenLoginDialog () {
      this.loginDialogOpen = true
    },
    onCloseLoginDialog () {
      this.loginDialogOpen = false
    },
    onOpenJoinDialog () {
      this.joinDialogOpen = true
    },
    onCloseJoinDialog () {
      this.joinDialogOpen = false
    }
  }
}
</script>
