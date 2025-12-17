import { Button, message } from "antd";
import { logout } from "../api/auth";
import { clearToken } from "../utils/auth";
import { useNavigate } from "react-router-dom";

export default function LogoutButton() {
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await logout();
    } catch {}
    clearToken();
    message.success("已退出登录");
    navigate("/login");
  };

  return <Button danger onClick={handleLogout}>退出登录</Button>;
}
