import { useEffect, useState } from 'react'
import { message } from 'antd'
import GenericTable from '../../components/tables/GenericTable'
import { ENDPOINTS } from '../../api/endpoints'


export default function OwnerList() {
const [data, setData] = useState([])
const [loading, setLoading] = useState(false)


useEffect(() => {
(async () => {
setLoading(true)
try {
const res = await fetch(ENDPOINTS.owners)
const json = await res.json()
setData(json.list || [])
} catch(e) { message.error('获取业主列表失败') }
finally { setLoading(false) }
})()
}, [])


const columns = [
{ title: '业主ID', dataIndex: 'owner_id' },
{ title: '姓名', dataIndex: 'name' },
{ title: '房号', dataIndex: 'room_no' },
{ title: '联系电话', dataIndex: 'phone' },
]


return <GenericTable rowKey="owner_id" columns={columns} data={data} loading={loading} />
}