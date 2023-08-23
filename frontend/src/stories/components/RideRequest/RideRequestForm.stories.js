import React from 'react';
import RideRequestForm from "main/components/RideRequest/RideRequestForm"
import { rideReqFixtures } from 'fixtures/rideRequestFixtures';

export default {
    title: 'components/RideRequest/RideRequestForm',
    component: RideRequestForm
};

const Template = (args) => {
    return (
        <RideRequestForm {...args} />
    )
};


export const Default = Template.bind({});

Default.args = {
    submitText: "Create",
    submitAction: () => { console.log("Submit was clicked"); }
};

export const Show = Template.bind({});

Show.args = {
    intitialContents: rideReqFixtures.oneRideReq,
    submitText: "Create",
    submitAction: () => { console.log("Submit was clicked"); }
};