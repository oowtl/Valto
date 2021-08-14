<template>
  <el-dialog v-if="state.form" custom-class="profile-dialog" title="프로필" v-model="state.dialogVisible" @close="handleClose">
    <el-form :model="state.form" status-icon :rules="state.rules" ref="profileForm" :label-position="state.align">
      <el-form-item prop="userId" label="아이디" :label-width="state.formLabelWidth" >
        <el-input v-model="state.form.userId" autocomplete="off" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item prop="nickName" label="닉네임" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.nickName" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item prop="name" label="이름" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.name" autocomplete="off"></el-input>
      </el-form-item>
      <!-- 비밀번호 부분은 생각 필요 -->
      <el-form-item prop="oldPassword" label="비밀번호-개발중" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.oldPassword" autocomplete="off" show-password></el-input>
      </el-form-item>
      <!-- <el-form-item prop="point" label="포인트" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.point" autocomplete="off" ></el-input>
      </el-form-item> -->
      <p>보유 포인트: {{ state.form.point }}</p>
      <p v-if="state.form.userRecord">전적:{{ state.form.userRecord.win }}승 {{ state.form.userRecord.lose }}패 {{ state.form.userRecord.draw }}무</p>
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
import { ElLoading } from 'element-plus'
import { ElMessage } from 'element-plus'
import { useStore } from 'vuex'

export default {
  name: 'profile-dialog',

  props: {
    open: {
      type: Boolean,
      default: false
    },
  },

  setup(props, { emit }) {
    const store = useStore()
    const profileForm = ref(null)
    const currentNickName = ref('')
    const flag = ref({
      nickName: true,
      name: true,
    })

    const dummyValidation = function (rule, value, callback) {
      if (value === currentNickName.value) {
        flag.value.nickName = true
        return callback()
      } else {
        flag.value.nickName = false
        console.log('wating for blur')
      }
    }

    // 닉네임
    const checkNickname = function (rule, value, callback) {
      if (!value) {
        flag.value.nickName = false
        return callback(new Error('필수 입력 항목입니다.'))
      } else if (value.length < 2) {
        flag.value.nickName = false
        return callback(new Error('최소 2글자를 입력해야 합니다.'))
      } else if (value.length > 30) {
        flag.value.nickName = false
        return callback(new Error('최대 30글자까지 입력 가능합니다.'))
      } else if (value === currentNickName.value) {
        return callback()
      } else {
        store.dispatch('root/checkNickname', state.form.nickName)
        .then(function (result) {
          if (result.status === 200){
            console.log('nickName is available')
            flag.value.nickName = true
            return callback()
          }
        })
        .catch(function (err) {
          // if (err.response.data.status === 409) {
          console.log('닉네임중복')
          flag.value.nickName = false
          return callback(new Error('이미 존재하는 닉네임입니다.'))
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
      form: null,
      rules: {
        nickName: [
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
      align: 'left'
    })

    const clickUpdateProfile = function () {
      // 수정 클릭 시 validate 체크 후 그 결과 값에 따라, 로그인 API 호출 또는 경고창 표시
      if (!state.isInvalid) {
        let loadingInstance = ElLoading.service({ fullscreen: true });
        store.dispatch('root/requestUpdateProfile', {
          userId: state.form.userId,
          nickName: state.form.nickName,
          name: state.form.name
        })
          .then(function (result) {
            // status code 수정
            if(result.status === 201){
              ElMessage({ message: '프로필 수정에 성공했습니다.', type: 'success', duration: 2000 })
              loadingInstance.close()
              handleClose()
            }
          })
          .catch(function (err) {
            ElMessage({ message: '프로필 수정에 실패했습니다.', type: 'error', duration: 2000 })
            loadingInstance.close()
          })
      } else {
        alert('Validate error!')
      }
    }

    // 닫기
    const handleClose = function () {
      state.form = null
      emit('closeProfileDialog')
    }

    // 모달 창이 열릴 때 내 프로필 받아오는 함수 호출
    watch(() => props.open, (newVal, oldVal) => {
      if (newVal === true) {
        console.log('profile dialog opened')
        store.dispatch('root/requestMyProfile')
        // 전적, 포인트, 닉네임
          .then(function (result) {
            console.log(result.data)
            console.log('myprofile request successful')
            state.form = result.data
            currentNickName.value = result.data.nickName
          })
          .catch(function (err) {
            alert('프로필 정보를 받아오는 데 실패했습니다.')
            handleClose()
          })
      } else if (newVal === false) {
        console.log('profile dialog closed')
      }
    })

    return { state, handleClose, profileForm, clickUpdateProfile, dummyValidation, currentNickName, flag }
  }
}
</script>
<style>
.profile-dialog {
  width: 400px !important;
  height: 500px;
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

