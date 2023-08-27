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
    public void logged_in_users_cant_get_all_of_theirs() throws Exception {
        mockMvc.perform(get("/api/riderapplication/all"))
                        .andExpect(status().is(403); 
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void logged_admin_users_cant_get_all_of_theirs() throws Exception {
        mockMvc.perform(get("/api/riderapplication/all"))
                            .andExpect(status().is(403)); 
    }

    @WithMockUser(roles = { "DRIVER" })
    @Test
    public void logged_driver_cant_get_all_of_theirs() throws Exception {
        mockMvc.perform(get("/api/riderapplication/all"))
                            .andExpect(status().is(403)); 
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
    public void logged_in_users_cant_get_by_id() throws Exception {
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
        long milli = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / milli) * milli;
        long userId2 = userId + 1;

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
        public void test_that_logged_in_user_cant_get_by_id_when_the_id_does_not_exist() throws Exception {

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

        long milli = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / milli) * milli;

        Date date = new Date(dateOnly);

        RiderApplication application_original = RiderApplication.builder()
                                        .userId(userId)
                                        .perm_number("6057444")
                                        .created_date(date)
                                        .description("Broke my arms")
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
        String expectedJson = mapper.writeValueAsString(application_edited);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
        
    }

    @WithMockUser(roles = { "USER", "MEMBER", "DRIVER", "ADMIN" })
    @Test
    public void test_that_all_roles_cant_update_others_application() throws Exception {
        long userId = currentUserService.getCurrentUser().getUser().getId();
        long otherUserId = userId + 1;

        long milli = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / milli) * milli;

        Date date = new Date(dateOnly);

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

        when(riderApplicationRepository.findByIdAndUserId(eq(6L), eq(otherUserId))).thenReturn(Optional.of(application_original));

         MvcResult response = mockMvc.perform(
                    put("/api/riderapplication/put?id=6")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .characterEncoding("utf-8")
                                    .content(requestBody)
                                    .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();

        verify(riderApplicationRepository, times(1)).findByIdAndUserId(eq(6L), eq(userId));
        Map<String, Object> json = responseToJson(response);
        assertEquals("RiderApplication with id 6 not found", json.get("message"));

    }


    @WithMockUser(roles = { "USER", "MEMBER", "DRIVER", "ADMIN" })
    @Test
    public void test_that_all_roles_cant_update_a_nonexistant_application() throws Exception {
        long userId = currentUserService.getCurrentUser().getUser().getId();

        long milli = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / milli) * milli;

        Date date = new Date(dateOnly);

        RiderApplication application_original = RiderApplication.builder()
                                        .userId(userId)
                                        .perm_number("6057444")
                                        .created_date(date)
                                        .description("Broke my arms")
                                        .updated_date(date)
                                        .build();

        
        String requestBody = mapper.writeValueAsString(application_original);

        when(riderApplicationRepository.findByIdAndUserId(eq(6L), eq(userId))).thenReturn(Optional.empty());

         MvcResult response = mockMvc.perform(
                    put("/api/riderapplication/put?id=6")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .characterEncoding("utf-8")
                                    .content(requestBody)
                                    .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();

        verify(riderApplicationRepository, times(1)).findByIdAndUserId(eq(6L), eq(userId));
        Map<String, Object> json = responseToJson(response);
        assertEquals("RiderApplication with id 6 not found", json.get("message"));

    }

    @WithMockUser(roles = { "USER", "MEMBER", "DRIVER", "ADMIN" })
    @Test
    public void test_that_all_roles_can_create_a_new_application() throws Exception {
        long userId = currentUserService.getCurrentUser().getUser().getId();
        long milli = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / milli) * milli;

        Date date = new Date(dateOnly);

        RiderApplication application_original = RiderApplication.builder()
                                        .userId(userId)
                                        .perm_number("6057444")
                                        .created_date(date)
                                        .description("Broke my arms")
                                        .build();

        when(riderApplicationRepository.save(eq(application_original))).thenReturn(application_original);

        String postRequestString = "perm_number=6057444&description=Broke my arms";

         MvcResult response = mockMvc.perform(
                    post("/api/riderapplication/new?" + postRequestString)
                                    .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

        verify(riderApplicationRepository, times(1)).save(application_original);
        String expectedJson = mapper.writeValueAsString(application_original);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);

    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void admin_can_return_all_applications() throws Exception {
        mockMvc.perform(get("/api/riderapplication/admin/all"))
                        .andExpect(status().is(200));
    }

    @WithMockUser(roles = { "USER", "MEMBER", "DRIVER", "ADMIN" })
    @Test
    public void roles_can_get_all_theirs() throws Exception {

        long userId = currentUserService.getCurrentUser().getUser().getId();
        long milli = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / milli) * milli;

        Date date = new Date(dateOnly);

        RiderApplication application_original = RiderApplication.builder()
                                        .userId(userId)
                                        .perm_number("6057444")
                                        .created_date(date)
                                        .description("Broke my arms")
                                        .build();

        RiderApplication application2 = RiderApplication.builder()
                                        .userId(userId)
                                        .perm_number("6057422")
                                        .created_date(date)
                                        .description("Broke legs")
                                        .build();

        ArrayList<RiderApplication> expectedApplications = new ArrayList<>();
        expectedApplications.addAll(Arrays.asList(application_original, application2));

        when(riderApplicationRepository.findByUserId(eq(userId))).thenReturn(expectedApplications);

        MvcResult response = mockMvc.perform(get("/api/riderapplication/all"))
                        .andExpect(status().isOk()).andReturn();
        
        verify(riderApplicationRepository, times(1)).findByUserId(eq(userId));
        String expectedJson = mapper.writeValueAsString(expectedApplications);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);

    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void admin_can_return_all_member_applications() throws Exception {
        long userId = currentUserService.getCurrentUser().getUser().getId();
        long milli = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / milli) * milli;
        long userId2 = userId + 1;

        Date date = new Date(dateOnly);

        RiderApplication application_original = RiderApplication.builder()
                                        .userId(userId)
                                        .perm_number("6057444")
                                        .created_date(date)
                                        .description("Broke my arms")
                                        .build();

        RiderApplication application2 = RiderApplication.builder()
                                        .userId(userId2)
                                        .perm_number("6057422")
                                        .created_date(date)
                                        .description("Broke legs")
                                        .build();

        ArrayList<RiderApplication> expectedApplications = new ArrayList<>();
        expectedApplications.addAll(Arrays.asList(application_original, application2));

         when(riderApplicationRepository.findAll()).thenReturn(expectedApplications);

         MvcResult response = mockMvc.perform(get("/api/riderapplication/admin/all"))
                        .andExpect(status().isOk()).andReturn();
        
        verify(riderApplicationRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedApplications);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void admin_can_get_all_member_applications() throws Exception {
        long userId = currentUserService.getCurrentUser().getUser().getId();
        long milli = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / milli) * milli;
        long userId2 = userId + 1;

        Date date = new Date(dateOnly);

        RiderApplication application_original = RiderApplication.builder()
                                        .userId(userId)
                                        .perm_number("6057444")
                                        .created_date(date)
                                        .description("Broke my arms")
                                        .status("pending")
                                        .build();

        RiderApplication application2 = RiderApplication.builder()
                                        .userId(userId2)
                                        .perm_number("6057422")
                                        .created_date(date)
                                        .description("Broke legs")
                                        .status("pending")
                                        .build();


        ArrayList<RiderApplication> expectedApplications = new ArrayList<>();
        expectedApplications.addAll(Arrays.asList(application_original, application2));

         when(riderApplicationRepository.findByStatus("pending")).thenReturn(expectedApplications);

         MvcResult response = mockMvc.perform(get("/api/riderapplication/admin/pending"))
                        .andExpect(status().isOk()).andReturn();
        
        verify(riderApplicationRepository, times(1)).findByStatus("pending");
        String expectedJson = mapper.writeValueAsString(expectedApplications);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "USER", "MEMBER", "DRIVER", "ADMIN" })
    @Test
    public void any_role_can_cancel_their_application() throws Exception {
        long userId = currentUserService.getCurrentUser().getUser().getId();
        long milli = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / milli) * milli;
        long userId2 = userId + 1;

        Date date = new Date(dateOnly);

        RiderApplication application_original = RiderApplication.builder()
                                        .userId(userId)
                                        .perm_number("6057444")
                                        .created_date(date)
                                        .description("Broke my arms")
                                        .status("pending")
                                        .build();

         RiderApplication application_edited = RiderApplication.builder()
                                        .userId(userId)
                                        .perm_number("6057444")
                                        .created_date(date)
                                        .updated_date(date)
                                        .cancelled_date(date)
                                        .description("Broke my arms")
                                        .status("cancelled")
                                        .build();

        String requestBody = mapper.writeValueAsString(application_edited);
        
        when(riderApplicationRepository.findByIdAndUserId(eq(6L), eq(userId))).thenReturn(Optional.of(application_original));

        MvcResult response = mockMvc.perform(
                    put("/api/riderapplication/cancel?id=6")
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

   


    @WithMockUser(roles = { "USER", "MEMBER", "DRIVER", "ADMIN" })
    @Test
    public void rider_tries_to_cancel_nonexistant_ride_and_gets_right_error_message() throws Exception {
        long userId = currentUserService.getCurrentUser().getUser().getId();
        long milli = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / milli) * milli;
        long userId2 = userId + 1;

        Date date = new Date(dateOnly);
        RiderApplication application = RiderApplication.builder()
                                        .userId(userId)
                                        .perm_number("6057444")
                                        .created_date(date)
                                        .description("Broke my arms")
                                        .status("pending")
                                        .build();

                                        
        when(riderApplicationRepository.findByIdAndUserId(eq(67L), eq(userId))).thenReturn(Optional.empty());

        String requestBody = mapper.writeValueAsString(application);
        MvcResult response = mockMvc.perform(put("/api/riderapplication/cancel?id=67")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(requestBody)
                .with(csrf()))
                .andExpect(status().isNotFound()).andReturn();

        verify(riderApplicationRepository, times(1)).findByIdAndUserId(eq(67L), eq(userId));
        Map<String, Object> json = responseToJson(response);
        assertEquals("RiderApplication with id 67 not found", json.get("message"));
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void admin_cant_get_nonexistant_application() throws Exception {
        when(riderApplicationRepository.findById(eq(15L))).thenReturn(Optional.empty());

        MvcResult response = mockMvc.perform(get("/api/riderapplication/admin/get?id=15"))
                                .andExpect(status().isNotFound()).andReturn();
        

        verify(riderApplicationRepository, times(1)).findById(eq(15L));
        Map<String,Object> json = responseToJson(response);
        assertEquals("RiderApplication with id 15 not found", json.get("message"));
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void admin_can_edit_application() throws Exception {
        Long userId = currentUserService.getCurrentUser().getUser().getId();
        long milli = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / milli) * milli;

        Date date = new Date(dateOnly);

        RiderApplication application_original = RiderApplication.builder()
                                        .userId(userId)
                                        .perm_number("6057444")
                                        .created_date(date)
                                        .description("Broke my arms")
                                        .build();
                                        
        RiderApplication application_edited = RiderApplication.builder()
                                        .userId(userId)
                                        .perm_number("6057444")
                                        .created_date(date)
                                        .description("Broke my arms")
                                        .status("pending")
                                        .notes("currently reviewing")
                                        .updated_date(date)
                                        .build();
        String requestBody = mapper.writeValueAsString(application_edited);

        when(riderApplicationRepository.findById(eq(6L))).thenReturn(Optional.of(application_original));


        MvcResult response = mockMvc.perform(
                    put("/api/riderapplication/admin?id=6")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .characterEncoding("utf-8")
                                    .content(requestBody)
                                    .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

        verify(riderApplicationRepository, times(1)).findById(eq(6L));
        String expectedJson = mapper.writeValueAsString(application_edited);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
        
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void admin_cant_edit_a_nonexistant_application() throws Exception {
        long userId = currentUserService.getCurrentUser().getUser().getId();
        long milli = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / milli) * milli;
        long userId2 = userId + 1;

        Date date = new Date(dateOnly);
        RiderApplication application = RiderApplication.builder()
                                        .userId(userId)
                                        .perm_number("6057444")
                                        .created_date(date)
                                        .description("Broke my arms")
                                        .status("pending")
                                        .build();

                                        
        when(riderApplicationRepository.findById(eq(67L))).thenReturn(Optional.empty());

        String requestBody = mapper.writeValueAsString(application);
        MvcResult response = mockMvc.perform(put("/api/riderapplication/admin?id=67")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(requestBody)
                .with(csrf()))
                .andExpect(status().isNotFound()).andReturn();

        verify(riderApplicationRepository, times(1)).findById(eq(67L));
        Map<String, Object> json = responseToJson(response);
        assertEquals("RiderApplication with id 67 not found", json.get("message"));
    }
        


    @WithMockUser(roles = { "ADMIN"})
    @Test
    public void logged_in_admins_can_get_any_by_id() throws Exception {
        mockMvc.perform(get("/api/riderapplication/admin/get?id=7"))
                            .andExpect(status().is(404)); 
    }
    @WithMockUser(roles = { "USER", "MEMBER", "DRIVER"})
    @Test
    public void logged_in_members_cant_get_any_by_id() throws Exception {
        mockMvc.perform(get("/api/riderapplication/admin/get?id=7"))
                            .andExpect(status().is(403)); 
    }
     @Test
    public void logged_out_users_cannot_get_any_by_id() throws Exception {
        mockMvc.perform(get("/api/riderapplication/admin/get?id=7"))
                            .andExpect(status().is(403)); 
    }

    @WithMockUser(roles = { "ADMIN"})
    @Test
    public void logged_in_admins_can_get_any_by_ids() throws Exception {
        long userId = currentUserService.getCurrentUser().getUser().getId();
        long milli = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / milli) * milli;
        long userId2 = userId + 1;

        Date date = new Date();

        RiderApplication application = RiderApplication.builder()
                        .userId(userId)
                        .perm_number("6057444")
                        .created_date(date)
                        .description("Broke my arms")
                        .build();

        when(riderApplicationRepository.findById(eq(7L))).thenReturn(Optional.of(application));

               
        MvcResult response = mockMvc.perform(get("/api/riderapplication/admin/get?id=7"))
                        .andExpect(status().isOk()).andReturn();

            

            verify(riderApplicationRepository, times(1)).findById(eq(7L));
            String expectedJson = mapper.writeValueAsString(application);
            String responseString = response.getResponse().getContentAsString();
            assertEquals(expectedJson, responseString);
    }


   
}
