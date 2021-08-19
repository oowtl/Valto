<template>
	<ov-video v-if="this.streamManager" :stream-manager="this.streamManager"/>
</template>

<script>
import OvVideo from './OvVideo'
import { reactive, computed } from 'vue'

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
      // console.log('!UserVideo connection data : ' + connection.data) // << 이거는 출력됨?
      // console.log('!UserVideo setup streamManager : ' + props.streamManager)
			return connection.data
    }

    const state = reactive({
      // 이부분 확인 >>> unserialized된 json === object니까 괜찮을듯
      clientData: computed(() => getConnectionData().clientData)
    })

    return { state, getConnectionData }
  }
}
</script>
