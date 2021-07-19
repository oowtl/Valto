

// 공용 함수
import $axios from 'axios'
import { useStore } from 'vuex'

export default {
  // 공용 axios
  commonAxios ({ state }, payload) {
    let token = localStorage.getItem('jwt')
    const config = {
      Authorization: `Bearer ${token}`
    }
    const request = {
      headers: config,
      ...payload
    }
    console.log(request)
    return $axios(request)
  },
}
