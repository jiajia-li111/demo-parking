import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'


export default defineConfig({
plugins: [react()],
server: {
port: 5173,
proxy: {
// 当 VITE_USE_MOCK=false 且本地后端在 5000 端口时，代理 /api
'/api': {
target: process.env.VITE_API_BASE || 'http://127.0.0.1:5000',
changeOrigin: true,
},
},
},
})
