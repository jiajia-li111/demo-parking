import http from "./http";

export function getAccessEvents() {
  return http.get("/access-events");
}

export function getAccessEvent(eventId) {
  return http.get(`/access-events/${eventId}`);
}

export function createAccessEvent(data) {
  return http.post("/access-events", data);
}
