import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import { BrowserRouter as Router } from "react-router-dom";

import RiderApplicationForm from "main/components/RiderApplication/RiderApplicationForm";
import riderApplicationFixtures from 'fixtures/riderApplicationFixtures';

import { QueryClient, QueryClientProvider } from "react-query";

const mockedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockedNavigate
}));

describe("RideForm tests", () => {
    const queryClient = new QueryClient();

    // const expectedHeaders = ["Day of Week", "Start Time", "End Time", "Pick Up Location", "Drop Off Location", "Room Number for Dropoff", "Course Number"];
    const testId = "RiderApplicationForm";

    test("renders correctly with no initialContents", async () => {
        render(
            <QueryClientProvider client={queryClient}>
                <Router>
                    <RiderApplicationForm />
                </Router>
            </QueryClientProvider>
        );

        expect(await screen.findByText(/Create/)).toBeInTheDocument();

        const expectedHeaders = ["Perm Number", "Description"]

        expectedHeaders.forEach((headerText) => {
            const header = screen.getByText(headerText);
            expect(header).toBeInTheDocument();
          });

    });

    test("renders correctly when passing in pending application", async () => {
        render(
            <QueryClientProvider client={queryClient}>
                <Router>
                    <RiderApplicationForm initialContents={riderApplicationFixtures.oneRiderApplicationPending} userEmail="test@ucsb.edu" />
                </Router>
            </QueryClientProvider>
        );

        expect(await screen.findByText(/Create/)).toBeInTheDocument();

        const expectedHeaders = ["Applicant ID", "Status", "Email", "Perm Number", "Description", "Notes", "Date Applied:", "Last Updated:"]
        const expectedfields = ["id","status","email","perm_number","description","notes","created_date","updated_date"]
        expectedHeaders.forEach((headerText) => {
            const header = screen.getByText(headerText);
            expect(header).toBeInTheDocument();
          });
        expectedfields.forEach((field) =>{
            const fieldElement = screen.getByTestId(`${testId}-${field}`)
            expect(fieldElement).toBeInTheDocument();
            expect(fieldElement).toHaveValue(field=="email" ? "test@ucsb.edu": String(riderApplicationFixtures.oneRiderApplicationPending[field]));
        })

    });
    

    test("renders correctly when passing in cancelled application", async () => {
        render(
            <QueryClientProvider client={queryClient}>
                <Router>
                    <RiderApplicationForm initialContents={riderApplicationFixtures.oneRiderApplicationCancelled} userEmail="test@ucsb.edu" />
                </Router>
            </QueryClientProvider>
        );

        expect(await screen.findByText(/Create/)).toBeInTheDocument();

        const expectedHeaders = ["Applicant ID", "Status", "Email", "Perm Number", "Description", "Notes", "Date Applied:", "Last Updated:", "Application Cancelled On:"]
        const expectedfields = ["id","status","email","perm_number","description","notes","created_date","updated_date","cancelled_date"]
        expectedHeaders.forEach((headerText) => {
            const header = screen.getByText(headerText);
            expect(header).toBeInTheDocument();
          });
        expectedfields.forEach((field) =>{
            const fieldElement = screen.getByTestId(`${testId}-${field}`)
            expect(fieldElement).toBeInTheDocument();
            expect(fieldElement).toHaveValue(field=="email" ? "test@ucsb.edu": String(riderApplicationFixtures.oneRiderApplicationCancelled[field]));
        })

    });

});