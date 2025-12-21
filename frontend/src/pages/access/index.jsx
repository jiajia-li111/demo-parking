import { useState } from "react";
import { Card, Form, Input, Select, Space, Button, Descriptions, message } from "antd";
import { calculateFee, processPayment, vehicleEntry } from "../../api/access";

const payMethodOptions = [
  { value: "01", label: "微信" },
  { value: "02", label: "支付宝" },
  { value: "03", label: "现金" },
];

export default function AccessPage() {
  const [entryResult, setEntryResult] = useState(null);
  const [calcResult, setCalcResult] = useState(null);
  const [processing, setProcessing] = useState(false);

  const [entryForm] = Form.useForm();
  const [calcForm] = Form.useForm();
  const [payForm] = Form.useForm();

  const handleEntry = async () => {
    try {
      const values = await entryForm.validateFields();
      const res = await vehicleEntry(values);
      setEntryResult(res);
      message.success("进场处理完成");
    } catch (err) {
      if (err?.errorFields) return;
      message.error("进场处理失败");
    }
  };

  const handleCalculate = async () => {
    try {
      const values = await calcForm.validateFields();
      const res = await calculateFee(values);
      setCalcResult(res);
      payForm.setFieldsValue({
        sessionId: res.sessionId,
        gateId: values.gateId,
      });
      message.success("费用计算完成");
    } catch (err) {
      if (err?.errorFields) return;
      message.error("费用计算失败");
    }
  };

  const handlePayment = async () => {
    try {
      setProcessing(true);
      const values = await payForm.validateFields();
      const res = await processPayment(values);
      setCalcResult(res);
      message.success("支付完成");
    } catch (err) {
      if (err?.errorFields) return;
      message.error("支付失败");
    } finally {
      setProcessing(false);
    }
  };

  return (
    <div style={{ display: "grid", gap: 16 }}>
      <Card title="车辆进场">
        <Space align="start" style={{ width: "100%" }}>
          <Form form={entryForm} layout="inline" style={{ flex: 1 }}>
            <Form.Item
              label="车牌号"
              name="licensePlate"
              rules={[{ required: true, message: "请输入车牌号" }]}
            >
              <Input placeholder="如 京A12345" />
            </Form.Item>
            <Form.Item
              label="通道ID"
              name="gateId"
              rules={[{ required: true, message: "请输入通道ID" }]}
            >
              <Input placeholder="如 in1" />
            </Form.Item>
            <Form.Item>
              <Button type="primary" onClick={handleEntry}>
                进场
              </Button>
            </Form.Item>
          </Form>
          {entryResult ? (
            <Descriptions title="进场结果" bordered column={1} size="small" style={{ minWidth: 320 }}>
              <Descriptions.Item label="会话ID">{entryResult.sessionId}</Descriptions.Item>
              <Descriptions.Item label="车位ID">{entryResult.spaceId}</Descriptions.Item>
              <Descriptions.Item label="楼层">{entryResult.floorName}</Descriptions.Item>
              <Descriptions.Item label="进场时间">{entryResult.entryTime}</Descriptions.Item>
              <Descriptions.Item label="放行">{entryResult.pass ? "是" : "否"}</Descriptions.Item>
              <Descriptions.Item label="提示">{entryResult.message}</Descriptions.Item>
            </Descriptions>
          ) : null}
        </Space>
      </Card>

      <Card title="车辆出场">
        <Space direction="vertical" style={{ width: "100%" }} size={16}>
          <Form form={calcForm} layout="inline">
            <Form.Item
              label="车牌号"
              name="licensePlate"
              rules={[{ required: true, message: "请输入车牌号" }]}
            >
              <Input placeholder="如 京A12345" />
            </Form.Item>
            <Form.Item
              label="通道ID"
              name="gateId"
              rules={[{ required: true, message: "请输入通道ID" }]}
            >
              <Input placeholder="如 out1" />
            </Form.Item>
            <Form.Item>
              <Button type="primary" onClick={handleCalculate}>
                计算费用
              </Button>
            </Form.Item>
          </Form>

          <Form form={payForm} layout="inline">
            <Form.Item
              label="会话ID"
              name="sessionId"
              rules={[{ required: true, message: "请输入会话ID" }]}
            >
              <Input placeholder="s20241105001" />
            </Form.Item>
            <Form.Item
              label="通道ID"
              name="gateId"
              rules={[{ required: true, message: "请输入通道ID" }]}
            >
              <Input placeholder="如 out1" />
            </Form.Item>
            <Form.Item
              label="支付方式"
              name="payMethod"
              rules={[{ required: true, message: "请选择支付方式" }]}
            >
              <Select style={{ width: 120 }} options={payMethodOptions} placeholder="请选择" />
            </Form.Item>
            <Form.Item>
              <Button type="primary" onClick={handlePayment} loading={processing}>
                确认支付并放行
              </Button>
            </Form.Item>
          </Form>

          {calcResult ? (
            <Descriptions title="出场结果" bordered column={2} size="small">
              <Descriptions.Item label="会话ID">{calcResult.sessionId}</Descriptions.Item>
              <Descriptions.Item label="停车时长">{calcResult.parkingHours} 小时</Descriptions.Item>
              <Descriptions.Item label="应付金额">{calcResult.payAmount}</Descriptions.Item>
              <Descriptions.Item label="支付方式">{calcResult.payMethodName || calcResult.payMethod}</Descriptions.Item>
              <Descriptions.Item label="支付状态">{calcResult.payStatusDesc || calcResult.payStatus}</Descriptions.Item>
              <Descriptions.Item label="支付时间">{calcResult.payTime}</Descriptions.Item>
              <Descriptions.Item label="出场时间">{calcResult.exitTime}</Descriptions.Item>
              <Descriptions.Item label="放行">{calcResult.pass ? "是" : "否"}</Descriptions.Item>
              <Descriptions.Item label="提示">{calcResult.message}</Descriptions.Item>
            </Descriptions>
          ) : null}
        </Space>
      </Card>
    </div>
  );
}
