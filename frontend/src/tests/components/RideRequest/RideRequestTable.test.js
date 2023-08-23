import { fireEvent, render, waitFor, screen } from "@testing-library/react";
import { rideReqFixtures } from "fixtures/rideRequestFixtures";
import RideRequestTable from "main/components/RideRequest/RideRequestTable";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import { currentUserFixtures } from "fixtures/currentUserFixtures";


const mockedNavigate = jest.fn();

const mockDeleteMutation = jest.fn();
jest.mock('react-query', () => ({
  ...jest.requireActual('react-query'),
  useMutation: () => ({
    mutate: mockDeleteMutation,
  }),
}));


jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockedNavigate
}));

describe("RideRequestTable tests", () => {
  const queryClient = new QueryClient();


  test("renders without crashing for empty table with user not logged in", () => {
    const currentUser = null;

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <RideRequestTable rideReqs={[]} currentUser={currentUser} />
        </MemoryRouter>
      </QueryClientProvider>

    );
  });
  test("renders without crashing for empty table for ordinary user", () => {
    const currentUser = currentUserFixtures.userOnly;

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <RideRequestTable rideReqs={[]} currentUser={currentUser} />
        </MemoryRouter>
      </QueryClientProvider>

    );
  });

  test("renders without crashing for empty table for admin", () => {
    const currentUser = currentUserFixtures.adminUser;

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <RideRequestTable rideReqs={[]} currentUser={currentUser} />
        </MemoryRouter>
      </QueryClientProvider>

    );
  });

  test("renders without crashing for empty table for driver", () => {
    const currentUser = currentUserFixtures.driverOnly;

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <RideRequestTable rideReqs={[]} currentUser={currentUser} />
        </MemoryRouter>
      </QueryClientProvider>

    );
  });


  test("Has the expected column headers and content for adminUser", () => {

    const currentUser = currentUserFixtures.adminUser;

    const { getByText, getByTestId } = render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <RideRequestTable rideReqs={rideReqFixtures.threeRideReqs} currentUser={currentUser} />
        </MemoryRouter>
      </QueryClientProvider>

    );

    const expectedHeaders = [ 'id', 'Day', 'Student', 'Driver', 'Start Time', 'End Time', 'Pick-up Location', 'Pick-up Room #', 'Drop-off Location', 'Drop-off Room #', 'Course', 'Notes' ];
    const expectedFields = [ 'id', 'day', 'student', 'driver', 'startTime', 'endTime', 'pickupLocation', 'pickupRoom', 'dropoffLocation', 'dropoffRoom', 'course', 'notes' ];
    const testId = "RideRequestTable";

    expectedHeaders.forEach((headerText) => {
      const header = getByText(headerText);
      expect(header).toBeInTheDocument();
    });

    expectedFields.forEach((field) => {
      const header = getByTestId(`${testId}-cell-row-0-col-${field}`);
      expect(header).toBeInTheDocument();
    });

    expect(getByTestId(`${testId}-cell-row-0-col-id`)).toHaveTextContent("2");
    expect(getByTestId(`${testId}-cell-row-1-col-id`)).toHaveTextContent("3");

    const editButton = getByTestId(`${testId}-cell-row-0-col-Edit-button`);
    expect(editButton).toBeInTheDocument();
    expect(editButton).toHaveClass("btn-primary");

    const deleteButton = getByTestId(`${testId}-cell-row-0-col-Delete-button`);
    expect(deleteButton).toBeInTheDocument();
    expect(deleteButton).toHaveClass("btn-danger");

  });

  test("Has the expected column headers and content for ordinary user", () => {

    const currentUser = currentUserFixtures.userOnly;

    const { getByText, getByTestId } = render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <RideRequestTable rideReqs={rideReqFixtures.threeRideReqs} currentUser={currentUser} />
        </MemoryRouter>
      </QueryClientProvider>

    );

    const expectedHeaders = [ 'id', 'Day', 'Student', 'Driver', 'Start Time', 'End Time', 'Pick-up Location', 'Pick-up Room #', 'Drop-off Location', 'Drop-off Room #', 'Course', 'Notes' ];
    const expectedFields = [ 'id', 'day', 'student', 'driver', 'startTime', 'endTime', 'pickupLocation', 'pickupRoom', 'dropoffLocation', 'dropoffRoom', 'course', 'notes' ];
    const testId = "RideRequestTable";

    expectedHeaders.forEach((headerText) => {
      const header = getByText(headerText);
      expect(header).toBeInTheDocument();
    });

    expectedFields.forEach((field) => {
      const header = getByTestId(`${testId}-cell-row-0-col-${field}`);
      expect(header).toBeInTheDocument();
    });

    expect(getByTestId(`${testId}-cell-row-0-col-id`)).toHaveTextContent("2");
    expect(getByTestId(`${testId}-cell-row-1-col-id`)).toHaveTextContent("3");

    expect(screen.queryByText("Delete")).not.toBeInTheDocument();
    expect(screen.queryByText("Edit")).not.toBeInTheDocument();

  });

  test("Has the expected column headers and content for driver", () => {

    const currentUser = currentUserFixtures.driverOnly;

    const { getByText, getByTestId } = render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <RideRequestTable rideReqs={rideReqFixtures.threeRideReqs} currentUser={currentUser} />
        </MemoryRouter>
      </QueryClientProvider>

    );

    const expectedHeaders = [ 'id', 'Day', 'Student', 'Start Time', 'End Time', 'Pick-up Location', 'Pick-up Room #', 'Drop-off Location', 'Drop-off Room #', 'Course', 'Notes' ];
    const expectedFields = [ 'id', 'day', 'student', 'startTime', 'endTime', 'pickupLocation', 'pickupRoom', 'dropoffLocation', 'dropoffRoom', 'course', 'notes' ];
    const testId = "RideRequestTable";

    expectedHeaders.forEach((headerText) => {
      const header = getByText(headerText);
      expect(header).toBeInTheDocument();
    });

    expectedFields.forEach((field) => {
      const header = getByTestId(`${testId}-cell-row-0-col-${field}`);
      expect(header).toBeInTheDocument();
    });

    expect(getByTestId(`${testId}-cell-row-0-col-id`)).toHaveTextContent("2");
    expect(getByTestId(`${testId}-cell-row-1-col-id`)).toHaveTextContent("3");

    expect(screen.queryByText("Delete")).not.toBeInTheDocument();
    expect(screen.queryByText("Edit")).not.toBeInTheDocument();

  });

  test("Edit button navigates to the edit page for admin user", async () => {

    const currentUser = currentUserFixtures.adminUser;

    const { getByTestId } = render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <RideRequestTable rideReqs={rideReqFixtures.threeRideReqs} currentUser={currentUser} />
        </MemoryRouter>
      </QueryClientProvider>

    );

    await waitFor(() => { expect(getByTestId(`RideRequestTable-cell-row-0-col-id`)).toHaveTextContent("2"); });

    const editButton = getByTestId(`RideRequestTable-cell-row-0-col-Edit-button`);
    expect(editButton).toBeInTheDocument();
    
    fireEvent.click(editButton);

    await waitFor(() => expect(mockedNavigate).toHaveBeenCalledWith('/ride-request/edit/2'));

  });

  test("Edit button navigates to the edit page for rider", async () => {

    const currentUser = currentUserFixtures.riderOnly;

    const {getByTestId } = render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <RideRequestTable rideReqs={rideReqFixtures.threeRideReqs} currentUser={currentUser} />
        </MemoryRouter>
      </QueryClientProvider>

    );

    await waitFor(() => { expect(getByTestId(`RideRequestTable-cell-row-0-col-id`)).toHaveTextContent("2"); });

    const editButton = getByTestId(`RideRequestTable-cell-row-0-col-Edit-button`);
    expect(editButton).toBeInTheDocument();
    
    fireEvent.click(editButton);

    await waitFor(() => expect(mockedNavigate).toHaveBeenCalledWith('/ride-request/edit/2'));

  });

  
  test("Delete button calls deleteMutation for rider (which admin is classifed as)", async () => {

    const currentUser = currentUserFixtures.adminOnly;
  
    const { getByTestId } = render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <RideRequestTable rideReqs={rideReqFixtures.threeRideReqs} currentUser={currentUser} />
        </MemoryRouter>
      </QueryClientProvider>
    );
  
    await waitFor(() => { expect(getByTestId(`RideRequestTable-cell-row-0-col-id`)).toHaveTextContent("2"); });
  
    const deleteButton = getByTestId(`RideRequestTable-cell-row-0-col-Delete-button`);
    expect(deleteButton).toBeInTheDocument();
    
    fireEvent.click(deleteButton);
  
    expect(mockDeleteMutation).toHaveBeenCalled();
  });
  
  
});