import http from "./http";

// 修改密码（配合后端 @RequestParam）
export function updatePassword({ userId, oldPassword, newPassword }) {
  return http.put("/users/password", null, {
    params: {
      userId,
      oldPassword,
      newPassword,
    },
  });
}




export function getUsersPage(params) {
  return http.get("/users/page", { params });
}

export function createUser(data) {
  return http.post("/users", data);
}

// 删除用户
export function deleteUser(userId) {
  return http.delete(`/users/${userId}`);
}
