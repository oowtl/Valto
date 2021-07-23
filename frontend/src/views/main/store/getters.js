// 플랫폼 관련 정보 가져오기
export function getIsDesktopPlatform (state) {
  return state.isDesktopPlatform
}
// 메뉴 객체 가져오기
export function getMenus (state) {
	return state.menus
}
// Active된 메뉴 인덱스 가져오기
export function getActiveMenuIndex (state) {
	const keys = Object.keys(state.menus)
	return keys.findIndex(item => item === state.activeMenu)
}

// 로그인 여부
export function getIsLoggedIn (state) {
  return state.userId ? true : false
}

// 유저 아이디
export function getUserId (state) {
  return state.userId
}

<<<<<<< HEAD
// 방 검색시 업데이트
export function roomList(state){
  return state.roomList
}
=======
// 비공개 방 설정
// export function getIsChecked (state) {
//   return state.checked ? true : false
// }
>>>>>>> ef2d194a3ebeed4bf6c0459a0cbc0a9e294e14cf
