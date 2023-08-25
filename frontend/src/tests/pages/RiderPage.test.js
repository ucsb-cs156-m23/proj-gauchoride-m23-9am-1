import { render, waitFor } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import RiderPage from "main/pages/RiderPage";


import { apiCurrentUserFixtures } from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
import { rideReqFixtures } from "fixtures/rideRequestFixtures";
import axios from "axios";
import AxiosMockAdapter from "axios-mock-adapter";
import mockConsole from "jest-mock-console";


const mockToast = jest.fn();
jest.mock('react-toastify', () => {
    const originalModule = jest.requireActual('react-toastify');
    return {
        __esModule: true,
        ...originalModule,
        toast: (x) => mockToast(x)
    };
});

describe("RiderPage tests", () => {

    const axiosMock = new AxiosMockAdapter(axios);

    const testId = "RideRequestTable";

    const setupRiderOnly = () => {
        axiosMock.reset();
        axiosMock.resetHistory();
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.riderOnly);
        axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
    };

    const setupAdminUser = () => {
        axiosMock.reset();
        axiosMock.resetHistory();
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.adminUser);
        axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
    };

    test("renders without crashing for admin user", () => {

        setupAdminUser();
        const queryClient = new QueryClient();
        axiosMock.onGet("/api/ride_request/all").reply(200, []);

        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <RiderPage />
                </MemoryRouter>
            </QueryClientProvider>
        );

    });

    test("renders three rides without crashing for admin user", async () => {

        setupAdminUser();
        const queryClient = new QueryClient();
        axiosMock.onGet("/api/ride_request/all").reply(200, rideReqFixtures.threeRideReqs);

        const { getByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <RiderPage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor(() => { expect(getByTestId(`${testId}-cell-row-0-col-id`)).toHaveTextContent("2"); });
        expect(getByTestId(`${testId}-cell-row-1-col-id`)).toHaveTextContent("3");
        expect(getByTestId(`${testId}-cell-row-2-col-id`)).toHaveTextContent("4");

    });

    test("renders three rides without crashing for rider", async () => {

        setupRiderOnly();
        const queryClient = new QueryClient();
        axiosMock.onGet("/api/ride_request/all").reply(200, rideReqFixtures.threeRideReqs);

        const { getByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <RiderPage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor(() => { expect(getByTestId(`${testId}-cell-row-0-col-id`)).toHaveTextContent("2"); });
        expect(getByTestId(`${testId}-cell-row-1-col-id`)).toHaveTextContent("3");
        expect(getByTestId(`${testId}-cell-row-2-col-id`)).toHaveTextContent("4");

    });

    test("renders empty table when backend unavailable, rider only", async () => {

        setupRiderOnly();
        const queryClient = new QueryClient();
        axiosMock.onGet("/api/ride_request/all").timeout();
        const restoreConsole = mockConsole();

        const { queryByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <RiderPage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor(() => { expect(axiosMock.history.get.length).toBeGreaterThanOrEqual(1); });
        //console.debug(axiosMock.history);
        ///console.debug(console.error.mock);
        //const errorMessage = console.error.mock.calls[0][0];
        //expect(errorMessage).toMatch("Error communicating with backend via GET on /api/ride_request/all");

        restoreConsole();
        expect(queryByTestId(`${testId}-cell-row-0-col-id`)).not.toBeInTheDocument();

    });

});
