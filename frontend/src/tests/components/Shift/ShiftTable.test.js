import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import shiftFixtures from "fixtures/shiftFixtures";
import ShiftTable from "main/components/Shift/ShiftTable"
import { BrowserRouter as Router } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import { currentUserFixtures } from "fixtures/currentUserFixtures";


const mockedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockedNavigate
}));




describe("ShiftTable tests", () => {
    const queryClient = new QueryClient();
    const testId = "ShiftTable"

    test("renders without crashing for empty table", () => {
        render(
            <QueryClientProvider client={queryClient}>
                <Router>
                <ShiftTable shift={[]} />
                </Router>
            </QueryClientProvider>
        );
    });

    test("renders without crashing for three shifts", () => {
        render(
            <QueryClientProvider client={queryClient}>
                <Router>
                <ShiftTable shift={shiftFixtures.threeShifts} />
                </Router>
            </QueryClientProvider>
        );
    });

    test("Has the expected column headers and content", () => {
        const { getByText, getByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <Router>
                <ShiftTable shift={shiftFixtures.threeShifts} currentUser={currentUserFixtures.driverOnly}/>
                </Router>
            </QueryClientProvider>
        );
    
        const expectedHeaders = ["id", "Day", "Shift start", "Shift end", "Driver", "Backup driver"];
        const expectedFields = ["id", "day", "shiftStart", "shiftEnd", "driverID", "driverBackupID"];

        expectedHeaders.forEach( (headerText)=> {
            const header = getByText(headerText);
            expect(header).toBeInTheDocument();
        });

        expectedFields.forEach( (field)=> {
          const header = getByTestId(`${testId}-cell-row-0-col-${field}`);
          expect(header).toBeInTheDocument();
        });
        const editButton = screen.queryByTestId(`${testId}-cell-row-0-col-Edit-button`);
        expect(editButton).not.toBeInTheDocument();
        const deleteButton = screen.queryByTestId(`${testId}-cell-row-0-col-Delete-button`);
        expect(deleteButton).not.toBeInTheDocument();

        expect(getByTestId(`${testId}-cell-row-0-col-id`)).toHaveTextContent("1");
        expect(getByTestId(`${testId}-cell-row-0-col-day`)).toHaveTextContent("Monday");
        expect(getByTestId(`${testId}-cell-row-0-col-shiftStart`)).toHaveTextContent("08:00AM");
        expect(getByTestId(`${testId}-cell-row-0-col-shiftEnd`)).toHaveTextContent("11:00AM");
        expect(getByTestId(`${testId}-cell-row-0-col-driverID`)).toHaveTextContent("1");
        expect(getByTestId(`${testId}-cell-row-1-col-id`)).toHaveTextContent("2");
        expect(getByTestId(`${testId}-cell-row-1-col-day`)).toHaveTextContent("Tuesday");
        expect(getByTestId(`${testId}-cell-row-1-col-shiftStart`)).toHaveTextContent("11:00AM");
        expect(getByTestId(`${testId}-cell-row-1-col-shiftEnd`)).toHaveTextContent("02:00PM");
        expect(getByTestId(`${testId}-cell-row-1-col-driverID`)).toHaveTextContent("2");

      });

      test("Edit button navigates to the edit page", async () => {
        // arrange
        const currentUser = currentUserFixtures.adminUser;
    
        // act - render the component
        render(
          <QueryClientProvider client={queryClient}>
            <MemoryRouter>
              <ShiftTable shift={shiftFixtures.threeShifts} currentUser={currentUser} />
            </MemoryRouter>
          </QueryClientProvider>
        );
    
        // assert - check that the expected content is rendered
        expect(await screen.findByTestId(`${testId}-cell-row-0-col-id`)).toHaveTextContent("1");
        expect(screen.getByTestId(`${testId}-cell-row-0-col-day`)).toHaveTextContent("Monday");
    
        const editButton = screen.getByTestId(`${testId}-cell-row-0-col-Edit-button`);
        expect(editButton).toBeInTheDocument();
        expect(editButton).toHaveClass("btn-primary");
    
        // act - click the edit button
        fireEvent.click(editButton);
    
        // assert - check that the navigate function was called with the expected path
        await waitFor(() => expect(mockedNavigate).toHaveBeenCalledWith('/shift/edit/1'));
    
      });
    test("Delete button calls delete callback", async () => {
        // arrange
        const currentUser = currentUserFixtures.adminUser;

        // act - render the component
        render(
        <QueryClientProvider client={queryClient}>
            <MemoryRouter>
            <ShiftTable shift={shiftFixtures.threeShifts} currentUser={currentUser} />
            </MemoryRouter>
        </QueryClientProvider>
        );

        // assert - check that the expected content is rendered
        expect(await screen.findByTestId(`${testId}-cell-row-0-col-id`)).toHaveTextContent("1");
        expect(screen.getByTestId(`${testId}-cell-row-0-col-day`)).toHaveTextContent("Monday");

        const deleteButton = screen.getByTestId(`${testId}-cell-row-0-col-Delete-button`);
        expect(deleteButton).toBeInTheDocument();
        expect(deleteButton).toHaveClass("btn-danger");

        // act - click the delete button
        fireEvent.click(deleteButton);
    });
});
