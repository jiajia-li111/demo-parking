import http from "./http";

export function getGates() {
  return http.get("/gates");
}

export function createGate(data) {
  return http.post("/gates", data);
}

export function updateGate(gateId, data) {
  return http.put(`/gates/${gateId}`, data);
}

export function deleteGate(gateId) {
  return http.delete(`/gates/${gateId}`);
}
