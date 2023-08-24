package edu.ucsb.cs156.gauchoride.controllers;

import edu.ucsb.cs156.gauchoride.repositories.UserRepository;
import edu.ucsb.cs156.gauchoride.testconfig.TestConfig;
import edu.ucsb.cs156.gauchoride.ControllerTestCase;
import edu.ucsb.cs156.gauchoride.entities.Ride;
import edu.ucsb.cs156.gauchoride.entities.Shift;
import edu.ucsb.cs156.gauchoride.repositories.ShiftRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ShiftController.class)
@Import(TestConfig.class)
public class ShiftControllerTests extends ControllerTestCase {

        @MockBean
        ShiftRepository shiftRepository;

        @MockBean
        UserRepository userRepository;

        // Authorization tests for /api/shift/all

        @Test
        public void logged_out_users_cannot_get_all() throws Exception {
                mockMvc.perform(get("/api/shift/all"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_users_can_get_all() throws Exception {
                mockMvc.perform(get("/api/shift/all"))
                                .andExpect(status().is(200)); 
        }

        @WithMockUser(roles = { "RIDER" })
        @Test
        public void logged_in_rider_can_get_all() throws Exception {
                mockMvc.perform(get("/api/shift/all"))
                                .andExpect(status().is(200)); 
        }

        @WithMockUser(roles = { "DRIVER" })
        @Test
        public void logged_in_driver_can_get_all() throws Exception {
                mockMvc.perform(get("/api/shift/all"))
                                .andExpect(status().is(200));
        }

        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void logged_in_admin_can_get_all() throws Exception {
                mockMvc.perform(get("/api/shift/all"))
                                .andExpect(status().is(200));
        }

        // Authorization tests for /api/shift/get?id={}

        @Test
        public void logged_out_users_cannot_get_by_id() throws Exception {
                mockMvc.perform(get("/api/shift/get?id=7"))
                                .andExpect(status().is(403)); 
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_users_can_get_by_id() throws Exception {
                mockMvc.perform(get("/api/shift/get?id=7"))
                                .andExpect(status().is(404)); 
        }

        @WithMockUser(roles = { "RIDER" })
        @Test
        public void logged_in_riders_can_get_by_id() throws Exception {
                mockMvc.perform(get("/api/shift/get?id=7"))
                                .andExpect(status().is(404)); 
        }


        @WithMockUser(roles = { "DRIVER" })
        @Test
        public void logged_in_driver_can_get_by_id() throws Exception {
                mockMvc.perform(get("/api/shift/get?id=7"))
                                .andExpect(status().is(404));
        }

        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void logged_in_admin_can_get_by_id() throws Exception {
                mockMvc.perform(get("/api/shift/get?id=7"))
                                .andExpect(status().is(404));
        }

        // authorization tests for /drivershifts

        @Test
        public void logged_out_users_cannot_get_driver_shifts() throws Exception {
                mockMvc.perform(get("/api/shift/drivershifts"))
                                .andExpect(status().is(403)); 
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_users_cannot_get_driver_shifts() throws Exception {
                mockMvc.perform(get("/api/shift/drivershifts"))
                                .andExpect(status().is(403)); 
        }

        @WithMockUser(roles = { "RIDER" })
        @Test
        public void logged_in_riders_cannot_get_driver_shifts() throws Exception {
                mockMvc.perform(get("/api/shift/drivershifts"))
                                .andExpect(status().is(403)); 
        }


        @WithMockUser(roles = { "DRIVER" })
        @Test
        public void logged_in_driver_can_get_driver_shifts() throws Exception {
                mockMvc.perform(get("/api/shift/drivershifts"))
                                .andExpect(status().is(200)); 
        }

        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void logged_in_admin_cannot_get_driver_shifts() throws Exception {
                mockMvc.perform(get("/api/shift/drivershifts"))
                                .andExpect(status().is(403)); 
        }



        // Authorization tests for /api/shift/post

        @Test
        public void logged_out_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/shift/post"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/shift/post"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "RIDER" })
        @Test
        public void logged_in_riders_cannot_post() throws Exception {
                mockMvc.perform(post("/api/shift/post"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "DRIVER" })
        @Test
        public void logged_in_driver_cannot_post() throws Exception {
                mockMvc.perform(post("/api/shift/post"))
                                .andExpect(status().is(403));
        }


        

        // Authorization tests for delete /api/shift

        @Test
         public void logged_out_users_cannot_delete() throws Exception {
                 mockMvc.perform(delete("/api/shift?id=9"))
                                 .andExpect(status().is(403));
        }


        @WithMockUser(roles = { "USER" })
        @Test
         public void logged_in_users_cannot_delete() throws Exception {
                 mockMvc.perform(delete("/api/shift?id=9"))
                                 .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "RIDER" })
        @Test
         public void logged_in_riders_cannot_delete() throws Exception {
                 mockMvc.perform(delete("/api/shift?id=9"))
                                 .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "DRIVER" })
        @Test
         public void logged_in_drivers_cannot_delete() throws Exception {
                 mockMvc.perform(delete("/api/shift?id=9"))
                                 .andExpect(status().is(403));
        }


        // Authorization tests for put /api/shift

        @Test
        public void logged_out_users_cannot_edit() throws Exception {
                 mockMvc.perform(put("/api/shift?id=9"))
                                 .andExpect(status().is(403));
        }
        
        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_users_cannot_edit() throws Exception {
                 mockMvc.perform(put("/api/shift?id=9"))
                                 .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "RIDER" })
        @Test
        public void logged_in_riders_cannot_edit() throws Exception {
                 mockMvc.perform(put("/api/shift?id=9"))
                                 .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "DRIVER" })
        @Test
        public void logged_in_drivers_cannot_edit() throws Exception {
                 mockMvc.perform(put("/api/shift?id=9"))
                                 .andExpect(status().is(403));
        }


