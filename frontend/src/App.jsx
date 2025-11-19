import { ConfigProvider } from 'antd'
import zhCN from 'antd/locale/zh_CN'
import AppLayout from './components/Layout/AppLayout'
import AppRouter from './router'


export default function App() {
return (
<ConfigProvider locale={zhCN}>
<AppLayout>
<AppRouter />
</AppLayout>
</ConfigProvider>
)
}
