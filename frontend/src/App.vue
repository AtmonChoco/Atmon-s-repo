<template>
  <div id="app">
    <nav class="navbar">
      <div class="nav-container">
        <div class="logo" @click="goToHome">🎁 手办商城</div>
        <div class="nav-links">
          <span @click="goToHome" :class="{ active: currentPage === 'home' }">首页</span>
          <span @click="goToCart" :class="{ active: currentPage === 'cart' }">
            购物车 🛒 ({{ cartCount }})
          </span>
          <span v-if="!user" @click="showLogin = true">登录/注册</span>
          <span v-else @click="goToOrders" :class="{ active: currentPage === 'orders' }">我的订单</span>
          <span v-if="user" @click="logout">退出 ({{ user.username }})</span>
        </div>
      </div>
    </nav>

    <div class="container">
      <!-- 首页 -->
      <HomePage
          v-if="currentPage === 'home'"
          @add-to-cart="addToCart"
          @view-detail="viewDetail"
      />

      <!-- 购物车页面 -->
      <CartPage
          v-if="currentPage === 'cart'"
          :cart="cart"
          :user="user"
          @update-cart="updateCart"
          @go-home="goToHome"
          @order-created="handleOrderCreated"
      />

      <!-- 订单页面 -->
      <OrdersPage
          v-if="currentPage === 'orders' && user"
          :userId="user.id"
      />
    </div>

    <!-- 登录弹窗 -->
    <LoginModal
        v-if="showLogin"
        @close="showLogin = false"
        @login-success="handleLoginSuccess"
    />

    <!-- 商品详情弹窗 -->
    <DetailModal
        v-if="showDetail"
        :figureId="currentFigureId"
        @close="showDetail = false"
        @add-to-cart="addToCart"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import HomePage from './components/HomePage.vue'
import CartPage from './components/CartPage.vue'
import OrdersPage from './components/OrdersPage.vue'
import LoginModal from './components/LoginModal.vue'
import DetailModal from './components/DetailModal.vue'

const currentPage = ref('home')
const showLogin = ref(false)
const showDetail = ref(false)
const currentFigureId = ref(null)
const user = ref(null)
const cart = ref([])

// 购物车数量
const cartCount = computed(() => {
  return cart.value.reduce((sum, item) => sum + item.quantity, 0)
})

// 加载购物车和用户信息
onMounted(() => {
  const savedCart = localStorage.getItem('cart')
  if (savedCart) {
    cart.value = JSON.parse(savedCart)
  }
  const savedUser = localStorage.getItem('user')
  if (savedUser) {
    user.value = JSON.parse(savedUser)
  }
})

// 保存购物车
const saveCart = () => {
  localStorage.setItem('cart', JSON.stringify(cart.value))
}

// 更新购物车
const updateCart = (newCart) => {
  cart.value = newCart
  saveCart()
}

// 添加购物车
const addToCart = (figure) => {
  const existing = cart.value.find(item => item.id === figure.id)
  if (existing) {
    existing.quantity += figure.quantity || 1
  } else {
    cart.value.push({
      id: figure.id,
      name: figure.name,
      price: figure.price,
      imageUrl: figure.imageUrl,
      quantity: figure.quantity || 1
    })
  }
  saveCart()
  alert('已添加到购物车')
}

// 查看详情
const viewDetail = (id) => {
  currentFigureId.value = id
  showDetail.value = true
}

// 登录成功
const handleLoginSuccess = (userData) => {
  user.value = userData
  localStorage.setItem('user', JSON.stringify(userData))
  showLogin.value = false
}

// 退出登录
const logout = () => {
  user.value = null
  localStorage.removeItem('user')
  localStorage.removeItem('token')
  currentPage.value = 'home'
}

// 页面跳转
const goToHome = () => {
  currentPage.value = 'home'
}

const goToCart = () => {
  currentPage.value = 'cart'
}

const goToOrders = () => {
  currentPage.value = 'orders'
}

// 订单创建成功
const handleOrderCreated = (order) => {
  currentPage.value = 'orders'
}
</script>

<style>
/* ... 保持原有样式 ... */
</style>