import http from "./http";

/**
 * 修改密码
 * PUT /users/password
 * 参数走 query
 */
export function updatePassword(params) {
  return http.put("/users/password", null, {
    params,
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
