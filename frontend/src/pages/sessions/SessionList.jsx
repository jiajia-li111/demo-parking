import { useEffect, useState } from 'react'
import { Empty } from 'antd'


export default function SessionList() {
const [loading] = useState(false)
// 这里留空位：等你后端就绪再接
return <Empty description="请接入后端 /sessions 接口后显示数据" />
}