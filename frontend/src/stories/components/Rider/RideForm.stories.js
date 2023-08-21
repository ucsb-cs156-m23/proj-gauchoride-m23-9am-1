import React from 'react';
import RideForm from "main/components/Rider/RideForm"
import { rideFixtures } from 'fixtures/rideFixtures';

export default {
    title: 'components/Rider/RideForm',
    component: RideForm
};

const Template = (args) => {
    return (
        <RideForm {...args} />
    )
};


export const Default = Template.bind({});

Default.args = {
    submitText: "Create",
    submitAction: () => { console.log("Submit was clicked"); }
};

export const Show = Template.bind({});

Show.args = {
    intitialContents: rideFixtures.oneRide,
    submitText: "Create",
    submitAction: () => { console.log("Submit was clicked"); }
};