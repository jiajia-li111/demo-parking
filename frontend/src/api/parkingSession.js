import request from "./http";

// 车辆进场
export const entryParking = (params) => {
  return request.post("/parking-session/entry", null, { params });
};

// 计算停车费用
export const calculateFee = (params) => {
  return request.post("/parking-session/calculate-fee", null, { params });
};

// 支付并出闸
export const processPayment = (params) => {
  return request.post("/parking-session/process-payment", null, { params });
};
