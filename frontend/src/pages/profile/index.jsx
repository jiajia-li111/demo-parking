import { Descriptions } from "antd";
import { useEffect, useState } from "react";
import { getCurrentUser } from "../../api/auth";

export default function ProfilePage() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    getCurrentUser().then(setUser);
  }, []);

  if (!user) return null;

  return (
    <Descriptions title="当前用户信息" bordered>
      <Descriptions.Item label="用户名">{user.username}</Descriptions.Item>
      <Descriptions.Item label="角色">{user.roleName}</Descriptions.Item>
      <Descriptions.Item label="部门">{user.department}</Descriptions.Item>
      <Descriptions.Item label="状态">{user.statusDesc}</Descriptions.Item>
    </Descriptions>
  );
}
