
import React from 'react';

import DriverTable from "main/components/Drivers/DriverTable";
import usersFixtures from 'fixtures/usersFixtures';

export default {
    title: 'components/Drivers/DriverTable',
    component: DriverTable
};

const Template = (args) => {
    return (
        <DriverTable {...args} />
    )
};

export const Empty = Template.bind({});

Empty.args = {
    users: []
};

export const threeDrivers = Template.bind({});

threeDrivers.args = {
    users: usersFixtures.threeDrivers
};


