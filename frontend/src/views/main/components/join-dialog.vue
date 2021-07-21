<template>
  <el-dialog custom-class="join-dialog" title="회원가입" v-model="state.dialogVisible" @close="handleClose">
    <el-form :model="state.form" :rules="state.rules" ref="joinForm" :label-position="state.form.align">
      <el-form-item prop="department" label="소속" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.department" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item prop="position" label="직책" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.position" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item prop="name" label="이름" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="id" label="아이디" :label-width="state.formLabelWidth" >
        <el-input v-model="state.form.userId" autocomplete="off"></el-input>
        <el-button @click="userIdCheck">중복확인</el-button>
      </el-form-item>

      <el-form-item prop="password" label="비밀번호" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.password" autocomplete="off" show-password @blur="checkPassword"></el-input>
        <div v-if="!state.passwordFlag">유효하지 않는 비밀번호입니다.</div>
        <div v-else>유효한 비밀번호입니다.</div>
      </el-form-item>

      <el-form-item prop="passwordCheck" label="비밀번호 확인" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.passwordCheck" autocomplete="off" show-password @blur="passwordCheckValid"></el-input>
        <div v-if="!state.passwordCheckFlag">비밀번호가 동일하지 않습니다.</div>
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
  display: none;
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
// import { useRouter } from 'vue-router'
// import PasswordChecker from "vue-password-checker";
export default {
  name: 'join-dialog',

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },

  // components:{
  //   PasswordChecker
  // },

  setup(props, { emit }) {
    const store = useStore()
    // 마운드 이후 바인딩 될 예정 - 컨텍스트에 노출시켜야함. <return>
    // const loginForm = ref(null)
    const joinForm = ref(null)

    // const router = useRouter()

    /*
      // Element UI Validator
      // rules의 객체 키 값과 form의 객체 키 값이 같아야 매칭되어 적용됨
      //
    */
    const state = reactive({
      form: {
        department: '',
        position: '',
        name: '',
        userId: '',
        password: '',
        passwordCheck: '',
        align: 'left'
      },
      rules: {
        department: [
          { required: false, message: '최대 30자까지 입력 가능합니다.',trigger: 'blur', max: 30}
        ],
        position:[
          { required: false, message: '최대 30자까지 입력 가능합니다.', max:30}
        ],
        name: [
          { required: true, message: '필수 입력 항목입니다.'},
          { required: true,  message: '최대 30자까지 입력 가능합니다.',  max:30 }
          // 중복된 아이디 체크 에러 메세지
        ],
        userId: [
          { required: true, message: '필수 입력 항목입니다.'},
          { required: true, message: '최대 16 글짜까지 입력 가능합니다.', max:16 }
        ],
        password: [


          { required: true, message: '필수 입력 항목입니다.'},
          { required: true, message: '최소 9 글자를 입력해야합니다.', trigger: 'blur',min: 9},
          { required: true, message: '최대 16 글자까지 입력가능합니다.', trigger: 'blur',max: 16}
        ],
        passwordCheck:[
          {required: true, message: '필수 입력 항목입니다.'},
          { required: true, message: '최소 9 글자를 입력해야합니다.', trigger: 'blur',min: 9},
          { required: true, message: '최대 16 글자까지 입력가능합니다.', trigger: 'blur',max: 16}
          //password와 동일하지 않은경우 에러 메세지
          //password 숫자, 문자, 특수문자
        ]
      },
      dialogVisible: computed(() => props.open),
      formLabelWidth: '120px',
      passwordFlag: false,
      passwordCheckFlag: true
    })

    // onMounted(() => {
    //   console.log(joinForm.value)
    // })

    //비밀번호 숫자,영문,특수문자 확인
    const checkPassword = function() {
      console.log('upw' + state.form.password)
      console.log('passwordFlag' + state.passwordFlag)
      if (/^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/.test(state.form.password)){
        state.passwordFlag = true
      } else {
        state.passwordFlag = false
      }
    }
    //비밀번호 일치 유무 확인
    const passwordCheckValid = function(){
      if(state.form.password === state.form.passwordCheck){
        state.passwordCheckFlag = true
      }else{
        state.passwordCheckFlag = false
      }
    }

    const clickJoin = function () {
      // 회원가입 클릭 시 validate 체크 후 그 결과 값에 따라, 로그인 API 호출 또는 경고창 표시
      joinForm.value.validate((valid) => {
        if (valid) {
          console.log('submit')
          // console.log(state.form.password)
          // console.log(state.form.passwordCheck)
          console.log(state.form.userId)
          store.dispatch('root/requestJoin', {
            department: state.form.department,
            position: state.form.position,
            name: state.form.name,
            password: state.form.password,
            passwordCheck: state.form.passwordCheck,
            userId: state.form.userId
          })

          .then(function (result) {
            if(result.status === 201){
               alert('회원가입 성공')
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
    const userIdCheck = function() {
      store.dispatch('root/checkId', state.form.id)
      .then( function (result){
        if(result.status === 201){
          alert('사용가능한 아이디입니다.')
          // console.log("아이디가 중복되었습니다.")
        }
      })
      .catch(function (err){
          console.log(err.status)
          alert('아이디가 중복되었습니다.')

      })

    }


    const handleClose = function () {
      state.form.id = ''
      state.form.password = ''
      emit('closeJoinDialog')
    }

    return { state, handleClose ,joinForm , clickJoin , userIdCheck, checkPassword, passwordCheckValid}
  }
}
</script>
