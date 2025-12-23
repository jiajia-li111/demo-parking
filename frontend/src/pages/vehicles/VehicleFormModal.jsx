import { Modal, Form, Input, Select } from "antd";
import { useEffect } from "react";

const vehicleTypeOptions = [
  { value: "01", label: "小型车" },
  { value: "02", label: "大型车" },
];

const ownerCarOptions = [
  { value: "01", label: "业主车" },
  { value: "02", label: "非业主车" },
];

const parkingStatusOptions = [
  { value: "01", label: "在场" },
  { value: "02", label: "不在场" },
];

export default function VehicleFormModal({ open, onCancel, onOk, initialValues }) {
  const [form] = Form.useForm();

  useEffect(() => {
    if (open) {
      form.setFieldsValue(
        initialValues || {
          licensePlate: "",
          vehicleType: "01",
          isOwnerCar: "02",
          ownerId: "",
          isParking: "02",
        }
      );
    }
  }, [open, initialValues, form]);

  return (
    <Modal
      title={initialValues ? "编辑车辆" : "新增车辆"}
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
        {initialValues?.vehicleId && (
          <Form.Item label="车辆ID" name="vehicleId">
            <Input disabled />
          </Form.Item>
        )}

        <Form.Item
          label="车牌号"
          name="licensePlate"
          rules={[{ required: true, message: "请输入车牌号" }]}
        >
          <Input placeholder="如 粤A12345" />
        </Form.Item>

        <Form.Item
          label="车型"
          name="vehicleType"
          rules={[{ required: true, message: "请选择车型" }]}
        >
          <Select options={vehicleTypeOptions} />
        </Form.Item>

        <Form.Item
          label="是否业主车"
          name="isOwnerCar"
          rules={[{ required: true, message: "请选择是否业主车" }]}
        >
          <Select options={ownerCarOptions} />
        </Form.Item>

        <Form.Item
          label="关联业主ID"
          name="ownerId"
          dependencies={["isOwnerCar"]}
          rules={[
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (getFieldValue("isOwnerCar") === "01" && !value) {
                  return Promise.reject(new Error("业主车必须填写业主ID"));
                }
                return Promise.resolve();
              },
            }),
          ]}
        >
          <Input placeholder="业主车需填写" />
        </Form.Item>

        <Form.Item
          label="是否在停车场"
          name="isParking"
          rules={[{ required: true, message: "请选择车辆当前状态" }]}
        >
          <Select options={parkingStatusOptions} />
        </Form.Item>
      </Form>
    </Modal>
  );
}
