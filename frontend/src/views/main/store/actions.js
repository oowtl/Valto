// API
import $axios from 'axios'
import util from '../../../common/util.js'
import router from '@/common/lib/vue-router.js'

export function requestLogin ({ state }, payload) {
  // console.log('requestLogin', state, payload)
  // console.log("payload" + payload.id)
  const url = '/auth/login'
  let body = payload //id, password JSON객체
  return $axios.post(url, body)
}

//페이지 진입
export function onPageEnter ({ state }) {
  console.log('onPageEnter: actions.js')
  const url = '/users/me'
  const request = {
    method: 'get',
    url: url,
  }
  return util.commonAxios(state, request)
}

//회원가입
export function requestJoin({state}, payload){
  const url = "/users"
  let body = payload //회원가입 폼 JSON객체
  return $axios.post(url, body);
}

//아이디 중복 체크
export function checkId({state}, payload){
  console.log(payload)
  const url = "/users/" +  payload
  // let body = payload
  return $axios.get(url);
}

// Axios error handler
export function axiosErrorHandler ({ commit }, payload) {
  console.log('axiosErrorHandler, actions.js')
  const err = payload

  if (err.response) {
    alert('요청이 이루어졌으며 서버가 2xx의 범위를 벗어나는 상태 코드로 응답했습니다.')
    console.log(err.response)
    if (err.response.data.status === 401) {
      console.log('here')
      if (err.response.data.error === 'SignatureVerificationException') {
        alert('세션이 유효하지 않습니다')
        setLogout({ commit })
      } else if (err.response.data.error === 'JWTDecodeException') {
        alert('세션이 만료되었습니다')
        setLogout({ commit })
      }
    } else if (err.response.data.status === 403) {
      if (err.response.data.error === 'Forbidden') {
        alert('접근 권한이 없습니다.')
        setLogout({ commit })
    }
    }
  }
  else if (err.request) {
      // 요청이 이루어 졌으나 응답을 받지 못했습니다.
      console.log(err.request);
  }
}

// 로그아웃
export function setLogout({ commit }) {
  localStorage.removeItem('jwt')
  commit('setUserId', '')
  router.push({
    name: 'home'
    })
  console.log('logout')
}

// 내 프로필
// state가 굳이 필요한가
// export function getMyProfile({ state }) {
//   const url = '/users/me'
//   const request = {
//     method: 'get',
//     url: url,
//   }
//   return util.commonAxios(state, request)
// }
