import { Form, Input, Select } from 'antd'
import { SPACE_STATUS, BOOLEAN_YN } from '../../constants/enums'


export default function SpaceForm({ form, initialValues }) {
return (
<Form form={form} layout="vertical" initialValues={initialValues}>
<Form.Item label="车位ID" name="space_id" rules={[{ required: true }]}>
<Input placeholder="如 b1_001" />
</Form.Item>
<Form.Item label="所属楼层ID" name="floor_id" rules={[{ required: true }]}>
<Input placeholder="如 b1" />
</Form.Item>
<Form.Item label="车位编号" name="space_no" rules={[{ required: true }]}>
<Input placeholder="如 001" />
</Form.Item>
<Form.Item label="状态" name="status" rules={[{ required: true }]}>
<Select options={Object.entries(SPACE_STATUS).map(([value,label])=>({value,label}))} />
</Form.Item>
<Form.Item label="是否固定车位" name="is_fixed" rules={[{ required: true }]}>
<Select options={Object.entries(BOOLEAN_YN).map(([value,label])=>({value,label}))} />
</Form.Item>
<Form.Item label="关联业主ID" name="owner_id">
<Input placeholder="固定车位需填写，如 o202401" />
</Form.Item>
</Form>
)
}