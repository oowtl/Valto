r<template>
  <el-dialog custom-class="ranking-dialog" title="랭킹" v-model="state.dialogVisible" @close="handleClose">
    <ul class="ranking-list">
      <li v-for="rank in state.form.rankingList.length" class="ranking-list-item">
        <ranking-form>{{ rank }}등 </ranking-form>
        <ranking-id>{{ state.form.rankingList[rank-1].nickName }}</ranking-id>
        <ranking-point>{{ state.form.rankingList[rank-1].point }}점</ranking-point>
      </li>
    </ul>
  </el-dialog>
</template>

<style>
.ranking-dialog {
  width: 500px !important;
  height: 650px;
}
.ranking-list .ranking-list-item {
  list-style: none;
  font-size: 15px;
  margin: 5px 20px;
}
.ranking-list .ranking-list-item:nth-child( -n+3 ) {
  font-size: 20px;
  color: red;
} 
.ranking-list .ranking-list-item ranking-form{
  float: left;
}
.ranking-list .ranking-list-item ranking-id{
  margin-left: 30px;
}
.ranking-list .ranking-list-item ranking-point{
  float: right;
  margin-right: 20px;
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
        rankingList: [],
        align: 'left'
      },
      dialogVisible: computed(() => props.open),
      formLabelWidth: '120px',
    })

    watch(() => props.open, (newVal, oldVal) => {
      if (newVal === true) {
        store.dispatch('root/requestRanking')
          .then(function (result) {
            state.form.rankingList = result.data.rank
            console.log(state.form.rankingList)
          })
          .catch(function (err) {
            console.log(err)
          })
        } else if (newVal === false) {
          handleClose()
        }
      }
    );


    const handleClose = function () {
      emit('closeRankingDialog')
    }



    return { state, handleClose }
  }
}
</script>
