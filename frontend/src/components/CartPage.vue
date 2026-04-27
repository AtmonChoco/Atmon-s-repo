<template>
  <div class="cart-page">
    <h2>购物车</h2>

    <div v-if="cart.length === 0" class="empty-cart">
      <div class="empty-icon">🛒</div>
      <p>购物车还是空的</p >
      <button @click="$emit('go-home')" class="go-shop-btn">去逛逛</button>
    </div>

    <div v-else class="cart-content">
      <div class="cart-items">
        <div v-for="item in cart" :key="item.id" class="cart-item">
          <div class="item-image">
            <img :src="item.imageUrl || '/placeholder.png'" :alt="item.name" />
          </div>

          <div class="item-info">
            <h3>{{ item.name }}</h3>
            <p class="item-price">¥{{ item.price }}</p >
          </div>

          <div class="item-quantity">
            <button @click="updateQuantity(item.id, item.quantity - 1)" :disabled="item.quantity <= 1">-</button>
            <span>{{ item.quantity }}</span>
            <button @click="updateQuantity(item.id, item.quantity + 1)">+</button>
          </div>

          <div class="item-total">
            ¥{{ (item.price * item.quantity).toFixed(2) }}
          </div>

          <div class="item-remove">
            <button @click="removeItem(item.id)" class="remove-btn">删除</button>
          </div>
        </div>
      </div>

      <div class="cart-summary">
        <h3>订单摘要</h3>
        <div class="summary-row">
          <span>商品总数：</span>
          <span>{{ totalQuantity }} 件</span>
        </div>
        <div class="summary-row">
          <span>商品总价：</span>
          <span>¥{{ totalPrice.toFixed(2) }}</span>
        </div>
        <div class="summary-row">
          <span>运费：</span>
          <span>¥0.00</span>
        </div>
        <div class="summary-row total">
          <span>应付总额：</span>
          <span>¥{{ totalPrice.toFixed(2) }}</span>
        </div>

        <button
            v-if="user"
            @click="showCheckout = true"
            class="checkout-btn"
            :disabled="cart.length === 0"
        >
          去结算
        </button>
        <button v-else @click="showLoginModal = true" class="checkout-btn">
          登录后结算
        </button>
      </div>
    </div>

    <!-- 结算弹窗 -->
    <div v-if="showCheckout" class="modal-overlay" @click.self="showCheckout = false">
      <div class="modal checkout-modal">
        <div class="modal-header">
          <h2>确认订单</h2>
          <span class="close" @click="showCheckout = false">&times;</span>
        </div>

        <div class="modal-body">
          <div class="form-group">
            <label>收货人姓名：</label>
            <input v-model="orderInfo.receiverName" type="text" placeholder="请输入收货人姓名" />
          </div>

          <div class="form-group">
            <label>联系电话：</label>
            <input v-model="orderInfo.receiverPhone" type="tel" placeholder="请输入联系电话" />
          </div>

          <div class="form-group">
            <label>收货地址：</label>
            <textarea v-model="orderInfo.receiverAddress" rows="3" placeholder="请输入详细收货地址"></textarea>
          </div>

          <div class="order-summary">
            <p>共 {{ totalQuantity }} 件商品，总计 ¥{{ totalPrice.toFixed(2) }}</p >
          </div>

          <div class="action-buttons">
            <button @click="showCheckout = false" class="cancel-order-btn">取消</button>
            <button @click="submitOrder" class="submit-order-btn" :disabled="submitting">
              {{ submitting ? '提交中...' : '提交订单' }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 登录弹窗 -->
    <LoginModal
        v-if="showLoginModal"
        @close="showLoginModal = false"
        @login-success="handleLoginSuccess"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { createOrder } from '../api/order'
import LoginModal from './LoginModal.vue'

const props = defineProps({
  cart: {
    type: Array,
    default: () => []
  },
  user: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update-cart', 'go-home', 'order-created'])

const showCheckout = ref(false)
const showLoginModal = ref(false)
const submitting = ref(false)

const orderInfo = ref({
  receiverName: '',
  receiverPhone: '',
  receiverAddress: ''
})

// 总数量
const totalQuantity = computed(() => {
  return props.cart.reduce((sum, item) => sum + item.quantity, 0)
})

// 总价格
const totalPrice = computed(() => {
  return props.cart.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

// 更新数量
const updateQuantity = (id, newQuantity) => {
  if (newQuantity < 1) return
  const updatedCart = props.cart.map(item => {
    if (item.id === id) {
      return { ...item, quantity: newQuantity }
    }
    return item
  })
  emit('update-cart', updatedCart)
}

// 删除商品
const removeItem = (id) => {
  const updatedCart = props.cart.filter(item => item.id !== id)
  emit('update-cart', updatedCart)
}

// 提交订单
const submitOrder = async () => {
  if (!orderInfo.value.receiverName) {
    alert('请填写收货人姓名')
    return
  }
  if (!orderInfo.value.receiverPhone) {
    alert('请填写联系电话')
    return
  }
  if (!orderInfo.value.receiverAddress) {
    alert('请填写收货地址')
    return
  }

  submitting.value = true

  try {
    // 构建购物车数据
    const cartMap = {}
    props.cart.forEach(item => {
      cartMap[item.id] = item.quantity
    })

    const res = await createOrder({
      userId: props.user.id,
      receiverName: orderInfo.value.receiverName,
      receiverPhone: orderInfo.value.receiverPhone,
      receiverAddress: orderInfo.value.receiverAddress,
      cart: cartMap
    })

    if (res.code === 200) {
      alert('订单创建成功！')
      emit('order-created', res.data)
      emit('update-cart', [])  // 清空购物车
      showCheckout.value = false
      // 重置表单
      orderInfo.value = {
        receiverName: '',
        receiverPhone: '',
        receiverAddress: ''
      }
    } else {
      alert(res.message || '订单创建失败')
    }
  } catch (error) {
    console.error('提交订单失败:', error)
    alert('提交订单失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

// 登录成功后的处理
const handleLoginSuccess = (userData) => {
  showLoginModal.value = false
  // 刷新页面或触发父组件更新user
  window.location.reload()
}
</script>

<style scoped>
.cart-page {
  max-width: 1200px;
  margin: 0 auto;
}

.cart-page h2 {
  margin-bottom: 20px;
}

.empty-cart {
  text-align: center;
  padding: 80px 20px;
  background: white;
  border-radius: 12px;
}

.empty-icon {
  font-size: 80px;
  margin-bottom: 20px;
}

.empty-cart p {
  font-size: 18px;
  color: #999;
  margin-bottom: 20px;
}

.go-shop-btn {
  padding: 12px 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 25px;
  cursor: pointer;
}

.cart-content {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.cart-items {
  flex: 2;
  min-width: 300px;
  background: white;
  border-radius: 12px;
  overflow: hidden;
}

.cart-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.cart-item:last-child {
  border-bottom: none;
}

.item-image {
  width: 80px;
  height: 80px;
  background: #f8f9fa;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.item-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}

.item-info {
  flex: 1;
}

.item-info h3 {
  font-size: 16px;
  margin-bottom: 8px;
}

.item-price {
  color: #e74c3c;
  font-weight: bold;
}

.item-quantity {
  display: flex;
  align-items: center;
  gap: 10px;
}

.item-quantity button {
  width: 30px;
  height: 30px;
  background: #f0f0f0;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.item-quantity span {
  min-width: 30px;
  text-align: center;
}

.item-total {
  min-width: 100px;
  font-weight: bold;
  color: #e74c3c;
}

.remove-btn {
  padding: 6px 12px;
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.cart-summary {
  flex: 1;
  min-width: 280px;
  background: white;
  border-radius: 12px;
  padding: 20px;
  height: fit-content;
  position: sticky;
  top: 80px;
}

.cart-summary h3 {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  color: #666;
}

.summary-row.total {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #eee;
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.checkout-btn {
  width: 100%;
  padding: 14px;
  margin-top: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
}

.checkout-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 结算弹窗 */
.checkout-modal {
  width: 500px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.order-summary {
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  margin: 20px 0;
  text-align: center;
  font-weight: bold;
}

.action-buttons {
  display: flex;
  gap: 15px;
}

.cancel-order-btn,
.submit-order-btn {
  flex: 1;
  padding: 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.cancel-order-btn {
  background: #f0f0f0;
}

.submit-order-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
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

.close {
  font-size: 28px;
  cursor: pointer;
}
</style>