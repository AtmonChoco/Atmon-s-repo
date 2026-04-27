<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal detail-modal">
      <div class="modal-header">
        <h2>商品详情</h2>
        <span class="close" @click="$emit('close')">&times;</span>
      </div>

      <div v-if="loading" class="loading">
        加载中...
      </div>

      <div v-else-if="figure" class="modal-body">
        <div class="detail-content">
          <!-- 图片区域 -->
          <div class="detail-image">
            <img :src="figure.imageUrl || '/placeholder.png'" :alt="figure.name" />
          </div>

          <!-- 信息区域 -->
          <div class="detail-info">
            <h1 class="figure-name">{{ figure.name }}</h1>

            <div class="price-section">
              <span class="label">价格：</span>
              <span class="price">¥{{ figure.price }}</span>
            </div>

            <div class="stock-section">
              <span class="label">库存：</span>
              <span class="stock" :class="{ low: figure.stock < 10 }">
                {{ figure.stock }} 件
              </span>
            </div>

            <div class="brand-section">
              <span class="label">品牌：</span>
              <span>{{ figure.brand || '官方正版' }}</span>
            </div>

            <div class="category-section">
              <span class="label">分类：</span>
              <span class="category-tag">{{ figure.category }}</span>
            </div>

            <div class="description-section">
              <span class="label">商品描述：</span>
              <p class="description">{{ figure.description || '暂无描述' }}</p >
            </div>

            <!-- 购买区域 -->
            <div class="buy-section">
              <div class="quantity-selector">
                <span class="label">数量：</span>
                <button @click="decreaseQuantity" :disabled="quantity <= 1">-</button>
                <input
                    type="number"
                    v-model.number="quantity"
                    min="1"
                    :max="figure.stock"
                    @change="validateQuantity"
                />
                <button @click="increaseQuantity" :disabled="quantity >= figure.stock">+</button>
              </div>

              <div class="total-price">
                <span class="label">合计：</span>
                <span class="total">¥{{ (figure.price * quantity).toFixed(2) }}</span>
              </div>

              <div class="action-buttons">
                <button
                    class="add-cart-btn"
                    @click="addToCart"
                    :disabled="figure.stock === 0"
                >
                  🛒 加入购物车
                </button>
                <button
                    class="buy-now-btn"
                    @click="buyNow"
                    :disabled="figure.stock === 0"
                >
                  立即购买
                </button>
              </div>

              <div v-if="figure.stock === 0" class="out-of-stock">
                该商品已售罄
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="error">
        加载失败，请稍后重试
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { getFigureDetail } from '../api/figurine'

const props = defineProps({
  figureId: Number
})

const emit = defineEmits(['close', 'add-to-cart'])

const figure = ref(null)
const loading = ref(true)
const quantity = ref(1)

// 加载手办详情
const loadFigureDetail = async () => {
  loading.value = true
  try {
    const res = await getFigureDetail(props.figureId)
    if (res.code === 200) {
      figure.value = res.data
      quantity.value = 1
    } else {
      console.error('加载失败:', res.message)
    }
  } catch (error) {
    console.error('请求失败:', error)
  } finally {
    loading.value = false
  }
}

// 减少数量
const decreaseQuantity = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

// 增加数量
const increaseQuantity = () => {
  if (figure.value && quantity.value < figure.value.stock) {
    quantity.value++
  }
}

// 验证数量
const validateQuantity = () => {
  if (quantity.value < 1) {
    quantity.value = 1
  }
  if (figure.value && quantity.value > figure.value.stock) {
    quantity.value = figure.value.stock
  }
}

// 加入购物车
const addToCart = () => {
  if (!figure.value) return
  if (figure.value.stock === 0) {
    alert('该商品已售罄')
    return
  }

  emit('add-to-cart', {
    id: figure.value.id,
    name: figure.value.name,
    price: figure.value.price,
    imageUrl: figure.value.imageUrl,
    quantity: quantity.value
  })

  alert(`已添加 ${quantity.value} 件 "${figure.value.name}" 到购物车`)
  emit('close')
}

