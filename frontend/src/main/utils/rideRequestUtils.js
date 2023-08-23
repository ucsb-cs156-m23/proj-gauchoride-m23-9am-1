import { toast } from "react-toastify";

export function onDeleteSuccess(message) {
    console.log(message);
    toast(message);
}

export function cellToAxiosParamsDelete(cell) {
    return {
        url: "/api/ride_request",
        method: "DELETE",
        params: {
            id: cell.row.values.id
        }
    }
}

export function removeId(rideReq) {
    return {
        day: rideReq.day,
        startTime: rideReq.startTime,
        endTime: rideReq.endTime,
        pickupLocation: rideReq.pickupLocation,
        pickupRoom: rideReq.pickupRoom,
        dropoffLocation: rideReq.dropoffLocation,
        dropoffRoom: rideReq.dropoffRoom,
        course: rideReq.course,
        notes: rideReq.notes
    };
}