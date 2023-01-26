import request from '@/utils/request'

// 查询求文日志列表
export function listQiuwen_log(query) {
    return request({
        url: '/bot/qiuwen_log/list',
        method: 'get',
        params: query
    })
}

// 查询求文日志详细
export function getQiuwen_log(id) {
    return request({
        url: '/bot/qiuwen_log/' + id,
        method: 'get'
    })
}

// 新增求文日志
export function addQiuwen_log(data) {
    return request({
        url: '/bot/qiuwen_log',
        method: 'post',
        data: data
    })
}

// 修改求文日志
export function updateQiuwen_log(data) {
    return request({
        url: '/bot/qiuwen_log',
        method: 'put',
        data: data
    })
}

// 删除求文日志
export function delQiuwen_log(id) {
    return request({
        url: '/bot/qiuwen_log/' + id,
        method: 'delete'
    })
}
