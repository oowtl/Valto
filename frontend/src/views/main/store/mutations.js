export function setPlatform (state, isDesktop) {
  state.isDesktopPlatform = isDesktop
}

export function setMenuActive (state, index) {
	console.log('setMenuActive', state,index)
	const keys = Object.keys(state.menus)
	state.activeMenu = keys[index]
}

export function setMenuActiveMenuName (state, menuName) {
	state.activeMenu = menuName
}

export function setUserId (state, userId) {
  state.userId = userId
}

// export function mutationDetail (state, data) {
//   state.roomData = data
// }

// 검색시 방 리스트 업데이트
export function UPDATE_ROOMLIST(state, payload){
  state.roomList = payload
}
