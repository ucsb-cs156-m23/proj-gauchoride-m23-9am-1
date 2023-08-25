package edu.ucsb.cs156.gauchoride.controllers;

import edu.ucsb.cs156.gauchoride.ControllerTestCase;
import edu.ucsb.cs156.gauchoride.controllers.DriversController;
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
import java.util.ArrayList;

@WebMvcTest(controllers = DriversController.class)
@Import(TestConfig.class)
public class DriversControllerTests extends ControllerTestCase {

  @MockBean
  UserRepository userRepository;

    @Test
    public void logged_out_users_cannot_see_drivers() throws Exception {
                mockMvc.perform(get("/api/drivers/all"))
                                .andExpect(status().is(403));
    }

    @WithMockUser(roles={"USER"})
    @Test
    public void normal_user_cannot_see_drivers() throws Exception {
                mockMvc.perform(get("/api/drivers/all"))
                                .andExpect(status().is(403));
    }

    @WithMockUser(roles = { "DRIVER" })
    @Test
    public void driver_can_see_drivers() throws Exception {
      User testUser1 = User.builder()
                          .id(1)
                          .email("cgaucho@ucsb.edu")
                          .cellPhone("18002738255")
                          .driver(true)
                          .build();
      User testUser2 = User.builder()
                          .id(1)
                          .email("cgaucho2@ucsb.edu")
                          .cellPhone("988")
                          .driver(false)
                          .build();
      ArrayList<User> combined = new ArrayList<>();
      combined.add(testUser1);
      combined.add(testUser2);
      ArrayList<User> expectedOut = new ArrayList<>();
      expectedOut.add(testUser1);
      when(userRepository.findAll()).thenReturn(combined);
      MvcResult response = mockMvc.perform(get("/api/drivers/all"))
                                  .andExpect(status().isOk()).andReturn();
      verify(userRepository, times(1)).findAll();
      String expectedJson = mapper.writeValueAsString(expectedOut);
      String responseString = response.getResponse().getContentAsString();
      assertEquals(expectedJson,responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void admin_can_see_drivers() throws Exception {
                  mockMvc.perform(get("/api/drivers/all"))
                                  .andExpect(status().is(200));
    }
    
    @WithMockUser(roles = { "RIDER" })
    @Test
    public void rider_can_see_drivers() throws Exception {
                  mockMvc.perform(get("/api/drivers/all"))
                                  .andExpect(status().is(200));
    }
}
