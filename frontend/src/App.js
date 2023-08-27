import { BrowserRouter, Routes, Route } from "react-router-dom";
import HomePage from "main/pages/HomePage";
import ProfilePage from "main/pages/ProfilePage";
import AdminUsersPage from "main/pages/AdminUsersPage";
import PageNotFound from "main/pages/PageNotFound";
import PrivacyPolicyPage from "main/pages/PrivacyPolicyPage";

import RideRequestCreatePage from "main/pages/Ride/RideRequestCreatePage";
import RideRequestEditPage from "main/pages/Ride/RideRequestEditPage";
import RideRequestIndexPage from "main/pages/Ride/RideRequestIndexPage";
import ShiftPage from "main/pages/ShiftPage";





import { hasRole, useCurrentUser } from "main/utils/currentUser";

import "bootstrap/dist/css/bootstrap.css";
import DriversListPage from "main/pages/DriversListPage";
import DriverDashboardPage from "main/pages/Drivers/driverDashboardPage";


function App() {

  const { data: currentUser } = useCurrentUser();

  return (
    <BrowserRouter>
      <Routes>
        <Route exact path="/" element={<HomePage />} />
        <Route exact path="/profile" element={<ProfilePage />} />
        <Route exact path="/privacy" element={<PrivacyPolicyPage />} />
        {
          hasRole(currentUser, "ROLE_ADMIN") && <Route exact path="/admin/users" element={<AdminUsersPage />} />
        }
        {
          (hasRole(currentUser, "ROLE_ADMIN") || hasRole(currentUser, "ROLE_DRIVER") || hasRole(currentUser, "ROLE_RIDER") )&& <Route exact path="/ride/" element={<RideRequestIndexPage />} />
        }
        {
          (hasRole(currentUser, "ROLE_RIDER") || hasRole(currentUser, "ROLE_ADMIN"))  && <Route exact path="/ride/create" element={<RideRequestCreatePage />} />
        }
        {
          (hasRole(currentUser, "ROLE_ADMIN")  || hasRole(currentUser, "ROLE_RIDER") )&& <Route exact path="/ride/edit/:id" element={<RideRequestEditPage />} />
        }
        {
          (hasRole(currentUser, "ROLE_DRIVER") ) && <Route exact path="/driver" element={<DriverDashboardPage />} />
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
          hasRole(currentUser, "ROLE_ADMIN") && <Route exact path="/drivers" element={<DriversListPage />} />
        }
        {
          hasRole(currentUser, "ROLE_DRIVER") && <Route exact path="/drivers" element={<DriversListPage />} />
        }
        {
          hasRole(currentUser, "ROLE_RIDER") && <Route exact path="/drivers" element={<DriversListPage />} />
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
