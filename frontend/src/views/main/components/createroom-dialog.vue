<template>
  <el-dialog custom-class="createroom-dialog" title="방 생성하기" v-model="state.dialogVisible" @close="handleClose">
    <el-form :model="state.form" :rules="state.rules" ref="createRoomForm" :label-position="state.form.align">
      <!-- 제목 설정하기 -->
      <el-form-item prop="title" label="제목" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.title" autocomplete="off"></el-input>
      </el-form-item>
      <!-- 주제 설정하기 -->
      <el-form-item prop="topic" label="주제" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.topic" autocomplete="off"></el-input>
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
      <!-- 관전자 인원수 설정하기 -->
      <el-form-item prop="observers" label="관전인원수" :label-width="state.formLabelWidth">
        <el-select v-model="state.form.observers" placeholder="관전자 인원수">
          <el-option
            v-for="number in obs_numbers"
            :key="number.value"
            :label="number.label"
            :value="number.value">
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
        <el-button type="primary" @click="clickCreateRoom">생성</el-button>
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
import { reactive, computed, ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
export default {
  data() {
    return {
        part_numbers: [{
          value: 2,
          label: '2'
        }, {
          value: 4,
          label: '4'
        }, {
          value: 6,
          label: '6'
        }, {
          value: 8,
          label: '8'
        }, {
          value: 10,
          label: '10'
        }],
        obs_numbers: [{
          value: 1,
          label: '1'
        }, {
          value: 3,
          label: '3'
        }, {
          value: 5,
          label: '5'
        }, {
          value: 7,
          label: '7'
        }, {
          value: 9,
          label: '9'
        }],
        times: [{
          value: 20,
          label: '20'
        }, {
          value: 30,
          label: '30'
        }, {
          value: 40,
          label: '40'
        }, {
          value: 50,
          label: '50'
        }, {
          value: 60,
          label: '60'
        }],
        value: ''
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

    const state = reactive({
      form: {
        title: '',
        topic: '',
        participants: '',
        observers: '',
        times: '',
        privateRoom: false,
        roomPassword: '',
        align: 'left'
      },
      rules: {
        title: [
          { required: true, message: '필수 입력 항목입니다.' },
          { required: true, message: '최대 30자까지 입력 가능합니다.', max:30 }
        ],
        topic: [
          { required: true, message: '선택 항목입니다.'},
        ],
        participants: [
          { required: true, message: '참가자 인원수 선택하세요.' }
        ],
        observers: [
          { required: true, message: '관전자 인원수 선택하세요.' }
        ],
        times: [
          { required: true, message: '토론시간 선택하세요.' }
        ],
        privateRoom: [
          { required: true, message: '비공개 여부 체크하세요' }
        ],
        roomPassword: [
          { required: true, message: '방 비밀번호 입력하세요' }
        ],
      },
      dialogVisible: computed(() => props.open),
      formLabelWidth: '120px',
    })

    // onMounted(() => {
    //   console.log(joinForm.value)
    // })

    const clickCreateRoom = function () {
      createRoomForm.value.validate((valid) => {
        if (valid) {
          store.dispatch('root/requestCreateRoom', {
            title: state.form.title,                // string
            topic: state.form.topic,                // string
            participants: state.form.participants,  // integer
            observers: state.form.observers,        // integer
            times: state.form.times,                // integer
            privateRoom: state.form.privateRoom,    // boolean
            roomPassword: state.form.roomPassword,  // string
          })
        // api 응답 결과로 받은 conference_id 값으로 '방 상세보기' 페이지에 진입해야함
          .then(function (result) {
            emit('closeCreateRoomDialog')
          })
          .catch(function (err) {
             alert(err)
          })
        } else {
          alert('Validate error!')
        }
      });
    }

    const handleClose = function () {
      emit('closeCreateRoomDialog')
    }

    const buttonCheck = function () {
      state.privateRoom = !state.privateRoom
    }

    return { state, handleClose, createRoomForm, clickCreateRoom, buttonCheck}
  }
}
</script>
