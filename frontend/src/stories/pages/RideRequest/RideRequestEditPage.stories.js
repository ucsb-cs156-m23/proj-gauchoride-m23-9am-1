import React from 'react';

import RideRequestEditPage from 'main/pages/RideRequest/RideRequestEditPage';
import { rideReqFixtures } from 'fixtures/rideRequestFixtures';

export default {
    title: 'pages/RideRequest/RideRequestEditPage',
    component: RideRequestEditPage
};

const Template = () => <RideRequestEditPage />;

export const Default = Template.bind({});

Default.args = {
    initialContents: rideReqFixtures.oneRideReq
};




