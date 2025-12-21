import http from "./http";

export function getDailyStats(date) {
  return http.get("/reports/daily-stats", {
    params: date ? { date } : undefined,
  });
}

export function getMonthlyStats(year, month) {
  return http.get("/reports/monthly-stats", {
    params: { year, month },
  });
}

export function getCardExpiryAlerts(days = 7) {
  return http.get("/reports/card-expiry-alerts", { params: { days } });
}
