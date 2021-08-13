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
        <div class="side-wrapper">
          <el-button @click="clickHistory">기록</el-button>
        </div>
      </div>
      <div class="tool-wrapper">
        <!-- <i class="el-icon-search" @click="onClickSearchIcon"></i> -->
        <div class="search-field" :style="{ visibility: state.searchVisibility }">
          <el-input
            placeholder="밸런스 토론 검색"
            v-model="state.searchValue"
            @keyup.enter="searchRoom"
            @blur="onBlurSearch"
            ref="mysearch">
            <!--  나중에 메소드 이름은 다시 정할것 -->
          </el-input>
        </div>
        <div class="button-wrapper" v-if="!state.loginFlag">
          <el-button @click="clickJoin" icon="el-icon-circle-plus-outline">회원가입</el-button>
          <el-button type="primary" @click="clickLogin" icon="el-icon-key">로그인</el-button>
        </div>
        <div class="button-wrapper" v-if="state.loginFlag">
          <el-button @click="clickCreateRoom" icon="el-icon-circle-plus-outline">방생성</el-button>
          <el-button @click="clickProfile" icon="el-icon-user-solid">프로필</el-button>
          <el-button @click="clickLogout" icon="el-icon-switch-button">로그아웃</el-button>
        </div>
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
import { reactive, computed, ref } from 'vue'
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
      searchVisibility: 'hidden',
      searchValue: null,
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

    const mysearch = ref(null)


    const onClickSearchIcon = function () {
      state.searchVisibility = 'visible'
      mysearch.value.focus()
    }

    const onBlurSearch = function () {
      state.searchVisibility = 'hidden'
    }

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
      console.log('clickCreateRoom')
      emit('openCreateRoomDialog')
    }

    // 내 프로필 클릭시
    const clickProfile = () => {
      emit('openProfileDialog')
    }

    const changeCollapse = () => {
      state.isCollapse = !state.isCollapse
    }

    const searchRoom = function () {
      router.push({
        name: 'home',
        query: {
          q: state.searchValue,
          sort: 'default'
        }
      })
    }

    return { state, menuSelect, clickLogo, clickLogin, changeCollapse, clickJoin, clickProfile , clickLogout , clickCreateRoom, searchRoom, clickHistory, mysearch, onClickSearchIcon, onBlurSearch }
  }
}
</script>
<style>
  .main-header {
    padding: 10px 20px;
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
    width: 15%;
    float: left;
  }
  .main-header .hide-on-small .left-wrapper .logo-wrapper {
    width: 50%;
    float: left;
    cursor: pointer;
  }
  .main-header .hide-on-small .left-wrapper .logo-wrapper .ic.ic-logo {
    width: 50px;
    height: 50px;
    background-size: contain;
    background-repeat: no-repeat;
    background-image: url('../../../assets/images/ssafy-logo.png');
  }
  .main-header .hide-on-small .left-wrapper .side-wrapper {
    width: 50%;
    float: right;
  }
  .main-header .hide-on-small .left-wrapper .side-wrapper .el-button {
    width: 100%;
    height: 50px;
    cursor: pointer;
    margin-right: 1%;
  }
  .main-header .hide-on-small .tool-wrapper {
    width: 70%;
    float: right;
  }
  .main-header .hide-on-small .tool-wrapper .button-wrapper {
    width: 55%;
    float: right;
  }
  .main-header .hide-on-small .tool-wrapper .button-wrapper .el-button {
    width: 30%;
    height: 50px;
    cursor: pointer;
    margin: 0 1%;
  }
  .main-header .hide-on-small .tool-wrapper .search-field {
    width: 45%;
    height: 50px;
    max-width: 400px;
    display: inline-block;
    background-color: white;
  }
  .main-header .hide-on-small .tool-wrapper .search-field .el-input {
    width: 100%;
    height: 100%;
  }
  .main-header .hide-on-small .tool-wrapper .search-field .el-input .el-input__inner {
    width: 88%;
    height: 50px;
    margin: 0 1%;
  }
  .main-header .hide-on-small .tool-wrapper .search-field .el-input .el-input__prefix {
    top: 5px;
  }

