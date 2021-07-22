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

    // 아이디, 닉네임 입력 대기용 dummy function
    const dummyValidation = function (rule, value, callback) {
      console.log('wating')
    }

    // 아이디
    const checkUserId = function (rule, value, callback) {
      if (value.length > 16) {
        state.flag.userId = false
        return callback(new Error('최대 16자까지 입력 가능합니다.'))
      } else {
        store.dispatch('root/checkId', state.form.userId)
        .then(function (result) {
          // 200으로 수정
          if (result.status === 201){
            console.log('id is available')
            state.flag.userId = true
            console.log(state.flag.userId)
            return callback()
          }
        })
        .catch(function (err) {
          // if (err.response.data.status === 409) {
          console.log('아이디중복')
          state.flag.userId = false
          return callback(new Error('이미 존재하는 ID입니다.'))
        })
      }
    }

    // 비밀번호 조합 확인
    const checkPassword = function (rule, value, callback) {
      if (value.length < 9) {
        state.flag.password = false
        return callback(new Error('최소 9글자를 입력해야 합니다.'))
      } else if (value.length > 16) {
        state.flag.password = false
        return callback(new Error('최대 16글자까지 입력 가능합니다.'))
      } else if (!/^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/.test(value)) {
        state.flag.password = false
        return callback(new Error('비밀번호는 영문, 숫자, 특수문자가 조합되어야 합니다.'))
      } else {
        joinForm.value.validateField('passwordConfirm')
        state.flag.password = true
        return callback()
      }
    }

    // 비밀번호 일치 확인
    const checkConfirm = function (rule, value, callback) {
      if (value !== state.form.password) {
        state.flag.passwordConfirm = false
        return callback(new Error('입력한 비밀번호와 일치하지 않습니다.'))
      } else {
        state.flag.passwordConfirm = true
        return callback()
      }
    }

    // 닉네임; back에서 만들어주면 주석풀기
    const checkNickname = function (rule, value, callback) {
      return callback()
      // if (value.length < 2) {
      //   state.flag.nickname = false
      //   return callback(new Error('최소 2글자를 입력해야 합니다.'))
      // } else if (value.length > 30) {
      //   state.flag.nickname = false
      //   return callback(new Error('최대 30글자까지 입력 가능합니다.'))
      // } else {
      //   store.dispatch('root/checkNickname', state.form.nickname)
      //   .then(function (result) {
      //     // 200으로 수정
      //     if (result.status === 201){
      //       console.log('nickname is available')
      //       state.flag.nickname = true
      //       return callback()
      //     }
      //   })
      //   .catch(function (err) {
      //     // if (err.response.data.status === 409) {
      //     console.log('닉네임중복')
      //     state.flag.nickname = false
      //     return callback(new Error('이미 존재하는 ID입니다.'))
      //   })
      // }
    }


    // 이름
    const checkName = function (rule, value, callback) {
      if (value.length < 2) {
        state.flag.name = false
        return callback(new Error('최소 2글자를 입력해야 합니다.'))
      } else if (value.length > 30) {
        state.flag.name = false
        return callback(new Error('최대 30글자까지 입력 가능합니다.'))
      } else {
        state.flag.name = true
        return callback()
      }
    }

    const state = reactive({
      dialogVisible: computed(() => props.open),
      formLabelWidth: '120px',
      flag: {
        userId: false,
        password: false,
        passwordConfirm: false,
        nickname: false,
        name: false,
      },
      isInvalid: computed(() => {
        // 왜안되냐
        // return Object.values(state.flag).some(bool => bool === false)
        return state.flag.userId && state.flag.password && state.flag.passwordConfirm && state.flag.nickname && state.flag.name
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
          { required: true, message: '필수 입력 항목입니다.' },
          { validator: dummyValidation, trigger: 'change' },
          { validator: checkUserId, trigger: 'blur' },
        ],
        password: [
          { required: true, message: '필수 입력 항목입니다.' },
          { validator: checkPassword, trigger: ['blur', 'change'] },
        ],
        passwordConfirm: [
          { required: true, message: '필수 입력 항목입니다.' },
          { validator: checkConfirm, trigger: ['blur', 'change'] },
        ],
        nickname: [
          { required: true, message: '필수 입력 항목입니다.' },
          { validator: dummyValidation, trigger: 'change' },
          { validator: checkNickname, trigger: 'blur' },
        ],
        name: [
          { required: true, message: '필수 입력 항목입니다.' },
          { validator: checkName, trigger: ['blur', 'change'] },
        ],
      },
    })



    // 회원가입 실행
    const clickJoin = function () {
      // 회원가입 클릭 시 validate 체크 후 그 결과 값에 따라, 로그인 API 호출 또는 경고창 표시
      form.value.validate((valid) => {
        if (valid) {
          store.dispatch('root/requestJoin', {
            nickname: state.form.nickname,
            name: state.form.name,
            userId: state.form.id,
            password: state.form.password,
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
      })
    }

    // 닫기
    const handleClose = function () {
      state.form.nickname = ''
      state.form.name = ''
      state.form.userId = ''
      state.form.password = ''
      state.form.passwordConfirm = ''
      state.validId = ''
      state.invalidId = ''
      state.validNickname = ''
      state.invalidNickname = ''
      emit('closeJoinDialog')
    }

    return { state, handleClose, joinForm, clickJoin, dummyValidation }
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
