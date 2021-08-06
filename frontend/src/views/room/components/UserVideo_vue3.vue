<template>
<div v-if="state.streammanager">
	<h1>test</h1>
	<ov-video :stream-manager="state.streamManager"/>
	<div><p>{{ state.clientData }}</p></div>
</div>
</template>

<script>
import OvVideo from './OvVideo'
import { reactive, computed, onMounted } from 'vue'

export default {
	name: 'UserVideo',

	components: {
		OvVideo,
	},

  props: {
    streamManager: {
      type: Object,
    },
  },

  setup (props) {
    const getConnectionData = function () {
      const { connection } = props.streamManager.stream
      console.log('!UserVideo connection data : ' + connection.data) // << 이거는 출력됨?
      console.log('!UserVideo setup streamManager : ' + props.streamManager)
			return connection.data
    }

    const state = reactive({
      // 이부분 확인 >>> unserialized된 json === object니까 괜찮을듯
      clientData: computed(() => getConnectionData().clientData)
    })

    onMounted(() => {
      console.log('!UserVideo onMounted streamManager : ' + props.streamManager)
    })

    return { state, getConnectionData }
  }
}
</script>
