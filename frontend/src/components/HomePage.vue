<template>
  <div class="home-page">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <input
          type="text"
          v-model="keyword"
          placeholder="搜索手办..."
          @keyup.enter="search"
      />
      <button @click="search">搜索</button>
    </div>

    <!-- 分类筛选 -->
    <div class="categories">
      <span
          v-for="cat in categories"
          :key="cat"
          @click="currentCategory = cat"
          :class="{ active: currentCategory === cat }"
      >
        {{ cat }}
      </span>
    </div>

    <!-- 商品列表 -->
    <div class="figure-grid">
      <div v-for="figure in figures" :key="figure.id" class="figure-card">
        <div class="figure-image" @click="viewDetail(figure.id)">
          <img :src="figure.imageUrl || '/placeholder.png'" :alt="figure.name" />
        </div>
        <div class="figure-info">
          <h3 @click="viewDetail(figure.id)">{{ figure.name }}</h3>
          <p class="price">¥{{ figure.price }}</p >
          <p class="stock" :class="{ low: figure.stock < 10 }">
            库存: {{ figure.stock }}
          </p >
          <div class="buy-section">
            <input type="number" v-model.number="quantities[figure.id]" min="1" :max="figure.stock" />
            <button @click="addToCart(figure)" :disabled="figure.stock === 0">
              {{ figure.stock === 0 ? '缺货' : '加入购物车' }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-if="figures.length === 0" class="empty">
      暂无手办商品
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { getFigureList, searchFigure, getFigureByCategory } from '../api/figurine'

const emit = defineEmits(['add-to-cart', 'view-detail'])

const figures = ref([])
const keyword = ref('')
const currentCategory = ref('全部')
const quantities = ref({})

const categories = ['全部', '火影忍者', '海贼王', '鬼灭之刃', 'EVA', '龙珠']

const loadFigures = async () => {
  try {
    let res
    if (keyword.value) {
      res = await searchFigure(keyword.value)
    } else if (currentCategory.value !== '全部') {
      res = await getFigureByCategory(currentCategory.value)
    } else {
      res = await getFigureList()
    }
    figures.value = res.data || []
    // 初始化数量
    figures.value.forEach(f => {
      if (!quantities.value[f.id]) {
        quantities.value[f.id] = 1
      }
    })
  } catch (error) {
    console.error('加载失败:', error)
  }
}

const search = () => {
  loadFigures()
}

const addToCart = (figure) => {
  const quantity = quantities.value[figure.id] || 1
  emit('add-to-cart', { ...figure, quantity })
}

const viewDetail = (id) => {
  emit('view-detail', id)
}

watch(currentCategory, () => {
  keyword.value = ''
  loadFigures()
})

onMounted(() => {
  loadFigures()
})
</script>

<style scoped>
.home-page {
  max-width: 1200px;
  margin: 0 auto;
}

.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 30px;
}

.search-bar input {
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ddd;
  border-radius: 25px;
  font-size: 16px;
}

.search-bar button {
  padding: 12px 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 25px;
  cursor: pointer;
}

.categories {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.categories span {
  padding: 8px 20px;
  background: white;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s;
}

.categories span:hover,
.categories span.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.figure-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.figure-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  transition: transform 0.3s;
}

.figure-card:hover {
  transform: translateY(-5px);
}

.figure-image {
  height: 250px;
  background: #f0f0f0;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.figure-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}

.figure-info {
  padding: 15px;
}

.figure-info h3 {
  font-size: 16px;
  margin-bottom: 10px;
  cursor: pointer;
}

.figure-info h3:hover {
  color: #667eea;
}

.price {
  font-size: 20px;
  font-weight: bold;
  color: #e74c3c;
  margin-bottom: 5px;
}

.stock {
  font-size: 12px;
  color: #27ae60;
  margin-bottom: 10px;
}

.stock.low {
  color: #e67e22;
}

.buy-section {
  display: flex;
  gap: 10px;
  align-items: center;
}

.buy-section input {
  width: 60px;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 5px;
  text-align: center;
}

.buy-section button {
  flex: 1;
  padding: 8px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.buy-section button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.empty {
  text-align: center;
  padding: 50px;
  color: #999;
}
</style>