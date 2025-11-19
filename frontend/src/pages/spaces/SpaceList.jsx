import { useEffect, useState } from 'react'
import { message, Tag } from 'antd'
import GenericTable from '../../components/tables/GenericTable'
import { ENDPOINTS } from '../../api/endpoints'
import { SPACE_STATUS } from '../../constants/enums'


export default function SpaceList() {
const [data, setData] = useState([])
const [loading, setLoading] = useState(false)


useEffect(() => {
(async () => {
setLoading(true)
try {
const res = await fetch(ENDPOINTS.spaces)
const json = await res.json()
setData(json.list || [])
} catch(e) { message.error('获取车位列表失败') }
finally { setLoading(false) }
})()
}, [])


const columns = [
{ title: '车位ID', dataIndex: 'space_id' },
{ title: '楼层ID', dataIndex: 'floor_id' },
{ title: '编号', dataIndex: 'space_no' },
{ title: '状态', dataIndex: 'status', render: (v)=> <Tag>{SPACE_STATUS[v] || v}</Tag> },
{ title: '是否固定', dataIndex: 'is_fixed' },
{ title: '业主ID', dataIndex: 'owner_id' },
]


return <GenericTable rowKey="space_id" columns={columns} data={data} loading={loading} />
}