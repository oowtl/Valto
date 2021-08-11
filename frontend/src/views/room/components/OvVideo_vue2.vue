<template>
	<video autoplay/>
</template>

<script>
export default {
	name: 'OvVideo',

	props: {
		streamManager: Object,
	},

	mounted () {
    console.log(this.$el)
		this.streamManager.addVideoElement(this.$el);
	},
};
</script>
