import request from '@/utils/request'

// 查询归档文件列表
export function listArchived_file(query) {
    return request({
        url: '/mirai/archived_file/list',
        method: 'get',
        params: query
    })
}

// 查询归档文件详细
export function getArchived_file(id) {
    return request({
        url: '/mirai/archived_file/' + id,
        method: 'get'
    })
}

// 新增归档文件
export function addArchived_file(data) {
    return request({
        url: '/mirai/archived_file',
        method: 'post',
        data: data
    })
}

// 修改归档文件
export function updateArchived_file(data) {
    return request({
        url: '/mirai/archived_file',
        method: 'put',
        data: data
    })
}

// 删除归档文件
export function delArchived_file(id) {
    return request({
        url: '/mirai/archived_file/' + id,
        method: 'delete'
    })
}

// 删除归档文件
export function changeArchived_file_enabled(id, enabled) {
    return request({
        url: '/mirai/archived_file/changeStatus',
        method: 'post',
        data: {stringIds: [id], status: enabled}
    })
}
