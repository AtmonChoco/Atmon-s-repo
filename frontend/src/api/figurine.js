import request from '../utils/request'

// 获取在售手办列表
export const getFigureList = () => {
    return request.get('/figure/list')
}

// 获取手办详情
export const getFigureDetail = (id) => {
    return request.get(`/figure/${id}`)
}

// 搜索手办
export const searchFigure = (keyword) => {
    return request.get('/figure/search', { params: { keyword } })
}

// 获取分类手办
export const getFigureByCategory = (category) => {
    return request.get(`/figure/category/${category}`)
}