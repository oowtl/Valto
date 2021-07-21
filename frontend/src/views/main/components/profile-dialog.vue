<template>
  <el-dialog custom-class="profile-dialog" title="프로필" v-model="state.dialogVisible" @close="handleClose">
    <el-form :model="state.form" status-icon :rules="state.rules" ref="profileForm" :label-position="state.form.align">
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
        <el-input v-model="state.form.id" autocomplete="off" :disabled="true"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="clickUpdateProfile">저장</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<style>
.profile-dialog {
  width: 400px !important;
  height: 450px;
}
.profile-dialog .el-dialog__headerbtn {
  float: right;
}
.profile-dialog .el-form-item__content {
  margin-left: 0 !important;
  float: right;
  width: 200px;
  display: inline-block;
}
.profile-dialog .el-form-item {
  margin-bottom: 20px;
}
.profile-dialog .el-form-item__error {
  font-size: 12px;
  color: red;
}
.profile-dialog .el-input__suffix {
  /* display: none; */
}
.profile-dialog .el-dialog__footer {
  margin: 0 calc(50% - 80px);
  padding-top: 0;
  display: inline-block;
}
.profile-dialog .dialog-footer .el-button {
  width: 120px;
}
</style>
<script>
import { reactive, computed, ref, watch } from 'vue'
import { useStore } from 'vuex'

export default {
  name: 'profile-dialog',

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },

  setup(props, { emit }) {
    const store = useStore()
    const profileForm = ref(null)

    // 비밀번호 조합 확인
    // const checkPassword = function() {
    //   if (/^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/.test(state.form.password)){
    //     return callback(new Error('Please input the age'));
    //   } else if {

    //   } else {
    //   }
    // }

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
        id: '',
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
        ],
      },
      dialogVisible: computed(() => props.open),
      formLabelWidth: '120px',
    })

    const clickUpdateProfile = function () {
      // 회원가입 클릭 시 validate 체크 후 그 결과 값에 따라, 로그인 API 호출 또는 경고창 표시
      joinForm.value.validate((valid) => {
        if (valid) {

          store.dispatch('root/', {
            department: state.form.department,
            position: state.form.position,
            name: state.form.name,
            id: state.form.id,
            password: state.form.password,
            passwordCheck: state.form.passwordCheck
          })

          .then(function (result) {
            console.log("result.id" +result.id)
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

    const handleClose = function () {
      state.form.department = ''
      state.form.position = ''
      state.form.name = ''
      state.form.id = ''
      emit('closeProfileDialog')
    }

    watch(() => props.open, (newVal, oldVal) => {
      if (newVal === true) {
        console.log('profile dialog opened')
        store.dispatch('root/onPageEnter')
          .then(function (result) {
            console.log('me request successful')
            console.log(result)
            state.form.department = result.data.department
            state.form.position = result.data.position
            state.form.name = result.data.username
            state.form.id = result.data.userId
          })
          .catch(function (err) {
            store.dispatch('root/axiosErrorHandler', err)
          })
      } else if (newVal === false) {
        console.log('profile dialog closed')
      }
    })

    return { state, handleClose, profileForm, clickUpdateProfile }
  }
}
</script>
