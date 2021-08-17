<template>
  <el-dialog custom-class="ranking-dialog" title="랭킹" v-model="state.dialogVisible" @close="handleClose">
    <ul class="ranking-list">
      <li v-for="rank in 10" class="ranking-list-item">
          <div>{{ rank }}등 : {{ state.form.rankingList[rank-1] }}</div>
      </li>
    </ul>
  </el-dialog>
</template>
<style>
.ranking-dialog {
  width: 400px !important;
  height: 600px;
}
.ranking-list .ranking-list-item {
  list-style:none;
  font-size: 30px;
  margin-left: 30%
}

</style>
<script>
import { reactive, computed, ref, watch } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
export default {
    name: 'ranking-dialog',

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },

  setup(props, { emit }) {
    const store = useStore()
    const router = useRouter()

    const state = reactive({
      form: {    
        rankingList: ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'],
        align: 'left'
      },
      dialogVisible: computed(() => props.open),
      formLabelWidth: '120px',
    })

    // watch(() => props.open, (newVal, oldVal) => {
    //   if (newVal === true) {
    //     store.dispatch('root/requestRanking')
    //       .then(function (result) {
    //         state.form.rankingList = result.data.rankingList
    //       })
    //       .catch(function (err) {
    //         console.log(err)
    //       })
    //     } else if (newVal === false) {
    //       console.log("ranking dialog closed");
    //     }
    //   }
    // );


    const handleClose = function () {
      emit('closeRankingDialog')
    }



    return { state, handleClose }
  }
}
</script>
