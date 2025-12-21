import { useEffect, useMemo, useState } from "react";
import { Button, Modal, Form, Input, Select, Table, Space, message } from "antd";
import { getGates, createGate, updateGate, deleteGate } from "../../api/gates";

const gateTypeOptions = [
  { value: "01", label: "入口" },
  { value: "02", label: "出口" },
];

const statusOptions = [
  { value: "01", label: "正常" },
  { value: "02", label: "故障" },
];

export default function GatesPage() {
  const [gates, setGates] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form] = Form.useForm();

  const loadGates = async () => {
    setLoading(true);
    try {
      const data = await getGates();
      setGates(data || []);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadGates();
  }, []);

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      if (editing) {
        await updateGate(editing.gateId, { ...editing, ...values });
        message.success("通道更新成功");
      } else {
        await createGate(values);
        message.success("通道创建成功");
      }
      setModalOpen(false);
      setEditing(null);
      form.resetFields();
      loadGates();
    } catch (err) {
      if (err?.errorFields) return;
      message.error("操作失败");
    }
  };

  const columns = useMemo(
    () => [
      { title: "通道ID", dataIndex: "gateId" },
      {
        title: "通道类型",
        dataIndex: "gateType",
        render: (value) => gateTypeOptions.find((o) => o.value === value)?.label || value,
      },
      { title: "所属出入口", dataIndex: "entranceId" },
      {
        title: "状态",
        dataIndex: "status",
        render: (value) => statusOptions.find((o) => o.value === value)?.label || value,
      },
      {
        title: "操作",
        render: (_, record) => (
          <Space>
            <Button
              size="small"
              onClick={() => {
                setEditing(record);
                form.setFieldsValue(record);
                setModalOpen(true);
              }}
            >
              编辑
            </Button>
            <Button
              size="small"
              danger
              onClick={async () => {
                await deleteGate(record.gateId);
                message.success("删除成功");
                loadGates();
              }}
            >
              删除
            </Button>
          </Space>
        ),
      },
    ],
    [form]
  );

  return (
    <div style={{ padding: 24, background: "#fff" }}>
      <Space style={{ marginBottom: 16 }}>
        <Button
          type="primary"
          onClick={() => {
            setEditing(null);
            form.resetFields();
            setModalOpen(true);
          }}
        >
          新增通道
        </Button>
      </Space>

      <Table rowKey="gateId" columns={columns} dataSource={gates} loading={loading} />

      <Modal
        open={modalOpen}
        title={editing ? "编辑通道" : "新增通道"}
        onOk={handleSubmit}
        onCancel={() => {
          setModalOpen(false);
          setEditing(null);
        }}
        destroyOnClose
      >
        <Form form={form} layout="vertical">
          <Form.Item
            label="通道ID"
            name="gateId"
            rules={[{ required: true, message: "请输入通道ID" }]}
          >
            <Input disabled={!!editing} placeholder="如 in1" />
          </Form.Item>
          <Form.Item
            label="通道类型"
            name="gateType"
            rules={[{ required: true, message: "请选择通道类型" }]}
          >
            <Select options={gateTypeOptions} />
          </Form.Item>
          <Form.Item
            label="所属出入口"
            name="entranceId"
            rules={[{ required: true, message: "请输入所属出入口" }]}
          >
            <Input placeholder="如 01" />
          </Form.Item>
          <Form.Item
            label="状态"
            name="status"
            rules={[{ required: true, message: "请选择状态" }]}
          >
            <Select options={statusOptions} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
