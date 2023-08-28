import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import ShiftEditPage from "main/pages/Shift/ShiftEditPage";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";

import { apiCurrentUserFixtures } from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
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

const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => {
    const originalModule = jest.requireActual('react-router-dom');
    return {
        __esModule: true,
        ...originalModule,
        useParams: () => ({
            id: 17
        }),
        Navigate: (x) => { mockNavigate(x); return null; }
    };
});

describe("ShiftEditPage tests", () => {

    describe("when the backend doesn't return data", () => {

        const axiosMock = new AxiosMockAdapter(axios);

        beforeEach(() => {
            axiosMock.reset();
            axiosMock.resetHistory();
            axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
            axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
            axiosMock.onGet("/api/shift", { params: { id: 17 } }).timeout();
        });

        const queryClient = new QueryClient();
        test("renders header but table is not present", async () => {

            const restoreConsole = mockConsole();

            render(
                <QueryClientProvider client={queryClient}>
                    <MemoryRouter>
                        <ShiftEditPage />
                    </MemoryRouter>
                </QueryClientProvider>
            );
            await screen.findByText("Edit Shift");
            expect(screen.queryByTestId("shift-day")).not.toBeInTheDocument();
            restoreConsole();
        });
    });

    describe("tests where backend is working normally", () => {

        const axiosMock = new AxiosMockAdapter(axios);

        beforeEach(() => {
            axiosMock.reset();
            axiosMock.resetHistory();
            axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
            axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
            axiosMock.onGet("/api/shift", { params: { id: 17 } }).reply(200, {
                id: 17,
                day: "Monday",
                shiftStart: "11:00AM",
                shiftEnd: "1:00PM",
                driverID: 1,
                driverBackupID: 2,
            });
            axiosMock.onPut('/api/shift').reply(200, {
                id: 17,
                day: "Tuesday",
                shiftStart: "2:00PM",
                shiftEnd: "4:00PM",
                driverID: 1,
                driverBackupID: 2,
            });
        });

        const queryClient = new QueryClient();
    
        test("Is populated with the data provided", async () => {

            render(
                <QueryClientProvider client={queryClient}>
                    <MemoryRouter>
                        <ShiftEditPage />
                    </MemoryRouter>
                </QueryClientProvider>
            );

            await screen.findByTestId("ShiftForm-id");

            const idField = screen.getByTestId("ShiftForm-id");
            const dayField = screen.getByTestId("ShiftForm-day");
            const shiftStartField = screen.getByTestId("ShiftForm-shiftStart");
            const shiftEndField = screen.getByTestId("ShiftForm-shiftEnd");
            const driverIDField = screen.getByTestId("ShiftForm-driverID");
            const driverBackupIDField = screen.getByTestId("ShiftForm-driverID");

            const submitButton = screen.getByTestId("ShiftForm-submit");

            expect(idField).toBeInTheDocument();
            expect(idField).toHaveValue("17");
            expect(dayField).toBeInTheDocument();
            expect(dayField).toHaveValue("Monday");
            expect(shiftStartField).toBeInTheDocument();
            expect(shiftStartField).toHaveValue("11:00AM");
            expect(shiftEndField).toBeInTheDocument();
            expect(shiftEndField).toHaveValue("1:00PM");
            expect(driverIDField).toBeInTheDocument();
            expect(driverIDField).toHaveValue(1);
            expect(driverBackupIDField).toBeInTheDocument();
            expect(driverBackupIDField).toHaveValue(2);

            expect(submitButton).toHaveTextContent("Update");
            
            fireEvent.change(dayField, { target: { value: 'Tuesday' } });
            fireEvent.change(shiftStartField, { target: { value: '2:00PM' } });
            fireEvent.change(shiftEndField, { target: { value: '4:00PM' } });
            fireEvent.change(driverIDField, { target: { value: 2 } });
            fireEvent.change(driverBackupIDField, { target: { value: 3 } });

            fireEvent.click(submitButton);

            await waitFor(() => expect(mockToast).toBeCalled());
            expect(mockToast).toBeCalledWith("Shift Updated - id: 17 time: 2:00PM - 4:00PM");
            
            expect(mockNavigate).toBeCalledWith({ "to": "/shift/list" });

            expect(axiosMock.history.put.length).toBe(1); // times called
            expect(axiosMock.history.put[0].params).toEqual({ id: 17 });
            expect(axiosMock.history.put[0].data).toBe(JSON.stringify({
                id: 17,
                day: "Tuesday",
                shiftStart: "2:00PM",
                shiftEnd: "4:00PM",
                driverID: 2,
                driverBackupID: 3,
            })); // posted object


        });

        test("Changes when you click Update", async () => {

            render(
                <QueryClientProvider client={queryClient}>
                    <MemoryRouter>
                        <ShiftEditPage />
                    </MemoryRouter>
                </QueryClientProvider>
            );

            await screen.findByTestId("ShiftForm-id");

            const idField = screen.getByTestId("ShiftForm-id");
            const dayField = screen.getByTestId("ShiftForm-day");
            const shiftStartField = screen.getByTestId("ShiftForm-shiftStart");
            const shiftEndField = screen.getByTestId("ShiftForm-shiftEnd");
            const driverIDField = screen.getByTestId("ShiftForm-driverID");
            const driverBackupIDField = screen.getByTestId("ShiftForm-driverID");

            const submitButton = screen.getByTestId("ShiftForm-submit");

            expect(idField).toHaveValue("17");
            expect(dayField).toHaveValue("Monday");
            expect(shiftStartField).toHaveValue("11:00AM");
            expect(shiftEndField).toHaveValue("1:00PM");
            expect(driverIDField).toHaveValue(1);
            expect(driverBackupIDField).toHaveValue(2);
            expect(submitButton).toBeInTheDocument();

            fireEvent.change(dayField, { target: { value: 'Tuesday' } });
            fireEvent.change(shiftStartField, { target: { value: '2:00PM' } });
            fireEvent.change(shiftEndField, { target: { value: '4:00PM' } });
            fireEvent.change(driverIDField, { target: { value: 2 } });
            fireEvent.change(driverBackupIDField, { target: { value: 3 } });

            fireEvent.click(submitButton);


            await waitFor(() => expect(mockToast).toBeCalled());
            expect(mockToast).toBeCalledWith("Shift Updated - id: 17 time: 2:00PM - 4:00PM");
            expect(mockNavigate).toBeCalledWith({ "to": "/shift/list" });
        });

       
    });
    

});


