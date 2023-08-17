package edu.ucsb.cs156.gauchoride.controllers;

import edu.ucsb.cs156.gauchoride.ControllerTestCase;
import edu.ucsb.cs156.gauchoride.controllers.UserProfileController;
import edu.ucsb.cs156.gauchoride.models.CurrentUser;
import edu.ucsb.cs156.gauchoride.repositories.UserRepository;
import edu.ucsb.cs156.gauchoride.testconfig.TestConfig;
import org.springframework.http.MediaType;

import edu.ucsb.cs156.gauchoride.entities.User;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = UserProfileController.class)
@Import(TestConfig.class)
public class UserProfileControllerTests extends ControllerTestCase {

  @MockBean
  UserRepository userRepository;

    @Test
        public void logged_out_users_cannot_edit_profile() throws Exception {
                mockMvc.perform(put("/api/userprofile?cellPhone=1234"))
                                .andExpect(status().is(403));
    }

    @WithMockUser(roles={"USER"})
    @Test
        public void logged_in_users_cannot_edit_profile_that_does_not_exist() throws Exception {
                when(userRepository.findById(eq(1L))).thenReturn(Optional.empty());
                MvcResult response = mockMvc.perform(
                    put("/api/userprofile?cellPhone=1234").with(csrf())).andExpect(status().isNotFound()).andReturn();
                verify(userRepository, times(1)).findById(1L);
    }

  @WithMockUser(roles = { "USER" })
  @Test
  public void currentUser__logged_in_can_edit_profile() throws Exception {

    // arrange

    CurrentUser currentUser = currentUserService.getCurrentUser();

    User fakeUserOld = User.builder()
        .id(1)
        .email("test@ucsb.edu")
        .cellPhone("123456789")
        .googleSub("None")
        .pictureUrl("None")
        .fullName("Test User 1")
        .givenName("Test")
        .familyName("User 1")
        .emailVerified(true)
        .locale("UCSB")
        .hostedDomain("None")
        .admin(false)
        .driver(false)
        .rider(true)
        .build();
    User fakeUserNew = User.builder()
        .id(1)
        .email("test@ucsb.edu")
        .cellPhone("1234")
        .googleSub("None")
        .pictureUrl("None")
        .fullName("Test User 1")
        .givenName("Test")
        .familyName("User 1")
        .emailVerified(true)
        .locale("UCSB")
        .hostedDomain("None")
        .admin(false)
        .driver(false)
        .rider(true)
        .build();

    String expectedJson = mapper.writeValueAsString(fakeUserNew);

    when(userRepository.findById(eq(1L))).thenReturn(Optional.of(fakeUserOld));

    // act

    MvcResult response = mockMvc.perform(
                    put("/api/userprofile?cellPhone=1234").with(csrf())).andExpect(status().isOk()).andReturn();

    // assert
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);
  }
}
