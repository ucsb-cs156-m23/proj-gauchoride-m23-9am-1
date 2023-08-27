import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import { BrowserRouter as Router } from "react-router-dom";

import ShiftForm from "main/components/Shift/ShiftForm";
import shiftFixtures from 'fixtures/shiftFixtures';

import { QueryClient, QueryClientProvider } from "react-query";

const mockedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockedNavigate
}));

describe("ShiftForm tests", () => {
    const queryClient = new QueryClient();

    // const expectedHeaders = ["Day of Week", "Start Time", "End Time", "Pick Up Location", "Drop Off Location", "Room Number for Dropoff", "Course Number"];
    const testId = "ShiftForm";

    test("renders correctly with no initialContents", async () => {
        render(
            <QueryClientProvider client={queryClient}>
                <Router>
                    <ShiftForm/>
                </Router>
            </QueryClientProvider>
        );

        expect(await screen.findByText(/Create/)).toBeInTheDocument();

        const expectedHeaders = ["Day","Shift Start", "Shift End", "Driver ID", "Backup Driver ID"]

        expectedHeaders.forEach((headerText) => {
            const header = screen.getByText(headerText);
            expect(header).toBeInTheDocument();
          });
        const cancelButton = screen.getByTestId(`${testId}-submit`);

        fireEvent.click(cancelButton);
        await screen.findByText(/Day is required./);
        expect(screen.getByText(/Shift Start is required./)).toBeInTheDocument();
        expect(screen.getByText(/Shift End is required./)).toBeInTheDocument();
        // expect(screen.getByText(/Driver ID is required./)).toBeInTheDocument();
        // expect(screen.getByText(/Backup Driver ID is required./)).toBeInTheDocument();
        const driverId = screen.getAllByText(/Driver ID is required./)[0]
        expect(driverId).toBeInTheDocument();
        const backupDriverId = screen.getAllByText(/Driver ID is required./)[1]
        expect(backupDriverId).toBeInTheDocument();
        
        

    });

    test("renders correctly when passing in one shift initialContents", async () => {
        render(
            <QueryClientProvider client={queryClient}>
                <Router>
                    <ShiftForm initialContents={shiftFixtures.oneShift} />
                </Router>
            </QueryClientProvider>
        );

        expect(await screen.findByText(/Create/)).toBeInTheDocument();

        const expectedHeaders = ["Id","Day","Shift Start", "Shift End", "Driver ID", "Backup Driver ID"]
        const expectedfields = ["id","day","shiftStart","shiftEnd","driverID","driverBackupID"]
        expectedHeaders.forEach((headerText) => {
            const header = screen.getByText(headerText);
            expect(header).toBeInTheDocument();
          });
        expectedfields.forEach((field) =>{
            const fieldElement = screen.getByTestId(`${testId}-${field}`)
            expect(fieldElement).toBeInTheDocument();
            expect(fieldElement).toHaveValue(String(shiftFixtures.oneShift[field]));
        })

    });

    test("that navigate(-1) is called when Cancel is clicked", async () => {
        render(
            <QueryClientProvider client={queryClient}>
                <Router>
                    <ShiftForm />
                </Router>
            </QueryClientProvider>
        );
        expect(await screen.findByTestId(`${testId}-cancel`)).toBeInTheDocument();
        const cancelButton = screen.getByTestId(`${testId}-cancel`);

        fireEvent.click(cancelButton);

        await waitFor(() => expect(mockedNavigate).toHaveBeenCalledWith(-1));
    });

});