
import React from 'react';

import ShiftPage from "main/pages/Shift/ShiftPage";
import shiftFixtures from 'fixtures/shiftFixtures';
import { currentUserFixtures } from 'fixtures/currentUserFixtures';

export default {
    title: 'pages/ShiftPage/ShiftPage',
    component: ShiftPage
};

const Template = () => <ShiftPage />;

export const Default = Template.bind({});

export const ThreeShifts = Template.bind({});

ThreeShifts.args = {
    shift: shiftFixtures.threeShifts,
    currentUser: currentUserFixtures.adminOnly
};

