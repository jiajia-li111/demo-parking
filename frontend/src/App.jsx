import { createBrowserRouter, RouterProvider, Navigate } from "react-router-dom";
import MainLayout from "./layouts/MainLayout";

import LoginPage from "./pages/login";
import ProfilePage from "./pages/profile";
import MonthlyCardsPage from "./pages/monthly-cards";
import UsersPage from "./pages/users";
import GatesPage from "./pages/gates";
import AccessEventsPage from "./pages/access-events";
import ReportsPage from "./pages/reports";

// 使用 parking 相关页面（推荐，因为注释中标记为真实路径）
import EntryPage from "./pages/parking/entry";
import ExitPage from "./pages/parking/exit";

function AuthGuard({ children }) {
  const token = localStorage.getItem("token");
  return token ? children : <Navigate to="/login" replace />;
}

const router = createBrowserRouter([
  { path: "/login", element: <LoginPage /> },

  {
    path: "/",
    element: (
      <AuthGuard>
        <MainLayout />
      </AuthGuard>
    ),
    children: [
      { index: true, element: <ProfilePage /> },
      { path: "monthly-cards", element: <MonthlyCardsPage /> },
      { path: "users", element: <UsersPage /> },
      { path: "gates", element: <GatesPage /> },

      // 🚗 出闸 / 入闸（真实路径）
      { path: "access/entry", element: <EntryPage /> },
      { path: "access/exit", element: <ExitPage /> },

      { path: "access-events", element: <AccessEventsPage /> },
      { path: "reports", element: <ReportsPage /> },
    ],
  },

  { path: "*", element: <Navigate to="/" replace /> },
]);

export default function App() {
  return <RouterProvider router={router} />;
}