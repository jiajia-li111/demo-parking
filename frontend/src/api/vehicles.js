import http from "./http";

export function getVehicles() {
  return http.get("/vehicle");
}

export function getVehicleById(vehicleId) {
  return http.get(`/vehicle/${vehicleId}`);
}

export function createVehicle(data) {
  return http.post("/vehicle", data);
}

export function updateVehicle(data) {
  return http.put("/vehicle", data);
}

export function deleteVehicle(vehicleId) {
  return http.delete(`/vehicle/${vehicleId}`);
}

export function getVehiclesByVehicleType(vehicleType) {
  return http.get(`/vehicle/by-type/${vehicleType}`);
}

export function getVehiclesByIsOwnerCar(isOwnerCar) {
  return http.get(`/vehicle/by-owner-car/${isOwnerCar}`);
}

export function getVehiclesByOwnerId(ownerId) {
  return http.get(`/vehicle/by-owner/${ownerId}`);
}

export function searchVehiclesByLicensePlate(keyword) {
  return http.get(`/vehicle/search/${keyword}`);
}

export function getVehicleByLicensePlate(licensePlate) {
  return http.get(`/vehicle/by-license/${licensePlate}`);
}
