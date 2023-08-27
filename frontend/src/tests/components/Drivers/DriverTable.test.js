import { render } from "@testing-library/react";
import usersFixtures from "fixtures/usersFixtures";
import DriverTable from "main/components/Drivers/DriverTable"
import { QueryClient, QueryClientProvider } from "react-query";


describe("DriverTable tests", () => {
    const queryClient = new QueryClient();

    test("renders without crashing for empty table", () => {
        render(
            <QueryClientProvider client={queryClient}>
                <DriverTable users={[]} />
            </QueryClientProvider>
        );
    });

    test("renders without crashing for three users", () => {
        render(
            <QueryClientProvider client={queryClient}>
                <DriverTable users={usersFixtures.threeDrivers} />
            </QueryClientProvider>
        );
    });

    test("Has the expected column headers and content", () => {
        const { getByText, getByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <DriverTable users={usersFixtures.threeDrivers}/>
            </QueryClientProvider>
        );
    
        const expectedHeaders = ["First Name", "Last Name", "Phone Number", "Email"];
        const expectedFields = ["givenName", "familyName", "cellPhone","email"];
        const testId = "DriverTable";

        expectedHeaders.forEach( (headerText)=> {
            const header = getByText(headerText);
            expect(header).toBeInTheDocument();
        });

        expectedFields.forEach( (field)=> {
          const header = getByTestId(`${testId}-cell-row-0-col-${field}`);
          expect(header).toBeInTheDocument();
        });

        expect(getByTestId(`${testId}-cell-row-0-col-givenName`)).toHaveTextContent("Phill");
        expect(getByTestId(`${testId}-cell-row-0-col-familyName`)).toHaveTextContent("Conrad");
        expect(getByTestId(`${testId}-cell-row-0-col-cellPhone`)).toHaveTextContent("8008135000");
        expect(getByTestId(`${testId}-cell-row-0-col-email`)).toHaveTextContent("phtcon@ucsb.edu");

        expect(getByTestId(`${testId}-cell-row-1-col-givenName`)).toHaveTextContent("Phillip");
        expect(getByTestId(`${testId}-cell-row-1-col-familyName`)).toHaveTextContent("Conrad");
        expect(getByTestId(`${testId}-cell-row-1-col-cellPhone`)).toHaveTextContent("911");
        expect(getByTestId(`${testId}-cell-row-1-col-email`)).toHaveTextContent("pconrad.cis@gmail.com");

        expect(getByTestId(`${testId}-cell-row-2-col-givenName`)).toHaveTextContent("Craig");
        expect(getByTestId(`${testId}-cell-row-2-col-familyName`)).toHaveTextContent("Zzyxx");
        expect(getByTestId(`${testId}-cell-row-2-col-cellPhone`)).toHaveTextContent("8002738255");
        expect(getByTestId(`${testId}-cell-row-2-col-email`)).toHaveTextContent("craig.zzyzx@example.org");

      });
});

