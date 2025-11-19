import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Descriptions } from 'antd'
import { ENDPOINTS } from '../../api/endpoints'


export default function VehicleDetail() {
const { vehicle_id } = useParams()
const [data, setData] = useState(null)


useEffect(() => {
(async () => {
const res = await fetch(ENDPOINTS.vehicleById(vehicle_id))
const json = await res.json()
setData(json)
})()
}, [vehicle_id])


if (!data) return null


return (
<Descriptions title="车辆详情" bordered column={1}>
<Descriptions.Item label="车辆ID">{data.vehicle_id}</Descriptions.Item>
<Descriptions.Item label="车牌号">{data.license_plate}</Descriptions.Item>
<Descriptions.Item label="车型">{data.vehicle_type}</Descriptions.Item>
<Descriptions.Item label="是否业主车">{data.is_owner_car}</Descriptions.Item>
<Descriptions.Item label="业主ID">{data.owner_id || '-'}</Descriptions.Item>
</Descriptions>
)
}