        // // Tests with mocks for database actions


        // GET ALL

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_user_can_get_all_shifts() throws Exception {
                // arrange

                long userId = currentUserService.getCurrentUser().getUser().getId();

                Shift shift1 = Shift.builder()
                                .driverID(userId)
                                .day("Monday")
                                .shiftStart("10:30AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(1)
                                .build();

                Shift shift2 = Shift.builder()
                                .driverID(userId)
                                .day("Tuesday")
                                .shiftStart("10:30AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(userId + 1)
                                .build();

                ArrayList<Shift> expectedShifts = new ArrayList<>();
                expectedShifts.addAll(Arrays.asList(shift1, shift2));

                when(shiftRepository.findAll()).thenReturn(expectedShifts);

                // act
                MvcResult response = mockMvc.perform(get("/api/shift/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(shiftRepository, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(expectedShifts);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "RIDER" })
        @Test
        public void logged_in_rider_can_get_all_shifts() throws Exception {
                // arrange

                long userId = currentUserService.getCurrentUser().getUser().getId();

                Shift shift1 = Shift.builder()
                                .driverID(userId)
                                .day("Monday")
                                .shiftStart("10:30AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(1)
                                .build();

                Shift shift2 = Shift.builder()
                                .driverID(userId)
                                .day("Tuesday")
                                .shiftStart("10:30AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(userId + 1)
                                .build();

                ArrayList<Shift> expectedShifts = new ArrayList<>();
                expectedShifts.addAll(Arrays.asList(shift1, shift2));

                when(shiftRepository.findAll()).thenReturn(expectedShifts);

                // act
                MvcResult response = mockMvc.perform(get("/api/shift/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(shiftRepository, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(expectedShifts);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        

        @WithMockUser(roles = { "DRIVER" })
        @Test
        public void logged_in_driver_can_get_all_shifts() throws Exception {
                // arrange

                long userId = currentUserService.getCurrentUser().getUser().getId();

                Shift shift1 = Shift.builder()
                                .driverID(userId)
                                .day("Monday")
                                .shiftStart("10:30AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(1)
                                .build();

                Shift shift2 = Shift.builder()
                                .driverID(userId)
                                .day("Tuesday")
                                .shiftStart("10:30AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(userId + 1)
                                .build();

                ArrayList<Shift> expectedShifts = new ArrayList<>();
                expectedShifts.addAll(Arrays.asList(shift1, shift2));
                
                when(shiftRepository.findAll()).thenReturn(expectedShifts);
                
                MvcResult response = mockMvc.perform(get("/api/shift/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(shiftRepository, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(expectedShifts);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }


        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void logged_in_admin_can_get_all_shifts() throws Exception {
                // arrange

                long userId = currentUserService.getCurrentUser().getUser().getId();

                Shift shift1 = Shift.builder()
                                .driverID(userId)
                                .day("Monday")
                                .shiftStart("10:30AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(1)
                                .build();

                Shift shift2 = Shift.builder()
                                .driverID(userId)
                                .day("Tuesday")
                                .shiftStart("10:30AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(userId + 1)
                                .build();


                ArrayList<Shift> expectedShifts = new ArrayList<>();
                expectedShifts.addAll(Arrays.asList(shift1, shift2));

                when(shiftRepository.findAll()).thenReturn(expectedShifts);

                // act
                MvcResult response = mockMvc.perform(get("/api/shift/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(shiftRepository, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(expectedShifts);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }


        
        // GET BY ID


        @WithMockUser(roles = { "USER" })
        @Test
        public void test_that_logged_in_user_can_get_by_id_when_the_id_exists_and_user_id_matches() throws Exception {
                // arrange

                long userId = currentUserService.getCurrentUser().getUser().getId();

                Shift shift = Shift.builder()
                                .driverID(userId)
                                .day("Monday")
                                .shiftStart("10:00AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(1)
                                .build();

                when(shiftRepository.findById(eq(7L))).thenReturn(Optional.of(shift));

                // act
                MvcResult response = mockMvc.perform(get("/api/shift/get?id=7"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(shiftRepository, times(1)).findById(eq(7L));
                String expectedJson = mapper.writeValueAsString(shift);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void test_that_logged_in_user_can_get_by_id_when_the_id_does_not_exist() throws Exception {
                // arrange

                when(shiftRepository.findById(eq(7L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(get("/api/shift/get?id=7"))
                                .andExpect(status().isNotFound()).andReturn();

                // assert

                verify(shiftRepository, times(1)).findById(eq(7L));
                Map<String, Object> json = responseToJson(response);
                assertEquals("EntityNotFoundException", json.get("type"));
                assertEquals("Shift with id 7 not found", json.get("message"));
        }
        
        @WithMockUser(roles = { "RIDER" })
        @Test
        public void test_that_logged_in_rider_can_get_by_id_when_the_id_does_not_exist() throws Exception {
                // arrange

                when(shiftRepository.findById(eq(7L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(get("/api/shift/get?id=7"))
                                .andExpect(status().isNotFound()).andReturn();

                // assert

                verify(shiftRepository, times(1)).findById(eq(7L));
                Map<String, Object> json = responseToJson(response);
                assertEquals("EntityNotFoundException", json.get("type"));
                assertEquals("Shift with id 7 not found", json.get("message"));
        }
        

        @WithMockUser(roles = { "RIDER" })
        @Test
        public void test_that_logged_in_rider_can_get_by_id_when_the_id_exists_and_user_id_matches() throws Exception {
                // arrange

                long userId = currentUserService.getCurrentUser().getUser().getId();

                Shift shift = Shift.builder()
                                .driverID(userId)
                                .day("Monday")
                                .shiftStart("10:00AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(1)
                                .build();

                when(shiftRepository.findById(eq(7L))).thenReturn(Optional.of(shift));

                // act
                MvcResult response = mockMvc.perform(get("/api/shift/get?id=7"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(shiftRepository, times(1)).findById(eq(7L));
                String expectedJson = mapper.writeValueAsString(shift);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }




        @WithMockUser(roles = { "DRIVER" })
        @Test
        public void test_that_logged_in_driver_can_get_by_id_when_the_id_exists() throws Exception {
                // arrange

                long userId = currentUserService.getCurrentUser().getUser().getId();

                Shift shift = Shift.builder()
                                .driverID(userId)
                                .day("Monday")
                                .shiftStart("10:00AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(1)
                                .build();

                when(shiftRepository.findById(eq(7L))).thenReturn(Optional.of(shift));

                // act
                MvcResult response = mockMvc.perform(get("/api/shift/get?id=7"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(shiftRepository, times(1)).findById(eq(7L));
                String expectedJson = mapper.writeValueAsString(shift);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "DRIVER" })
        @Test
        public void test_that_logged_in_driver_can_get_by_id_when_the_id_does_not_exist() throws Exception {
                // arrange

                when(shiftRepository.findById(eq(7L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(get("/api/shift/get?id=7"))
                                .andExpect(status().isNotFound()).andReturn();

                // assert

                verify(shiftRepository, times(1)).findById(eq(7L));
                Map<String, Object> json = responseToJson(response);
                assertEquals("EntityNotFoundException", json.get("type"));
                assertEquals("Shift with id 7 not found", json.get("message"));
        }

        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void test_that_logged_in_admin_can_get_by_id_when_the_id_exists() throws Exception {
                // arrange

                long userId = currentUserService.getCurrentUser().getUser().getId();

                Shift shift = Shift.builder()
                                .driverID(userId)
                                .day("Monday")
                                .shiftStart("10:00AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(1)
                                .build();

                when(shiftRepository.findById(eq(7L))).thenReturn(Optional.of(shift));

                // act
                MvcResult response = mockMvc.perform(get("/api/shift/get?id=7"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(shiftRepository, times(1)).findById(eq(7L));
                String expectedJson = mapper.writeValueAsString(shift);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void test_that_logged_in_admin_can_get_by_id_when_the_id_does_not_exist() throws Exception {
                // arrange

                when(shiftRepository.findById(eq(7L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(get("/api/shift/get?id=7"))
                                .andExpect(status().isNotFound()).andReturn();

                // assert

                verify(shiftRepository, times(1)).findById(eq(7L));
                Map<String, Object> json = responseToJson(response);
                assertEquals("EntityNotFoundException", json.get("type"));
                assertEquals("Shift with id 7 not found", json.get("message"));
        }


        

        // GET DRIVER SHIFTS

        @WithMockUser(roles = { "DRIVER" })
        @Test
        public void logged_in_driver_can_get_all_their_shifts() throws Exception {
                // arrange

                long userId = currentUserService.getCurrentUser().getUser().getId();

                Shift shift1 = Shift.builder()
                                .driverID(userId)
                                .day("Monday")
                                .shiftStart("10:30AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(userId + 1)
                                .build();

                Shift shift2 = Shift.builder()
                                .driverID(userId)
                                .day("Tuesday")
                                .shiftStart("10:30AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(userId + 1)
                                .build();


                ArrayList<Shift> expectedShifts = new ArrayList<>();
                expectedShifts.addAll(Arrays.asList(shift1, shift2));

                when(shiftRepository.findByDriverID(userId)).thenReturn(expectedShifts);

                // act
                MvcResult response = mockMvc.perform(get("/api/shift/drivershifts"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(shiftRepository, times(1)).findByDriverID(userId);
                String expectedJson = mapper.writeValueAsString(expectedShifts);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }


        

        // POST

        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void an_admin_can_post_a_new_shift() throws Exception {
                // arrange

                long userId = currentUserService.getCurrentUser().getUser().getId();

                Shift shift1 = Shift.builder()
                                .driverID(userId)
                                .day("Monday")
                                .shiftStart("10:30AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(1)
                                .build();

                when(shiftRepository.save(eq(shift1))).thenReturn(shift1);

                String postRequestString = "day=Monday&shiftStart=10:30AM&shiftEnd=12:30PM&driverID="+userId+"&driverBackupID=1";

                // act
                MvcResult response = mockMvc.perform(
                                post("/api/shift/post?" + postRequestString)
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(shiftRepository, times(1)).save(shift1);
                String expectedJson = mapper.writeValueAsString(shift1);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        
        // DELETE

        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void admin_can_delete_any_shift() throws Exception {
                // arrange

                long userId = currentUserService.getCurrentUser().getUser().getId();
                long otherUserId = userId + 1;

                Shift shift1 = Shift.builder()
                                .driverID(otherUserId)
                                .day("Monday")
                                .shiftStart("10:30AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(1)
                                .build();

                when(shiftRepository.findById(eq(15L))).thenReturn(Optional.of(shift1));

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/shift?id=15")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(shiftRepository, times(1)).findById(15L);
                verify(shiftRepository, times(1)).delete(shift1);

                Map<String, Object> json = responseToJson(response);
                assertEquals("Shift with id 15 deleted", json.get("message"));
        }

        

        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void admin_tries_to_delete_non_existant_shift_and_gets_right_error_message() throws Exception {
                // arrange

                when(shiftRepository.findById(eq(15L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/shift?id=15")
                                                .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();

                // assert
                verify(shiftRepository, times(1)).findById(15L);
                Map<String, Object> json = responseToJson(response);
                assertEquals("Shift with id 15 not found", json.get("message"));
        }


        // EDIT

        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void admin_can_edit_an_existing_shift() throws Exception {
                // arrange

                long userId = currentUserService.getCurrentUser().getUser().getId();
                long otherUserId = userId + 1;

                Shift shift_original = Shift.builder()
                                .driverID(otherUserId)
                                .day("Monday")
                                .shiftStart("10:30AM")
                                .shiftEnd("12:30PM")
                                .driverBackupID(1)
                                .build();

                Shift shift_edited = Shift.builder()
                                .driverID(userId)
                                .day("Tuesday")
                                .shiftStart("11:30AM")
                                .shiftEnd("01:30PM")
                                .driverBackupID(1)
                                .build();

                String requestBody = mapper.writeValueAsString(shift_edited);

                when(shiftRepository.findById(eq(67L))).thenReturn(Optional.of(shift_original));

                // act
                MvcResult response = mockMvc.perform(
                                put("/api/shift?id=67")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .characterEncoding("utf-8")
                                                .content(requestBody)
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(shiftRepository, times(1)).findById(67L);
                verify(shiftRepository, times(1)).save(shift_edited); // should be saved with correct user
                String responseString = response.getResponse().getContentAsString();
                assertEquals(requestBody, responseString);
        }

}