</style>

<!--
<template>
  <el-row
    class="main-sidebar"
    :gutter="10"
    :style="{ 'width': width }">
    <div class="hide-on-small">
      <loading
          :show="show"
          :label="label">
      </loading>
      <el-menu
        :default-active="String(state.activeIndex)"
        active-text-color="#ffd04b"
        class="el-menu-vertical-demo"
        @select="menuSelect">
        <el-menu-item v-for="(item, index) in state.menuItems" :key="index" :index="index.toString()">
          <i v-if="item.icon" :class="['ic', item.icon]"/>
          <el-button @click.prevent="doAjax">{{ item.title }}</el-button>
        </el-menu-item>
      </el-menu>
    </div>
  </el-row>
</template>
<style>
.main-sidebar .el-menu {
  margin-top: 0;
  padding-left: 0;
}
.main-sidebar .hide-on-small {
  height: 100%;
}
.main-sidebar .hide-on-small .el-menu {
  height: 100%;
}
.main-sidebar .el-menu .el-menu-item {
  cursor: pointer;
  border-right: none;
}
.main-sidebar .el-menu .el-menu-item .ic {
  margin-right: 5px;
}
</style>
<script>
import { reactive, computed, ref } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
// Import component
import Loading from 'vue-full-loading';
// Import stylesheet
import 'vue-loading-overlay/dist/vue-loading.css';

export default {
  name: 'main-header',

  props: {
    width: {
      type: String,
      default: '240px'
    }
  },
  setup() {

    // const isLoading = ref(false);
    // const fullPage = ref(true);

    const show = ref(false);

    const doAjax = () => {
      show.value = true;

      setTimeout(() => {
        show.value = false
      }, 1000)
      // // console.log('fullPage'+fullPage.value)
      // isLoading.value = true;
      // // fullPage.value = true;

      // setTimeout(() => {
      //   isLoading.value = false
      // }, 2000)
      // show.value = true;
    }

    // const onCancel= ()=> {
    //     console.log('User cancelled the loader.');
    //     //because the props is single flow direction, you need to set isLoading status normally.
    //     isLoading.value = false;
    // }

    const store = useStore()
    const router = useRouter()

    const state = reactive({
      searchValue: null,
      loginFlag: computed(() => store.getters['root/getIsLoggedIn']),
      menuItems: computed(() => {
        const MenuItems = store.getters['root/getMenus']
        // Object.keys() 메소드는 주어진 객체의 속성 이름들을
        // 일반적인 반복문과 동일한 순서로 순회되는 열거할 수 있는 배열로 반환합니다.
        // >>> key값만 배열화하여 반환: home, history
        // let keys = Object.keys(MenuItems)
        let menuArray = []
        for (let menu in MenuItems) {
          if (MenuItems[`${menu}`].needLogin === true && state.loginFlag === false) {
            // 추가하지 않음
          } else {
            let menuObject = {}
            menuObject.icon = MenuItems[`${menu}`].icon
            menuObject.title =  MenuItems[`${menu}`].name
            menuArray.push(menuObject)
          }
        }
        return menuArray
      }),
      activeIndex: computed(() => store.getters['root/getActiveMenuIndex'])
    })

    if (state.activeIndex === -1) {
      state.activeIndex = 0
      store.commit('root/setMenuActive', 0)
    }

    const menuSelect = function (param) {
      store.commit('root/setMenuActive', param)
      const MenuItems = store.getters['root/getMenus']
      let keys = Object.keys(MenuItems)
      router.push({
        name: keys[param]
      })
    }

    return { state, menuSelect , doAjax, show}
  },
  components:{
    Loading
  }
}
</script>
-->
