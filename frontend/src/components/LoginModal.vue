<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal">
      <div class="modal-header">
        <h2>{{ isLogin ? '登录' : '注册' }}</h2>
        <span class="close" @click="$emit('close')">&times;</span>
      </div>

      <div class="modal-body">
        <input
            v-model="form.username"
            type="text"
            placeholder="用户名"
        />
        <input
            v-model="form.password"
            type="password"
            placeholder="密码"
        />
        <input
            v-if="!isLogin"
            v-model="form.email"
            type="email"
            placeholder="邮箱"
        />
        <input
            v-if="!isLogin"
            v-model="form.phone"
            type="tel"
            placeholder="手机号"
        />
        <input
            v-if="!isLogin"
            v-model="form.address"
            type="text"
            placeholder="收货地址"
        />

        <button @click="submit">{{ isLogin ? '登录' : '注册' }}</button>

        <p class="switch">
          <span @click="isLogin = !isLogin">
            {{ isLogin ? '没有账号？去注册' : '已有账号？去登录' }}
          </span>
        </p >
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { login, register } from '../api/user'

const emit = defineEmits(['close', 'login-success'])

const isLogin = ref(true)
const form = ref({
  username: '',
  password: '',
  email: '',
  phone: '',
  address: ''
})

const submit = async () => {
  try {
    if (isLogin.value) {
      const res = await login({
        username: form.value.username,
        password: form.value.password
      })
      if (res.code === 200) {
        alert('登录成功')
        emit('login-success', res.data)
        emit('close')
      } else {
        alert(res.message)
      }
    } else {
      const res = await register(form.value)
      if (res.code === 200) {
        alert('注册成功，请登录')
        isLogin.value = true
        form.value.password = ''
      } else {
        alert(res.message)
      }
    }
  } catch (error) {
    alert('操作失败')
  }
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: white;
  border-radius: 12px;
  width: 400px;
  max-width: 90%;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.modal-body {
  padding: 20px;
}

.modal-body input {
  width: 100%;
  padding: 12px;
  margin-bottom: 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.modal-body button {
  width: 100%;
  padding: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

.switch {
  text-align: center;
  margin-top: 15px;
  color: #667eea;
  cursor: pointer;
}
</style>