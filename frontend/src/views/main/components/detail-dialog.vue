<template>
  <el-dialog custom-class="detail-dialog" title="상세보기" v-model="state.dialogVisible" @close="handleClose">
    <el-form :model="state.form" ref="detailForm" :label-position="state.form.align">
      <h1>방 번호: {{ state.form.roomId }}</h1>
      <h1>방 제목: {{ state.form.title }}</h1>
      <br>
      <h1>주제: {{ state.form.topicAgree }} VS {{ state.form.topicOpposite }}</h1>
      <br>
      <h1>토론 시간: {{ state.form.times }}</h1>
      <h1>찬성 인원: 아직 안함</h1>
      <h1>반대 인원: 아직 안함</h1>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="clickConference(state.form.roomId)" v-model="state.dialogVisible">입장</el-button>
      </span>
    </template>
  </el-dialog>

</template>
<script>
import { reactive, computed, ref, watch } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

export default {
  name: 'detail-dialog',

  props: {
    open: {
      type: Boolean,
      default: false
    },
    roomId: {
      type: String,
    }
  },

  setup(props, { emit }) {
    const router = useRouter()
    const store = useStore()
    const detailForm = ref(null)

    const state = reactive({
      form: {
        roomId: '',
        participants: '',
        observers: '',
        times: '',
        userId: '',
        title: '',
        topicAgree: '',
        topicOpposite: '',
        users: [],
        align: 'left'
      },

      dialogVisible: computed(() => props.open),
      formLabelWidth: '120px',
    })

    // 닫기
    const handleClose = function () {
      state.form.roomId = '',
      state.form.participants = '',
      state.form.observers = '',
      state.form.times = '',
      state.form.userId = '',
      state.form.title = '',
      state.form.topicAgree = '',
      state.form.topicOpposite = '',
      emit('closeDetailDialog')
    }

    // 모달 창이 열릴 때 내 프로필 받아오는 함수 호출
    watch(() => props.open, (newVal, oldVal) => {
      if (newVal === true) {
        state.form = {
          roomId: props.roomId,
          participants: '6',
          observers: '5',
          times: '30',
          userId: '',
          title: '아무나 들어오셈',
          topicAgree: '찬성 의견',
          topicOpposite: '반대 의견',
          users: [],
        }
        // store.dispatch('root/requestDetail', props.roomId)
        //   .then(function (result) {
        //     state.form.roomId = result.data.roomId
        //     state.form.participants = result.data.participants
        //     state.form.observers = result.data.observers
        //     state.form.times = result.data.times
        //     state.form.userId = result.data.userId
        //     state.form.title = result.data.title
        //     state.form.topicAgree = result.data.topicAgree
        //     state.form.topicOpposite = result.data.topicOpposite
        //   })
        //   .catch(function (err) {
        //     console.log(err)
        //   })
      } else if (newVal === false) {
        console.log('detail dialog closed')
      }
    })

    const clickConference = function (id) {
      router.push({
        name: 'conference-detail',
        params: {
          conferenceId: id
        }
      })
    }

    return { state, handleClose, detailForm, clickConference }
  }
}
</script>
<style>
.detail-dialog {
  width: 800px !important;
  height: 600px;
}
/* .detail-dialog .el-dialog__headerbtn {
  float: right;
} */
/* .detail-dialog .el-form-item__content {
  margin-left: 0 !important;
  float: right;
  width: 200px;
  display: inline-block;
} */
/* .detail-dialog .el-form-item {
  margin-bottom: 20px;
} */
/* .detail-dialog .el-form-item__error {
  font-size: 12px;
  color: red;
} */
/* .detail-dialog .el-input__suffix {
  display: none;
} */
.detail-dialog .el-dialog__footer {
  margin: 0 calc(50% - 80px);
  padding-top: 0;
  display: inline-block;
}
/* .detail-dialog .dialog-footer .el-button {
  width: 120px;
} */
</style>
