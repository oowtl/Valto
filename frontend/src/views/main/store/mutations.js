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

//LoadingStatus 변화
export function startSpinner(state){
  state.LoadingStatus = true;
}

export function endSpinner(state){
  state.LoadingStatus = false;
}


