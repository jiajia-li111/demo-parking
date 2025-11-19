// 简易Mock（仅示例CRUD），可以替换为MSW或Mock Service Worker
let inited = false


export function setupMockIfNeeded() {
if (inited) return
inited = true


const store = {
vehicles: [
{ vehicle_id: 'v2024001', license_plate: '粤A12345', vehicle_type: '01', is_owner_car: '01', owner_id: 'o202401' },
{ vehicle_id: 'v2024002', license_plate: '粤B88888', vehicle_type: '02', is_owner_car: '02', owner_id: null },
],
owners: [
{ owner_id: 'o202401', name: '张三', room_no: '3栋2单元501', phone: '13800138000' },
],
spaces: [
{ space_id: 'b1_001', floor_id: 'b1', space_no: '001', status: '01', is_fixed: '01', owner_id: 'o202401' },
{ space_id: 'b1_002', floor_id: 'b1', space_no: '002', status: '01', is_fixed: '02', owner_id: null },
],
}


// 覆盖 fetch 用于 demo（也可用 axios-mock-adapter）
const origFetch = window.fetch
window.fetch = async (input, init = {}) => {
const url = typeof input === 'string' ? input : input.url
const method = (init.method || 'GET').toUpperCase()


// 车辆
if (url.endsWith('/vehicles')) {
if (method === 'GET') {
return resp(200, { list: store.vehicles, total: store.vehicles.length })
}
if (method === 'POST') {
const body = JSON.parse(init.body || '{}')
store.vehicles.unshift(body)
return resp(201, body)
}
}
if (/\/vehicles\/.+/.test(url)) {
const id = url.split('/').pop()
const idx = store.vehicles.findIndex(v => v.vehicle_id === id)
if (method === 'GET') return resp(200, store.vehicles[idx])
if (method === 'PUT') {
const body = JSON.parse(init.body || '{}')
store.vehicles[idx] = { ...store.vehicles[idx], ...body }
return resp(200, store.vehicles[idx])
}
if (method === 'DELETE') {
store.vehicles.splice(idx, 1)
return resp(204)
}
}


// 业主
if (url.endsWith('/owners') && method === 'GET') {
return resp(200, { list: store.owners, total: store.owners.length })
}


// 车位
if (url.endsWith('/spaces') && method === 'GET') {
return resp(200, { list: store.spaces, total: store.spaces.length })
}


// 其它交给原始 fetch（用于代理到真实后端）
return origFetch(input, init)
}
}