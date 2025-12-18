import { Table, Button, Space, Popconfirm, message, Tag } from "antd";
import { useEffect, useState } from "react";
import dayjs from "dayjs";

import {
  getMonthlyCards,
  deleteMonthlyCard,
  createMonthlyCard,
  updateMonthlyCard,
} from "../../api/monthlyCards";

import MonthlyCardForm from "./MonthlyCardForm";

export default function MonthlyCardsPage() {
  const [data, setData] = useState([]);
  const [modalOpen, setModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState("create"); // create | edit
  const [editingRow, setEditingRow] = useState(null);

  /** 加载列表 */
  const loadData = async () => {
    const list = await getMonthlyCards();
    setData(list);
  };

  useEffect(() => {
    loadData();
  }, []);

  /** 表格列定义 */
  const columns = [
    {
      title: "月卡ID",
      dataIndex: "cardId",
    },
    {
      title: "车辆ID",
      dataIndex: "vehicleId",
    },
    {
      title: "发行人ID",
      dataIndex: "issuerId",
    },
    {
      title: "开始日期",
      dataIndex: "startDate",
      render: (v) => dayjs(v).format("YYYY-MM-DD"),
    },
    {
      title: "结束日期",
      dataIndex: "endDate",
      render: (v) => dayjs(v).format("YYYY-MM-DD"),
    },
    {
      title: "状态",
      dataIndex: "status",
      render: (v) => {
        if (v === "01") return <Tag color="green">启用</Tag>;
        if (v === "02") return <Tag color="orange">挂失</Tag>;
        if (v === "03") return <Tag color="red">过期</Tag>;
        return v;
      },
    },
    {
      title: "操作",
      render: (_, row) => (
        <Space>
          <Button
            size="small"
            onClick={() => {
              setModalMode("edit");
              setEditingRow(row);
              setModalOpen(true);
            }}
          >
            编辑
          </Button>

          <Popconfirm
            title="确定删除该月卡？"
            onConfirm={async () => {
              await deleteMonthlyCard(row.cardId);
              message.success("删除成功");
              loadData();
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
    <>
      {/* 新增按钮 */}
      <div style={{ marginBottom: 16 }}>
        <Button
          type="primary"
          onClick={() => {
            setModalMode("create");
            setEditingRow(null);
            setModalOpen(true);
          }}
        >
          新增月卡
        </Button>
      </div>

      {/* 表格 */}
      <Table
        rowKey="cardId"
        columns={columns}
        dataSource={data}
        bordered
      />

      {/* 新增 / 编辑弹窗 */}
      <MonthlyCardForm
        open={modalOpen}
        mode={modalMode}
        initialValues={editingRow}
        onCancel={() => setModalOpen(false)}
        onOk={async (values) => {
          const body = {
            ...values,
            startDate: values.startDate.toISOString(),
            endDate: values.endDate.toISOString(),
          };

          if (modalMode === "create") {
            await createMonthlyCard(body);
            message.success("新增成功");
          } else {
            await updateMonthlyCard(editingRow.cardId, body);
            message.success("更新成功");
          }

          setModalOpen(false);
          loadData();
        }}
      />
    </>
  );
}

