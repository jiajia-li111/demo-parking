import http from "./http";

export function vehicleEntry(params) {
  return http.post("/parking-session/entry", null, { params });
}

export function calculateFee(params) {
  return http.post("/parking-session/calculate-fee", null, { params });
}

export function processPayment(params) {
  return http.post("/parking-session/process-payment", null, { params });
}
