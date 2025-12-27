import { Modal, Form, Input, Select, message } from "antd";
import { createUser } from "../../api/users";

export default function CreateUserModal({ open, onClose, onSuccess }) {
  const [form] = Form.useForm();

  const handleOk = async () => {
    try {
      const values = await form.validateFields();

      await createUser({
        ...values,
        status: "01", // 默认启用
      });

      message.success("用户创建成功");
      form.resetFields();
      onSuccess();
    } catch (err) {
      if (err?.errorFields) return;
      message.error("创建用户失败");
    }
  };

  return (
    <Modal
      title="新建用户"
      open={open}
      onOk={handleOk}
      onCancel={onClose}
      destroyOnClose
    >
      <Form form={form} layout="vertical">
       

        <Form.Item
          label="用户名"
          name="username"
          rules={[{ required: true, message: "请输入用户名" }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="密码"
          name="password"
          rules={[{ required: true, message: "请输入初始密码" }]}
        >
          <Input.Password />
        </Form.Item>

        <Form.Item
          label="角色"
          name="roleId"
          rules={[{ required: true, message: "请选择角色" }]}
        >
          <Select
            options={[
              { value: "r001", label: "超级管理员" },
              { value: "r002", label: "操作员" },
              { value: "r003", label: "财务" },
            ]}
          />
        </Form.Item>

        <Form.Item
          label="部门"
          name="department"
          rules={[{ required: true, message: "请输入部门" }]}
        >
          <Input placeholder="管理中心" />
        </Form.Item>
      </Form>
    </Modal>
  );
}
