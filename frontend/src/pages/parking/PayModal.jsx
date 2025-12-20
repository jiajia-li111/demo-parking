import { Modal, Descriptions, Select, message } from "antd";
import { useState } from "react";
import { processPayment } from "../../api/parkingSession";

export default function PayModal({ open, onClose, feeInfo, gateId }) {
  const [payMethod, setPayMethod] = useState("01");
  const [loading, setLoading] = useState(false);

  const handlePay = async () => {
    setLoading(true);
    try {
      const res = await processPayment({
        sessionId: feeInfo.sessionId,
        gateId,
        payMethod,
      });
      message.success(res.message || "出场成功");
      onClose();
    } catch {
      message.error("支付失败");
    } finally {
      setLoading(false);
    }
  };

  if (!feeInfo) return null;

  return (
    <Modal
      title="确认支付并出场"
      open={open}
      onOk={handlePay}
      onCancel={onClose}
      confirmLoading={loading}
    >
      <Descriptions column={1}>
        <Descriptions.Item label="停车时长">
          {feeInfo.parkingHours} 小时
        </Descriptions.Item>
        <Descriptions.Item label="应付金额">
          ¥ {feeInfo.payAmount}
        </Descriptions.Item>
        <Descriptions.Item label="支付方式">
          <Select
            value={payMethod}
            onChange={setPayMethod}
            options={[
              { value: "01", label: "微信支付" },
              { value: "02", label: "支付宝" },
            ]}
          />
        </Descriptions.Item>
      </Descriptions>
    </Modal>
  );
}
