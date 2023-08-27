import React from "react";
import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import ShiftTable from "main/components/Shift/ShiftTable"

import { useBackend } from "main/utils/useBackend";
import { useCurrentUser , hasRole} from 'main/utils/currentUser'
import { Button } from 'react-bootstrap';
const ShiftPage = () => {
    const currentUser = useCurrentUser();

    const { data: shift, error: _error, status: _status } =
        useBackend(
            // Stryker disable next-line all : don't test internal caching of React Query
            ["/api/shift/all"],
            // Stryker disable next-line StringLiteral,ObjectLiteral : since "GET" is default, "" is an equivalent mutation
            { method: "GET", url: "/api/shift/all" },
            []
        );
    const createButton = () => {
        if (hasRole(currentUser, "ROLE_ADMIN")) {
            return (
                <Button
                    variant="primary"
                    href="/shift/create"
                    style={{ float: "right" }}
                >
                    Create Shift
                </Button>
            )
        } 
    }
    return (
        <BasicLayout>
            {createButton()}
            <h2>Shift</h2>
            <ShiftTable shift={shift} currentUser={currentUser}/>
        </BasicLayout>
    );
};

export default ShiftPage;