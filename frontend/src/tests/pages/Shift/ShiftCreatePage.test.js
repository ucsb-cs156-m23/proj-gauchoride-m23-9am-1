import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import ShiftCreatePage from "main/pages/Shift/ShiftCreatePage";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";

import { apiCurrentUserFixtures } from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";

import axios from "axios";
import AxiosMockAdapter from "axios-mock-adapter";

const mockToast = jest.fn();
jest.mock('react-toastify', () => {
    const originalModule = jest.requireActual('react-toastify');
    return {
        __esModule: true,
        ...originalModule,
        toast: (x) => mockToast(x)
    };
});

const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => {
    const originalModule = jest.requireActual('react-router-dom');
    return {
        __esModule: true,
        ...originalModule,
        Navigate: (x) => { mockNavigate(x); return null; }
    };
});

describe("ShiftCreatePage tests", () => {

    const axiosMock = new AxiosMockAdapter(axios);

    beforeEach(() => {
        axiosMock.reset();
        axiosMock.resetHistory();
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
        axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
    });


    const queryClient = new QueryClient();
    test("renders without crashing", () => {
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <ShiftCreatePage />
                </MemoryRouter>
            </QueryClientProvider>
        );
    });

    test("on submit, makes request to backend, and redirects to /shift/list", async () => {

        const queryClient = new QueryClient();
        const shift = {
            id: 1,
            day: "Monday",
            shiftStart: "11:00AM",
            shiftEnd: "1:00PM",
            driverID: 1,
            driverBackupID: 2
        };

        axiosMock.onPost("/api/shift/post").reply(202, shift);

        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <ShiftCreatePage />
                </MemoryRouter>
            </QueryClientProvider>
        )

        await waitFor(() => {
            expect(screen.getByTestId("ShiftForm-day")).toBeInTheDocument();
        });

        const dayInput = screen.getByLabelText("Day of Week");
        expect(dayInput).toBeInTheDocument();

        const shiftStartInput = screen.getByLabelText("Shift Start");
        expect(shiftStartInput).toBeInTheDocument();

        const shiftEndInput = screen.getByLabelText("Shift End");
        expect(shiftEndInput).toBeInTheDocument();

        const driverIDInput = screen.getByLabelText("Driver ID");
        expect(driverIDInput).toBeInTheDocument();

        const backupDriverIDInput = screen.getByLabelText("Backup Driver ID");
        expect(backupDriverIDInput).toBeInTheDocument();

        const createButton = screen.getByText("Create");
        expect(createButton).toBeInTheDocument();

        fireEvent.change(dayInput, { target: { value: 'Monday' } })
        fireEvent.change(shiftStartInput, { target: { value: '11:00AM' } })
        fireEvent.change(shiftEndInput, { target: { value: '1:00PM' } })
        fireEvent.change(driverIDInput, { target: { value: 1 } })
        fireEvent.change(backupDriverIDInput, { target: { value: 2 } })

        fireEvent.click(createButton);

        await waitFor(() => expect(axiosMock.history.post.length).toBe(1));

        expect(axiosMock.history.post[0].params).toEqual({
            day: "Monday",
            shiftStart: "11:00AM",
            shiftEnd: "1:00PM",
            driverID: "1",
            driverBackupID: "2"
        });

        // assert - check that the toast was called with the expected message
        expect(mockToast).toBeCalledWith("New Menu Item Created - id: 1 time: 11:AM - 1:00PM");
        expect(mockNavigate).toBeCalledWith({ "to": "/shift/list" });

    });

});


