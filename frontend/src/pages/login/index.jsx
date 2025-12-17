import { Button, Form, Input, Card, message } from "antd";
import { login } from "../../api/auth";
import { setToken } from "../../utils/auth";
import { useNavigate } from "react-router-dom";

export default function LoginPage() {
  const navigate = useNavigate();

  const onFinish = async (values) => {
    try {
      const data = await login(values);
      setToken(data.token);
      message.success("登录成功");
      navigate("/");
    } catch (e) {
      message.error("登录失败");
    }
  };

  return (
    <div
      style={{
        height: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
      }}
    >
      <Card title="停车场管理系统登录" style={{ width: 300 }}>
        <Form onFinish={onFinish}>
          <Form.Item name="username" rules={[{ required: true }]}>
            <Input placeholder="用户名" />
          </Form.Item>
          <Form.Item name="password" rules={[{ required: true }]}>
            <Input.Password placeholder="密码" />
          </Form.Item>
          <Button type="primary" block htmlType="submit">
            登录
          </Button>
        </Form>
      </Card>
    </div>
  );
}