// 立即购买
const buyNow = () => {
  if (!figure.value) return
  if (figure.value.stock === 0) {
    alert('该商品已售罄')
    return
  }

  // 添加到购物车后跳转到购物车页面
  emit('add-to-cart', {
    id: figure.value.id,
    name: figure.value.name,
    price: figure.value.price,
    imageUrl: figure.value.imageUrl,
    quantity: quantity.value
  })

  alert('已添加到购物车，请去购物车结算')
  emit('close')
}

// 监听figureId变化，重新加载
watch(() => props.figureId, () => {
  if (props.figureId) {
    loadFigureDetail()
  }
}, { immediate: true })
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.detail-modal {
  background: white;
  border-radius: 16px;
  width: 900px;
  max-width: 90%;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #eee;
}

.modal-header h2 {
  margin: 0;
  font-size: 20px;
}

.close {
  font-size: 28px;
  cursor: pointer;
  color: #999;
  transition: color 0.3s;
}

.close:hover {
  color: #333;
}

.loading, .error {
  text-align: center;
  padding: 60px;
  color: #999;
}

.modal-body {
  padding: 24px;
}

.detail-content {
  display: flex;
  gap: 30px;
  flex-wrap: wrap;
}

.detail-image {
  flex: 1;
  min-width: 250px;
  background: #f8f9fa;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.detail-image img {
  max-width: 100%;
  max-height: 400px;
  object-fit: contain;
}

.detail-info {
  flex: 1;
  min-width: 280px;
}

.figure-name {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.price-section,
.stock-section,
.brand-section,
.category-section {
  margin-bottom: 16px;
  display: flex;
  align-items: baseline;
}

.label {
  width: 70px;
  color: #666;
  font-size: 14px;
}

.price {
  font-size: 28px;
  font-weight: bold;
  color: #e74c3c;
}

.stock {
  color: #27ae60;
  font-weight: 500;
}

.stock.low {
  color: #e67e22;
}

.category-tag {
  display: inline-block;
  padding: 4px 12px;
  background: #f0f0f0;
  border-radius: 20px;
  font-size: 13px;
}

.description-section {
  margin: 20px 0;
}

.description {
  margin-top: 8px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 8px;
  line-height: 1.6;
  color: #555;
}

.buy-section {
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.quantity-selector {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.quantity-selector button {
  width: 32px;
  height: 32px;
  background: #f0f0f0;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 18px;
  transition: background 0.3s;
}

.quantity-selector button:hover:not(:disabled) {
  background: #ddd;
}

.quantity-selector button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.quantity-selector input {
  width: 60px;
  height: 32px;
  text-align: center;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

/* 隐藏number输入框的上下箭头 */
.quantity-selector input::-webkit-inner-spin-button,
.quantity-selector input::-webkit-outer-spin-button {
  opacity: 1;
}

.total-price {
  display: flex;
  align-items: baseline;
  gap: 10px;
  margin-bottom: 20px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 8px;
}

.total {
  font-size: 24px;
  font-weight: bold;
  color: #e74c3c;
}

.action-buttons {
  display: flex;
  gap: 15px;
}

.add-cart-btn,
.buy-now-btn {
  flex: 1;
  padding: 14px;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  transition: transform 0.2s, opacity 0.3s;
}

.add-cart-btn {
  background: #fff;
  border: 2px solid #667eea;
  color: #667eea;
}

.add-cart-btn:hover:not(:disabled) {
  background: #667eea;
  color: white;
  transform: translateY(-2px);
}

.buy-now-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.buy-now-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  opacity: 0.9;
}

.add-cart-btn:disabled,
.buy-now-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

.out-of-stock {
  margin-top: 15px;
  padding: 12px;
  background: #ffeaa7;
  color: #d63031;
  text-align: center;
  border-radius: 8px;
  font-weight: 500;
}
</style>