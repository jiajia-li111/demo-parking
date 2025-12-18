import { Modal, Form, Input, message } from "antd";
import { updatePassword } from "../api/users";

export default function ChangePasswordModal({ open, onClose }) {
  const [form] = Form.useForm();

  const handleOk = async () => {
    const { oldPassword, newPassword } = await form.validateFields();

    const userId = localStorage.getItem("userId");

    await updatePassword({
      userId,
      oldPassword,
      newPassword,
    });

    message.success("密码修改成功，请重新登录");
    localStorage.clear();
    window.location.href = "/login";
  };

  return (
    <Modal
      title="修改密码"
      open={open}
      onOk={handleOk}
      onCancel={onClose}
      okText="确认"
      cancelText="取消"
    >
      <Form form={form} layout="vertical">
        <Form.Item
          name="oldPassword"
          label="原密码"
          rules={[{ required: true }]}
        >
          <Input.Password />
        </Form.Item>

        <Form.Item
          name="newPassword"
          label="新密码"
          rules={[{ required: true, min: 6 }]}
        >
          <Input.Password />
        </Form.Item>
      </Form>
    </Modal>
  );
}
