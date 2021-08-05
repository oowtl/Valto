<template>
  {{ $route.params.conferenceId + '번 방 상세 보기 페이지' }}
</template>
<style>
</style>
<script>
import { reactive, onMounted, onUnmounted, computed } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'

export default {
  name: 'conference-detail',

  setup () {
    const route = useRoute()
    const store = useStore()
    const state = reactive({
      conferenceId: '',
      loginFlag: computed(() => store.getters['root/getIsLoggedIn']),
      userId: computed(() => store.getters['root/getUserId'])
    })

    // 페이지 진입시 불리는 훅
    onMounted(() => {
      state.conferenceId = route.params.conferenceId
      store.commit('root/setMenuActiveMenuName', 'home')
    })

    // 페이지 이탈시 불리는 훅
    onUnmounted(() => {
      state.conferenceId = ''
    })

    return { state }
  }
}
</script>
