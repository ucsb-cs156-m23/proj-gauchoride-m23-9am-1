  // Stryker disable all : placeholder page
import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import React from 'react'
import { useBackend } from 'main/utils/useBackend';
import DriverTable from 'main/components/Drivers/DriverTable';
import { useCurrentUser } from 'main/utils/currentUser'

export default function DriversListPage() {

  const currentUser = useCurrentUser();

  const { data: drivers, error: _error, status: _status } =
      useBackend(
          // Stryker disable next-line all : don't test internal caching of React Query
          ["/api/drivers/all"],
          { method: "GET", url: "/api/drivers/all" },
          // Stryker disable next-line all : don't test default value of empty list
          []
      );

  return (
      <BasicLayout>
          <div className="pt-2">
              <h1>Drivers</h1>
              <DriverTable users={drivers} currentUser={currentUser} />
          </div>
      </BasicLayout>
  );
}