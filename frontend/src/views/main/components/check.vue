<template>
  <el-form :model="state.form" status-icon :rules="state.rules" ref="profileForm" label-width="120px">
    <el-form-item label="Age" prop="age">
      <el-input v-model.number="state.form.age"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm('ruleForm')">Submit</el-button>
      <el-button @click="resetForm('ruleForm')">Reset</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
import { reactive, computed, ref } from 'vue'
// import { Loading } from 'element-plus'

export default {
  name: 'profile-dialog',

  setup() {
    const profileForm = ref(null)
    // let loadingInstance = Loading.service({ fullscreen: true })
    const checkAge = function (rule, value, callback) {
      console.log('실행됨')
      if (!value) {
        return callback(new Error('Please input the age'));
      }
      setTimeout(() => {
        if (!Number.isInteger(value)) {
          callback(new Error('Please input digits'));
        } else {
          if (value < 18) {
            callback(new Error('Age must be greater than 18'));
          } else {
            callback();
          }
        }
      }, 1000)
    }
    const state = reactive({
      loading: true,
      form: {
        age: ''
      },
      rules: {
        age: [
          { validator: checkAge, trigger: ['blur', 'change'] }
        ]
      },
    })
    return { profileForm, state, checkAge }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!');
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>
