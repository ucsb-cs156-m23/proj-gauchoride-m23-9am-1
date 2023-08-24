import React from "react";
import OurTable, { ButtonColumn } from "main/components/OurTable";
import { useBackendMutation } from "main/utils/useBackend";
import { cellToAxiosParamsDelete, onDeleteSuccess } from "main/utils/rideRequestUtils";
import { useNavigate } from "react-router-dom";
import { hasRole } from "main/utils/currentUser";

export default function RideRequestTable({ rideReqs, currentUser }) {

    const navigate = useNavigate();

    const editCallback = (cell) => {
        navigate(`/ride-request/edit/${cell.row.values.id}`)
    }

    // Stryker disable all : hard to test for query caching

    const deleteMutation = useBackendMutation(
        cellToAxiosParamsDelete,
        { onSuccess: onDeleteSuccess },
        ["/api/ride_request/all"]
    );
    // Stryker restore all 

    // Stryker disable next-line all : TODO try to make a good test for this
    const deleteCallback = async (cell) => { deleteMutation.mutate(cell); }


    const columnsAdmin = [
        { Header: 'id', accessor: 'id' },
        { Header: 'Day', accessor: 'day' },
        { Header: 'Student', accessor: 'student' },
        { Header: 'Driver', accessor: 'driver' },
        { Header: 'Start Time', accessor: 'startTime' },
        { Header: 'End Time', accessor: 'endTime' },
        { Header: 'Pick-up Location', accessor: 'pickupLocation' },
        { Header: 'Pick-up Room #', accessor: 'pickupRoom' },
        { Header: 'Drop-off Location', accessor: 'dropoffLocation' },
        { Header: 'Drop-off Room #', accessor: 'dropoffRoom' },
        { Header: 'Course',  accessor: 'course' },
        { Header: 'Notes',  accessor: 'notes' },
        ButtonColumn("Edit", "primary", editCallback, "RideRequestTable"),
        ButtonColumn("Delete", "danger", deleteCallback, "RideRequestTable")
    ];

    const columnsDriver = [
        { Header: 'id', accessor: 'id' },
        { Header: 'Day', accessor: 'day' },
        { Header: 'Student', accessor: 'student' },
        { Header: 'Start Time', accessor: 'startTime' },
        { Header: 'End Time', accessor: 'endTime' },
        { Header: 'Pick-up Location', accessor: 'pickupLocation' },
        { Header: 'Pick-up Room #', accessor: 'pickupRoom' },
        { Header: 'Drop-off Location', accessor: 'dropoffLocation' },
        { Header: 'Drop-off Room #', accessor: 'dropoffRoom' },
        { Header: 'Course',  accessor: 'course' },
        { Header: 'Notes',  accessor: 'notes' },
    ];

    const columnsRider = [
        { Header: 'id', accessor: 'id' },
        { Header: 'Day', accessor: 'day' },
        { Header: 'Driver', accessor: 'driver' },
        { Header: 'Start Time', accessor: 'startTime' },
        { Header: 'End Time', accessor: 'endTime' },
        { Header: 'Pick-up Location', accessor: 'pickupLocation' },
        { Header: 'Pick-up Room #', accessor: 'pickupRoom' },
        { Header: 'Drop-off Location', accessor: 'dropoffLocation' },
        { Header: 'Drop-off Room #', accessor: 'dropoffRoom' },
        { Header: 'Course',  accessor: 'course' },
        { Header: 'Notes',  accessor: 'notes' },
        ButtonColumn("Edit", "primary", editCallback, "RideRequestTable"),
        ButtonColumn("Delete", "danger", deleteCallback, "RideRequestTable")
    ];
   
    const columnsToDisplay = hasRole(currentUser, "ROLE_ADMIN") ? columnsAdmin :
                             hasRole(currentUser, "ROLE_DRIVER") ? columnsDriver :
                             hasRole(currentUser, "ROLE_RIDER") ? columnsRider : [];
    
    return <OurTable
        data={rideReqs}
        columns={columnsToDisplay}
        testid={"RideRequestTable"}
    />;
};