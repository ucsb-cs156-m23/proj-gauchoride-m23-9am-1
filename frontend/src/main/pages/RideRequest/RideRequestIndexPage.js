import React from 'react';
import { useBackend } from 'main/utils/useBackend';

import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import RideRequestTable from 'main/components/RideRequest/RideRequestTable';
import {useCurrentUser } from 'main/utils/currentUser'

export default function RideRequestIndexPage() {

  const currentUser = useCurrentUser();

  const { data: rideReqs, error: _error, status: _status } =
  useBackend(
    // Stryker disable all : hard to test for query caching
    ["/api/ride_request/all"],
    { method: "GET", url: "/api/ride_request/all" },
    []
    // Stryker restore all 
  );

  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>Ride Requests</h1>
        <RideRequestTable rideReqs={rideReqs} currentUser={currentUser} />
      </div>
    </BasicLayout>
  )
}