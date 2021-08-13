<template>
<div v-if="streamManager">
	<h1>test</h1>
	<ov-video :stream-manager="streamManager"/>
	<div><p>{{ clientData }}</p></div>
</div>
</template>

<script>
import OvVideo from './OvVideo';

export default {
	name: 'UserVideo',

	components: {
		OvVideo,
	},

	props: {
		streamManager: Object,
	},

	computed: {
		clientData () {
			const { clientData } = this.getConnectionData()
			return clientData;
		},
	},

  mounted: function () {
    console.log('%$$$$$$$$$$$$$$$$$$')
    console.log(this.streamManager)
  },

	methods: {
		getConnectionData () {
			console.log(this.streamManager.stream+ '###')
			const { connection } = this.streamManager.stream;
      console.log(connection.data)
			return connection.data;
		},
	},
};
</script>
