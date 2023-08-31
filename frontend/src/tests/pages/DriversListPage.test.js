import { render, screen, waitFor} from "@testing-library/react";
import DriversListPage from "main/pages/DriversListPage";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";

import { apiCurrentUserFixtures } from "fixtures/currentUserFixtures";
import usersFixtures from "fixtures/usersFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
import axios from "axios";
import AxiosMockAdapter from "axios-mock-adapter";


describe("DriverPage tests", () => {

    const axiosMock = new AxiosMockAdapter(axios);

    const queryClient = new QueryClient();
    test("Renders expected content", async () => {
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.riderOnly);
        axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
        axiosMock.onGet("/api/drivers/all").reply(200, usersFixtures.threeDrivers);

        // act
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <DriversListPage/>
                </MemoryRouter>
            </QueryClientProvider>
        );

        // assert
        await waitFor( () => expect(screen.getByTestId("DriverTable-cell-row-0-col-givenName")).toHaveTextContent("Phill"));
        const lastName = screen.getByTestId("DriverTable-cell-row-0-col-familyName");
        expect(lastName).toHaveTextContent("Conrad");
        const phoneNumber = screen.getByTestId("DriverTable-cell-row-0-col-cellPhone");
        expect(phoneNumber).toHaveTextContent("8008135000");
        const email = screen.getByTestId("DriverTable-cell-row-0-col-email");
        expect(email).toHaveTextContent("phtcon@ucsb.edu");
    });

});