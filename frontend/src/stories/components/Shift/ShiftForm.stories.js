import React from 'react';
import ShiftForm from "main/components/Shift/ShiftForm"
import shiftFixtures from 'fixtures/shiftFixtures';

export default {
    title: 'components/Shift/ShiftForm',
    component: ShiftForm
};

const Template = (args) => {
    return (
        <ShiftForm {...args} />
    )
};


export const Empty = Template.bind({});

Empty.args = {
    buttonLabel: "Create",
    submitAction: () => { console.log("Submit was clicked"); }
};

export const OneShift = Template.bind({});

OneShift.args = {
    initialContents: shiftFixtures.oneShift,
    buttonLabel: "Update",
    submitAction: () => { console.log("Submit was clicked"); }
};