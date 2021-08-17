<template>
  <el-row
    class="main-header"
    :gutter="10"
    :style="{ 'height': height }">
    <div class="hide-on-small">
      <div class="left-wrapper">
        <div class="logo-wrapper" @click="clickLogo">
          <div class="ic ic-logo"/>
        </div>
        <ul class="side-wrapper">
          <li class="li-wrapper" @click="clickHistory">기록</li>
          <li class="li-wrapper" @click="clickRanking">랭킹</li>
        </ul>
      </div>
      <div class="tool-wrapper">
        <ul class="button-wrapper" v-if="!state.loginFlag">
          <li class="button-list" @click="clickJoin" icon="el-icon-circle-plus-outline">회원가입</li>
          <li class="button-list" type="primary" @click="clickLogin" icon="el-icon-key">로그인</li>
        </ul>
        <ul class="button-wrapper" v-if="state.loginFlag">
          <li class="button-list" @click="clickCreateRoom" icon="el-icon-circle-plus-outline">방생성</li>
          <li class="button-list" @click="clickProfile" icon="el-icon-user-solid">프로필</li>
          <li class="button-list" @click="clickLogout" icon="el-icon-switch-button">로그아웃</li>
        </ul>
      </div>

    </div>

    <!-- 모바일 화면 -->
    <div class="hide-on-big">
      <div class="menu-icon-wrapper" @click="changeCollapse"><i class="el-icon-menu"></i></div>
      <div class="logo-wrapper" @click="clickLogo"><div class="ic ic-logo"/></div>
      <div class="menu-icon-wrapper"><i class="el-icon-search"></i></div>
      <div class="mobile-sidebar-wrapper" v-if="!state.isCollapse">
        <div class="mobile-sidebar">
          <div class="mobile-sidebar-tool-wrapper" v-if="!state.loginFlag">
            <div class="logo-wrapper"><div class="ic ic-logo"/></div>
            <el-button type="primary" class="mobile-sidebar-btn login-btn" icon="el-icon-circle-plus-outline" @click="clickLogin">로그인</el-button>
            <el-button class="mobile-sidebar-btn register-btn" icon="el-icon-key" @click="clickJoin">회원가입</el-button>
          </div>
          <div class="mobile-sidebar-tool-wrapper" v-if="state.loginFlag">
            <div class="logo-wrapper"><div class="ic ic-logo"/></div>
            <el-button @click="clickCreateRoom" icon="el-icon-circle-plus-outline">방생성 </el-button>
            <el-button @click="clickProfile" icon="el-icon-user-solid">프로필</el-button>
            <el-button class="login-btn" icon="el-icon-switch-button" @click="clickLogout">로그아웃</el-button>
          </div>
          <el-menu
            :default-active="String(state.activeIndex)"
            active-text-color="#ffd04b"
            class="el-menu-vertical-demo"
            @select="menuSelect">
            <el-menu-item v-for="(item, index) in state.menuItems" :key="index" :index="index.toString()">
              <i v-if="item.icon" :class="['ic', item.icon]"/>
              <span>{{ item.title }}</span>
            </el-menu-item>
          </el-menu>
        </div>
        <div class="mobile-sidebar-backdrop" @click="changeCollapse"></div>
      </div>
    </div>
  </el-row>
