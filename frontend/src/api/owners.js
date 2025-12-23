import http from "./http";

export function getOwners() {
  return http.get("/owner");
}

export function getOwnerById(ownerId) {
  return http.get(`/owner/${ownerId}`);
}

export function createOwner(data) {
  return http.post("/owner", data);
}

export function updateOwner(data) {
  return http.put("/owner", data);
}

export function deleteOwner(ownerId) {
  return http.delete(`/owner/${ownerId}`);
}

export function getOwnersByName(name) {
  return http.get(`/owner/by-name/${name}`);
}

export function getOwnerByRoomNo(roomNo) {
  return http.get(`/owner/by-room-no/${roomNo}`);
}

export function getOwnerByPhone(phone) {
  return http.get(`/owner/by-phone/${phone}`);
}
