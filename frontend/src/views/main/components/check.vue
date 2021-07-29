<template>

  <button @click="test">check</button>
  <h1>this is check page</h1>
  <h1>this is check page</h1>
  <h1>this is check page</h1>
</template>
<script>
// import { reactive } from '@vue/reactivity'
import { reactive} from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
// import state from ''
export default{
  props: {
   roomId: {
     type: String,
     default: 'test'

   }
 },
 setup() {
   const route = useRoute()


   const state = reactive({

   })

   const test = function(){

     console.log('test' + route.params.roomId)
   }
  return { state, test }
 }

}


</script>
