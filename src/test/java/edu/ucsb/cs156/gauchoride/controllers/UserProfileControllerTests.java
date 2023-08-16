package edu.ucsb.cs156.gauchoride.controllers;

import edu.ucsb.cs156.gauchoride.ControllerTestCase;
import edu.ucsb.cs156.gauchoride.controllers.UserProfileController;
import edu.ucsb.cs156.gauchoride.models.CurrentUser;
import edu.ucsb.cs156.gauchoride.repositories.UserRepository;
import edu.ucsb.cs156.gauchoride.testconfig.TestConfig;
import org.springframework.http.MediaType;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

  @WithMockUser(roles = { "USER" })
  @Test
  public void currentUser__logged_in_can_edit_profile() throws Exception {

    // arrange

    CurrentUser currentUser = currentUserService.getCurrentUser();
    String expectedJson = mapper.writeValueAsString(currentUser);

    // act

    MvcResult response = mockMvc.perform(
                    put("/api/userprofile?cellPhone=1234")
                                    .characterEncoding("utf-8")).andExpect(status().isOk()).andReturn();

    // assert
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);
  }
}
