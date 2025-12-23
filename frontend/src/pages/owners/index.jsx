import { useEffect, useState } from "react";
import { Table, Button, Space, Popconfirm, message, Form, Input, Card } from "antd";
import {
  getOwners,
  createOwner,
  updateOwner,
  deleteOwner,
  getOwnersByName,
  getOwnerByRoomNo,
  getOwnerByPhone,
} from "../../api/owners";
import OwnerFormModal from "./OwnerFormModal";

export default function OwnersPage() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [searchForm] = Form.useForm();

  const loadOwners = async () => {
    setLoading(true);
    try {
      const list = await getOwners();
      setData(list || []);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadOwners();
  }, []);

  const handleSearch = async () => {
    const { name, roomNo, phone } = searchForm.getFieldsValue();
    setLoading(true);

    try {
      if (name) {
        const list = await getOwnersByName(name);
        setData(list || []);
        return;
      }

      if (roomNo) {
        const owner = await getOwnerByRoomNo(roomNo);
        setData(owner ? [owner] : []);
        return;
      }

      if (phone) {
        const owner = await getOwnerByPhone(phone);
        setData(owner ? [owner] : []);
        return;
      }

      await loadOwners();
    } catch (err) {
      if (!err?.success) {
        setData([]);
      }
    } finally {
      setLoading(false);
    }
  };

  const columns = [
    { title: "业主ID", dataIndex: "ownerId" },
    { title: "姓名", dataIndex: "name" },
    { title: "房号", dataIndex: "roomNo" },
    { title: "联系电话", dataIndex: "phone" },
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
            title="确定删除该业主？"
            onConfirm={async () => {
              await deleteOwner(row.ownerId);
              message.success("删除成功");
              loadOwners();
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
      <Card>
        <Form layout="inline" form={searchForm} style={{ gap: 12 }}>
          <Form.Item label="姓名" name="name">
            <Input placeholder="按姓名查询" allowClear />
          </Form.Item>
          <Form.Item label="房号" name="roomNo">
            <Input placeholder="按房号查询" allowClear />
          </Form.Item>
          <Form.Item label="电话" name="phone">
            <Input placeholder="按电话查询" allowClear />
          </Form.Item>
          <Space>
            <Button type="primary" onClick={handleSearch} loading={loading}>
              查询
            </Button>
            <Button
              onClick={() => {
                searchForm.resetFields();
                loadOwners();
              }}
            >
              重置
            </Button>
            <Button
              type="primary"
              onClick={() => {
                setEditing(null);
                setModalOpen(true);
              }}
            >
              新增业主
            </Button>
          </Space>
        </Form>
      </Card>

      <Card>
        <Table
          rowKey="ownerId"
          columns={columns}
          dataSource={data}
          bordered
          loading={loading}
        />
      </Card>

      <OwnerFormModal
        open={modalOpen}
        initialValues={editing}
        onCancel={() => setModalOpen(false)}
        onOk={async (values) => {
          if (editing) {
            await updateOwner(values);
            message.success("更新成功");
          } else {
            await createOwner(values);
            message.success("新增成功");
          }
          setModalOpen(false);
          setEditing(null);
          loadOwners();
        }}
      />
    </div>
  );
}
