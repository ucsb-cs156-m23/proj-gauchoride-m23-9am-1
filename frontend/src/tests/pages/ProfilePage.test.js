import { fireEvent, screen, render, waitFor } from "@testing-library/react";
import React from "react";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import { apiCurrentUserFixtures }  from "fixtures/currentUserFixtures";
import { Modal } from "react-bootstrap";

import axios from "axios";
import AxiosMockAdapter from "axios-mock-adapter";
import ProfilePage from "main/pages/ProfilePage";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
import mockConsole from "jest-mock-console";

describe("ProfilePage tests", () => {
    const location= window.location;
    delete window.location;
    window.location = {
        ...location,
        reload: jest.fn()
    };
    const queryClient = new QueryClient();

    test("renders correctly for regular logged in user", async () => {

        const axiosMock =new AxiosMockAdapter(axios);
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
        axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
        axiosMock.onPut('/api/userprofile?cellPhone=1234').reply(200, {
            id: 1,
            cellPhone: "1234",
            email: "phtcon@ucsb.edu",
            googleSub: "115856948234298493496",
            pictureUrl: "https://lh3.googleusercontent.com/-bQynVrzVIrU/AAAAAAAAAAI/AAAAAAAAAAA/AMZuucmkGuVsELD1ZeV5iDUAUfe6_K-p8w/s96-c/photo.jpg",
            fullName: "Phill Conrad",
            givenName: "Phill",
            familyName: "Conrad",
            emailVerified: true,
            locale: "en",
            hostedDomain: "ucsb.edu",
            admin: true,
            driver: false,
            rider: false
        });

        const { getByText } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <ProfilePage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor( () => expect(getByText("Phillip Conrad")).toBeInTheDocument() );
        expect(getByText("pconrad.cis@gmail.com")).toBeInTheDocument();
        const updatePhoneButton = getByText("Update Phone Number")
        expect(updatePhoneButton).toBeInTheDocument();
        const logSpy = jest.spyOn(console, 'log');

        expect(screen.queryByText("Update Phone Number:")).not.toBeInTheDocument()
        fireEvent.click(updatePhoneButton);
        // expect(logSpy).toHaveBeenCalledWith('true modal');
        await waitFor( () => expect(getByText("Update Phone Number:")).toBeInTheDocument() );
        expect(getByText("Enter New Phone Number")).toBeInTheDocument();

        const updateButton = getByText("Update")
        const cancelButton = getByText("Cancel")
        expect(updateButton).toBeInTheDocument();
        expect(cancelButton).toBeInTheDocument();

        fireEvent.click(cancelButton);
        // expect(screen.queryByText("Update Phone Number:")).not.toBeInTheDocument()
        // expect(logSpy).toHaveBeenCalledWith('false modal');
        // expect(handleClose).toHaveBeenCalledTimes(1)
        // const newPhone = screen.getByTestId("new_phone")
        expect(getByText("Update Phone Number:"));
        

        fireEvent.click(updatePhoneButton);
      
        // Assert


        const newPhone = screen.getByTestId("new_phone")
        expect(newPhone).toBeInTheDocument();
        expect(newPhone).toHaveValue("");

        fireEvent.change(newPhone,{target: {value: '1234'}});
        
        updateButton.click();
        expect(window.location.reload).toHaveBeenCalledTimes(1);
        expect(axiosMock.history.put.length).toBe(1); // times called
        // expect(axiosMock.history.put[0].params).toEqual({ cellPhone: "1234" });

        // jest.restoreAllMocks();
        // window.location = location;

    });

    test("renders correctly for admin user", async () => {

        const axiosMock =new AxiosMockAdapter(axios);
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.adminUser);
        axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);

        const { getByText, getByTestId, queryByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <ProfilePage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor( () => expect(getByText("Phill Conrad")).toBeInTheDocument() );
        expect(getByText("phtcon@ucsb.edu")).toBeInTheDocument();
        expect(getByTestId("role-badge-user")).toBeInTheDocument();
        expect(getByTestId("role-badge-admin")).toBeInTheDocument();
        expect(getByTestId("role-badge-member")).toBeInTheDocument();

        expect(getByTestId("role-missing-driver")).toBeInTheDocument();
        expect(getByTestId("role-missing-rider")).toBeInTheDocument();

        expect(queryByTestId("role-badge-driver")).not.toBeInTheDocument();
        expect(queryByTestId("role-badge-rider")).not.toBeInTheDocument();
    });


    test("renders correctly for driver", async () => {

        const axiosMock =new AxiosMockAdapter(axios);
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.driverOnly);
        axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);

        const { getByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <ProfilePage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor( () =>  expect(getByTestId("role-badge-driver")).toBeInTheDocument() );
        expect(getByTestId("role-missing-admin")).toBeInTheDocument();
        expect(getByTestId("role-missing-member")).toBeInTheDocument();
        expect(getByTestId("role-missing-rider")).toBeInTheDocument();
    });

    test("renders correctly for rider", async () => {

        const axiosMock =new AxiosMockAdapter(axios);
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.riderOnly);
        axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);

        const { getByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <ProfilePage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor( () => expect(getByTestId("role-badge-rider")).toBeInTheDocument() );        
        expect(getByTestId("role-missing-driver")).toBeInTheDocument();
        expect(getByTestId("role-missing-admin")).toBeInTheDocument();
        expect(getByTestId("role-missing-member")).toBeInTheDocument();
    });
});


