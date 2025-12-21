import { Layout, Menu, Button, Space } from "antd";
import { Outlet, useNavigate, useLocation } from "react-router-dom";
import { useState } from "react";
import LogoutButton from "../components/LogoutButton";
import ChangePasswordModal from "../components/ChangePasswordModal";

const { Header, Sider, Content } = Layout;

export default function MainLayout() {
  const navigate = useNavigate();
  const location = useLocation();
  const [pwdOpen, setPwdOpen] = useState(false);

  const menuItems = [
  { key: "/", label: "个人信息" },
  { key: "/monthly-cards", label: "月卡管理" },
  { key: "/users", label: "用户管理" },
  { key: "/gates", label: "通道管理" },
  { key: "/access", label: "出入场管理" },
  { key: "/access-events", label: "过闸事件" },
  { key: "/reports", label: "报表统计" },
];


  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Header
        style={{
          background: "#fff",
          display: "flex",
          justifyContent: "flex-end",
          paddingRight: 16,
        }}
      >
        <Space>
          <Button onClick={() => setPwdOpen(true)}>
            修改密码
          </Button>
          <LogoutButton />
        </Space>
      </Header>

      <Layout>
        <Sider width={200} style={{ background: "#fff" }}>
          <Menu
            mode="inline"
            selectedKeys={[location.pathname]}
            items={menuItems}
            onClick={({ key }) => navigate(key)}
          />
        </Sider>

        <Content style={{ padding: 16 }}>
          <Outlet />
        </Content>
      </Layout>

      <ChangePasswordModal
        open={pwdOpen}
        onClose={() => setPwdOpen(false)}
      />
    </Layout>
  );
}
