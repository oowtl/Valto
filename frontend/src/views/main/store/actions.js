// API
import $axios from 'axios'
import util from '../../../common/util.js'
import router from '@/common/lib/vue-router.js'

export function requestLogin ({ commit }, payload) {
  const url = '/auth/login'
  let body = payload //id, password JSON객체
  return $axios.post(url, body)
}

//페이지 진입
export function onPageEnter () {
  console.log('onPageEnter: actions.js')
  const url = '/users/me'
  const request = {
    method: 'get',
    url: url,
  }
  return util.commonAxios(request)
}

//회원가입
export function requestJoin({ commit }, payload) {
  const url = '/users'
  let body = payload
  return $axios.post(url, body);
}

//아이디 중복 체크
export function checkId({ commit }, payload) {
  const url = '/users/' +  payload + '/id'
  return $axios.get(url);
}

// 닉네임 중복 체크
export function checkNickname({ commit }, payload) {
  const url = '/users/' +  payload + '/nick'
  return $axios.get(url);
}

// Axios error handler
export function axiosErrorHandler ({ commit }, payload) {
  console.log('axiosErrorHandler, actions.js')
  const err = payload

  if (err.response) {
    // alert('요청이 이루어졌으며 서버가 2xx의 범위를 벗어나는 상태 코드로 응답했습니다.')
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
  // 여기서 commit시 자동으로 root/가 붙어있음
  commit('setUserId', '')
  router.push({
    name: 'home'
    })
  console.log('logout')
}

// 방 생성
export function requestCreateRoom({ commit }, payload) {
  console.log(commit)
  const url = '/room'
  const request = {
    method: 'post',
    url: url,
    data: payload
  }
  return util.commonAxios(request)
}

// 내 프로필 확인하기
export function requestMyProfile() {
  const url = '/users/myprofile'
  const request = {
    method: 'get',
    url: url,
  }
  return util.commonAxios(request)
}

// 내 프로필 수정요청
export function requestUpdateProfile(payload) {
  const url = '/users/' +  payload.userId
  const request = {
    method: 'patch',
    url: url,
    data: payload
  }
  return util.commonAxios(request)
}

// 방 목록 요청 (비로그인 상태에서도 가능)
export function requestRoomList({ commit }, payload) {
  const url = '/room'
  const body = { params: payload }
  return $axios.get(url, body)
}


// 방 상세 정보 요청
export function requestDetail({ state }, payload) {
  const url = '/room/' + payload
  const request = {
    method: 'get',
    url: url,
  }
  return util.commonAxios(state, request)
}

// 방 입장
export function requestEnterRoom({ state }, roomId, payload) {
  const url = '/users/' + roomId + '/admission'
  const request = {
    method: 'post',
    url: url,
    data: payload
  }
  return util.commonAxios(state, request)
}

// export function requestDetail({ commit }, payload){
//   console.log('payload는', payload)
//   const roomId = String(payload)
//   // const url = `/room/${ roomId }`
//   console.log('action 도착')
//   const url = '/room/' + roomId
//   $axios.get(url)
//     console.log('axios 보내기')
//     .then(function (result) {
//       console.log('result는', result.data)
//       commit('mutationDetail', result.data)
//     })
//     .catch(function (err) {
//       console.log('에러발생')
//       alert(err)
//     })
// }
