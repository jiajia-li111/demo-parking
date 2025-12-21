import { useEffect, useState } from "react";
import {
  Button,
  Descriptions,
  Drawer,
  Form,
  Input,
  Modal,
  Select,
  Space,
  Table,
  DatePicker,
  message,
} from "antd";
import dayjs from "dayjs";
import { createAccessEvent, getAccessEvent, getAccessEvents } from "../../api/accessEvents";

const recognitionOptions = [
  { value: "01", label: "识别成功" },
  { value: "02", label: "无牌" },
  { value: "03", label: "黑名单" },
];

const eventTypeOptions = [
  { value: "01", label: "进场" },
  { value: "02", label: "出场" },
];

const handleStatusOptions = [
  { value: "01", label: "放行" },
  { value: "02", label: "拦截" },
  { value: "03", label: "人工操作" },
];

export default function AccessEventsPage() {
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [drawerOpen, setDrawerOpen] = useState(false);
  const [detail, setDetail] = useState(null);
  const [createOpen, setCreateOpen] = useState(false);
  const [form] = Form.useForm();

  const loadEvents = async () => {
    setLoading(true);
    try {
      const data = await getAccessEvents();
      setList(data || []);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadEvents();
  }, []);

  const openDetail = async (eventId) => {
    const data = await getAccessEvent(eventId);
    setDetail(data);
    setDrawerOpen(true);
  };

  const handleCreate = async () => {
    try {
      const values = await form.validateFields();
      const body = {
        ...values,
        eventTime: values.eventTime.format("YYYY-MM-DDTHH:mm:ss"),
      };
      await createAccessEvent(body);
      message.success("事件创建成功");
      setCreateOpen(false);
      form.resetFields();
      loadEvents();
    } catch (err) {
      if (err?.errorFields) return;
      message.error("创建失败");
    }
  };

  const columns = [
    { title: "事件ID", dataIndex: "eventId" },
    { title: "通道ID", dataIndex: "gateId" },
    { title: "时间", dataIndex: "eventTime" },
    { title: "车辆ID", dataIndex: "vehicleId" },
    {
      title: "识别结果",
      dataIndex: "recognitionResult",
      render: (v) => recognitionOptions.find((o) => o.value === v)?.label || v,
    },
    {
      title: "事件类型",
      dataIndex: "eventType",
      render: (v) => eventTypeOptions.find((o) => o.value === v)?.label || v,
    },
    {
      title: "处理状态",
      dataIndex: "handleStatus",
      render: (v) => handleStatusOptions.find((o) => o.value === v)?.label || v,
    },
    {
      title: "操作",
      render: (_, record) => (
        <Button type="link" onClick={() => openDetail(record.eventId)}>
          查看
        </Button>
      ),
    },
  ];

  return (
    <div style={{ padding: 24, background: "#fff" }}>
      <Space style={{ marginBottom: 16 }}>
        <Button type="primary" onClick={() => setCreateOpen(true)}>
          新建过闸事件
        </Button>
      </Space>

      <Table rowKey="eventId" columns={columns} dataSource={list} loading={loading} />

      <Drawer
        width={420}
        title="事件详情"
        open={drawerOpen}
        onClose={() => setDrawerOpen(false)}
        destroyOnClose
      >
        {detail ? (
          <Descriptions bordered size="small" column={1}>
            <Descriptions.Item label="事件ID">{detail.eventId}</Descriptions.Item>
            <Descriptions.Item label="通道ID">{detail.gateId}</Descriptions.Item>
            <Descriptions.Item label="时间">{detail.eventTime}</Descriptions.Item>
            <Descriptions.Item label="车辆ID">{detail.vehicleId}</Descriptions.Item>
            <Descriptions.Item label="识别结果">
              {recognitionOptions.find((o) => o.value === detail.recognitionResult)?.label ||
                detail.recognitionResult}
            </Descriptions.Item>
            <Descriptions.Item label="事件类型">
              {eventTypeOptions.find((o) => o.value === detail.eventType)?.label || detail.eventType}
            </Descriptions.Item>
            <Descriptions.Item label="处理状态">
              {handleStatusOptions.find((o) => o.value === detail.handleStatus)?.label ||
                detail.handleStatus}
            </Descriptions.Item>
            <Descriptions.Item label="会话ID">{detail.sessionId}</Descriptions.Item>
            <Descriptions.Item label="操作人">{detail.operatorId}</Descriptions.Item>
          </Descriptions>
        ) : null}
      </Drawer>

      <Modal
        title="新建过闸事件"
        open={createOpen}
        onOk={handleCreate}
        onCancel={() => setCreateOpen(false)}
        destroyOnClose
      >
        <Form form={form} layout="vertical" initialValues={{ eventTime: dayjs() }}>
          <Form.Item
            label="事件ID"
            name="eventId"
            rules={[{ required: true, message: "请输入事件ID" }]}
          >
            <Input placeholder="e20241105001" />
          </Form.Item>
          <Form.Item
            label="通道ID"
            name="gateId"
            rules={[{ required: true, message: "请输入通道ID" }]}
          >
            <Input placeholder="如 in1" />
          </Form.Item>
          <Form.Item
            label="事件时间"
            name="eventTime"
            rules={[{ required: true, message: "请选择事件时间" }]}
          >
            <DatePicker showTime style={{ width: "100%" }} />
          </Form.Item>
          <Form.Item label="车辆ID" name="vehicleId">
            <Input placeholder="可选" />
          </Form.Item>
          <Form.Item
            label="识别结果"
            name="recognitionResult"
            rules={[{ required: true, message: "请选择识别结果" }]}
          >
            <Select options={recognitionOptions} />
          </Form.Item>
          <Form.Item
            label="事件类型"
            name="eventType"
            rules={[{ required: true, message: "请选择事件类型" }]}
          >
            <Select options={eventTypeOptions} />
          </Form.Item>
          <Form.Item label="会话ID" name="sessionId">
            <Input placeholder="出场时可填" />
          </Form.Item>
          <Form.Item
            label="处理状态"
            name="handleStatus"
            rules={[{ required: true, message: "请选择处理状态" }]}
          >
            <Select options={handleStatusOptions} />
          </Form.Item>
          <Form.Item label="操作人ID" name="operatorId">
            <Input placeholder="人工操作必填" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
