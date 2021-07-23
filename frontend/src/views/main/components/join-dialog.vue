<template>
  <el-dialog custom-class="join-dialog" title="회원가입" v-model="state.dialogVisible" @close="handleClose">
    <el-form :model="state.form" status-icon :rules="state.rules" ref="joinForm" >
      <el-form-item prop="userId" label="아이디" :label-width="state.formLabelWidth" >
        <el-input v-model="state.form.userId" autocomplete="off"></el-input>
        <!-- <el-button @click="axiosCheckId">중복확인</el-button> -->
        <!-- <p v-if="!!state.validId">사용 가능한 ID입니다.</p> -->
      </el-form-item>
      <el-form-item prop="password" label="비밀번호" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.password" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item prop="passwordConfirm" label="비밀번호 확인" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.passwordConfirm" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item prop="nickname" label="닉네임" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.nickname" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item prop="name" label="이름" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.name" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <!-- <el-button type="primary" @click="clickJoin">회원가입</el-button> -->
        <el-button type="primary" @click="clickJoin" :disabled="state.isInvalid">회원가입</el-button>
      </span>
    </template>
  </el-dialog>
</template>
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
    const flag = ref({
      userId: false,
      password: false,
      passwordConfirm: false,
      nickname: false,
      name: false,
    })

    // 아이디, 닉네임 입력 대기용 dummy function
    const dummyValidation = function (rule, value, callback) {
      console.log('wating for blur')
    }

    // 아이디
    const checkUserId = function (rule, value, callback) {
      if (!value) {
        flag.value.userId = false
        return callback(new Error('필수 입력 항목입니다.'))
      } else if (value.length > 16) {
        flag.value.userId = false
        return callback(new Error('최대 16자까지 입력 가능합니다.'))
      } else {
        store.dispatch('root/checkId', state.form.userId)
        .then(function (result) {
          if (result.status === 200){
            console.log('id is available')
            flag.value.userId = true
            console.log(flag.value.userId)
            return callback()
          }
        })
        .catch(function (err) {
          // if (err.response.data.status === 409) {
          flag.value.userId = false
          return callback(new Error('이미 존재하는 ID입니다.'))
        })
      }
    }

    // 비밀번호 조합 확인
    const checkPassword = function (rule, value, callback) {
      if (!value) {
        flag.value.password = false
        return callback(new Error('필수 입력 항목입니다.'))
      } else if (value.length < 9) {
        flag.value.password = false
        return callback(new Error('최소 9글자를 입력해야 합니다.'))
      } else if (value.length > 16) {
        flag.value.password = false
        return callback(new Error('최대 16글자까지 입력 가능합니다.'))
      } else if (!/^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/.test(value)) {
        flag.value.password = false
        return callback(new Error('비밀번호는 영문, 숫자, 특수문자가 조합되어야 합니다.'))
      } else {
        joinForm.value.validateField('passwordConfirm')
        flag.value.password = true
        return callback()
      }
    }

    // 비밀번호 일치 확인
    const checkConfirm = function (rule, value, callback) {
      if (!value) {
        flag.value.passwordConfirm = false
        return callback(new Error('필수 입력 항목입니다.'))
      } else if (value !== state.form.password) {
        flag.value.passwordConfirm = false
        return callback(new Error('입력한 비밀번호와 일치하지 않습니다.'))
      } else {
        flag.value.passwordConfirm = true
        return callback()
      }
    }

    // 닉네임
    const checkNickname = function (rule, value, callback) {
      if (!value) {
        flag.value.nickname = false
        return callback(new Error('필수 입력 항목입니다.'))
      } else if (value.length < 2) {
        flag.value.nickname = false
        return callback(new Error('최소 2글자를 입력해야 합니다.'))
      } else if (value.length > 30) {
        flag.value.nickname = false
        return callback(new Error('최대 30글자까지 입력 가능합니다.'))
      } else {
        store.dispatch('root/checkNickname', state.form.nickname)
        .then(function (result) {
          if (result.status === 200){
            console.log('nickname is available')
            flag.value.nickname = true
            return callback()
          }
        })
        .catch(function (err) {
          // if (err.response.data.status === 409) {
          console.log('닉네임중복')
          flag.value.nickname = false
          return callback(new Error('이미 존재하는 ID입니다.'))
        })
      }
    }


    // 이름
    const checkName = function (rule, value, callback) {
      if (!value) {
        flag.value.name = false
        return callback(new Error('필수 입력 항목입니다.'))
      } else if (value.length < 2) {
        flag.value.name = false
        return callback(new Error('최소 2글자를 입력해야 합니다.'))
      } else if (value.length > 30) {
        flag.value.name = false
        return callback(new Error('최대 30글자까지 입력 가능합니다.'))
      } else {
        flag.value.name = true
        return callback()
      }
    }

    const state = reactive({
      isInvalid: computed(() => {
        return Object.values(flag.value).some(bool => bool === false)
      }),
      form: {
        nickname: '',
        name: '',
        userId: '',
        password: '',
        passwordConfirm: '',
        align: 'left'
      },
      rules: {
        userId: [
          { validator: dummyValidation, trigger: 'change' },
          { validator: checkUserId, trigger: 'blur' },
          { required: true },
        ],
        password: [
          { validator: checkPassword, trigger: ['blur', 'change'] },
          { required: true },
        ],
        passwordConfirm: [
          { validator: checkConfirm, trigger: ['blur', 'change'] },
          { required: true },
        ],
        nickname: [
          { validator: dummyValidation, trigger: 'change' },
          { validator: checkNickname, trigger: 'blur' },
          { required: true },
        ],
        name: [
          { validator: checkName, trigger: ['blur', 'change'] },
          { required: true },
        ],
      },
      dialogVisible: computed(() => props.open),
      formLabelWidth: '120px',
    })



    // 회원가입 실행
    const clickJoin = function () {
      if (!state.isInvalid) {
        store.dispatch('root/requestJoin', {
          userId: state.form.userId,
          password: state.form.password,
          nickName: state.form.nickname,
          name: state.form.name,
        })
          .then(function (result) {
            console.log('result.id' + result.userId)
            // status code 수정
            if(result.status === 201){
              alert('회원가입 성공')
              handleClose()
            }
          })
          .catch(function (err) {
            alert(err)
          })
        } else {
          alert('Validate error!')
        }
    }

    // 닫기
    const handleClose = function () {
      state.form.nickname = ''
      state.form.name = ''
      state.form.userId = ''
      state.form.password = ''
      state.form.passwordConfirm = ''
      emit('closeJoinDialog')
    }

    return { state, handleClose, joinForm, clickJoin, dummyValidation, flag }
  }
}
</script>
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
