import { Routes, Route, Navigate } from 'react-router-dom'
import Dashboard from './pages/Dashboard'
import VehicleList from './pages/vehicles/VehicleList'
import VehicleDetail from './pages/vehicles/VehicleDetail'
import OwnerList from './pages/owners/OwnerList'
import SpaceList from './pages/spaces/SpaceList'
import SessionList from './pages/sessions/SessionList'
import PaymentList from './pages/payments/PaymentList'
import RuleList from './pages/rules/RuleList'


export default function AppRouter() {
return (
<Routes>
<Route path="/" element={<Navigate to="/dashboard" replace />} />
<Route path="/dashboard" element={<Dashboard />} />
<Route path="/vehicles" element={<VehicleList />} />
<Route path="/vehicles/:vehicle_id" element={<VehicleDetail />} />
<Route path="/owners" element={<OwnerList />} />
<Route path="/spaces" element={<SpaceList />} />
<Route path="/sessions" element={<SessionList />} />
<Route path="/payments" element={<PaymentList />} />
<Route path="/rules" element={<RuleList />} />
</Routes>
)
}