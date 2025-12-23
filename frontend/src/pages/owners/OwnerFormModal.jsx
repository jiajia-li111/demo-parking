import { Modal, Form, Input } from "antd";
import { useEffect } from "react";

export default function OwnerFormModal({ open, onCancel, onOk, initialValues }) {
  const [form] = Form.useForm();

  useEffect(() => {
    if (open) {
      form.setFieldsValue(
        initialValues || { ownerId: undefined, name: "", roomNo: "", phone: "" }
      );
    }
  }, [open, initialValues, form]);

  return (
    <Modal
      title={initialValues ? "编辑业主" : "新增业主"}
      open={open}
      onCancel={() => {
        form.resetFields();
        onCancel?.();
      }}
      onOk={() => {
        form
          .validateFields()
          .then((values) => {
            onOk?.(values);
          })
          .catch(() => {});
      }}
      destroyOnClose
    >
      <Form form={form} layout="vertical">
        <Form.Item label="业主ID" name="ownerId" tooltip="不填则按后端策略生成">
          <Input placeholder="可留空，由后端生成" />
        </Form.Item>
        <Form.Item
          label="姓名"
          name="name"
          rules={[{ required: true, message: "请输入业主姓名" }]}
        >
          <Input placeholder="请输入业主姓名" />
        </Form.Item>
        <Form.Item
          label="房号"
          name="roomNo"
          rules={[{ required: true, message: "请输入房号" }]}
        >
          <Input placeholder="如 3栋2单元501" />
        </Form.Item>
        <Form.Item
          label="联系电话"
          name="phone"
          rules={[{ required: true, message: "请输入联系电话" }]}
        >
          <Input placeholder="请输入联系电话" />
        </Form.Item>
      </Form>
    </Modal>
  );
}
