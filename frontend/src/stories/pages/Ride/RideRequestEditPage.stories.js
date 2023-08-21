import React from 'react';

import RideRequestEditPage from 'main/pages/Rider/RideRequestEditPage';
import { rideFixtures } from 'fixtures/rideFixtures';

export default {
    title: 'pages/Ride/RideRequestEditPage',
    component: RideRequestEditPage
};

const Template = () => <RideRequestEditPage />;

export const Default = Template.bind({});

Default.args = {
    initialContents: rideFixtures.oneRide
};




