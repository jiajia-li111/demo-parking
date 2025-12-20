import { createBrowserRouter, RouterProvider, Navigate } from "react-router-dom";
import MainLayout from "./layouts/MainLayout";
import LoginPage from "./pages/login";
import ProfilePage from "./pages/profile";
import MonthlyCardsPage from "./pages/monthly-cards";
import UsersPage from "./pages/users";

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

      // 🚗 出闸入闸管理
      { path: "parking/entry", element: <EntryPage /> },
      { path: "parking/exit", element: <ExitPage /> },
    ],
  },

  { path: "*", element: <Navigate to="/" replace /> },
]);

export default function App() {
  return <RouterProvider router={router} />;
}

