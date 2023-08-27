
import React from "react";
import OurTable, { ButtonColumn } from "main/components/OurTable";

import { useBackendMutation } from "main/utils/useBackend";
import { cellToAxiosParamsDelete, onDeleteSuccess } from "main/utils/ShiftUtils"
import { useNavigate } from "react-router-dom";
import { hasRole } from "main/utils/currentUser";

export default function ShiftTable({ 
    shifts,
    currentUser,
    testIdPrefix = "ShiftTable"}) {

    const navigate = useNavigate();

    const editCallback = (cell) => {
        navigate(`/shift/edit/${cell.row.values.id}`)
    }

    const deleteMutation = useBackendMutation(
        cellToAxiosParamsDelete,
        { onSuccess: onDeleteSuccess },
        ["/api/shift/all"]
    );

    const deleteCallback = async (cell) => { deleteMutation.mutate(cell); }

    const columns = [
        {
            Header: 'id',
            accessor: 'id', // accessor is the "key" in the data
        },
        {
            Header: 'Day',
            accessor: 'day',
        },
        {
            Header: 'Shift Start',
            accessor: 'shiftStart',
        },
        {
            Header: 'Shift End',
            accessor: 'shiftEnd',
        },
        {
            Header: 'Driver ID',
            accessor: 'driverID'
        },
        {
            Header: 'Backup Backup ID',
            accessor: 'driverBackupID',
        }
    ];

    if (hasRole(currentUser, "ROLE_ADMIN")) {
        columns.push(ButtonColumn("Edit", "primary", editCallback, testIdPrefix));
        columns.push(ButtonColumn("Delete", "danger", deleteCallback, testIdPrefix));
    } 

    return <OurTable
        data={shifts}
        columns={columns}
        testid={testIdPrefix}
    />;
};