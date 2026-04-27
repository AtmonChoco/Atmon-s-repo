import request from '../utils/request'

// 创建订单
export const createOrder = (data) => {
    return request.post('/order/create', data)
}

// 支付订单
export const payOrder = (orderId) => {
    return request.put(`/order/pay/${orderId}`)
}

// 取消订单
export const cancelOrder = (orderId) => {
    return request.put(`/order/cancel/${orderId}`)
}

// 获取用户订单
export const getUserOrders = (userId) => {
    return request.get(`/order/user/${userId}`)
}

// 获取订单详情
export const getOrderDetail = (orderId) => {
    return request.get(`/order/${orderId}`)
}

// 获取订单明细
export const getOrderItems = (orderId) => {
    return request.get(`/order/items/${orderId}`)
}