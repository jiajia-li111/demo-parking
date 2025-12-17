import http from "./http";

export function getMonthlyCards() {
  return http.get("/monthly-cards");
}

export function createMonthlyCard(data) {
  return http.post("/monthly-cards", data);
}

export function updateMonthlyCard(cardId, data) {
  return http.put(`/monthly-cards/${cardId}`, data);
}

export function deleteMonthlyCard(cardId) {
  return http.delete(`/monthly-cards/${cardId}`);
}
