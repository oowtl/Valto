<template>
  <el-container class="main-wrapper">
    <main-header
      :height="`70px`"
      @openLoginDialog="onOpenLoginDialog"
      @openJoinDialog="onOpenJoinDialog"
<<<<<<< HEAD
      @openCreateRoomDialog="onOpenCreateRoomDialog"
      />
<<<<<<< HEAD


=======
>>>>>>> ef2d194a3ebeed4bf6c0459a0cbc0a9e294e14cf
=======
      @openProfileDialog="onOpenProfileDialog"
      />
>>>>>>> 2e5b746cc00bf59bfde1a2337c4d392807fdd8d6
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
    @closeLoginDialog="onCloseLoginDialog"
  />
  <join-dialog
    :open="joinDialogOpen"
<<<<<<< HEAD
    @closeJoinDialog="onCloseJoinDialog"/>
  <createroom-dialog
    :open="createRoomDialogOpen"
    @closeCreateRoomDialog="onCloseCreateRoomDialog"/>
=======
    @closeJoinDialog="onCloseJoinDialog"
  />
  <profile-dialog
    :open="profileDialogOpen"
    @closeProfileDialog="onCloseProfileDialog"
  />
>>>>>>> 2e5b746cc00bf59bfde1a2337c4d392807fdd8d6
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
<<<<<<< HEAD
import CreateroomDialog from './components/createroom-dialog'
=======
import ProfileDialog from './components/profile-dialog'
>>>>>>> 2e5b746cc00bf59bfde1a2337c4d392807fdd8d6

export default {
  name: 'Main',
  components: {
    MainHeader,
    MainSidebar,
    MainFooter,
    LoginDialog,
    JoinDialog,
<<<<<<< HEAD
<<<<<<< HEAD
=======
    CreateroomDialog
>>>>>>> ef2d194a3ebeed4bf6c0459a0cbc0a9e294e14cf
=======
    ProfileDialog,
>>>>>>> 2e5b746cc00bf59bfde1a2337c4d392807fdd8d6
  },
  data () {
    return {
      loginDialogOpen: false,
      joinDialogOpen: false,
<<<<<<< HEAD
      localLoginFlag: false,
<<<<<<< HEAD
=======
      createRoomDialogOpen: false
>>>>>>> ef2d194a3ebeed4bf6c0459a0cbc0a9e294e14cf
=======
      profileDialogOpen: false,
      localLoginFlag: false,
>>>>>>> 2e5b746cc00bf59bfde1a2337c4d392807fdd8d6
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
    },
<<<<<<< HEAD
    onOpenCreateRoomDialog () {
      this.createRoomDialogOpen = true
    },
    onCloseCreateRoomDialog () {
      this.createRoomDialogOpen = false
    }
=======
    onOpenProfileDialog () {
      this.profileDialogOpen = true
    },
    onCloseProfileDialog () {
      this.profileDialogOpen = false
    },
>>>>>>> 2e5b746cc00bf59bfde1a2337c4d392807fdd8d6
  }
}
</script>
