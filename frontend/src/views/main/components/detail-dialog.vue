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
        </span>
        <h3>토론 시간: {{ state.form.times }}분</h3>
        <h3>좌파 인원: in development</h3>
        <h3>우파 인원: in development</h3>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button
            type="primary"
            @click="clickEnter(state.form.roomId)"
            v-model="state.dialogVisible"
            >입장</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script>
import { reactive, computed, ref, watch } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

export default {
  name: "detail-dialog",

  props: {
    open: {
      type: Boolean,
      default: false
    },
    roomId: {
      type: String
    }
  },

  setup(props, { emit }) {
    const router = useRouter();
    const store = useStore();
    const detailForm = ref(null);
    const state = reactive({
      form: null,
      dialogVisible: computed(() => props.open),
      formLabelWidth: "120px",
      align: "left",
      token: null
    });

    // 닫기
    const handleClose = function() {
      state.form = null;
      emit("closeDetailDialog");
    };

    // 모달 창이 열릴 때 내 프로필 받아오는 함수 호출
    watch(
      () => props.open,
      (newVal, oldVal) => {
        if (newVal === true) {
          store
            .dispatch("root/requestDetail", props.roomId)
            .then(function(result) {
              console.log(result);
              state.form = result.data;
            })
            .catch(function(err) {
              console.log(err);
            });
        } else if (newVal === false) {
          console.log("detail dialog closed");
        }
      }
    );

    const clickEnter = function(roomId) {
      router.push({
        name: "room",
        params: {
          roomId: roomId
        }
      });
    };
    return { state, handleClose, detailForm, clickEnter };
  }
};
</script>
<style>
.detail-dialog {
  width: 600px !important;
  height: 450px;
}
.topics {
  text-align: center;
  font-weight: bold;
  font-size: 32px;
  color: black;
  display: block;
  margin-top: 20px;
  margin-bottom: 80px;
}
/* .detail-dialog .el-dialog__headerbtn {
  float: right;
} */
/* .detail-dialog .el-form-item__content {
  margin-left: 0 !important;
  float: right;
  width: 200px;
  display: inline-block;
} */
/* .detail-dialog .el-form-item {
  margin-bottom: 20px;
} */
/* .detail-dialog .el-form-item__error {
  font-size: 12px;
  color: red;
} */
/* .detail-dialog .el-input__suffix {
  display: none;
} */
.detail-dialog .el-dialog__footer {
  margin: 0 calc(50% - 80px);
  padding-top: 0;
  display: inline-block;
}
/* .detail-dialog .dialog-footer .el-button {
  width: 120px;
} */
</style>
p
