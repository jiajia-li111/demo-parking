import { Form, Input, Select, Switch } from 'antd'
import { VEHICLE_TYPE, BOOLEAN_YN } from '../../constants/enums'


export default function VehicleForm({ form, initialValues }) {
return (
<Form form={form} layout="vertical" initialValues={initialValues}>
<Form.Item label="车辆ID" name="vehicle_id" rules={[{ required: true, message: '必填' }]}>
<Input placeholder="如 v2024001" />
</Form.Item>
<Form.Item label="车牌号" name="license_plate" rules={[{ required: true }]}>
<Input placeholder="如 粤A12345" />
</Form.Item>
<Form.Item label="车型" name="vehicle_type" rules={[{ required: true }]}>
<Select options={Object.entries(VEHICLE_TYPE).map(([value,label])=>({value,label}))} />
</Form.Item>
<Form.Item label="是否业主车" name="is_owner_car" rules={[{ required: true }]}>
<Select options={Object.entries(BOOLEAN_YN).map(([value,label])=>({value,label}))} />
</Form.Item>
<Form.Item label="关联业主ID" name="owner_id">
<Input placeholder="如 o202401（业主车须填写）" />
</Form.Item>
</Form>
)
}