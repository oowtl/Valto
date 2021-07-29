<template>
  <el-dialog custom-class="profile-dialog" title="프로필" v-model="state.dialogVisible" @close="handleClose">
    <el-form :model="state.form" status-icon :rules="state.rules" ref="profileForm" :label-position="state.form.align">
      <el-form-item prop="userId" label="아이디" :label-width="state.formLabelWidth" >
        <el-input v-model="state.form.userId" autocomplete="off" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item prop="nickname" label="닉네임" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.nickname" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item prop="name" label="이름" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.name" autocomplete="off"></el-input>
      </el-form-item>
      <!-- <el-form-item prop="point" label="포인트" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.point" autocomplete="off" ></el-input>
      </el-form-item> -->
      <p>{{ state.form.point }}</p>
      <p>{{ state.form.userRecord }}</p>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="clickUpdateProfile" :disabled="state.isInvalid">수정</el-button>
      </span>
    </template>
  </el-dialog>
</template>
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
    const flag = ref({
      nickname: false,
      name: false,
    })

    const dummyValidation = function (rule, value, callback) {
      console.log('wating for blur')
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
        userId: '',
        nickname: '',
        name: '',
        point: '',
        userRecord: null,
        align: 'left'
      },
      rules: {
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

    const clickUpdateProfile = function () {
      // 수정 클릭 시 validate 체크 후 그 결과 값에 따라, 로그인 API 호출 또는 경고창 표시
      if (!state.isInvalid) {
        store.dispatch('root/requestUpdateProfile', {
          name: state.form.name,
          nickname: state.form.nickname,
        })
          .then(function (result) {
            // status code 수정
            if(result.status === 201){
              alert('프로필을 수정하였습니다.')
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
      state.form.userId = ''
      state.form.nickname = ''
      state.form.name = ''
      state.form.point = ''
      emit('closeProfileDialog')
    }

    // 모달 창이 열릴 때 내 프로필 받아오는 함수 호출
    watch(() => props.open, (newVal, oldVal) => {
      if (newVal === true) {
        console.log('profile dialog opened')
        store.dispatch('root/requestMyProfile')
        // 전적, 포인트, 닉네임
          .then(function (result) {
            console.log('myprofile request successful')
            state.form.userId = result.data.userId
            state.form.nickname = result.data.nickname
            state.form.name = result.data.username
            state.form.point = result.data.point
            state.form.userRecord = result.data.userRecord
          })
          .catch(function (err) {
            store.dispatch('root/axiosErrorHandler', err)
          })
      } else if (newVal === false) {
        console.log('profile dialog closed')
      }
    })

    return { state, handleClose, profileForm, clickUpdateProfile, dummyValidation, flag }
  }
}
</script>
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

