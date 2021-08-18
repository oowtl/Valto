<template>
  <ov-video :stream-manager="this.streamManager" class="video"/>
  <p class="userName">{{ state.clientData }}</p>
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
			return JSON.parse(connection.data.split('%')[2])
    }

    const state = reactive({
      clientData: computed(() => getConnectionData().serverData)
    })

    return { state, getConnectionData }
  }
}
</script>
