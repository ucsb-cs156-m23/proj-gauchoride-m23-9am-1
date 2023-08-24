package edu.ucsb.cs156.gauchoride.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ucsb.cs156.gauchoride.entities.Ride;
import edu.ucsb.cs156.gauchoride.entities.Shift;
import edu.ucsb.cs156.gauchoride.repositories.ShiftRepository;
import edu.ucsb.cs156.gauchoride.repositories.UserRepository;
import edu.ucsb.cs156.gauchoride.errors.EntityNotFoundException;
import edu.ucsb.cs156.gauchoride.models.CurrentUser;

import java.nio.file.AccessDeniedException;
import java.time.LocalTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;


@Tag(name = "Shift information")
@RequestMapping("/api/shift")
@RestController
public class ShiftController extends ApiController {
    @Autowired
    ShiftRepository shiftRepository;

    @Autowired
    ObjectMapper mapper;

    @Operation(summary = "Get a list of all shifts")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_DRIVER') || hasRole('ROLE_RIDER') || hasRole('ROLE_USER')")
    @GetMapping("/all")
    public ResponseEntity<String> allShifts()
            throws JsonProcessingException {
        Iterable<Shift> shifts = shiftRepository.findAll();
        String body = mapper.writeValueAsString(shifts);
        return ResponseEntity.ok().body(body);
    }

    @Operation(summary = "Get shift by id")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_DRIVER') || hasRole('ROLE_RIDER') || hasRole('ROLE_USER')")
    @GetMapping("/get")
    public Shift shiftByID(
            @Parameter(name = "id", description = "Long, id number of shift to get", example = "1", required = true) @RequestParam Long id)
            throws JsonProcessingException {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Shift.class, id));
        return shift;
    }

    @Operation(summary = "Get a list of all shifts for given driverId")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @GetMapping("/drivershifts")
    public ResponseEntity<String> driverShifts()
            throws JsonProcessingException {
        Long driverID = super.getCurrentUser().getUser().getId();
        Iterable<Shift> shifts = shiftRepository.findByDriverID(driverID);
        String body = mapper.writeValueAsString(shifts);
        return ResponseEntity.ok().body(body);
    }

    @Operation(summary = "Create a new shift for the table")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public Shift postShift(
        @Parameter(name="day", description ="String, day of the week", example = "Monday") @RequestParam String day,
        @Parameter(name="shiftStart", description="String, Format: HH:MM(A/P)M", example="11:00AM") @RequestParam String shiftStart,
        @Parameter(name="shiftEnd", description="String, Format: HH:MM(A/P)M", example="11:00AM") @RequestParam String shiftEnd,
        @Parameter(name="driverID", description = "Long, driver ID") @RequestParam long driverID ,
        @Parameter(name="driverBackupID", description = "Long, driver backup id") @RequestParam long driverBackupID
        )
        {

        Shift shift = new Shift();

        shift.setDriverID(getCurrentUser().getUser().getId());
        shift.setDay(day);
        shift.setShiftStart(shiftStart);
        shift.setShiftEnd(shiftEnd);
        shift.setDriverBackupID(driverBackupID);

        Shift savedShift = shiftRepository.save(shift);

        return savedShift;
    }



    @Operation(summary= "Delete a Shift, drivers can only delete their own")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("")
    public Object deleteShift(
        @Parameter(name="id", description = "Long, id number of shift to get", example = "1", required = true) 
        @RequestParam long id){
            Shift shift = shiftRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(Shift.class, id));
    
            shiftRepository.delete(shift);
            return genericMessage("Shift with id %s deleted".formatted(id));

    }


    @Operation(summary = "Update a single shift, drivers can only update their own")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("")
    public Shift updateShift(
            @Parameter(name="id", description = "Long, id number of shift to get", example = "1", required = true)
            @RequestParam Long id,
            @RequestBody @Valid Shift incoming) {

        Shift shift = shiftRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(Shift.class, id));

        
        shift.setDay(incoming.getDay());
        shift.setShiftStart(incoming.getShiftStart());
        shift.setShiftEnd(incoming.getShiftEnd());
        shift.setDriverID(getCurrentUser().getUser().getId());
        shift.setDriverBackupID(incoming.getDriverBackupID());

        shiftRepository.save(shift);

        return shift;
    }

}