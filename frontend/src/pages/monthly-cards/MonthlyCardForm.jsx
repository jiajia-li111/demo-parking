import { Modal, Form, Input, DatePicker } from "antd";
import dayjs from "dayjs";

export default function MonthlyCardForm({
  open,
  mode,
  initialValues,
  onOk,
  onCancel,
}) {
  const [form] = Form.useForm();

  return (
    <Modal
      open={open}
      title={mode === "edit" ? "编辑月卡" : "新增月卡"}
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
                  : dayjs(),
                endDate: initialValues.endDate
                  ? dayjs(initialValues.endDate)
                  : null,
              }
            : {
                startDate: dayjs(),
              }
        }
      >
        {/* ✅ 车牌号（字段名 vehicleId，后端依赖） */}
        <Form.Item
          label="车牌号"
          name="vehicleId"
          rules={[{ required: true, message: "请输入车牌号" }]}
        >
          <Input placeholder="如：粤A12345" />
        </Form.Item>

        {/* 发行人ID：仅展示，不提交 */}
        <Form.Item label="发行人ID">
          <Input disabled value={localStorage.getItem("userId")} />
        </Form.Item>

        <Form.Item label="开始日期" name="startDate">
          <DatePicker style={{ width: "100%" }} />
        </Form.Item>

        <Form.Item
          label="结束日期"
          name="endDate"
          rules={[{ required: true, message: "请选择结束日期" }]}
        >
          <DatePicker style={{ width: "100%" }} />
        </Form.Item>
      </Form>
    </Modal>
  );
}

