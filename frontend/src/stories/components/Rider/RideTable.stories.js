import React from 'react';
import RideTable from 'main/components/Rider/RideTable';
import { rideFixtures } from 'fixtures/rideFixtures';
import { currentUserFixtures } from 'fixtures/currentUserFixtures';

export default {
    title: 'components/Rider/RideTable',
    component: RideTable
};

const Template = (args) => {
    return (
        <RideTable {...args} />
    )
};

export const Empty = Template.bind({});

Empty.args = {
    rides: []
};


export const DriverThreeSubjectsNoButtons = Template.bind({});

DriverThreeSubjectsNoButtons.args = {
    rides: rideFixtures.threeRidesTable,
    currentUser: currentUserFixtures.driverOnly
};


export const RiderThreeSubjectsWithButtons = Template.bind({});
RiderThreeSubjectsWithButtons.args = {
    rides: rideFixtures.threeRidesTable,
    currentUser: currentUserFixtures.userOnly
};

export const AdminThreeSubjectsWithButtons = Template.bind({});
AdminThreeSubjectsWithButtons.args = {
    rides: rideFixtures.threeRidesTable,
    currentUser: currentUserFixtures.adminUser
};
