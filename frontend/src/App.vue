<template>
  <!-- main 으로 시작 -->
  <Main v-if="!test" />
  <Check v-if="test" />
</template>

<script>
import Main from './views/main/main.vue'
import Check from './views/main/components/check.vue'

export default {
  name: 'App',

  components: {
    Main,
    Check,
  },

  data: () => ({
    // test: true,
    test: false,
  }),
}
</script>
