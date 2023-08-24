package edu.ucsb.cs156.gauchoride.controllers;

import edu.ucsb.cs156.gauchoride.repositories.UserRepository;
import edu.ucsb.cs156.gauchoride.testconfig.TestConfig;
import edu.ucsb.cs156.gauchoride.ControllerTestCase;
import edu.ucsb.cs156.gauchoride.entities.RideRequest;
import edu.ucsb.cs156.gauchoride.repositories.RideRequestRepository;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = RideRequestController.class)
@Import(TestConfig.class)
public class RideRequestControllerTests extends ControllerTestCase {

        @MockBean
        RideRequestRepository rideReqRepository;

        @MockBean
        UserRepository riderRepository;

        // Authorization tests for /api/ride_request/all

        @Test
        public void logged_out_riders_cannot_get_all() throws Exception {
                mockMvc.perform(get("/api/ride_request/all"))
                                .andExpect(status().is(403)); // logged out riders can't get all
        }

        @WithMockUser(roles = { "RIDER" })
        @Test
        public void logged_in_riders_can_get_all_of_theirs() throws Exception {
                mockMvc.perform(get("/api/ride_request/all"))
                                .andExpect(status().is(200)); // logged
        }

        @WithMockUser(roles = { "DRIVER" })
        @Test
        public void logged_in_driver_can_get_all() throws Exception {
                mockMvc.perform(get("/api/ride_request/all"))
                                .andExpect(status().is(200)); // logged
        }

        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void logged_in_admin_can_get_all() throws Exception {
                mockMvc.perform(get("/api/ride_request/all"))
                                .andExpect(status().is(200)); // logged
        }

        // Authorization tests for /api/ride_request?id={}

        @Test
        public void logged_out_riders_cannot_get_by_id() throws Exception {
                mockMvc.perform(get("/api/ride_request?id=7"))
                                .andExpect(status().is(403)); // logged out riders can't get by id
        }

        @WithMockUser(roles = { "RIDER" })
        @Test
        public void logged_in_riders_can_get_by_id_that_is_theirs() throws Exception {
                mockMvc.perform(get("/api/ride_request?id=7"))
                                .andExpect(status().is(404)); // logged, but no id exists
        }

        @WithMockUser(roles = { "DRIVER" })
        @Test
        public void logged_in_driver_can_get_by_id() throws Exception {
                mockMvc.perform(get("/api/ride_request?id=7"))
                                .andExpect(status().is(404)); // logged, but no id exists
        }

        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void logged_in_admin_can_get_by_id() throws Exception {
                mockMvc.perform(get("/api/ride_request?id=7"))
                                .andExpect(status().is(404)); // logged, but no id exists
        }

        // Authorization tests for /api/ride_request/post

