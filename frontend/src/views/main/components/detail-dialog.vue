<template>
  <div style="padding: 14px;">
    <el-dialog v-if="state.form" custom-class="detail-dialog" :title="state.form.title" v-model="state.dialogVisible" @close="handleClose">
      <el-form :label-position="state.align">
        <span class="topics">
          <span>{{ state.form.topicAgree }}</span>
          <span style="color: #6b6b6b;"> vs. </span>
          <span>{{ state.form.topicOpposite }}</span>
          <hr>
        </span>

        <el-form-item label="토론 시간: " :label-width="state.formLabelWidth">
          {{ state.form.times }}분
        </el-form-item>
        <el-form-item label="주제1(왼쪽) 인원수: " :label-width="state.formLabelWidth">
          {{ state.form.agreeUsers.length }}/{{ state.divide_participants }}
        </el-form-item>
        <el-form-item label="주제2(오른쪽) 인원수: " :label-width="state.formLabelWidth">
          {{ state.form.oppositeUsers.length }}/{{ state.divide_participants }}
        </el-form-item>
        <el-form-item label="관전자 인원수: " :label-width="state.formLabelWidth">
          {{ state.form.observerUsers.length }}/{{ state.form.observers }}
        </el-form-item>
        <el-form-item prop="userSide" label="userSide" :label-width="state.formLabelWidth">
          <el-select class="positionSelect" v-model="state.form.userSide" placeholder="포지션">
            <el-option
              v-for="position in userSide"
              :key="position.value"
              :label="position.label"
              :value="position.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="roomPassword" label="방 비밀번호: " :label-width="state.formLabelWidth">
          <div v-if="!state.form.privateRoom" autocomplete="off">없음</div>
          <el-input v-if="state.form.privateRoom" v-model="state.form.roomPassword" placeholder="방 비밀번호를 입력하시오" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="clickEnter(state.form.roomId)" v-model="state.dialogVisible">입장</el-button>
        </span>
      </template>
    </el-dialog>
  </div>

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

  data() {
    return {
      userSide: [{
          value: 'agree',
          label: '주제1(찬성)'
        }, {
          value: 'opposite',
          label: '주제2(반대)'
        }, {
          value: 'observer',
          label: '관전자'
        }],
    }
  },

  setup(props, { emit }) {
    const router = useRouter()
    const store = useStore()
    const detailForm = ref(null)
    const state = reactive({
      form: null,
      userSide: '',
      roomPassword: '',
      divide_participants: '',
      dialogVisible: computed(() => props.open),
      formLabelWidth: '250px',
      align: 'left',
      token: null,
      rules: {
        roomPassword: [
          { required: true, message: '방 비밀번호 입력하세요' }
        ],
      }
    })

    // 닫기
    const handleClose = function () {
      state.form = null
      emit('closeDetailDialog')
    }

    // 모달 창이 열릴 때 내 프로필 받아오는 함수 호출
    watch(() => props.open, (newVal, oldVal) => {
      if (newVal === true) {
        store.dispatch('root/requestDetail', props.roomId)
          .then(function (result) {
            console.log("result는", result)
            state.form = result.data
            state.divide_participants = result.data.participants/2
          })
          .catch(function (err) {
            console.log(err)
          })
        } else if (newVal === false) {
        console.log('detail dialog closed')
      }
    })

    const clickEnter = function (roomId) {
      store.dispatch('root/requestRoomToken', roomId )
        .then((result) => {
          // 임시로 로컬스토리지에 저장
          localStorage.setItem('st', result.data[0])
          console.log(`TOKEN: ${localStorage.getItem('st')})`)
          router.push({
            name: 'room',
            params: {
              roomId: roomId
            }
          })
        })
        .catch((err) => {
          console.log(err)
          handleClose()
        })
    }

    // const clickEnter = function (roomId) {
    //   store.dispatch('root/requestRoomToken', {
    //     roomId: roomId,
    //     privateRoom: state.form.privateRoom,
    //     roomPassword: state.roomPassword,
    //     userSide: state.userSide
    //   })
    //     .then((result) => {
    //       // 임시로 로컬스토리지에 저장
    //       localStorage.setItem('st', result.data[0])
    //       console.log(`TOKEN: ${localStorage.getItem('st')})`)
    //       router.push({
    //         name: 'room',
    //         params: {
    //           roomId: roomId
    //         }
    //       })
    //     })
    //     .catch((err) => {
    //       console.log(err)
    //       handleClose()
    //     })
    // }
    return { state, handleClose, detailForm, clickEnter }
  }
}
</script>
<style>
.detail-dialog {
  width: 600px !important;
  height: 800px;
}
.topics {
  text-align: center;
  font-weight: bold;
  font-size: 40px;
  color: black;
  display:block;
  margin-top: 20px;
  margin-bottom: 80px;
}


/* .detail-dialog .el-dialog__headerbtn {
  float: right;
} */
.detail-dialog .el-form-item__label {
  margin-left: 50px !important;
  font-size: 25px;
  font-weight: bold;
  display: inline-block;
}
.detail-dialog .el-form-item__content {
  margin-left: 0 !important;
  font-size: 25px;
  font-weight: bold;
  display: inline-block;
  text-align: center;
}
.detail-dialog .el-form-item__content .positionSelect {
  width: 130px;
  font-weight: bold;
}

/* .detail-dialog .el-form-item {
  margin-bottom: 20px;
  font-size: 20px;
} */
/* .detail-dialog .el-form-item__error {
  font-size: 12px;
  color: red;
} */
/* .detail-dialog .el-input__suffix {
  display: none;
} */
.detail-dialog .el-dialog__footer {
  margin: 20px calc(50% - 80px);
  padding-top: 0;
  display: inline-block;
}
.detail-dialog .dialog-footer .el-button {
  width: 120px;
  height: 100px;
  font-size: 30px;
}
</style>
p
