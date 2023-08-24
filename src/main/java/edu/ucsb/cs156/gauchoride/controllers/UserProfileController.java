package edu.ucsb.cs156.gauchoride.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

import edu.ucsb.cs156.gauchoride.repositories.UserRepository;
import edu.ucsb.cs156.gauchoride.errors.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.v3.oas.annotations.Parameter;

import edu.ucsb.cs156.gauchoride.entities.User;
import edu.ucsb.cs156.gauchoride.models.CurrentUser;
import edu.ucsb.cs156.gauchoride.services.CurrentUserService;

@Tag(name="Current User Profile")
@RequestMapping("/api/userprofile")
@RestController
public class UserProfileController extends ApiController {
  @Autowired
  UserRepository userRepository;
  @Operation(summary = "Update user cell phone number")
  @PreAuthorize("hasRole('ROLE_USER')")
  @PutMapping("")
  public User updateUsersCellPhone(
    @Parameter(name="cellPhone") @RequestParam String cellPhone) {
        Long id = super.getCurrentUser().getUser().getId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        user.setCellPhone(cellPhone);
        userRepository.save(user);
        return user;        
    }
}