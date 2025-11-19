import { Form, Input } from 'antd'


export default function OwnerForm({ form, initialValues }) {
return (
<Form form={form} layout="vertical" initialValues={initialValues}>
<Form.Item label="业主ID" name="owner_id" rules={[{ required: true }]}>
<Input placeholder="如 o202401" />
</Form.Item>
<Form.Item label="姓名" name="name" rules={[{ required: true }]}>
<Input />
</Form.Item>
<Form.Item label="房号" name="room_no" rules={[{ required: true }]}>
<Input placeholder="如 3栋2单元501" />
</Form.Item>
<Form.Item label="联系电话" name="phone" rules={[{ required: true }]}>
<Input placeholder="如 13800138000" />
</Form.Item>
</Form>
)
}