import { Modal, Form, Input, message } from "antd";
import { updatePassword } from "../api/users";

export default function ChangePasswordModal({ open, onClose }) {
  const [form] = Form.useForm();

  const handleOk = async () => {
    try {
      const { oldPassword, newPassword } = await form.validateFields();

      const userId = localStorage.getItem("userId");
      if (!userId) {
        message.error("用户信息缺失，请重新登录");
        return;
      }

      await updatePassword({
        userId,
        oldPassword,
        newPassword,
      });

      message.success("密码修改成功，请重新登录");
      localStorage.clear();
      window.location.href = "/login";
    } catch (e) {}
  };

  return (
    <Modal
      title="修改密码"
      open={open}
      onOk={handleOk}
      onCancel={onClose}
      okText="确认"
      cancelText="取消"
      destroyOnClose
    >
      <Form form={form} layout="vertical">
        <Form.Item
          name="oldPassword"
          label="原密码"
          rules={[{ required: true, message: "请输入原密码" }]}
        >
          <Input.Password />
        </Form.Item>

        <Form.Item
          name="newPassword"
          label="新密码"
          rules={[
            { required: true, message: "请输入新密码" },
            { min: 6, message: "新密码至少 6 位" },
          ]}
        >
          <Input.Password />
        </Form.Item>
      </Form>
    </Modal>
  );
}
