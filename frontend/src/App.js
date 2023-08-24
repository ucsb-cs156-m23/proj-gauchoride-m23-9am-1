import { BrowserRouter, Routes, Route } from "react-router-dom";
import HomePage from "main/pages/HomePage";
import ProfilePage from "main/pages/ProfilePage";
import AdminUsersPage from "main/pages/AdminUsersPage";
import PageNotFound from "main/pages/PageNotFound";

import RideRequestIndexPage from "main/pages/RideRequest/RideRequestIndexPage";
import RideRequestCreatePage from "main/pages/RideRequest/RideRequestCreatePage";
import RideRequestEditPage from "main/pages/RideRequest/RideRequestEditPage";

import RiderPage from "main/pages/RiderPage";
import ShiftPage from "main/pages/ShiftPage";

import { hasRole, useCurrentUser } from "main/utils/currentUser";

import "bootstrap/dist/css/bootstrap.css";


function App() {

  const { data: currentUser } = useCurrentUser();

  return (
    <BrowserRouter>
      <Routes>
        <Route exact path="/" element={<HomePage />} />
        <Route exact path="/profile" element={<ProfilePage />} />
        {
          hasRole(currentUser, "ROLE_ADMIN") && <Route exact path="/admin/users" element={<AdminUsersPage />} />
        }
        { 
          (hasRole(currentUser, "ROLE_RIDER") || hasRole(currentUser, "ROLE_ADMIN")) && <Route exact path="/ride-request/create" element={<RideRequestCreatePage />} />
        }
        {
          (hasRole(currentUser, "ROLE_RIDER")  || hasRole(currentUser, "ROLE_ADMIN") )&& <Route exact path="/ride-request/edit/:id" element={<RideRequestEditPage />} />
        }
        {
          (hasRole(currentUser, "ROLE_RIDER") || hasRole(currentUser, "ROLE_ADMIN")) &&  <Route exact path="/rider" element={<RiderPage />} />
        }
        {
          hasRole(currentUser, "ROLE_ADMIN") && <Route exact path="/shift/list" element={<ShiftPage />} />
        }
        {
          hasRole(currentUser, "ROLE_DRIVER") && <Route exact path="/shift/list" element={<ShiftPage />} />
        }
        {
          hasRole(currentUser, "ROLE_RIDER") && <Route exact path="/shift/list" element={<ShiftPage />} />
        }
        {
          hasRole(currentUser, "ROLE_USER")
        }
        <Route exact path="/*" element={<PageNotFound />} />

      </Routes>
    </BrowserRouter>
  );
}

export default App;
