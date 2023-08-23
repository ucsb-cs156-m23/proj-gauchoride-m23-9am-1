import React from 'react';
import RideRequestTable from 'main/components/RideRequest/RideRequestTable';
import { rideReqFixtures } from 'fixtures/rideRequestFixtures';
import { currentUserFixtures } from 'fixtures/currentUserFixtures';

export default {
    title: 'components/RideRequest/RideRequestTable',
    component: RideRequestTable
};

const Template = (args) => {
    return (
        <RideRequestTable {...args} />
    )
};

export const Empty = Template.bind({});

Empty.args = {
    rideReqs: []
};


export const DriverThreeSubjectsNoButtons = Template.bind({});
DriverThreeSubjectsNoButtons.args = {
    rideReqs: rideReqFixtures.threeRideReqs,
    currentUser: currentUserFixtures.driverOnly
};


export const RiderThreeSubjectsWithButtons = Template.bind({});
RiderThreeSubjectsWithButtons.args = {
    rideReqs: rideReqFixtures.threeRideReqs,
    currentUser: currentUserFixtures.userOnly
};

export const AdminThreeSubjectsWithButtons = Template.bind({});
AdminThreeSubjectsWithButtons.args = {
    rideReqs: rideReqFixtures.threeRideReqs,
    currentUser: currentUserFixtures.adminUser
};
