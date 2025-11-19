import { Card, Statistic, Row, Col } from 'antd'


export default function Dashboard() {
return (
<Row gutter={16}>
<Col span={6}><Card><Statistic title="车辆数" value={2} /></Card></Col>
<Col span={6}><Card><Statistic title="业主数" value={1} /></Card></Col>
<Col span={6}><Card><Statistic title="车位数" value={2} /></Card></Col>
<Col span={6}><Card><Statistic title="进行中会话" value={0} /></Card></Col>
</Row>
)
}