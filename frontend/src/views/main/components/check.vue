<template>
  <h1>Room Id: {{ state.roomId }}</h1>
  <h2>this is test page</h2>
  <h2>this is test page</h2>
  <h2>this is test page</h2>
</template>
<script>
// import { reactive } from '@vue/reactivity'
import { reactive} from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
// import state from ''
export default{
  // props: {
  //   roomId: {
  //     type: String,
  //     default: 'test'
  //   }
  // },
  setup() {
    const route = useRoute()
    const state = reactive({
      roomId: route.params.roomId
    })
    const test = function(){
      console.log(state.roomId)
    }
  return { state, test }
  }
}
</script>
