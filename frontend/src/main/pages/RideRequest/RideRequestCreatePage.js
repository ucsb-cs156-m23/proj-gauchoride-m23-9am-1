import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import RideRequestForm from "main/components/RideRequest/RideRequestForm";
import { Navigate } from 'react-router-dom'
import { useBackendMutation } from "main/utils/useBackend";
import { toast } from "react-toastify";
import { removeId } from "main/utils/rideRequestUtils";

export default function RideRequestCreatePage() {

    const objectToAxiosParams = (rideReq) => ({
        url: "/api/ride_request/post",
        method: "POST",
        params: removeId(rideReq)
    });

    const onSuccess = (rideReq) => {
        toast(`Ride Request Created - id: ${rideReq.id}`);
    }

    const mutation = useBackendMutation(
        objectToAxiosParams,
        { onSuccess },
        // Stryker disable next-line all : hard to set up test for caching
        ["/api/ride_request/all"]
    );

    const { isSuccess } = mutation

    const onSubmit = async (data) => {
        mutation.mutate(data);
    }

    if (isSuccess) {
        return <Navigate to="/rider" />
    }

    return (
        <BasicLayout>
        <div className="pt-2">
            <h1>Request a Ride</h1>
            <RideRequestForm submitAction={onSubmit} />
        </div>
        </BasicLayout>
    )
}