import { useEffect, useState } from "react";
import { Button, Modal, Form, Input, Space, message } from "antd";
import GenericTable from "../../components/tables/GenericTable";
import VehicleForm from "../../components/forms/VehicleForm";
import { ENDPOINTS } from "../../api/endpoints";

export default function VehicleList() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [open, setOpen] = useState(false);
  const [form] = Form.useForm();

  useEffect(() => {
    fetchList();
  }, []);

  const fetchList = async () => {
    setLoading(true);
    try {
      const res = await fetch(ENDPOINTS.vehicles);
      const json = await res.json();
      setData(json.list || []);
    } catch (err) {
      message.error("获取车辆列表失败");
    } finally {
      setLoading(false);
    }
  };

  // ✅ return 必须在函数体内部！
  return (
    <>
      <Space style={{ marginBottom: 12 }}>
        <Button type="primary" onClick={() => setOpen(true)}>新增车辆</Button>
        <Input.Search placeholder="按车牌搜索" />
      </Space>

      <GenericTable rowKey="vehicle_id" columns={[]} data={data} loading={loading} />

      <Modal title="车辆信息" open={open} onCancel={() => setOpen(false)} footer={null}>
        <VehicleForm form={form} />
      </Modal>
    </>
  );
}

