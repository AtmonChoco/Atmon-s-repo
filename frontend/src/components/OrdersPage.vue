<template>
  <div class="orders-page">
    <h2>我的订单</h2>

    <div v-if="orders.length === 0" class="empty">
      暂无订单
    </div>

    <div v-else class="order-list">
      <div v-for="order in orders" :key="order.id" class="order-card">
        <div class="order-header">
          <span>订单号: {{ order.orderNo }}</span>
          <span class="status" :class="getStatusClass(order.status)">
            {{ getStatusText(order.status) }}
          </span>
        </div>

        <div class="order-body">
          <div class="order-info">
            <p>总金额: ¥{{ order.totalAmount }}</p >
            <p>下单时间: {{ formatDate(order.createTime) }}</p >
            <p>收货地址: {{ order.receiverAddress }}</p >
          </div>

          <div class="order-actions">
            <button
                v-if="order.status === 0"
                @click="handlePay(order.id)"
                class="pay-btn"
            >
              去支付
            </button>
            <button
                v-if="order.status === 0"
                @click="handleCancel(order.id)"
                class="cancel-btn"
            >
              取消订单
            </button>
            <button @click="viewItems(order.id)" class="detail-btn">
              查看详情
            </button>
          </div>
        </div>

        <!-- 订单明细 -->
        <div v-if="expandedOrder === order.id" class="order-items">
          <h4>商品清单</h4>
          <div v-for="item in orderItems" :key="item.id" class="order-item">
            <span>{{ item.figureName }}</span>
            <span>x{{ item.quantity }}</span>
            <span>¥{{ item.totalPrice }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUserOrders, payOrder, cancelOrder, getOrderItems } from '../api/order'

const props = defineProps({
  userId: Number
})

const orders = ref([])
const expandedOrder = ref(null)
const orderItems = ref([])

const loadOrders = async () => {
  try {
    const res = await getUserOrders(props.userId)
    orders.value = res.data || []
  } catch (error) {
    console.error('加载订单失败:', error)
  }
}

const handlePay = async (orderId) => {
  try {
    const res = await payOrder(orderId)
    if (res.code === 200) {
      alert('支付成功')
      loadOrders()
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('支付失败')
  }
}

const handleCancel = async (orderId) => {
  if (confirm('确定要取消这个订单吗？')) {
    try {
      const res = await cancelOrder(orderId)
      if (res.code === 200) {
        alert('订单已取消')
        loadOrders()
      } else {
        alert(res.message)
      }
    } catch (error) {
      alert('取消失败')
    }
  }
}

const viewItems = async (orderId) => {
  if (expandedOrder.value === orderId) {
    expandedOrder.value = null
  } else {
    try {
      const res = await getOrderItems(orderId)
      orderItems.value = res.data || []
      expandedOrder.value = orderId
    } catch (error) {
      console.error('加载明细失败:', error)
    }
  }
}

const getStatusText = (status) => {
  const statusMap = {
    0: '待付款', 1: '已付款', 2: '已发货', 3: '已完成', 4: '已取消'
  }
  return statusMap[status] || '未知'
}

const getStatusClass = (status) => {
  if (status === 0) return 'status-wait'
  if (status === 1) return 'status-paid'
  if (status === 4) return 'status-cancel'
  return ''
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString()
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.orders-page {
  max-width: 900px;
  margin: 0 auto;
}

.orders-page h2 {
  margin-bottom: 20px;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.order-header {
  display: flex;
  justify-content: space-between;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
  margin-bottom: 15px;
}

.order-body {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-info p {
  margin-bottom: 8px;
  color: #666;
}

.order-actions {
  display: flex;
  gap: 10px;
}

button {
  padding: 8px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.pay-btn {
  background: #27ae60;
  color: white;
}

.cancel-btn {
  background: #e74c3c;
  color: white;
}

.detail-btn {
  background: #3498db;
  color: white;
}

.status-wait {
  color: #e67e22;
}

.status-paid {
  color: #27ae60;
}

.status-cancel {
  color: #999;
}

.order-items {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.order-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.empty {
  text-align: center;
  padding: 50px;
  color: #999;
}
</style>