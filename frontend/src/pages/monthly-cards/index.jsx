import { Table, Button, Space, Popconfirm, message } from "antd";
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
  const [modalMode, setModalMode] = useState("create");
  const [editingRow, setEditingRow] = useState(null);

  /** 加载数据 */
  const loadData = async () => {
    const list = await getMonthlyCards();
    setData(list);
  };

  useEffect(() => {
    loadData();
  }, []);

  /** 表格列（已移除 月卡ID / 状态） */
  const columns = [
    {
      title: "车牌号",
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

      <Table
        rowKey="cardId"
        columns={columns}
        dataSource={data}
        bordered
      />

      <MonthlyCardForm
        open={modalOpen}
        mode={modalMode}
        initialValues={editingRow}
        onCancel={() => setModalOpen(false)}
        onOk={async (values) => {
          const body = {
            // ✅ 车牌号 → 后端读取 vehicleId
            vehicleId: values.vehicleId,

            // ✅ 发行人ID → 前端本地
            issuerId: localStorage.getItem("userId"),

            // 日期
            startDate: values.startDate
              ? values.startDate.toISOString()
              : undefined,
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


