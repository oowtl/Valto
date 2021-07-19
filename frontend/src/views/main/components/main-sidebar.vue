<template>
  <el-row
    class="main-sidebar"
    :gutter="10"
    :style="{ 'width': width }">
    <div class="hide-on-small">
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
import { reactive, computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

export default {
  name: 'main-header',

  props: {
    width: {
      type: String,
      default: '240px'
    }
  },
  setup() {
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

    return { state, menuSelect }
  }
}
</script>