</template>
<script>
import { reactive, computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

export default {
  name: 'main-header',

  props: {
    height: {
      type: String,
      default: '70px'
    },
  },

  setup(props, { emit }) {
    const store = useStore()
    const router = useRouter()
    const state = reactive({
      isCollapse: true,
      menuItems: computed(() => {
        const MenuItems = store.getters['root/getMenus']
        let keys = Object.keys(MenuItems)
        let menuArray = []
        for (let i = 0; i < keys.length; ++i) {
          let menuObject = {}
          menuObject.icon = MenuItems[keys[i]].icon
          menuObject.title = MenuItems[keys[i]].name
          menuArray.push(menuObject)
        }
        return menuArray
      }),
      activeIndex: computed(() => store.getters['root/getActiveMenuIndex']),
      loginFlag: computed(() => store.getters['root/getIsLoggedIn']),
      userId: computed(() => store.getters['root/getUserId'])
    })

    const menuSelect = function (param) {
      store.commit('root/setMenuActive', param)
      const MenuItems = store.getters['root/getMenus']
      let keys = Object.keys(MenuItems)
      router.push({
        name: keys[param]
      })
    }

    const clickLogo = () => {
      store.commit('root/setMenuActive', 0)
      const MenuItems = store.getters['root/getMenus']
      let keys = Object.keys(MenuItems)
      router.push({
        name: keys[0]
      })
    }


    const clickHistory = () => {
      store.commit('root/setMenuActive', 1)
      const MenuItems = store.getters['root/getMenus']
      let keys = Object.keys(MenuItems)
      router.push({
        name: keys[1]
      })
    }

    const clickRanking = () => {
      console.log('click랭킹함')
      emit('openRankingDialog')
    }
    // const clickLogo = () => {
    //   store.commit('root/setMenuActive', 0)
    //   const MenuItems = store.getters['root/getMenus']
    //   let keys = Object.keys(MenuItems)
    //   router.push({
    //     name: keys[0]
    //   })
    // }

    // 로그인 클릭시
    const clickLogin = () => {
      emit('openLoginDialog')
    }

    // 로그아웃 클릭시
    const clickLogout = () => {
      store.dispatch('root/setLogout')
    }

    // 회원가입 클릭시
    const clickJoin = () => {
      emit('openJoinDialog')
    }
    //방 생성 클릭시
    const clickCreateRoom = () => {
      emit('openCreateRoomDialog')
    }

    // 내 프로필 클릭시
    const clickProfile = () => {
      emit('openProfileDialog')
    }

    const changeCollapse = () => {
      state.isCollapse = !state.isCollapse
    }

    return {
      state,
      menuSelect,
      clickLogo,
      clickLogin,
      changeCollapse,
      clickJoin,
      clickProfile ,
      clickLogout ,
      clickCreateRoom,
      clickHistory,
    }
  }
}
</script>
<style>
  .main-header {
    padding: 10px 20px;
    border-bottom: 1px solid #cacaca;
  }
  /*Mobile, Tablet*/
  .menu-icon-wrapper {
    display: inline-block;
    vertical-align: top;
    position: relative;
    top: 14px;
  }

  .main-header .hide-on-big .logo-wrapper {
    display: inline-block;
    margin: 0 calc(50% - 51px)
  }
  .main-header .hide-on-big .logo-wrapper .ic.ic-logo {
    width: 70px;
    height: 50px;
    background-size: contain;
    background-repeat: no-repeat;
    background-image: url('../../../assets/images/ssafy-logo.png');
  }
  .mobile-sidebar-wrapper {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
  }
  .mobile-sidebar-wrapper .mobile-sidebar {
    width: 240px; height: calc(100vh - 1px);
    display: inline-block;
    background-color: white;
    padding: 0 10px;
    vertical-align: top;
  }
  .mobile-sidebar-wrapper .mobile-sidebar .mobile-sidebar-tool-wrapper {
    padding-bottom: 20px;
  }
  .mobile-sidebar-wrapper .mobile-sidebar .mobile-sidebar-btn {
    display: block;
    margin: 0 auto;
    margin-top: 25px;
    height: 30px;
    width: 100%;
  }
  .mobile-sidebar-wrapper .mobile-sidebar .mobile-sidebar-btn.login-btn {
    color: white;
  }
  .mobile-sidebar-wrapper .mobile-sidebar .mobile-sidebar-btn.register-btn {
    color: white;
  }
  .mobile-sidebar-wrapper .mobile-sidebar .logo-wrapper {
    display: block
  }
  .mobile-sidebar-wrapper .mobile-sidebar .logo-wrapper .ic.ic-logo {
    width: 70px;
    height: 50px;
    margin: 0 auto;
    margin-top: 30px;
    background-size: contain;
    background-repeat: no-repeat;
    background-image: url('../../../assets/images/ssafy-logo.png');
  }
  .mobile-sidebar-wrapper .mobile-sidebar-backdrop {
    width: calc(100% - 260px); height: calc(100vh - 1px);
    background-color: black;
    display: inline-block;
    opacity: 0.3;
  }
  .mobile-sidebar-wrapper .el-menu{
    margin-top: 0;
    padding-left: 0;
    height: calc(100% - 235px);
  }
  .mobile-sidebar-wrapper .el-menu .el-menu-item {
    cursor: pointer;
  }
  .mobile-sidebar-wrapper .el-menu .el-menu-item .ic {
    margin-right: 5px;
  }

  /*Desktop - Need to add Class if Need*/
  .main-header .hide-on-small {
    margin: auto 10%;
  }
  .main-header .hide-on-small .left-wrapper {
    width: 30%;
    height: 50px;
    float: left;
  }
  .main-header .hide-on-small .left-wrapper .logo-wrapper {
    display: inline-block;
    width: 25%;
    float: left;
    cursor: pointer;
  }
  .main-header .hide-on-small .left-wrapper .logo-wrapper .ic.ic-logo {
    width: 100px;
    height: 50px;
    margin-top: 14px;
    background-size: contain;
    background-repeat: no-repeat;
    background-image: url('../../../assets/images/ssafy-logo.png');
  }
  .main-header .hide-on-small .left-wrapper .side-wrapper {
    display: inline-block;
    width: 50%;
    float: right;
    margin-right: 10%;
    padding: 0;
  }
  .main-header .hide-on-small .left-wrapper .side-wrapper .li-wrapper {
    display: inline-block;
    /* width: 45%; */
    height: 25px;
    cursor: pointer;
    margin-right: 44px;
  }
  .main-header .hide-on-small .left-wrapper .side-wrapper .li-wrapper:hover {
    border-bottom: 2px solid black;
  }
  .main-header .hide-on-small .tool-wrapper {
    width: 70%;
    height: 50px;
    float: right;
  }
  .main-header .hide-on-small .tool-wrapper .button-wrapper {
    width: 44%;
    float: right;
    display: inline-block;
    text-align: right;
    padding: 0;
  }
  .main-header .hide-on-small .tool-wrapper .button-wrapper .button-list {
    /* width: 20%; */
    height: 25px;
    cursor: pointer;
    margin-left: 50px;
    display: inline-block;
    text-align: right;
    /* border-radius: 3px; */
    }
  .main-header .hide-on-small .tool-wrapper .button-wrapper .button-list:hover {
    /* background-color: rgba(0, 0, 0, 0.1); */
    border-bottom: 2px solid black;
  }
</style>
