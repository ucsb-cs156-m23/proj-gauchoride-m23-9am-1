package edu.ucsb.cs156.gauchoride.controllers;

import edu.ucsb.cs156.gauchoride.entities.RideRequest;
import edu.ucsb.cs156.gauchoride.errors.EntityNotFoundException;
import edu.ucsb.cs156.gauchoride.repositories.RideRequestRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder.SecretKeyReactiveJwtDecoderBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Ride Request")
@RequestMapping("/api/ride_request")
@RestController

public class RideRequestController extends ApiController {

    @Autowired
    RideRequestRepository rideReqRepository;

    @Operation(summary = "List all ride requests, only rider's if not admin/driver")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_DRIVER') || hasRole('ROLE_RIDER')")
    @GetMapping("/all")
    public Iterable<RideRequest> allRides() {
        Iterable<RideRequest> rideReqs;

        if (getCurrentUser().getRoles().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ||
            getCurrentUser().getRoles().contains(new SimpleGrantedAuthority("ROLE_DRIVER"))) {
            rideReqs = rideReqRepository.findAll();
        } else {
            rideReqs = rideReqRepository.findAllByRiderId(getCurrentUser().getUser().getId());
        }

        return rideReqs;
    }

    @Operation(summary = "Get a single ride request by id,")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_DRIVER') || hasRole('ROLE_RIDER')")
    @GetMapping("")
    public RideRequest getById(
        @Parameter(name="id", description = "long, Id of the Ride to get", required = true) @RequestParam Long id
    ) {
        RideRequest rideReq = rideReqRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(RideRequest.class, id));
        return rideReq;
    }

    @Operation(summary = "Create a new ride request")
    @PreAuthorize("hasRole('ROLE_ADMIN')|| hasRole('ROLE_RIDER')")
    @PostMapping("/post")
    public RideRequest createRideReq(
        @Parameter(name="day", description="String, Day of the week ride is requested (Monday - Sunday) and allows Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday", 
                    example="Tuesday", required = true) 
        @RequestParam String day,

        @Parameter(name="startTime", description="String, Time the ride starts HH:MM(A/P)M", example="12:30AM", required = true)
        @RequestParam String startTime,

        @Parameter(name="endTime", description="String, Time the ride ends HH:MM(A/P)M", example="12:30AM", required = true)
        @RequestParam String endTime,

        @Parameter(name="pickupLocation", description="String, Location the ride starts", example="Phelps Hall", required = true)
        @RequestParam String pickupLocation,

        @Parameter(name="pickupRoom", description="String, Room number for the class at the pickupLocation", example="1940", required = false)
        @RequestParam String pickupRoom,

        @Parameter(name="dropoffLocation", description="String, Location the ride ends", example="South Hall", required = true)
        @RequestParam String dropoffLocation,

        @Parameter(name="dropoffRoom", description="String, Room number for the dropoffLocation", example="1431", required = false)
        @RequestParam String dropoffRoom,

        @Parameter(name="course", description="String, Course number for the class at the dropoffLocation", example="CMPSC 156", required = true)
        @RequestParam String course,

        @Parameter(name="notes", description="String, Notes for additional information", example="Library entrance by the arbor", required = false)
        @RequestParam String notes
    ) {

        RideRequest rideReq = new RideRequest();
        rideReq.setRiderId(getCurrentUser().getUser().getId());
        rideReq.setStudent(getCurrentUser().getUser().getFullName());
        rideReq.setDay(day);
        rideReq.setStartTime(startTime);
        rideReq.setEndTime(endTime);
        rideReq.setPickupLocation(pickupLocation);
        rideReq.setPickupRoom(pickupRoom);
        rideReq.setDropoffLocation(dropoffLocation);
        rideReq.setDropoffRoom(dropoffRoom);
        rideReq.setCourse(course);
        rideReq.setNotes(notes);

        RideRequest savedRideReq = rideReqRepository.save(rideReq);
        return savedRideReq;
    }

    @Operation(summary = "Update a single ride request")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_RIDER')")
    @PutMapping("")
    public RideRequest updateRideReq(
            @Parameter(name="id", description="long, Id of the Ride to be edited", required = true) @RequestParam Long id,
            @RequestBody @Valid RideRequest incoming
    ) {
        RideRequest rideReq = rideReqRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(RideRequest.class, id));

        rideReq.setDay(incoming.getDay());
        rideReq.setStartTime(incoming.getStartTime());
        rideReq.setEndTime(incoming.getEndTime());
        rideReq.setPickupLocation(incoming.getPickupLocation());
        rideReq.setPickupRoom(incoming.getPickupRoom());
        rideReq.setDropoffLocation(incoming.getDropoffLocation());
        rideReq.setDropoffRoom(incoming.getDropoffRoom());
        rideReq.setCourse(incoming.getCourse());
        rideReq.setNotes(incoming.getNotes());

        rideReqRepository.save(rideReq);
        return rideReq;
    }

    @Operation(summary = "Delete a ride request")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_RIDER')")
    @DeleteMapping("")
    public Object deleteRideReq(
        @Parameter(name="id", description="long, Id of the Ride to be deleted", required = true) @RequestParam Long id
    ) {
        RideRequest rideReq = rideReqRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(RideRequest.class, id));

        rideReqRepository.delete(rideReq);
        return genericMessage("RideRequest with id %s deleted".formatted(id));
    }

}
