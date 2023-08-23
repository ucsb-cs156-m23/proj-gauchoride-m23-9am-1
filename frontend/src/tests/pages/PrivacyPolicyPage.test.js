import { getByDisplayValue, render, waitFor } from "@testing-library/react";
import PrivacyPolicyPage from "main/pages/PrivacyPolicyPage";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";

import { apiCurrentUserFixtures }  from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
import axios from "axios";
import AxiosMockAdapter from "axios-mock-adapter";

const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockNavigate
}));

describe("PageNotFound tests", () => {

    const axiosMock =new AxiosMockAdapter(axios);
    axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
    axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);

    const queryClient = new QueryClient();
    test("renders without crashing", () => {
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <PrivacyPolicyPage />
                </MemoryRouter>
            </QueryClientProvider>
        );
    });

    test("renders privacy policy HTML correctly", async () => {
        const { getByText } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <PrivacyPolicyPage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor( () => expect(getByText("PRIVACY POLICY")).toBeInTheDocument() );
    });

});
