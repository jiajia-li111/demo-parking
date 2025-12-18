import { Modal, Form, Input, DatePicker, Select } from "antd";
import dayjs from "dayjs";

export default function MonthlyCardForm({
  open,
  mode,          // "create" | "edit"
  initialValues,
  onOk,
  onCancel,
}) {
  const [form] = Form.useForm();

  const isEdit = mode === "edit";

  return (
    <Modal
      open={open}
      title={isEdit ? "编辑月卡" : "新增月卡"}
      okText="保存"
      cancelText="取消"
      destroyOnClose
      onCancel={onCancel}
      onOk={async () => {
        const values = await form.validateFields();
        onOk(values);
      }}
    >
      <Form
        form={form}
        layout="vertical"
        initialValues={
          initialValues
            ? {
                ...initialValues,
                startDate: initialValues.startDate
                  ? dayjs(initialValues.startDate)
                  : null,
                endDate: initialValues.endDate
                  ? dayjs(initialValues.endDate)
                  : null,
              }
            : {}
        }
      >
        <Form.Item
          label="月卡ID"
          name="cardId"
          rules={[{ required: true, message: "请输入月卡ID" }]}
        >
          <Input disabled={isEdit} />
        </Form.Item>

        <Form.Item
          label="车辆ID"
          name="vehicleId"
          rules={[{ required: true, message: "请输入车辆ID" }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="发行人ID"
          name="issuerId"
          rules={[{ required: true, message: "请输入发行人ID" }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="开始日期"
          name="startDate"
          rules={[{ required: true, message: "请选择开始日期" }]}
        >
          <DatePicker style={{ width: "100%" }} />
        </Form.Item>

        <Form.Item
          label="结束日期"
          name="endDate"
          rules={[{ required: true, message: "请选择结束日期" }]}
        >
          <DatePicker style={{ width: "100%" }} />
        </Form.Item>

        <Form.Item
          label="状态"
          name="status"
          rules={[{ required: true, message: "请选择状态" }]}
        >
          <Select
            options={[
              { label: "启用", value: "01" },
              { label: "挂失", value: "02" },
              { label: "过期", value: "03" },
            ]}
          />
        </Form.Item>
      </Form>
    </Modal>
  );
}
