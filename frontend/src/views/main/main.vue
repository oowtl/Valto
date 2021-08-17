<template>
  <el-container class="main-wrapper">
    <main-header
      :height="`70px`"
      @openRankingDialog="onOpenRankingDialog"
      @openLoginDialog="onOpenLoginDialog"
      @openJoinDialog="onOpenJoinDialog"
      @openCreateRoomDialog="onOpenCreateRoomDialog"
      @openProfileDialog="onOpenProfileDialog"
      />
    <el-container class="main-container">
      <el-main>
        <router-view @openDetailDialog="onOpenDetailDialog"></router-view>
      </el-main>
    </el-container>
  </el-container>
   <ranking-dialog
    :open="rankingDialogOpen"
    @closeRankingDialog="onCloseRankingDialog"/>
  <login-dialog
    :open="loginDialogOpen"
    @closeLoginDialog="onCloseLoginDialog"/>
  <join-dialog
    :open="joinDialogOpen"
    @closeJoinDialog="onCloseJoinDialog"/>
  <createroom-dialog
    :open="createRoomDialogOpen"
    @closeCreateRoomDialog="onCloseCreateRoomDialog"/>
  <profile-dialog
    :open="profileDialogOpen"
    :roomId="roomId"
    @closeProfileDialog="onCloseProfileDialog"
  />
  <detail-dialog
    :open="detailDialogOpen"
    :roomId="roomId"
    @closeDetailDialog="onCloseDetailDialog"
  />

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
import JoinDialog from './components/join-dialog'
import CreateroomDialog from './components/createroom-dialog'
import ProfileDialog from './components/profile-dialog'
import DetailDialog from './components/detail-dialog'
import RankingDialog from './components/ranking-dialog'



export default {
  name: 'Main',
  components: {
    MainHeader,
    LoginDialog,
    JoinDialog,
    CreateroomDialog,
    ProfileDialog,
    DetailDialog,
    RankingDialog
  },

  data () {

    return {
      loginDialogOpen: false,
      joinDialogOpen: false,
      localLoginFlag: false,
      profileDialogOpen: false,
      createRoomDialogOpen: false,
      detailDialogOpen: false,
      rankingDialogOpen: false,
      roomId: '',
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
    onOpenCreateRoomDialog () {
      this.createRoomDialogOpen = true
    },
    onCloseCreateRoomDialog () {
      this.createRoomDialogOpen = false
    },
    onOpenProfileDialog () {
      this.profileDialogOpen = true
    },
    onCloseProfileDialog () {
      this.profileDialogOpen = false
    },
    onOpenDetailDialog (roomId) {
      this.roomId = roomId
      this.detailDialogOpen = true
    },
    onCloseDetailDialog () {
      this.detailDialogOpen = false
    },
    onOpenRankingDialog () {
      console.log('랭킹다이아로그 열기')
      this.rankingDialogOpen = true
    },
    onCloseRankingDialog () {
      console.log('랭킹다이아로그 닫기')
      this.rankingDialogOpen = false
    },
  }
}




</script>
