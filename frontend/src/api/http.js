import axios from "axios";
import { message } from "antd";
import { getToken, clearToken } from "../utils/auth";

const http = axios.create({
  baseURL: "/api", // ⭐ 用 Vite proxy
  timeout: 10000,
});

http.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

http.interceptors.response.use(
  (res) => {
    const result = res.data;
    if (result.success) return result.data;
    message.error(result.msg || "请求失败");
    return Promise.reject(result);
  },
  (err) => {
    if (err.response?.status === 401) {
      message.error("登录失效，请重新登录");
      clearToken();
      window.location.href = "/login";
    }
    return Promise.reject(err);
  }
);

export default http;
