<template>
  <div style="padding: 14px;">
    <el-dialog
      v-if="state.form"
      custom-class="detail-dialog"
      :title="state.form.title"
      v-model="state.dialogVisible"
      @close="handleClose"
    >
      <el-form :label-position="state.align">
        <span class="topics">
          <span>{{ state.form.topicAgree }}</span>
          <span style="color: #6b6b6b;"> vs. </span>
          <span>{{ state.form.topicOpposite }}</span>
          <hr>
        </span>
        <el-form-item label="토론 시간: " :label-width="state.formLabelWidth">
          {{ state.form.times }}분
        </el-form-item>
        <el-form-item label="주제1(왼쪽) 인원수: " :label-width="state.formLabelWidth">
          {{ state.form.agreeUsers.length }}/{{ state.divide_participants }}
        </el-form-item>
        <el-form-item label="주제2(오른쪽) 인원수: " :label-width="state.formLabelWidth">
          {{ state.form.oppositeUsers.length }}/{{ state.divide_participants }}
        </el-form-item>
<<<<<<< HEAD
        <el-form-item prop="userSide" label="userSide" :label-width="state.formLabelWidth">
          <el-select class="positionSelect" v-model="state.form.userSide" placeholder="포지션">
            <el-option
              v-for="position in userSide"
              :key="position.value"
              :label="position.label"
              :value="position.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="roomPassword" label="방 비밀번호: " :label-width="state.formLabelWidth">
          <div v-if="!state.form.privateRoom" autocomplete="off">없음</div>
          <el-input v-if="state.form.privateRoom" v-model="state.form.roomPassword" placeholder="방 비밀번호를 입력하시오" autocomplete="off"></el-input>
=======
        <el-form-item prop="userSide" label="포지션" :label-width="state.formLabelWidth">
          <span class="dialog-position1">
            <el-button type="primary" @click="clickPosition1" :disabled="state.posi1">주제1</el-button>
          </span>
          <span class="dialog-position2">
            <el-button type="primary" @click="clickPosition2" :disabled="state.posi2">주제2</el-button>
          </span>
>>>>>>> front/ranking
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button
            type="primary"
            @click="clickEnter(state.form.roomId)"
            v-model="state.dialogVisible"
            :disabled="state.isInvalid"
            >입장</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { reactive, computed, ref, watch } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';

export default {
  name: 'detail-dialog',

  props: {
    open: {
      type: Boolean,
      default: false
    },
    roomId: {
      type: String
    }
  },

<<<<<<< HEAD
  data() {
    return {
      userSide: [{
          value: 'agree',
          label: '주제1(찬성)'
        }, {
          value: 'opposite',
          label: '주제2(반대)'
        },
      ],
    }
  },

=======
>>>>>>> front/ranking
  setup(props, { emit }) {
    const router = useRouter();
    const store = useStore();
    const detailForm = ref(null);
    const state = reactive({
      form: null,
      posi1: false,
      posi2: false,
      isInvalid: true,
      divide_participants: '',
      dialogVisible: computed(() => props.open),
      formLabelWidth: '250px',
      align: 'left',
      token: null,

    })

    const clickPosition1 = function () {
      state.posi1 = true
      state.posi2 = false
      state.isInvalid = false
      state.form.userside = 'agree'
    }

    const clickPosition2 = function () {
      state.posi1 = false
      state.posi2 = true
      state.isInvalid = false
      state.form.userside = 'opposite'
    }

    // 닫기
    const handleClose = function() {
      state.form = null;
      emit('closeDetailDialog')
    }

    // 모달 창이 열릴 때 내 프로필 받아오는 함수 호출
    // 여기 수정해야할거 같다. 내 프로필이 아니라 방을 만든 사람의 프로필 정보를 넣기 때문에 이상한거 같음.
    watch(() => props.open, (newVal, oldVal) => {
      if (newVal === true) {
        store.dispatch('root/requestDetail', props.roomId)
          .then(function (result) {
            state.form = result.data
            state.divide_participants = result.data.participants/2
<<<<<<< HEAD
=======
            if ( state.form.agreeUsers.length == state.divide_participants ) {
              state.posi1 = true
            }
            if ( state.form.oppositeUsers.length == state.divide_participants ) {
              state.posi2 = true
            }
>>>>>>> front/ranking
            console.log(state.form.userId)
          })
          .catch(function (err) {
            console.log(err)
            handleClose()
          })
        } else if (newVal === false) {
          handleClose()
        }
      }
    );

    const clickEnter = function(roomId) {
      store.commit('root/serUserSide', state.form.userSide)
      router.push({
        name: 'room',
        params: {
          roomId: roomId
        }
      });
    };


    return { state, handleClose, detailForm, clickEnter, clickPosition1, clickPosition2 };
  }
};
</script>
<style>
.detail-dialog {
  width: 600px !important;
  height: 700px;
}
.topics {
  text-align: center;
  font-weight: bold;
  font-size: 40px;
  color: black;
  display: block;
  margin-top: 20px;
  margin-bottom: 80px;
}


/* .detail-dialog .el-dialog__headerbtn {
  float: right;
} */
.detail-dialog .el-form-item__label {
  margin-left: 50px !important;
  font-size: 25px;
  font-weight: bold;
  display: inline-block;
}
.detail-dialog .el-form-item__content {
  margin-left: 0 !important;
  font-size: 25px;
  font-weight: bold;
  display: inline-block;
  text-align: center;
}
.detail-dialog .el-form-item__content .positionSelect {
  width: 130px;
  font-weight: bold;
}

/* .detail-dialog .el-form-item {
  margin-bottom: 20px;
  font-size: 20px;
} */
/* .detail-dialog .el-form-item__error {
  font-size: 12px;
  color: red;
} */
/* .detail-dialog .el-input__suffix {
  display: none;
} */
.detail-dialog .dialog-position1 {
  float: left;
  padding-top: 0;
  display: inline-block;
}
.detail-dialog .dialog-position2 {
  float: right;
  padding-top: 0;
  display: inline-block;
  margin-right: 20px;
}
.detail-dialog .el-dialog__footer {
  margin: 10px calc(50% - 80px);
  padding-top: 0;
  display: inline-block;
}
.detail-dialog .dialog-footer .el-button {
  width: 120px;
  height: 50px;
  font-size: 25px;
}
</style>
