<template>
	<video ref="root" autoplay/>
</template>

<script>
import { ref, onMounted } from 'vue'
export default {
	name: 'OvVideo',

	props: {
		streamManager:{
			type: Object,
		}
	},
	setup (props) {
	const root = ref(null)

	onMounted (() => {
    props.streamManager.addVideoElement(root.value);
	})
	return { root }
  }
};
</script>
