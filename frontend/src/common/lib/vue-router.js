import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/home/home'
import History from '@/views/history/history'
import Main from '@/views/main/main'
// import Check from '@/views/room/check'
import Room from '@/views/room/room'
import Layout from '@/views/layout'


// eslint-disable-next-line no-undef
const fullMenu = require('@/views/main/menu.json')
function makeRoutesFromMenu () {
  let mainChildren = Object.keys(fullMenu).map((key) => {
    if (key === 'home') {
      return { path: fullMenu[key].path, name: key, component: Home }
    } else if (key === 'history') {
      return { path: fullMenu[key].path, name: key, component: History }
    } else { // menu.json 에 들어있는 로그아웃 메뉴
      return null
    }
  })

  let routes = [
    {
      path: '/',
      name: 'layout',
      component: Main,
      // component: Layout,
      children: mainChildren
    },
    // {
    //   path: '/1',
    //   name: 'main',
    //   component: Main,
    //   children: mainChildren
    // },
    {
      path: '/room/:roomId',
      name: 'room',
      component: Room,
      // props: true,
    },
  ]

  // 로그아웃 파싱한 부분 제거
  // routes = routes.filter(item => item)
  // menu 자체에는 나오지 않는 페이지 라우터에 추가(방 상세보기)
  return routes
}

const routes = makeRoutesFromMenu()
//routes 부터 수정했음
const router = createRouter({
  history: createWebHistory(),
  routes
})



export default router
