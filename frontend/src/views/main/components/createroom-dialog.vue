<template>
  <el-dialog custom-class="createroom-dialog" title="방 생성하기" v-model="state.dialogVisible" @close="handleClose">
    <el-form :model="state.form" status-icon :rules="state.rules" ref="createRoomForm" :label-position="state.form.align">
      <!-- 제목 설정하기 -->
      <el-form-item prop="title" label="제목" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.title" autocomplete="off"></el-input>
      </el-form-item>
      <!-- 주제 설정하기 -->
      <el-form-item prop="topicAgree" label="주제1" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.topicAgree" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="topicOpposite" label="주제2" :label-width="state.formLabelWidth">
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
      <el-form-item prop="userSide" label="포지션" :label-width="state.formLabelWidth">
        <span class="dialog-position1">
          <el-button type="primary" @click="clickPosition1" :disabled="state.posi1">주제1</el-button>
        </span>
        <span class="dialog-position2">
          <el-button type="primary" @click="clickPosition2" :disabled="state.posi2">주제2</el-button>
        </span>
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
  height: 550px;
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
.createroom-dialog .dialog-position1 {
  float: left;
  padding-top: 0;
  display: inline-block;
}
.createroom-dialog .dialog-position2 {
  float: right;
  padding-top: 0;
  display: inline-block;
  margin-right: 20px;
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
        // userSide: [
        //   { value: 'agree', label: '주제1(찬성)' },
        //   { value: 'opposite', label: '주제2(반대)' },
        // ],
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
      topicOpposite: false,
      participants: false,
      userSide: false,
      times: false,
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
        flag.value.topicOpposite = false
        return callback(new Error('필수 입력 항목 입니다.'))
      }else if (value.length > 15) {
        flag.value.topicOpposite = false
        return callback(new Error(`최대 15글자까지 입력 가능합니다. 현재 ${value.length} 글자.`))
      }
      else {
        flag.value.topicOpposite = true
        return callback()
      }
    }

    const checkParticipants = function (rule, value, callback) {
      if (!value) {
        flag.value.participants = false
        return callback(new Error('필수 입력 항목 입니다.'))
      }
      else {
        flag.value.participants = true
        return callback()
      }
    }

    const checkUserSide = function (rule, value, callback) {
      if (!value) {
        flag.value.userSide = false
        return callback(new Error('필수 선택 항목 입니다.'))
      }
      else {
        flag.value.userSide = true
        return callback()
      }
    }

    const checkTimes = function (rule, value, callback) {
      if (!value) {
        flag.value.times = false
        return callback(new Error('필수 선택 항목 입니다.'))
      }
      else {
        flag.value.times = true
        return callback()
      }
    }

    const state = reactive({
      userId: computed(() => {
        return store.getters['root/getUserId']
      }),
      posi1: false,
      posi2: false,
      isInvalid: computed(() => {
        return Object.values(flag.value).some(bool => bool === false)
      }),
      form: {
        // userId: this.userId,
        title: '',
        topicAgree: '',
        topicOpposite: '',
        participants: '',
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
          { validator: checkParticipants, trigger: 'change' },
          { required: true, message: '참가자 인원수를 선택해 주세요.' }
        ],
        times: [
          { validator: checkTimes, trigger: 'change' },
          { required: true, message: '토론시간을 선택해 주세요.' }
        ],
      },

      dialogVisible: computed(() => props.open),
      formLabelWidth: '120px',
    })

    const clickPosition1 = function () {
      state.posi1 = true
      state.posi2 = false
      flag.value.userSide = true
      console.log(flag)
      state.form.userside = 'agree'
    }

    const clickPosition2 = function () {
      state.posi1 = false
      state.posi2 = true
      flag.value.userSide = true
      console.log(flag)
      state.form.userside = 'opposite'
    }

    const clickCreateRoom = function () {
      if (!state.isInvalid) {
        store.dispatch('root/requestCreateRoom', {
          userId: state.userId,                   // string
          title: state.form.title,                // string
          topicAgree: state.form.topicAgree,      // string
          topicOpposite: state.form.topicOpposite,// string
          participants: state.form.participants,  // integer
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



    return { state, handleClose, createRoomForm, clickCreateRoom, clickPosition1, clickPosition2, buttonCheck, dummyValidation, flag }
  }
}
</script>
