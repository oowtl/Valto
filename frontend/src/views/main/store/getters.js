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

// 비공개 방 설정
// export function getIsChecked (state) {
//   return state.checked ? true : false
// }

export function getUserSide (state) {
  return state.userSide
}
