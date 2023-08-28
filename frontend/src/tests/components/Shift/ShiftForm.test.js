import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import { BrowserRouter as Router } from "react-router-dom";

import ShiftForm from "main/components/Shift/ShiftForm";
import shiftFixtures from "fixtures/shiftFixtures";

import { QueryClient, QueryClientProvider } from "react-query";

const mockedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockedNavigate
}));

describe("ShiftForm tests", () => {
    const queryClient = new QueryClient();

    const expectedHeaders = ["Day of Week", "Start Time", "End Time", "Driver ID", "Backup Driver ID"];
    const testId = "ShiftForm";

    test("renders correctly with no initialContents", async () => {
        render(
            <QueryClientProvider client={queryClient}>
                <Router>
                    <ShiftForm />
                </Router>
            </QueryClientProvider>
        );

        expect(await screen.findByText(/Create/)).toBeInTheDocument();

        expectedHeaders.forEach((headerText) => {
            const header = screen.getByText(headerText);
            expect(header).toBeInTheDocument();
          });
        expect(screen.getByTestId(`${testId}-cancel`)).toBeInTheDocument();
        expect(screen.getByTestId(`${testId}-day`)).toBeInTheDocument();
        expect(screen.getByTestId(`${testId}-driverID`)).toBeInTheDocument();
        expect(screen.getByTestId(`${testId}-driverBackupID`)).toBeInTheDocument();
        expect(screen.getByTestId(`${testId}-submit`)).toBeInTheDocument();

    });

    test("renders correctly when passing in initialContents", async () => {
        render(
            <QueryClientProvider client={queryClient}>
                <Router>
                    <ShiftForm initialContents={shiftFixtures.oneShift} />
                </Router>
            </QueryClientProvider>
        );

        expect(await screen.findByText(/Create/)).toBeInTheDocument();

        expectedHeaders.forEach((headerText) => {
            const header = screen.getByText(headerText);
            expect(header).toBeInTheDocument();
        });

        expect(await screen.findByTestId(`${testId}-id`)).toBeInTheDocument();
        expect(screen.getByText(`Id`)).toBeInTheDocument();
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

    test("that the correct validations are performed", async () => {
        render(
            <QueryClientProvider client={queryClient}>
                <Router>
                    <ShiftForm />
                </Router>
            </QueryClientProvider>
        );

        expect(await screen.findByText(/Create/)).toBeInTheDocument();
        const submitButton = screen.getByText(/Create/);
        fireEvent.click(submitButton);

        await screen.findByText(/Day is Required./);
        expect(screen.getByText(/Start Time is Required./)).toBeInTheDocument();
        expect(screen.getByText(/End Time is Required./)).toBeInTheDocument();
        expect(screen.getByText(/Driver's ID is required./)).toBeInTheDocument();
        expect(screen.getByText(/Backup Driver ID is required./)).toBeInTheDocument();

        const shiftStartInput = screen.queryByTestId(`${testId}-shiftStart`)
        const shiftEndInput = screen.queryByTestId(`${testId}-shiftEnd`)

        fireEvent.change(shiftStartInput, { target: { value: "a".repeat(10) } });
        fireEvent.change(shiftEndInput, { target: { value: "a".repeat(10) } });
        fireEvent.click(submitButton);

        await waitFor(() => {
            expect(screen.getByText("Please enter start time in the format HH:MM AM/PM (e.g., 03:30PM).")).toBeInTheDocument();
        });

        await waitFor(() => {
            expect(screen.getByText("Please enter end time in the format HH:MM AM/PM (e.g., 03:30PM).")).toBeInTheDocument();
        });

    });

});
