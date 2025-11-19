// 统一管理接口路径，便于替换成你的后端路由
export const ENDPOINTS = {
vehicles: '/vehicles', // GET / POST
vehicleById: (id) => `/vehicles/${id}`, // GET / PUT / DELETE


owners: '/owners',
ownerById: (id) => `/owners/${id}`,


spaces: '/spaces',
spaceById: (id) => `/spaces/${id}`,


sessions: '/sessions',
sessionById: (id) => `/sessions/${id}`,


payments: '/payments',
rules: '/fee-rules',
}