import { Card, Form, Input, Button, Select, message, Descriptions } from "antd";
import { useState } from "react";
import { vehicleEntry } from "../../api/access";

export default function EntryPage() {
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);

  const onFinish = async (values) => {
    setLoading(true);
    try {
      const res = await vehicleEntry(values);
      setResult(res);
      message.success(res.message || "进场成功");
    } catch {
      message.error("进场失败");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card title="车辆进场">
      <Form layout="inline" onFinish={onFinish}>
        <Form.Item
          label="车牌号"
          name="licensePlate"
          rules={[{ required: true, message: "请输入车牌号" }]}
        >
          <Input placeholder="粤A12345" />
        </Form.Item>

        <Form.Item
          label="入口闸机"
          name="gateId"
          rules={[{ required: true, message: "请选择闸机" }]}
        >
          <Select
            style={{ width: 160 }}
            options={[
              { value: "in1", label: "入口一号闸" },
              { value: "in2", label: "入口二号闸" },
            ]}
          />
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit" loading={loading}>
            确认进场
          </Button>
        </Form.Item>
      </Form>

      {result && (
        <Descriptions bordered column={1} style={{ marginTop: 24 }}>
          <Descriptions.Item label="会话ID">{result.sessionId}</Descriptions.Item>
          <Descriptions.Item label="车位">{result.spaceId}</Descriptions.Item>
          <Descriptions.Item label="楼层">{result.floorName}</Descriptions.Item>
          <Descriptions.Item label="进场时间">{result.entryTime}</Descriptions.Item>
        </Descriptions>
      )}
    </Card>
  );
}
