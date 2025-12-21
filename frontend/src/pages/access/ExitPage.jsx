import { Card, Form, Input, Button, Select, message, Descriptions } from "antd";
import { useState } from "react";
import { calculateFee } from "../../api/access";
import PayModal from "./PayModal";

export default function ExitPage() {
  const [loading, setLoading] = useState(false);
  const [feeInfo, setFeeInfo] = useState(null);
  const [gateId, setGateId] = useState("");
  const [payOpen, setPayOpen] = useState(false);

  const onFinish = async (values) => {
    setGateId(values.gateId);
    setLoading(true);
    try {
      const res = await calculateFee(values);
      setFeeInfo(res);
      message.success("费用计算完成");
    } catch {
      message.error("计算费用失败");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card title="车辆出场">
      <Form layout="inline" onFinish={onFinish}>
        <Form.Item
          label="车牌号"
          name="licensePlate"
          rules={[{ required: true, message: "请输入车牌号" }]}
        >
          <Input placeholder="粤A12345" />
        </Form.Item>

        <Form.Item
          label="出口闸机"
          name="gateId"
          rules={[{ required: true, message: "请选择闸机" }]}
        >
          <Select
            style={{ width: 160 }}
            options={[
              { value: "out1", label: "出口一号闸" },
              { value: "out2", label: "出口二号闸" },
            ]}
          />
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit" loading={loading}>
            计算费用
          </Button>
        </Form.Item>
      </Form>

      {feeInfo && (
        <>
          <Descriptions bordered column={1} style={{ marginTop: 24 }}>
            <Descriptions.Item label="停车时长">
              {feeInfo.parkingHours} 小时
            </Descriptions.Item>
            <Descriptions.Item label="应付金额">
              ¥ {feeInfo.payAmount}
            </Descriptions.Item>
          </Descriptions>

          <Button
            type="primary"
            style={{ marginTop: 16 }}
            onClick={() => setPayOpen(true)}
          >
            确认支付并出场
          </Button>
        </>
      )}

      <PayModal
        open={payOpen}
        onClose={() => setPayOpen(false)}
        feeInfo={feeInfo}
        gateId={gateId}
      />
    </Card>
  );
}
