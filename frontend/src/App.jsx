import { createBrowserRouter, RouterProvider, Navigate } from "react-router-dom";
import MainLayout from "./layouts/MainLayout";
import LoginPage from "./pages/login";
import ProfilePage from "./pages/profile";
import MonthlyCardsPage from "./pages/monthly-cards";
import UsersPage from "./pages/users";
import GatesPage from "./pages/gates";
import AccessPage from "./pages/access";
import AccessEventsPage from "./pages/access-events";
import ReportsPage from "./pages/reports";

const router = createBrowserRouter([
  { path: "/login", element: <LoginPage /> },
  {
    path: "/",
    element: <MainLayout />,
    children: [
      { index: true, element: <ProfilePage /> },
      { path: "monthly-cards", element: <MonthlyCardsPage /> },
      { path: "users", element: <UsersPage /> }, // ✅ 关键在这
      { path: "gates", element: <GatesPage /> },
      { path: "access", element: <AccessPage /> },
      { path: "access-events", element: <AccessEventsPage /> },
      { path: "reports", element: <ReportsPage /> },
    ],
  },
  { path: "*", element: <Navigate to="/" /> },
]);

export default function App() {
  return <RouterProvider router={router} />;
}
