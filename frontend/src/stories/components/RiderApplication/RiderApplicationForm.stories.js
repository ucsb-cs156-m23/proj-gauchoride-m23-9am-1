
import React from 'react';

import RiderApplicationForm from "main/components/RiderApplication/RiderApplicationForm";
import riderApplicationFixtures from 'fixtures/riderApplicationFixtures';

export default {
    title: 'components/RiderApplication/RiderApplicationForm',
    component: RiderApplicationForm
};

const Template = (args) => {
    return (
        <RiderApplicationForm {...args} />
    )
};

export const Empty = Template.bind({});

Empty.args = {
    submitAction: () => { console.log("Submit was clicked"); },
    userEmail: "test@ucsb.edu"
};

export const oneRiderApplicationPending = Template.bind({});

oneRiderApplicationPending.args = {
    initialContents: riderApplicationFixtures.oneRiderApplicationPending,
    userEmail: "test@ucsb.edu",
    buttonLabel: "Update",
    submitAction: () => { console.log("Submit was clicked"); }
};

export const oneRiderApplicationCancelled = Template.bind({});

oneRiderApplicationCancelled.args = {
    initialContents: riderApplicationFixtures.oneRiderApplicationCancelled,
    userEmail: "test@ucsb.edu",
    buttonLabel: "Update",
    submitAction: () => { console.log("Submit was clicked"); }
};


