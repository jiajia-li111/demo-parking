import axios from 'axios'
import { setupMockIfNeeded } from './mock'


const useMock = import.meta.env.VITE_USE_MOCK === 'true'
const baseURL = import.meta.env.VITE_API_BASE || '/api'


if (useMock) setupMockIfNeeded()


export const http = axios.create({ baseURL })


http.interceptors.request.use((config) => {
// 可在此注入 token
return config
})


http.interceptors.response.use(
(res) => res,
(err) => {
console.error('API Error:', err?.response || err)
return Promise.reject(err)
}
)