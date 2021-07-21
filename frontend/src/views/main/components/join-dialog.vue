<template>
  <el-dialog custom-class="join-dialog" title="회원가입" v-model="state.dialogVisible" @close="handleClose">
    <el-form :model="state.joinForm" status-icon :rules="state.rules" ref="joinForm" >
      <el-form-item prop="department" label="소속" :label-width="state.formLabelWidth">
        <el-input v-model="state.joinForm.department" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item prop="position" label="직책" :label-width="state.formLabelWidth">
        <el-input v-model="state.joinForm.position" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item prop="name" label="이름" :label-width="state.formLabelWidth">
        <el-input v-model="state.joinForm.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="userId" label="아이디" :label-width="state.formLabelWidth" >
        <el-input v-model="state.joinForm.userId" autocomplete="off"></el-input>
        <el-button @click="axiosCheckId">중복확인</el-button>
        <!-- <p v-if="!!state.availableId">사용 가능한 ID입니다.</p> -->
      </el-form-item>

      <el-form-item prop="password" label="비밀번호" :label-width="state.formLabelWidth">
        <el-input v-model="state.joinForm.password" autocomplete="off" show-password></el-input>
      </el-form-item>

      <el-form-item prop="passwordConfirm" label="비밀번호 확인" :label-width="state.formLabelWidth">
        <el-input v-model="state.joinForm.passwordConfirm" autocomplete="off" show-password></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="clickJoin">회원가입</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<style>
.join-dialog {
  width: 400px !important;
  height: 650px;
}
.join-dialog .el-dialog__headerbtn {
  float: right;
}
.join-dialog .el-form-item__content {
  margin-left: 0 !important;
  float: right;
  width: 200px;
  display: inline-block;
}
.join-dialog .el-form-item {
  margin-bottom: 20px;
}
.join-dialog .el-form-item__error {
  font-size: 12px;
  color: red;
}
.join-dialog .el-input__suffix {
  /* display: none; */
}
.join-dialog .el-dialog__footer {
  margin: 0 calc(50% - 80px);
  padding-top: 0;
  display: inline-block;
}
.join-dialog .dialog-footer .el-button {
  width: 120px;
}
</style>
<script>
import { reactive, computed, ref } from 'vue'
import { useStore } from 'vuex'

export default {
  name: 'join-dialog',

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },

  setup(props, { emit }) {
    const store = useStore()
    const joinForm = ref(null)

    // 비밀번호 조합 확인
    const checkPassword = function (rule, value, callback) {
      if (value.length < 9) {
        return callback(new Error('최소 9 글자를 입력해야 합니다.'))
      } else if (value.length > 16) {
        return callback(new Error('최대 16글자까지 입력 가능합니다.'))
      } else if (!/^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/.test(value)) {
        return callback(new Error('비밀번호는 영문, 숫자, 특수문자가 조합되어야 합니다.'))
      } else {
        joinForm.value.validateField('passwordConfirm')
        return callback()
      }
    }

    // 비밀번호 일치 확인
    const confirmPassword = function (rule, value, callback) {
      if (value !== state.joinForm.password) {
        return callback(new Error('입력한 비밀번호와 일치하지 않습니다.'))
      } else {
        return callback()
      }
    }

    // 중복 아이디 확인
    const checkUserId = function (rule, value, callback) {
      if (value.length > 16) {
        return callback(new Error('최대 16자까지 입력 가능합니다.'))
      } else if (!value) {
        return callback(new Error('필수 입력 항목입니다.'))
      } else if (value === state.nonavailableId) {
        return callback(new Error('이미 존재하는 ID입니다.'))
      } else if (value !== state.availableId) {
        return callback(new Error('ID 중복을 확인해 주세요.'))
      } else {
        return callback()
      }
    }


    const state = reactive({
      availableId: '',
      nonavailableId: '',
      joinForm: {
        department: '',
        position: '',
        name: '',
        userId: '',
        password: '',
        passwordConfirm: '',
        align: 'left'
      },
      rules: {
        department: [
          { required: false, message: '최대 30자까지 입력 가능합니다.', trigger: ['blur', 'change'], max: 30 }
        ],
        position: [
          { required: false, message: '최대 30자까지 입력 가능합니다.', trigger: ['blur', 'change'], max: 30 }
        ],
        name: [
          { required: true, message: '필수 입력 항목입니다.'},
          { required: true, message: '최대 30자까지 입력 가능합니다.',  max:30 }
          // 중복된 아이디 체크 에러 메세지
        ],
        userId: [
          { required: true, message: '필수 입력 항목입니다.'},
          { validator: checkUserId, trigger: ['blur', 'change'] }
        ],
        password: [
          { required: true, message: '필수 입력 항목입니다.'},
          { validator: checkPassword, trigger: ['blur', 'change'] }
        ],
        passwordConfirm: [
          { required: true, message: '필수 입력 항목입니다.'},
          { validator: confirmPassword, trigger: ['blur', 'change'] }
        ]
      },
      dialogVisible: computed(() => props.open),
      formLabelWidth: '120px',
    })

    const clickJoin = function () {
      // 회원가입 클릭 시 validate 체크 후 그 결과 값에 따라, 로그인 API 호출 또는 경고창 표시
      joinForm.value.validate((valid) => {
        if (valid) {
          console.log('submit')
          console.log(state.joinForm.password)
          console.log(state.joinForm.passwordCheck)
          store.dispatch('root/requestJoin', {
            department: state.joinForm.department,
            position: state.joinForm.position,
            name: state.joinForm.name,
            userId: state.joinForm.id,
            password: state.joinForm.password,
            passwordCheck: state.joinForm.passwordCheck
          })

          .then(function (result) {
            console.log("result.id" + result.userId)
            if(result.status === 201){
              alert("회원가입 성공")
              emit('closeJoinDialog')
            }

          })
          .catch(function (err) {
            alert(err)
          })
        } else {
          alert('Validate error!')
        }
      });
    }

    //아이디 중복 검사
    const axiosCheckId = function() {
      store.dispatch('root/checkId', state.joinForm.userId)
      .then(function (result) {
        if (result.status === 201){
          console.log('id is available')
          state.availableId = state.joinForm.userId
          state.nonavailableId = ''
          joinForm.value.validateField('userId')
        }
      })
      .catch(function (err) {
        // if (err.response.data.status === 409) {
        console.log('아이디중복')
        state.availableId = ''
        state.nonavailableId = state.joinForm.userId
        joinForm.value.validateField('userId')
        // }
      })

    }


    const handleClose = function () {
      state.joinForm.userId = ''
      state.joinForm.password = ''
      state.joinForm.passwordConfirm = ''
      state.availableId = ''
      state.nonavailableId = ''
      emit('closeJoinDialog')
    }

    return { state, handleClose, joinForm, clickJoin, axiosCheckId }
  }
}
</script>
