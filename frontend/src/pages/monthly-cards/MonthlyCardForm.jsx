import { Modal, Form, Input, DatePicker, message } from "antd";
import { useEffect } from "react";
import dayjs from "dayjs";

export default function MonthlyCardForm({
  open,
  mode,              // "create" | "edit"
  initialValues,     // 编辑时传入的行数据
  onOk,
  onCancel,
}) {
  const [form] = Form.useForm();
  const isEdit = mode === "edit";

  /** 弹窗打开时处理表单回填 */
  useEffect(() => {
    if (!open) return;

    if (isEdit && initialValues) {
      form.setFieldsValue({
        licensePlate: initialValues.licensePlate,
        endDate: dayjs(initialValues.endDate),
      });
    } else {
      form.resetFields();
    }
  }, [open, isEdit, initialValues]);

  return (
    <Modal
      open={open}
      title={isEdit ? "编辑月卡" : "新增月卡"}
      okText="保存"
      cancelText="取消"
      destroyOnClose
      onCancel={onCancel}
      onOk={async () => {
        try {
          const values = await form.validateFields();
          await onOk(values);      // 👉 只把表单值交给父组件
          form.resetFields();
        } catch (e) {
          message.error("保存失败，请检查输入");
          console.error(e);
        }
      }}
    >
      <Form form={form} layout="vertical">
        <Form.Item
          label="车牌号"
          name="licensePlate"
          rules={[
            { required: true, message: "请输入车牌号" },
            { pattern: /^[\u4e00-\u9fa5][A-Z][A-Z0-9]{5}$/, message: "车牌格式不正确" },
          ]}
        >
          <Input placeholder="例如：粤A12345" />
        </Form.Item>

        <Form.Item
          label="到期时间"
          name="endDate"
          rules={[{ required: true, message: "请选择到期时间" }]}
        >
          <DatePicker
            showTime
            style={{ width: "100%" }}
            format="YYYY-MM-DD HH:mm:ss"
            disabledDate={(current) =>
              current && current < dayjs().startOf("day")
            }
          />
        </Form.Item>
      </Form>
    </Modal>
  );
}



