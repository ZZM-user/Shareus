import request from '@/utils/request'

// 查询QQ群组列表
export function listGroup(query) {
    return request({
        url: '/bot/group/list',
        method: 'get',
        params: query
    })
}

// 查询QQ群组详细
export function getGroup(id) {
    return request({
        url: '/bot/group/' + id,
        method: 'get'
    })
}

// 新增QQ群组
export function addGroup(data) {
    return request({
        url: '/bot/group',
        method: 'post',
        data: data
    })
}

// 修改QQ群组
export function updateGroup(data) {
    return request({
        url: '/bot/group',
        method: 'put',
        data: data
    })
}

// 删除QQ群组
export function delGroup(id) {
    return request({
        url: '/bot/group/' + id,
        method: 'delete'
    })
}