        @Test
        public void logged_out_riders_cannot_post() throws Exception {
                mockMvc.perform(post("/api/ride_request/post"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "DRIVER" })
        @Test
        public void logged_in_driver_cannot_post() throws Exception {
                mockMvc.perform(post("/api/ride_request/post"))
                                .andExpect(status().is(403));
        }

        // Authorization tests for delete /api/ride_request

        @Test
         public void logged_out_riders_cannot_delete() throws Exception {
                 mockMvc.perform(delete("/api/ride_request?id=9"))
                                 .andExpect(status().is(403));
        }


        // Authorization tests for put /api/ride_request

        @Test
         public void logged_out_riders_cannot_edit() throws Exception {
                 mockMvc.perform(put("/api/ride_request?id=9"))
                                 .andExpect(status().is(403));
        }

        // // Tests with mocks for database actions



        // GET BY ID


        @WithMockUser(roles = { "RIDER" })
        @Test
        public void test_that_logged_in_rider_can_get_by_id_when_the_id_exists() throws Exception {

                // arrange

                long riderId = currentUserService.getCurrentUser().getUser().getId();

                RideRequest ride = RideRequest.builder()
                                .riderId(riderId)
                                .student("CGaucho")
                                .day("Monday")
                                .course("CMPSC 156")
                                .startTime("2:00PM")
                                .endTime("3:15PM")
                                .dropoffLocation("South Hall")
                                .pickupLocation("Phelps Hall")
                                .dropoffRoom("1431")
                                .pickupRoom("2125")
                                .notes("see you soon :)")
                                .build();

                when(rideReqRepository.findById(eq(7L))).thenReturn(Optional.of(ride));

                // act
                MvcResult response = mockMvc.perform(get("/api/ride_request?id=7"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(rideReqRepository, times(1)).findById(eq(7L));
                String expectedJson = mapper.writeValueAsString(ride);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "RIDER" })
        @Test
        public void test_that_logged_in_rider_cant_get_by_id_when_the_id_does_not_exist() throws Exception {

                // arrange

                when(rideReqRepository.findById(eq(7L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(get("/api/ride_request?id=7"))
                                .andExpect(status().isNotFound()).andReturn();

                // assert

                verify(rideReqRepository, times(1)).findById(eq(7L));
                Map<String, Object> json = responseToJson(response);
                assertEquals("EntityNotFoundException", json.get("type"));
                assertEquals("RideRequest with id 7 not found", json.get("message"));
        }

        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void test_that_logged_in_admin_can_get_by_id_when_the_id_exists() throws Exception {

                // arrange

                long riderId = currentUserService.getCurrentUser().getUser().getId();
                long otherUserId = riderId + 1;

                RideRequest ride = RideRequest.builder()
                                .riderId(otherUserId)
                                .student("CGaucho")
                                .day("Monday")
                                .course("CMPSC 156")
                                .startTime("2:00PM")
                                .endTime("3:15PM")
                                .dropoffLocation("South Hall")
                                .pickupLocation("Phelps Hall")
                                .dropoffRoom("1431")
                                .pickupRoom("2125")
                                .notes("see you soon :)")
                                .build();

                when(rideReqRepository.findById(eq(7L))).thenReturn(Optional.of(ride));

                // act
                MvcResult response = mockMvc.perform(get("/api/ride_request?id=7"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(rideReqRepository, times(1)).findById(eq(7L));
                String expectedJson = mapper.writeValueAsString(ride);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "DRIVER" })
        @Test
        public void test_that_logged_in_driver_can_get_by_id_when_the_id_exists() throws Exception {

                // arrange

                long riderId = currentUserService.getCurrentUser().getUser().getId();
                long otherUserId = riderId + 1;

                RideRequest ride = RideRequest.builder()
                                .riderId(otherUserId)
                                .student("DGaucho")
                                .day("Monday")
                                .course("CMPSC 156")
                                .startTime("2:00PM")
                                .endTime("3:15PM")
                                .dropoffLocation("South Hall")
                                .pickupLocation("Phelps Hall")
                                .dropoffRoom("1431")
                                .pickupRoom("2125")
                                .notes("see you soon :)")
                                .build();

                when(rideReqRepository.findById(eq(7L))).thenReturn(Optional.of(ride));

                // act
                MvcResult response = mockMvc.perform(get("/api/ride_request?id=7"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(rideReqRepository, times(1)).findById(eq(7L));
                String expectedJson = mapper.writeValueAsString(ride);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void test_that_logged_in_admin_cant_get_by_id_when_the_id_does_not_exist() throws Exception {

                // arrange

                when(rideReqRepository.findById(eq(7L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(get("/api/ride_request?id=7"))
                                .andExpect(status().isNotFound()).andReturn();

                // assert

                verify(rideReqRepository, times(1)).findById(eq(7L));
                Map<String, Object> json = responseToJson(response);
                assertEquals("EntityNotFoundException", json.get("type"));
                assertEquals("RideRequest with id 7 not found", json.get("message"));
        }

        @WithMockUser(roles = { "DRIVER" })
        @Test
        public void test_that_logged_in_driver_cant_get_by_id_when_the_id_does_not_exist() throws Exception {

                // arrange

                when(rideReqRepository.findById(eq(7L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(get("/api/ride_request?id=7"))
                                .andExpect(status().isNotFound()).andReturn();

                // assert

                verify(rideReqRepository, times(1)).findById(eq(7L));
                Map<String, Object> json = responseToJson(response);
                assertEquals("EntityNotFoundException", json.get("type"));
                assertEquals("RideRequest with id 7 not found", json.get("message"));
        }
        


        // GET ALL

        @WithMockUser(roles = { "RIDER" })
        @Test
        public void logged_in_rider_can_get_all_their_own_rides() throws Exception {

                long riderId = currentUserService.getCurrentUser().getUser().getId();

                RideRequest ride1 = RideRequest.builder()
                                .riderId(riderId)
                                .student("CGaucho")
                                .day("Monday")
                                .course("CMPSC 156")
                                .startTime("2:00PM")
                                .endTime("3:15PM")
                                .dropoffLocation("South Hall")
                                .pickupLocation("Phelps Hall")
                                .dropoffRoom("1431")
                                .pickupRoom("2125")
                                .notes("see you soon :)")
                                .build();

                RideRequest ride3 = RideRequest.builder()
                                .riderId(riderId)
                                .student("CGaucho")
                                .day("Thursday")
                                .course("MATH 111C")
                                .startTime("9:30AM")
                                .endTime("10:45AM")
                                .dropoffLocation("Phelps Hall")
                                .pickupLocation("Student Resource Building")
                                .dropoffRoom("215")
                                .pickupRoom("1")
                                .notes("thanks for taking me")
                                .build();

                ArrayList<RideRequest> expectedRides = new ArrayList<>();
                expectedRides.addAll(Arrays.asList(ride1, ride3));

                when(rideReqRepository.findAllByRiderId(eq(riderId))).thenReturn(expectedRides);

                // act
                MvcResult response = mockMvc.perform(get("/api/ride_request/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(rideReqRepository, times(1)).findAllByRiderId(eq(riderId));
                String expectedJson = mapper.writeValueAsString(expectedRides);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void logged_in_admin_can_get_all_rides() throws Exception {

                long riderId = currentUserService.getCurrentUser().getUser().getId();
                long otherUserId = riderId + 1;

                RideRequest ride1 = RideRequest.builder()
                                .riderId(riderId)
                                .student("CGaucho")
                                .day("Monday")
                                .course("CMPSC 156")
                                .startTime("2:00PM")
                                .endTime("3:15PM")
                                .dropoffLocation("South Hall")
                                .pickupLocation("Phelps Hall")
                                .dropoffRoom("1431")
                                .pickupRoom("1215")
                                .notes("going to cs156!")
                                .build();

                RideRequest ride2 = RideRequest.builder()
                                .riderId(otherUserId)
                                .student("DGaucho")
                                .day("Thursday")
                                .course("MATH 118C")
                                .startTime("12:30PM")
                                .endTime("1:45PM")
                                .dropoffLocation("Phelps Hall")
                                .pickupLocation("UCen")
                                .dropoffRoom("3505")
                                .pickupRoom("2125")
                                .notes("looking forward to riding with you")
                                .build();

                RideRequest ride3 = RideRequest.builder()
                                .riderId(riderId)
                                .student("CGaucho")
                                .day("Thursday")
                                .course("MATH 111C")
                                .startTime("9:30AM")
                                .endTime("10:45AM")
                                .dropoffLocation("Phelps Hall")
                                .pickupLocation("Student Resource Building")
                                .dropoffRoom("3505")
                                .pickupRoom("auditorium")
                                .notes("see you soon :)")
                                .build();

                ArrayList<RideRequest> expectedRides = new ArrayList<>();
                expectedRides.addAll(Arrays.asList(ride1, ride2, ride3));

                when(rideReqRepository.findAll()).thenReturn(expectedRides);

                // act
                MvcResult response = mockMvc.perform(get("/api/ride_request/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(rideReqRepository, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(expectedRides);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "DRIVER" })
        @Test
        public void logged_in_driver_can_get_all_rides() throws Exception {

                long riderId = currentUserService.getCurrentUser().getUser().getId();
                long otherUserId = riderId + 1;

                RideRequest ride1 = RideRequest.builder()
                                .riderId(riderId)
                                .student("CGaucho")
                                .day("Monday")
                                .course("CMPSC 156")
                                .startTime("2:00PM")
                                .endTime("3:15PM")
                                .dropoffLocation("South Hall")
                                .pickupLocation("Phelps Hall")
                                .dropoffRoom("1431")
                                .pickupRoom("2125")
                                .notes("see you soon :)")
                                .build();

                RideRequest ride2 = RideRequest.builder()
                                .riderId(otherUserId)
                                .student("DGaucho")
                                .day("Thursday")
                                .course("MATH 118C")
                                .startTime("12:30PM")
                                .endTime("1:45PM")
                                .dropoffLocation("Phelps Hall")
                                .pickupLocation("UCen")
                                .dropoffRoom("3505")
                                .pickupRoom("panda express")
                                .notes("thanks for picking me up")
                                .build();

                RideRequest ride3 = RideRequest.builder()
                                .riderId(riderId)
                                .student("CGaucho")
                                .day("Thursday")
                                .course("MATH 111C")
                                .startTime("9:30AM")
                                .endTime("10:45AM")
                                .dropoffLocation("Phelps Hall")
                                .pickupLocation("Student Resource Building")
                                .dropoffRoom("3505")
                                .pickupRoom("lobby")
                                .notes("I am in the lobby!")
                                .build();

                ArrayList<RideRequest> expectedRides = new ArrayList<>();
                expectedRides.addAll(Arrays.asList(ride1, ride2, ride3));

                when(rideReqRepository.findAll()).thenReturn(expectedRides);

                // act
                MvcResult response = mockMvc.perform(get("/api/ride_request/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(rideReqRepository, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(expectedRides);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }



        // POST



        @WithMockUser(roles = { "RIDER" })
        @Test
        public void a_rider_can_post_a_new_ride() throws Exception {
                // arrange

                long riderId = currentUserService.getCurrentUser().getUser().getId();

                RideRequest ride1 = RideRequest.builder()
                        .riderId(riderId)
                        .student("Fake user")
                        .day("Monday")
                        .course("CMPSC 156")
                        .startTime("2:00PM")
                        .endTime("3:15PM")
                        .dropoffLocation("South Hall")
                        .pickupLocation("Phelps Hall")
                        .dropoffRoom("1431")
                        .pickupRoom("3505")
                        .notes("hi")
                        .build();

                when(rideReqRepository.save(eq(ride1))).thenReturn(ride1);

                String params = "day=Monday&course=CMPSC 156&startTime=2:00PM&endTime=3:15PM&pickupLocation=Phelps Hall&dropoffLocation=South Hall&dropoffRoom=1431&pickupRoom=3505&notes=hi";

                // act
                MvcResult response = mockMvc.perform(
                                post("/api/ride_request/post?" + params)
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(rideReqRepository, times(1)).save(ride1);
                String expectedJson = mapper.writeValueAsString(ride1);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }


        


        // DELETE

        @WithMockUser(roles = { "RIDER" })
        @Test
        public void rider_can_delete_their_own_ride() throws Exception {
                // arrange

                long riderId = currentUserService.getCurrentUser().getUser().getId();

                RideRequest ride1 = RideRequest.builder()
                        .riderId(riderId)
                        .student("CGaucho")
                        .day("Monday")
                        .course("CMPSC 156")
                        .startTime("2:00PM")
                        .endTime("3:15PM")
                        .dropoffLocation("South Hall")
                        .pickupLocation("Phelps Hall")
                        .dropoffRoom("1431")
                        .pickupRoom("3505")
                        .notes("waiting outside of 3505")
                        .build();

                when(rideReqRepository.findById(eq(15L))).thenReturn(Optional.of(ride1));

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/ride_request?id=15")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assertriderId
                verify(rideReqRepository, times(1)).findById(eq(15L));
                verify(rideReqRepository, times(1)).delete(ride1);

                Map<String, Object> json = responseToJson(response);
                assertEquals("RideRequest with id 15 deleted", json.get("message"));
        }

        @WithMockUser(roles = { "RIDER" })
        @Test
        public void rider_tries_to_delete_non_existant_ride_and_gets_right_error_message()
                        throws Exception {
                // arrange

                when(rideReqRepository.findById(eq(15L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/ride_request?id=15")
                                                .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();

                // assert
                verify(rideReqRepository, times(1)).findById(eq(15L));
                Map<String, Object> json = responseToJson(response);
                assertEquals("RideRequest with id 15 not found", json.get("message"));
        }


        @WithMockUser(roles = { "ADMIN", "RIDER" })
        @Test
        public void admin_can_delete_any_ride() throws Exception {
                // arrange

                long riderId = currentUserService.getCurrentUser().getUser().getId();
                long otherUserId = riderId + 1;

                RideRequest ride1 = RideRequest.builder()
                        .riderId(otherUserId)
                        .student("DGaucho")
                        .day("Monday")
                        .course("CMPSC 156")
                        .startTime("2:00PM")
                        .endTime("3:15PM")
                        .dropoffLocation("South Hall")
                        .pickupLocation("Phelps Hall")
                        .dropoffRoom("1431")
                        .pickupRoom("3505")
                        .notes("waiting outside of 3505")
                        .build();

                when(rideReqRepository.findById(eq(15L))).thenReturn(Optional.of(ride1));

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/ride_request?id=15")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(rideReqRepository, times(1)).findById(15L);
                verify(rideReqRepository, times(1)).delete(ride1);

                Map<String, Object> json = responseToJson(response);
                assertEquals("RideRequest with id 15 deleted", json.get("message"));
        }

        @WithMockUser(roles = { "ADMIN", "RIDER" })
        @Test
        public void admin_tries_to_delete_non_existant_ride_and_gets_right_error_message()
                        throws Exception {
                // arrange

                when(rideReqRepository.findById(eq(15L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/ride_request?id=15")
                                                .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();

                // assert
                verify(rideReqRepository, times(1)).findById(15L);
                Map<String, Object> json = responseToJson(response);
                assertEquals("RideRequest with id 15 not found", json.get("message"));
        }

        // EDIT

        @WithMockUser(roles = { "RIDER" })
        @Test
        public void rider_can_edit_their_own_ride() throws Exception {
                // arrange

                long riderId = currentUserService.getCurrentUser().getUser().getId();

                RideRequest ride_original = RideRequest.builder()
                                .riderId(riderId)
                                .student("CGaucho")
                                .day("Monday")
                                .course("CMPSC 156")
                                .startTime("2:00PM")
                                .endTime("3:15PM")
                                .dropoffLocation("South Hall")
                                .pickupLocation("Phelps Hall")
                                .dropoffRoom("1431")
                                .pickupRoom("3505")
                                .notes("waiting outside of 3505")
                                .build();

                RideRequest ride_edited = RideRequest.builder()
                                .riderId(riderId)
                                .student("CGaucho")
                                .day("Thursday")
                                .course("MATH 118C")
                                .startTime("12:30PM")
                                .endTime("1:45PM")
                                .dropoffLocation("Phelps Hall")
                                .pickupLocation("UCen")
                                .dropoffRoom("panda express")
                                .pickupRoom("3505")
                                .notes("eating some orange chicken")
                                .build();

                String requestBody = mapper.writeValueAsString(ride_edited);

                when(rideReqRepository.findById(eq(67L))).thenReturn(Optional.of(ride_original));

                // act
                MvcResult response = mockMvc.perform(
                                put("/api/ride_request?id=67")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .characterEncoding("utf-8")
                                                .content(requestBody)
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(rideReqRepository, times(1)).findById(eq(67L));
                verify(rideReqRepository, times(1)).save(ride_edited); // should be saved with correct rider
                String responseString = response.getResponse().getContentAsString();
                assertEquals(requestBody, responseString);
        }

        @WithMockUser(roles = { "RIDER" })
        @Test
        public void rider_cant_edit_ride_that_does_not_exist() throws Exception {
                // arrange

                long riderId = currentUserService.getCurrentUser().getUser().getId();

                RideRequest ride_edited = RideRequest.builder()
                                .riderId(riderId)
                                .student("CGaucho")
                                .day("Thursday")
                                .course("MATH 118C")
                                .startTime("12:30PM")
                                .endTime("1:45PM")
                                .dropoffLocation("Phelps Hall")
                                .pickupLocation("UCen")
                                .dropoffRoom("1431")
                                .pickupRoom("3505")
                                .notes("waiting outside of 3505")
                                .build();


                String requestBody = mapper.writeValueAsString(ride_edited);

                when(rideReqRepository.findById(eq(67L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(
                                put("/api/ride_request?id=67")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .characterEncoding("utf-8")
                                                .content(requestBody)
                                                .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();

                // assert
                verify(rideReqRepository, times(1)).findById(eq(67L));
                Map<String, Object> json = responseToJson(response);
                assertEquals("RideRequest with id 67 not found", json.get("message"));

        }


        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void admin_can_edit_an_existing_ride() throws Exception {
                // arrange

                long riderId = currentUserService.getCurrentUser().getUser().getId();
                long otherUserId = riderId + 1;

                RideRequest ride_original = RideRequest.builder()
                                .riderId(otherUserId)
                                .student("DGaucho")
                                .day("Monday")
                                .course("CMPSC 156")
                                .startTime("2:00PM")
                                .endTime("3:15PM")
                                .dropoffLocation("South Hall")
                                .pickupLocation("Phelps Hall")
                                .dropoffRoom("1431")
                                .pickupRoom("3505")
                                .notes("waiting outside of 3505")
                                .build();

                RideRequest ride_edited = RideRequest.builder()
                                .riderId(otherUserId)
                                .student("DGaucho")
                                .day("Thursday")
                                .course("MATH 118C")
                                .startTime("12:30PM")
                                .endTime("1:45PM")
                                .dropoffLocation("Phelps Hall")
                                .pickupLocation("UCen")
                                .dropoffRoom("3505")
                                .pickupRoom("starbucks")
                                .notes("ill buy you a coffee!")
                                .build();

                String requestBody = mapper.writeValueAsString(ride_edited);

                when(rideReqRepository.findById(eq(67L))).thenReturn(Optional.of(ride_original));

                // act
                MvcResult response = mockMvc.perform(
                                put("/api/ride_request?id=67")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .characterEncoding("utf-8")
                                                .content(requestBody)
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(rideReqRepository, times(1)).findById(67L);
                verify(rideReqRepository, times(1)).save(ride_edited); // should be saved with correct rider
                String responseString = response.getResponse().getContentAsString();
                assertEquals(requestBody, responseString);
        }

        @WithMockUser(roles = { "ADMIN" })
        @Test
        public void admin_cannot_edit_ride_that_does_not_exist() throws Exception {
                // arrange

                long riderId = currentUserService.getCurrentUser().getUser().getId();

                RideRequest ride_edited = RideRequest.builder()
                                .riderId(riderId)
                                .student("CGaucho")
                                .day("Thursday")
                                .course("MATH 118C")
                                .startTime("12:30PM")
                                .endTime("1:45PM")
                                .dropoffLocation("Phelps Hall")
                                .pickupLocation("UCen")
                                .dropoffRoom("1431")
                                .pickupRoom("outside")
                                .notes("courtyard")
                                .build();

                String requestBody = mapper.writeValueAsString(ride_edited);

                when(rideReqRepository.findById(eq(67L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(
                                put("/api/ride_request?id=67")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .characterEncoding("utf-8")
                                                .content(requestBody)
                                                .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();

                // assert
                verify(rideReqRepository, times(1)).findById(67L);
                Map<String, Object> json = responseToJson(response);
                assertEquals("RideRequest with id 67 not found", json.get("message"));
        }

}