import { Table, Button, Popconfirm, message, Space } from "antd";
import { useEffect, useState } from "react";
import { getUsersPage, deleteUser } from "../../api/users";
import CreateUserModal from "./CreateUserModal";

export default function UsersPage() {
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [createOpen, setCreateOpen] = useState(false); // ⭐ 新增

  // 拉用户数据
  const fetchUsers = async () => {
    setLoading(true);
    try {
      const res = await getUsersPage({ current: 1, size: 10 });
      setList(res.records || []);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  // 删除用户
  const handleDelete = async (userId) => {
    try {
      await deleteUser(userId);
      message.success("删除成功");
      fetchUsers();
    } catch {
      message.error("删除失败");
    }
  };

  const columns = [
    { title: "用户ID", dataIndex: "userId" },
    { title: "用户名", dataIndex: "username" },
    { title: "角色", dataIndex: "roleName" },
    { title: "部门", dataIndex: "department" },
    {
      title: "操作",
      render: (_, record) => (
        <Popconfirm
          title="确定要删除该用户吗？"
          onConfirm={() => handleDelete(record.userId)}
        >
          <Button danger size="small">
            删除
          </Button>
        </Popconfirm>
      ),
    },
  ];

  return (
    <div style={{ padding: 24, background: "#fff" }}>
      {/* ⭐ 新增用户按钮 */}
      <Space style={{ marginBottom: 16 }}>
        <Button type="primary" onClick={() => setCreateOpen(true)}>
          新增用户
        </Button>
      </Space>

      <Table
        rowKey="userId"
        columns={columns}
        dataSource={list}
        loading={loading}
        pagination={false}
      />

      {/* ⭐ 新增用户弹窗 */}
      <CreateUserModal
        open={createOpen}
        onClose={() => setCreateOpen(false)}
        onSuccess={() => {
          setCreateOpen(false);
          fetchUsers();
        }}
      />
    </div>
  );
}
