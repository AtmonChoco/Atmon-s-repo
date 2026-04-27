import request from '../utils/request'

// 用户注册
export const register = (data) => {
    return request.post('/user/register', data)
}

// 用户登录
export const login = (data) => {
    return request.post('/user/login', data)
}

// 获取用户信息
export const getUserInfo = (id) => {
    return request.get(`/user/${id}`)
}

// 更新个人信息
export const updateProfile = (data) => {
    return request.put('/user/profile', data)
}