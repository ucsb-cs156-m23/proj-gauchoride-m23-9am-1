import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import { useParams } from "react-router-dom";
import RideRequestForm from "main/components/RideRequest/RideRequestForm";
import { Navigate } from 'react-router-dom'
import { useBackend, useBackendMutation } from "main/utils/useBackend";
import { removeId } from "main/utils/rideRequestUtils";

import { toast } from "react-toastify";

export default function RideRequestEditPage() {
  let { id } = useParams();

  const { data: rideReq, _error, _status } =
    useBackend(
      // Stryker disable next-line all : don't test internal caching of React Query
      [`/api/ride_request?id=${id}`],
      {  // Stryker disable next-line all : GET is the default, so changing this to "" doesn't introduce a bug
        method: "GET",
        url: `/api/ride_request`,
        params: {
          id
        }
      }
    );


  const objectToAxiosPutParams = (rideReq) => ({
    url: "/api/ride_request",
    method: "PUT",
    params: {
      id: rideReq.id,
    },
    data: removeId(rideReq)
  });

  const onSuccess = (rideReq) => {
    toast(`Ride Request Updated - id: ${rideReq.id}`);
  }

  const mutation = useBackendMutation(
    objectToAxiosPutParams,
    { onSuccess },
    // Stryker disable next-line all : hard to set up test for caching
    [`/api/ride_request?id=${id}`]
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
                <h1>Edit Ride Request</h1>
                {rideReq &&
                  <RideRequestForm initialContents={rideReq} submitAction={onSubmit} buttonLabel="Update" />
                }
            </div>
        </BasicLayout>
    )
}