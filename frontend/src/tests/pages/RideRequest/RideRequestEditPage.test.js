import { fireEvent, render, waitFor } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import RideRequestEditPage from "main/pages/RideRequest/RideRequestEditPage";

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

describe("RideRequestEditPage tests", () => {

    describe("when the backend doesn't return a todo", () => {

        const axiosMock = new AxiosMockAdapter(axios);

        beforeEach(() => {
            axiosMock.reset();
            axiosMock.resetHistory();
            axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
            axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
            axiosMock.onGet("/api/ride_request", { params: { id: 17 } }).timeout();
        });

        const queryClient = new QueryClient();
        test("renders header but table is not present", async () => {

            const restoreConsole = mockConsole();

            const {queryByTestId, findByText} = render(
                <QueryClientProvider client={queryClient}>
                    <MemoryRouter>
                        <RideRequestEditPage />
                    </MemoryRouter>
                </QueryClientProvider>
            );
            await findByText("Edit Ride Request");
            expect(queryByTestId("RideRequestForm-day")).not.toBeInTheDocument();
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
            axiosMock.onGet("/api/ride_request", { params: { id: 17 } }).reply(200, {
                id: 17,
                day: "Monday",
                startTime: "3:30 PM",
                endTime: "4:30 PM", 
                pickupLocation: "Phelps",
                pickupRoom: "2225",
                dropoffLocation: "HSSB",
                dropoffRoom: "1215",
                course: "WRIT 105CD",
                notes: "hi :)"
            });
            axiosMock.onPut('/api/ride_request').reply(200, {
                id: 17,
                day: "Tuesday",
                startTime: "5:30 PM",
                endTime: "6:30 PM", 
                pickupLocation: "HSSB",
                pickupRoom: "2224",
                dropoffLocation: "SRB",
                dropoffRoom: "1214",
                course: "CMPSC 156",
                notes: "bye :("
            });
        });

        const queryClient = new QueryClient();
        test("renders without crashing", () => {

            render(
                <QueryClientProvider client={queryClient}>
                    <MemoryRouter>
                        <RideRequestEditPage />
                    </MemoryRouter>
                </QueryClientProvider>
            );

        });

        test("Is populated with the data provided", async () => {

            const { getByTestId, findByTestId } = render(
                <QueryClientProvider client={queryClient}>
                    <MemoryRouter>
                        <RideRequestEditPage />
                    </MemoryRouter>
                </QueryClientProvider>
            );

            await waitFor(() => { findByTestId("RideRequestForm-day"); });
            const dayField = getByTestId("RideRequestForm-day");
            const startTimeField = getByTestId("RideRequestForm-startTime");
            const endTimeField = getByTestId("RideRequestForm-endTime");
            const pickupLocationField = getByTestId("RideRequestForm-pickupLocation");
            const pickupRoomField = getByTestId("RideRequestForm-pickupRoom");
            const dropoffLocationField = getByTestId("RideRequestForm-dropoffLocation");
            const dropoffRoomField = getByTestId("RideRequestForm-dropoffRoom");
            const courseField = getByTestId("RideRequestForm-course");
            const notesField = getByTestId("RideRequestForm-notes");

            expect(dayField).toHaveValue("Monday");
            expect(startTimeField).toHaveValue("3:30 PM");
            expect(endTimeField).toHaveValue("4:30 PM");
            expect(pickupLocationField).toHaveValue("Phelps");
            expect(pickupRoomField).toHaveValue("2225");
            expect(dropoffLocationField).toHaveValue("HSSB");
            expect(dropoffRoomField).toHaveValue("1215");
            expect(courseField).toHaveValue("WRIT 105CD");
            expect(notesField).toHaveValue("hi :)");
            
        });

        test("Changes when you click Update", async () => {

            const { getByTestId, findByTestId } = render(
                <QueryClientProvider client={queryClient}>
                    <MemoryRouter>
                        <RideRequestEditPage />
                    </MemoryRouter>
                </QueryClientProvider>
            );

            await waitFor(() => { findByTestId("RideRequestForm-day"); });
            const dayField = getByTestId("RideRequestForm-day");
            const startTimeField = getByTestId("RideRequestForm-startTime");
            const endTimeField = getByTestId("RideRequestForm-endTime");
            const pickupLocationField = getByTestId("RideRequestForm-pickupLocation");
            const pickupRoomField = getByTestId("RideRequestForm-pickupRoom");
            const dropoffLocationField = getByTestId("RideRequestForm-dropoffLocation");
            const dropoffRoomField = getByTestId("RideRequestForm-dropoffRoom");
            const courseField = getByTestId("RideRequestForm-course");
            const notesField = getByTestId("RideRequestForm-notes");
            const submitButton = getByTestId("RideRequestForm-submit");

            expect(dayField).toHaveValue("Monday");
            expect(startTimeField).toHaveValue("3:30 PM");
            expect(endTimeField).toHaveValue("4:30 PM");
            expect(pickupLocationField).toHaveValue("Phelps");
            expect(pickupRoomField).toHaveValue("2225");
            expect(dropoffLocationField).toHaveValue("HSSB");
            expect(dropoffRoomField).toHaveValue("1215");
            expect(courseField).toHaveValue("WRIT 105CD");
            expect(notesField).toHaveValue("hi :)");
            expect(submitButton).toBeInTheDocument();

            fireEvent.change(dayField, { target: { value: 'Tuesday' } });
            fireEvent.change(startTimeField, { target: { value: '5:30 PM' } });
            fireEvent.change(endTimeField, { target: { value: '6:30 PM' } });
            fireEvent.change(pickupLocationField, { target: { value: 'HSSB' } });
            fireEvent.change(pickupRoomField, { target: { value: '2224' } });
            fireEvent.change(dropoffLocationField, { target: { value: 'SRB' } });
            fireEvent.change(dropoffRoomField, { target: { value: '1214' } });
            fireEvent.change(courseField, { target: { value: 'CMPSC 156' } });
            fireEvent.change(notesField, { target: { value: 'bye :(' } });
            fireEvent.click(submitButton);
    
            await waitFor(() => expect(mockToast).toHaveBeenCalled());
            expect(mockToast).toBeCalledWith("Ride Request Updated - id: 17");
            expect(mockNavigate).toBeCalledWith({ "to": "/rider" });

            expect(axiosMock.history.put.length).toBe(1); // times called
            expect(axiosMock.history.put[0].params).toEqual({ id: 17 });
            expect(axiosMock.history.put[0].data).toBe(JSON.stringify({
                day: "Tuesday",
                startTime: "5:30 PM",
                endTime: "6:30 PM", 
                pickupLocation: "HSSB",
                pickupRoom: "2224",
                dropoffLocation: "SRB",
                dropoffRoom: "1214",
                course: "CMPSC 156",
                notes: "bye :("
            })); // posted object

        });

       
    });
});
