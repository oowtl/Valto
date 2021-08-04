import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/home/home'
import ConferencesDetail from '@/views/conferences/conference-detail'
import History from '@/views/history/history'
import Main from '@/views/main/main'
import Check from '@/views/room/check'


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
      name: 'main',
      component: Main,
      children: mainChildren
    },
    {
      path: '/room/:roomId',
      name: 'room',
      component: Check,
      // props: true,
    },
  ]

  // 로그아웃 파싱한 부분 제거
  // routes = routes.filter(item => item)
  // menu 자체에는 나오지 않는 페이지 라우터에 추가(방 상세보기)
  routes.push({
    path: '/conferences/:conferenceId',
    name: 'conference-detail',
    component: ConferencesDetail
  })
  return routes
}

const routes = makeRoutesFromMenu()
//routes 부터 수정했음
const router = createRouter({
  history: createWebHistory(),
  routes
})



export default router
