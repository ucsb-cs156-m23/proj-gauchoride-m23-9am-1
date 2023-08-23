import React from 'react';
import { useBackend } from 'main/utils/useBackend';

import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import RideRequestTable from 'main/components/RideRequest/RideRequestTable';
import {useCurrentUser } from 'main/utils/currentUser'
import Button from 'react-bootstrap/Button';
import { Link } from 'react-router-dom';

export default function RiderPage() {

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
        <Button style={{ float: "right" }} as={Link} to="/ride-request/create">
          Request a Ride
        </Button>
        <h1>Ride Requests</h1>
        <RideRequestTable rideReqs={rideReqs} currentUser={currentUser} />
      </div>
    </BasicLayout>
  )
}