import { useEffect, useState } from "react";
import {
  Table,
  Button,
  Space,
  Popconfirm,
  message,
  Tag,
  Card,
} from "antd";
import {
  getVehicles,
  createVehicle,
  updateVehicle,
  deleteVehicle,
} from "../../api/vehicles";
import VehicleFormModal from "./VehicleFormModal";

// 数据映射配置
const vehicleTypeMap = {
  "01": "小型车",
  "02": "大型车",
};

const ownerCarMap = {
  "01": { label: "业主车", color: "green" },
  "02": { label: "非业主车", color: "default" },
};

const parkingMap = {
  "01": { label: "在场", color: "blue" },
  "02": { label: "不在场", color: "default" },
};

export default function VehiclesPage() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);

  // 加载数据
  const loadVehicles = async () => {
    setLoading(true);
    try {
      const list = await getVehicles();
      setData(list || []);
    } catch (err) {
      message.error("获取车辆列表失败");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadVehicles();
  }, []);

  const columns = [
    { title: "车辆ID", dataIndex: "vehicleId" },
    { title: "车牌号", dataIndex: "licensePlate" },
    {
      title: "车型",
      dataIndex: "vehicleType",
      render: (v) => vehicleTypeMap[v] || v,
    },
    {
      title: "是否业主车",
      dataIndex: "isOwnerCar",
      render: (v) => {
        const meta = ownerCarMap[v];
        return meta ? <Tag color={meta.color}>{meta.label}</Tag> : v;
      },
    },
    { title: "业主ID", dataIndex: "ownerId" },
    {
      title: "停车状态",
      dataIndex: "isParking",
      render: (v) => {
        const meta = parkingMap[v];
        return meta ? <Tag color={meta.color}>{meta.label}</Tag> : v;
      },
    },
    {
      title: "操作",
      render: (_, row) => (
        <Space>
          <Button
            size="small"
            onClick={() => {
              setEditing(row);
              setModalOpen(true);
            }}
          >
            编辑
          </Button>
          <Popconfirm
            title="确定删除该车辆？"
            onConfirm={async () => {
              await deleteVehicle(row.vehicleId);
              message.success("删除成功");
              loadVehicles();
            }}
          >
            <Button danger size="small">
              删除
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div style={{ display: "flex", flexDirection: "column", gap: 16 }}>
      <Card 
        title="车辆管理" 
        extra={
          <Button
            type="primary"
            onClick={() => {
              setEditing(null);
              setModalOpen(true);
            }}
          >
            新增车辆
          </Button>
        }
      >
        <Table
          rowKey="vehicleId"
          columns={columns}
          dataSource={data}
          bordered
          loading={loading}
        />
      </Card>

      <VehicleFormModal
        open={modalOpen}
        initialValues={editing}
        onCancel={() => setModalOpen(false)}
        onOk={async (values) => {
          if (editing) {
            await updateVehicle({ ...editing, ...values });
            message.success("更新成功");
          } else {
            await createVehicle(values);
            message.success("新增成功");
          }
          setModalOpen(false);
          setEditing(null);
          loadVehicles();
        }}
      />
    </div>
  );
}
