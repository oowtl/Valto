<template>
  <el-dialog custom-class="createroom-dialog" title="방 생성하기" v-model="state.dialogVisible" @close="handleClose">
    <el-form :model="state.form" status-icon :rules="state.rules" ref="createRoomForm" :label-position="state.form.align">
      <!-- 제목 설정하기 -->
      <el-form-item prop="title" label="제목" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.title" autocomplete="off"></el-input>
      </el-form-item>
      <!-- 주제 설정하기 -->
      <el-form-item prop="topicAgree" label="주제1(찬성)" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.topicAgree" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="topicOpposite" label="주제2(반대)" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.topicOpposite" autocomplete="off"></el-input>
      </el-form-item>
      <!-- 참가자 인원수 설정하기 -->
      <el-form-item prop="participants" label="참가인원수" :label-width="state.formLabelWidth">
        <el-select v-model="state.form.participants" placeholder="참가자 인원수">
          <el-option
            v-for="number in part_numbers"
            :key="number.value"
            :label="number.label"
            :value="number.value">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- 포지션 정하기 -->
      <el-form-item prop="userSide" label="userSide" :label-width="state.formLabelWidth">
        <el-select v-model="state.form.userSide" placeholder="포지션">
          <el-option
            v-for="position in userSide"
            :key="position.value"
            :label="position.label"
            :value="position.value">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- 룰(시간 설정하기) -->
      <el-form-item prop="times" label="토론시간" :label-width="state.formLabelWidth">
        <el-select v-model="state.form.times" placeholder="토론시간">
          <el-option
            v-for="time in times"
            :key="time.value"
            :label="time.label"
            :value="time.value">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- 비공개 여부 -->
      <el-checkbox v-model="state.form.privateRoom" @click="buttonCheck">비공개</el-checkbox>
      <el-input v-if="state.form.privateRoom" v-model="state.form.roomPassword" autocomplete="off"></el-input>
    </el-form>
    <!-- 생성 버튼 -->
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="clickCreateRoom" :disabled="state.isInvalid">생성</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<style>
.createroom-dialog {
  width: 400px !important;
  height: 650px;
}
.createroom-dialog .el-dialog__headerbtn {
  float: right;
}
.createroom-dialog .el-form-item__content {
  margin-left: 0 !important;
  float: right;
  width: 200px;
  display: inline-block;
}
.createroom-dialog .el-form-item {
  margin-bottom: 20px;
}
.createroom-dialog .el-form-item__error {
  font-size: 12px;
  color: red;
}
.createroom-dialog .el-input__suffix {
  display: none;
}
.createroom-dialog .el-dialog__footer {
  margin: 0 calc(50% - 80px);
  padding-top: 0;
  display: inline-block;
}
.createroom-dialog .dialog-footer .el-button {
  width: 120px;
}
</style>
<script>
import { reactive, computed, ref } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
export default {
  data() {
    return {
        part_numbers: [
          { value: 2, label: '2' },
          { value: 4, label: '4' },
          { value: 6, label: '6' },
          { value: 8, label: '8' },
          { value: 10,label: '10'},
        ],
        userSide: [
          { value: 'agree', label: '주제1(찬성)' },
          { value: 'opposite', label: '주제2(반대)' },
        ],
        times: [
          { value: 20, label: '20' },
          { value: 30, label: '30' },
          { value: 40, label: '40' },
          { value: 50, label: '50' },
          { value: 60, label: '60' },
        ],
        value: '',
    }
  },
  name: 'createroom-dialog',

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },

  setup(props, { emit }) {
    const store = useStore()
    const createRoomForm = ref(null)
    const router = useRouter()

    const flag = ref({
      title: false,
      topicAgree: false,
      topicOpposite: false
    })

    // 아이디, 닉네임 입력 대기용 dummy function
    const dummyValidation = function (rule, value, callback) {
    }

    const checkTitle = function (rule, value, callback) {
      if(!value) {
        flag.value.title = false
        return callback(new Error('제목은 필수 입력 항목입니다.'))
      } else if (value.length < 2) {
        flag.value.title = false
        return callback(new Error('최소 2글자 이상 입력해야 합니다.'))
      } else if (value.length > 15) {
        flag.value.title = false
        return callback(new Error(`최대 15글자까지 입력 가능합니다. 현재 ${value.length} 글자.` ))
      } else {
        flag.value.title = true
        return callback()
      }
    }

    const checkTopicAgree = function (rule, value, callback) {
      if (!value) {
        flag.value.topicAgree = false
        return callback(new Error('필수 입력 항목 입니다.'))
      }else if (value.length > 15) {
        flag.value.topicAgree = false
        return callback(new Error(`최대 15글자까지 입력 가능합니다. 현재 ${value.length} 글자.`))
      }
      else {
        flag.value.topicAgree = true
        return callback()
      }
    }

    const checkTopicOpposite = function (rule, value, callback) {
      if (!value) {
        flag.value.topicAgree = false
        return callback(new Error('필수 입력 항목 입니다.'))
      }else if (value.length > 15) {
        flag.value.topicAgree = false
        return callback(new Error(`최대 15글자까지 입력 가능합니다. 현재 ${value.length} 글자.`))
      }
      else {
        flag.value.topicOpposite = true
        return callback()
      }
    }


    const state = reactive({
      userId: computed(() => {
        return store.getters['root/getUserId']
      }),
      isInvalid: computed(() => {
        return Object.values(flag.value).some(bool => bool === false)
      }),
      form: {
        // userId: this.userId,
        title: '',
        topicAgree: '',
        topicOpposite: '',
        participants: '',
        observers: '',
        userSide: '',
        times: '',
        privateRoom: false,
        roomPassword: '',
        align: 'left'
      },
      rules: {
        title: [
          // { validator: dummyValidation, trigger: 'change' },
          { validator : checkTitle, trigger: 'blur'},
          { required: true },
        ],
        topicAgree: [
          // { validator: dummyValidation, trigger: 'change' },
          { validator: checkTopicAgree, trigger: 'blur' },
          { required: true },
        ],
        topicOpposite: [
          // { validator: dummyValidation, trigger: 'change' },
          { validator: checkTopicOpposite, trigger: 'blur' },
          { required: true },
        ],
        participants: [
          { required: true, message: '참가자 인원수를 선택해 주세요.' }
        ],
        observers: [
          { required: true, message: '관전자 인원수를 선택해 주세요.' }
        ],
        userSide: [
          { required: true, message: '포지션을 선택해 주세요.' }
        ],
        times: [
          { required: true, message: '토론시간을 선택해 주세요.' }
        ],
        privateRoom: [
          { required: true, message: '비공개 여부를 체크해 주세요' }
        ],
        roomPassword: [
          { required: true, message: '방 비밀번호를 입력해 주세요' }
        ],
      },
      dialogVisible: computed(() => props.open),
      formLabelWidth: '120px',
    })

    const clickCreateRoom = function () {
      if (!state.isInvalid) {
        store.dispatch('root/requestCreateRoom', {
          userId: state.userId,              // string
          title: state.form.title,                // string
          topicAgree: state.form.topicAgree,      // string
          topicOpposite: state.form.topicOpposite,// string
          participants: state.form.participants,  // integer
          observers: state.form.observers,        // integer
          userSide: state.form.userSide,          // string
          times: state.form.times,                // integer
          privateRoom: state.form.privateRoom,    // boolean
          roomPassword: state.form.roomPassword,  // string
        })
          .then(function (result) {
            emit('closeCreateRoomDialog')
            router.push({
              name: 'room',
              params: { roomId: result.data.roomId }
            })
          })
          .catch(function (err) {
            alert(err)
          })
      } else {
        alert('Validate error!')
      }
    }

    const handleClose = function () {
      emit('closeCreateRoomDialog')
    }

    const buttonCheck = function () {
      state.privateRoom = !state.privateRoom
    }



    return { state, handleClose, createRoomForm, clickCreateRoom, buttonCheck, dummyValidation, flag }
  }
}
</script>
