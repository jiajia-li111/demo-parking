import { Layout, Menu } from 'antd'
import { CarOutlined, UserOutlined, AppstoreOutlined, DatabaseOutlined, ScheduleOutlined, PayCircleOutlined, RadarChartOutlined, DashboardOutlined } from '@ant-design/icons'
import { useNavigate, useLocation } from 'react-router-dom'


const { Header, Sider, Content } = Layout


const items = [
{ key: '/dashboard', icon: <DashboardOutlined />, label: '总览' },
{ key: '/vehicles', icon: <CarOutlined />, label: '车辆' },
{ key: '/owners', icon: <UserOutlined />, label: '业主' },
{ key: '/spaces', icon: <AppstoreOutlined />, label: '车位' },
{ key: '/sessions', icon: <ScheduleOutlined />, label: '停车会话' },
{ key: '/payments', icon: <PayCircleOutlined />, label: '支付记录' },
{ key: '/rules', icon: <RadarChartOutlined />, label: '收费规则' },
]


export default function AppLayout({ children }) {
const nav = useNavigate()
const { pathname } = useLocation()


return (
<Layout style={{ minHeight: '100vh' }}>
<Sider theme="light">
<div style={{ padding: 16, fontWeight: 700 }}>Parking Admin</div>
<Menu
mode="inline"
selectedKeys={[pathname]}
onClick={({ key }) => nav(key)}
items={items}
/>
</Sider>
<Layout>
<Header style={{ background: '#fff', paddingLeft: 16 }}>停车场管理系统</Header>
<Content style={{ margin: 16 }}>
<div style={{ background: '#fff', padding: 16, minHeight: 360 }}>{children}</div>
</Content>
</Layout>
</Layout>
)
}