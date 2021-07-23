<template>
  <el-dialog custom-class="login-dialog" title="로그인" v-model="state.dialogVisible" @close="handleClose">
    <el-form :model="state.form" :rules="state.rules" ref="loginForm" :label-position="state.form.align">
      <el-form-item prop="userId" label="아이디" :label-width="state.formLabelWidth" >
        <el-input v-model="state.form.userId" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="password" label="비밀번호" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.password" autocomplete="off" show-password></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="clickLogin" :disabled="state.isInvalid">로그인</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { reactive, computed, ref } from 'vue'
import { useStore } from 'vuex'

export default {
  name: 'login-dialog',

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },

  setup(props, { emit }) {
    const store = useStore()
    const loginForm = ref(null)
    const flag = ref({
      userId: false,
      password: false,
    })

    const checkUserId = function (rule, value, callback) {
      if (!value) {
        flag.value.userId = false
        return callback(new Error('필수 입력 항목입니다.'))
      } else {
        flag.value.userId = true
        return callback()
      }
    }

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
        flag.value.password = true
        return callback()
      }
    }

    const state = reactive({
      isInvalid: computed(() => {
        return Object.values(flag.value).some(bool => bool === false)
      }),
      form: {
        userId: '',
        password: '',
        align: 'left'
      },
      rules: {
        userId: [
          { validator: checkUserId, trigger: ['blur', 'change'] },
          { required: true },
        ],
        password: [
          { validator: checkPassword, trigger: ['blur', 'change'] },
          { required: true },
        ],
      },
      dialogVisible: computed(() => props.open),
      formLabelWidth: '120px',
    })

    const clickLogin = function () {
      if (!state.isInvalid) {
          store.dispatch('root/requestLogin', { userId: state.form.userId, password: state.form.password })
          .then(result => {
            if(result.status === 200){
              localStorage.setItem('jwt', result.data.accessToken)
              store.commit('root/setUserId', state.form.userId)
              console.log(state.form.userId)
              console.log('after commit, login dialog')
              alert('로그인 성공')
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

    const handleClose = function () {
      state.form.userId = ''
      state.form.password = ''
      emit('closeLoginDialog')
    }

    return { loginForm, state, clickLogin, handleClose }
  }
}
</script>
<style>
.login-dialog {
  width: 400px !important;
  height: 300px;
}
.login-dialog .el-dialog__headerbtn {
  float: right;
}
.login-dialog .el-form-item__content {
  margin-left: 0 !important;
  float: right;
  width: 200px;
  display: inline-block;
}
.login-dialog .el-form-item {
  margin-bottom: 20px;
}
.login-dialog .el-form-item__error {
  font-size: 12px;
  color: red;
}
.login-dialog .el-input__suffix {
  /* display: none; */
}
.login-dialog .el-dialog__footer {
  margin: 0 calc(50% - 80px);
  padding-top: 0;
  display: inline-block;
}
.login-dialog .dialog-footer .el-button {
  width: 120px;
}
</style>
