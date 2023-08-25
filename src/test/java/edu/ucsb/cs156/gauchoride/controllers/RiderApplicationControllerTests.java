package edu.ucsb.cs156.gauchoride.controllers;

import edu.ucsb.cs156.gauchoride.repositories.UserRepository;
import edu.ucsb.cs156.gauchoride.testconfig.TestConfig;
import edu.ucsb.cs156.gauchoride.ControllerTestCase;
import edu.ucsb.cs156.gauchoride.entities.RiderApplication;
import edu.ucsb.cs156.gauchoride.repositories.RiderApplicationRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Optional;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@WebMvcTest(controllers = RiderApplicationController.class)
@Import(TestConfig.class)

public class RiderApplicationControllerTests extends ControllerTestCase {
    @MockBean
    RiderApplicationRepository riderApplicationRepository;

    @MockBean
    UserRepository userRepository;

    @Test
    public void logged_out_users_cannot_get_all() throws Exception {
        mockMvc.perform(get("/api/riderapplication/all"))
                        .andExpect(status().is(403)); 
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void logged_in_users_can_get_all_of_theirs() throws Exception {
        mockMvc.perform(get("/api/riderapplication/all"))
                        .andExpect(status().is(200)); 
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void logged_admin_users_can_get_all_of_theirs() throws Exception {
        mockMvc.perform(get("/api/riderapplication/all"))
                            .andExpect(status().is(200)); 
    }

    @WithMockUser(roles = { "DRIVER" })
    @Test
    public void logged_driver_can_get_all_of_theirs() throws Exception {
        mockMvc.perform(get("/api/riderapplication/all"))
                            .andExpect(status().is(200)); 
    }

    @WithMockUser(roles = { "MEMBER" })
    @Test
    public void logged_member_can_get_all_of_theirs() throws Exception {
        mockMvc.perform(get("/api/riderapplication/all"))
                            .andExpect(status().is(200)); 
    }


    @Test
    public void logged_out_users_cannot_get_by_id() throws Exception {
        mockMvc.perform(get("/api/riderapplication/get?id=7"))
                            .andExpect(status().is(403)); 
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void logged_in_users_can_get_by_id() throws Exception {
        mockMvc.perform(get("/api/riderapplication/get?id=7"))
                            .andExpect(status().is(404)); 
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void logged_in_admins_can_get_by_id() throws Exception {
        mockMvc.perform(get("/api/riderapplication/get?id=7"))
                            .andExpect(status().is(404)); 
    }

    @WithMockUser(roles = { "DRIVER" })
    @Test
    public void logged_in_drivers_can_get_by_id() throws Exception {
        mockMvc.perform(get("/api/riderapplication/get?id=7"))
                            .andExpect(status().is(404)); 
    }

    @Test
    public void logged_out_users_cannot_post() throws Exception {
        mockMvc.perform(post("/api/riderapplication/new"))
                            .andExpect(status().is(403));
    }
    
    @WithMockUser(roles = { "USER", "MEMBER", "DRIVER", "ADMIN" })
    @Test
    public void test_that_logged_in_user_can_get_by_id_when_the_id_exists_and_user_id_matches() throws Exception {

        long userId = currentUserService.getCurrentUser().getUser().getId();

        Date date = new Date();

        RiderApplication application = RiderApplication.builder()
                        .userId(userId)
                        .perm_number("6057444")
                        .created_date(date)
                        .description("Broke my arms")
                        .build();

        when(riderApplicationRepository.findByIdAndUserId(eq(7L), eq(userId))).thenReturn(Optional.of(application));

               
        MvcResult response = mockMvc.perform(get("/api/riderapplication/get?id=7"))
                        .andExpect(status().isOk()).andReturn();

            

            verify(riderApplicationRepository, times(1)).findByIdAndUserId(eq(7L), eq(userId));
            String expectedJson = mapper.writeValueAsString(application);
            String responseString = response.getResponse().getContentAsString();
            assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "USER", "MEMBER", "DRIVER", "ADMIN" })
        @Test
        public void test_that_logged_in_user_can_get_by_id_when_the_id_does_not_exist() throws Exception {

                Long userId = currentUserService.getCurrentUser().getUser().getId();

                when(riderApplicationRepository.findByIdAndUserId(eq(7L), eq(userId))).thenReturn(Optional.empty());

                MvcResult response = mockMvc.perform(get("/api/riderapplication/get?id=7"))
                                .andExpect(status().isNotFound()).andReturn();


                verify(riderApplicationRepository, times(1)).findByIdAndUserId(eq(7L), eq(userId));
                Map<String, Object> json = responseToJson(response);
                assertEquals("EntityNotFoundException", json.get("type"));
                assertEquals("RiderApplication with id 7 not found", json.get("message"));
        }

    @WithMockUser(roles = { "USER", "MEMBER", "DRIVER", "ADMIN" })
    @Test
    public void test_that_all_roles_can_update_their_application() throws Exception {
        Long userId = currentUserService.getCurrentUser().getUser().getId();

        Date date = new Date();

        RiderApplication application_original = RiderApplication.builder()
                                        .userId(userId)
                                        .perm_number("6057444")
                                        .created_date(date)
                                        .description("Broke my arms")
                                        .updated_date(date)
                                        .build();
        RiderApplication application_edited = RiderApplication.builder()
                                        .userId(userId)
                                        .perm_number("6057345")
                                        .created_date(date)
                                        .description("Broke my arms and legs")
                                        .updated_date(date)
                                        .build();
        String requestBody = mapper.writeValueAsString(application_edited);

        when(riderApplicationRepository.findByIdAndUserId(eq(6L), eq(userId))).thenReturn(Optional.of(application_original));


        MvcResult response = mockMvc.perform(
                    put("/api/riderapplication/put?id=6")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .characterEncoding("utf-8")
                                    .content(requestBody)
                                    .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

        verify(riderApplicationRepository, times(1)).findByIdAndUserId(eq(6L), eq(userId));
        verify(riderApplicationRepository, times(1)).save(application_edited);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(requestBody, responseString);
        
    }

    





   
          







}
