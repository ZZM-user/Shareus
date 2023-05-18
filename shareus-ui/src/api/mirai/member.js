import request from '@/utils/request'

// 查询QQ成员列表
export function listMember(query) {
    return request({
        url: '/bot-web/member/list',
        method: 'get',
        params: query
    })
}

// 查询QQ成员详细
export function getMember(id) {
    return request({
        url: '/bot-web/member/' + id,
        method: 'get'
    })
}

// 新增QQ成员
export function addMember(data) {
    return request({
        url: '/bot-web/member',
        method: 'post',
        data: data
    })
}

// 修改QQ成员
export function updateMember(data) {
    return request({
        url: '/bot-web/member',
        method: 'put',
        data: data
    })
}

// 删除QQ成员
export function delMember(id) {
    return request({
        url: '/bot-web/member/' + id,
        method: 'delete'
    })
}
