package edu.ucsb.cs156.gauchoride.controllers;

import edu.ucsb.cs156.gauchoride.entities.RiderApplication;
import edu.ucsb.cs156.gauchoride.errors.EntityNotFoundException;
import edu.ucsb.cs156.gauchoride.repositories.RiderApplicationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder.SecretKeyReactiveJwtDecoderBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Calendar;
import java.text.ParseException;

import javax.validation.Valid;


@Tag(name = "Rider Application")
@RequestMapping("/api/riderapplication")
@RestController
public class RiderApplicationController extends ApiController {
    @Autowired
    RiderApplicationRepository riderApplicationRepository;

    @Autowired
    ObjectMapper mapper;

   

    @Operation(summary = "Creates a new application")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("/new")
    public RiderApplication newRiderApplication(
        
        @Parameter(name="perm_number") @RequestParam String perm_number,
       
        @Parameter(name="description") @RequestParam String description
        )
        {
        
        long milli = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / milli) * milli;

        Date date = new Date(dateOnly);


        RiderApplication riderApplication = new RiderApplication();
        riderApplication.setUserId(getCurrentUser().getUser().getId());
      
        riderApplication.setPerm_number(perm_number);
        riderApplication.setCreated_date(date);
        riderApplication.setDescription(description);
        
        RiderApplication savedRiderApplication = riderApplicationRepository.save(riderApplication);

        return savedRiderApplication;

    }

    @Operation(summary = "Gets a list of all rider request owned by current user")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/all")
    public ResponseEntity<String> riderRequests()
            throws JsonProcessingException {
                Long userId = super.getCurrentUser().getUser().getId();
                Iterable<RiderApplication> riderApplications = riderApplicationRepository.findByUserId(userId);
                String body = mapper.writeValueAsString(riderApplications);
                return ResponseEntity.ok().body(body);

            }

    @Operation(summary = "Gets a personal rider request by id")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER') || hasRole('ROLE_DRIVER') || hasRole('ROLE_MEMBER')")
    @GetMapping("/get")
    public RiderApplication riderApplicationID(
            @Parameter(name = "id", description = "Long, id number ride to get ", example = "1", required = true) @RequestParam Long id)
            throws JsonProcessingException {
        RiderApplication riderApplication = riderApplicationRepository.findByIdAndUserId(id, getCurrentUser().getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException(RiderApplication.class, id));
        return riderApplication;
    }

    @Operation(summary = "Update a personal application")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping("put")
    public RiderApplication updatePersonalApplication(@Parameter(name = "id", description = "Long, id number ride to get ", example = "1", required = true) @RequestParam long id, 
    @RequestBody @Valid RiderApplication incoming
    ){
        RiderApplication riderApplication = riderApplicationRepository.findByIdAndUserId(id, getCurrentUser().getUser().getId())
            .orElseThrow(() -> new EntityNotFoundException(RiderApplication.class, id));

            long milli = 60 * 60 * 24 * 1000;
            long currentTime = new Date().getTime();
            long dateOnly = (currentTime / milli) * milli;

            Date date = new Date(dateOnly);

            riderApplication.setUpdated_date(date);
            riderApplication.setPerm_number(incoming.getPerm_number());
            riderApplication.setDescription(incoming.getDescription());

            riderApplicationRepository.save(riderApplication);

            return riderApplication;
    }

    
    @Operation(summary = "Cancel a personal review")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping("/cancel")
    public RiderApplication cancelRiderApplication(
        @Parameter(name = "id", description = "Long, id number ride to get ", example = "1", required = true)  @RequestParam long id,
        @RequestBody @Valid RiderApplication incoming
    ) {
        RiderApplication riderApplication = riderApplicationRepository.findByIdAndUserId(id, getCurrentUser().getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException(RiderApplication.class, id));

        if(incoming.getStatus() != "cancelled"){
                long milli = 60 * 60 * 24 * 1000;
                long currentTime = new Date().getTime();
                long dateOnly = (currentTime / milli) * milli;

                Date date = new Date(dateOnly);
                riderApplication.setUpdated_date(date);
                riderApplication.setStatus("cancelled");
                riderApplication.setCancelled_date(date);
                riderApplicationRepository.save(riderApplication);
                return riderApplication;


        }else{
            throw new EntityNotFoundException(RiderApplication.class, id);
            
        }
       
   
    }

    @Operation(summary = "Gets all Rider Requests")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<String> allRequests()
            throws JsonProcessingException {
        Iterable<RiderApplication> riderApplications = riderApplicationRepository.findAll();
        String body = mapper.writeValueAsString(riderApplications);
        return ResponseEntity.ok().body(body);
    }

    @Operation(summary = "Gets all pending requests")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/pending")
    public ResponseEntity<String> allPendingRequests()
        throws JsonProcessingException {
            Iterable<RiderApplication> riderApplications = riderApplicationRepository.findByStatus("pending");
            String body = mapper.writeValueAsString(riderApplications);
            return ResponseEntity.ok().body(body);
    }

    @Operation(summary = "Gets specific rider application by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/get")
    public RiderApplication riderApplicationId(
            @Parameter(name = "id", description = "Long, id number ride to get ", example = "1", required = true) @RequestParam Long id)
            throws JsonProcessingException {
        RiderApplication riderApplication = riderApplicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(RiderApplication.class, id));
        return riderApplication;
    }

    @Operation(summary = "Update a rider Application")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin")
    public RiderApplication editRiderApplication(
        @Parameter(name = "id", description = "Long, id number ride to get ", example = "1", required = true)  @RequestParam long id,
        @RequestBody @Valid RiderApplication incoming
    ){
        RiderApplication riderApplication = riderApplicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(RiderApplication.class, id));
        long milli = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / milli) * milli;
        Date date = new Date(dateOnly);

        riderApplication.setUpdated_date(date);
        riderApplication.setNotes(incoming.getNotes());
        riderApplication.setStatus(incoming.getStatus());
        riderApplicationRepository.save(riderApplication);
        return riderApplication;

    }



}