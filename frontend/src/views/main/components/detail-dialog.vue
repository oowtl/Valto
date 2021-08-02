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
      <el-form-item v-if="state.form.privateRoom" prop="roomPassword" label="방 비밀번호" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.roomPassword" autocomplete="off" show-password></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="clickRoom(state.form.roomId)" v-model="state.dialogVisible">입장</el-button>
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
        privateRoom: '',
        roomPassword: '',
        users: [],
        align: 'left',
        exstatus: ''
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
      state.form.privateRoom = '',
      state.form.roomPassword = '',
      emit('closeDetailDialog')
    }

    watch(() => props.open, (newVal, oldVal) => {
      if (newVal === true) {
        state.form = {
          roomId: props.roomId,
          participants: '6',
          observers: '5',
          times: '30',
          userId: '6',
          title: '아무나 들어오셈',
          topicAgree: '찬성 의견',
          topicOpposite: '반대 의견',
          privateRoom: true,
          users: [],
          exstatus: 200
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
        //     state.form.privateRoom = result.data.privateRoom
        //   })
        //   .catch(function (err) {
        //     console.log(err)
        //   })
      } else if (newVal === false) {
        console.log('detail dialog closed')
      }
    })

    const clickRoom = function (id) {
      console.log('클릭했지롱', state)
      if(state.form.exstatus === 200) {
        console.log('200 맞지롱')
        router.push({
          name: 'room',
          params: {
            roomId: id
          }
        })
      } else {
        console.log('200 아님', state.form.exstatus)
      }
    }
    // const clickRoom = function (id) {
    //   store.dispatch('root/requestEnterRoom', state.form.roomId, { 
    //     privateRoom: state.form.privateRoom,
    //     roomPassword: state.form.roomPassword,
    //   })
    //   .then(result => {
    //     if(result.status === 200){
    //       router.push({
    //         name: 'room',
    //         params: {
    //           roomId: id
    //         }
    //       })
    //       handleClose()
    //     }
    //   })
    //   .catch(function (err) {
    //     alert(err)
    //   })
    // }


    return { state, handleClose, detailForm, clickRoom }
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
