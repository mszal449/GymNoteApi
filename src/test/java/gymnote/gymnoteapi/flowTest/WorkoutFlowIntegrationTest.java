package gymnote.gymnoteapi.flowTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import gymnote.gymnoteapi.entity.*;
import gymnote.gymnoteapi.model.auth.LoginRequest;
import gymnote.gymnoteapi.model.dto.WorkoutDTO;
import gymnote.gymnoteapi.model.dto.WorkoutExerciseDTO;
import gymnote.gymnoteapi.repository.TemplateRepository;
import gymnote.gymnoteapi.repository.UserRepository;
import gymnote.gymnoteapi.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class WorkoutFlowIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authToken;
    private User testUser;
    private Template testTemplate;

    @BeforeAll
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser = userRepository.save(testUser);

        // Create test template with exercises
        testTemplate = new Template();
        testTemplate.setUser(testUser);
        testTemplate.setTemplateName("Test Template");

        Exercise exercise1 = new Exercise();
        exercise1.setExerciseName("Squat");

        Exercise exercise2 = new Exercise();
        exercise2.setExerciseName("Bench Press");

        TemplateExercise templateExercise1 = new TemplateExercise();
        templateExercise1.setTemplate(testTemplate);
        templateExercise1.setExercise(exercise1);
        templateExercise1.setExerciseOrder(1);

        TemplateExercise templateExercise2 = new TemplateExercise();
        templateExercise2.setTemplate(testTemplate);
        templateExercise2.setExercise(exercise2);
        templateExercise2.setExerciseOrder(2);

        testTemplate.setTemplateExercises(Arrays.asList(templateExercise1, templateExercise2));
        testTemplate = templateRepository.save(testTemplate);
    }

    @Test
    void completeWorkoutFlow() throws Exception {
        // Step 1: Login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test@example.com");
        loginRequest.setPassword("password123");
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        authToken = JsonPath.read(loginResult.getResponse().getContentAsString(), "$.token");

        // Step 2: Start workout from template
        MvcResult startWorkoutResult = mockMvc.perform(post("/api/workouts/start")
                        .header("Authorization", "Bearer " + authToken)
                        .param("templateId", testTemplate.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        Long workoutId = JsonPath.read(startWorkoutResult.getResponse().getContentAsString(), "$.id");

        // Step 3: Add sets to exercises
        List<WorkoutExerciseDTO> updates = new ArrayList<>();
        WorkoutExerciseDTO update = new WorkoutExerciseDTO();
        update.setWorkoutId(1L);
        update.setRealOrder(1);

        // Add set information
        updates.add(update);

        mockMvc.perform(put("/api/workouts/" + workoutId + "/exercises")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk());

        // Step 4: Finish workout
        WorkoutDTO finishRequest = new WorkoutDTO();
        finishRequest.setEndTime(LocalDateTime.now());
        finishRequest.setNotes("Great workout!");

        mockMvc.perform(put("/api/workouts/" + workoutId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(finishRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.endTime").exists())
                .andExpect(jsonPath("$.notes").value("Great workout!"));

        // Step 5: Verify workout was saved correctly
        Workout savedWorkout = workoutRepository.findByIdAndUserId(workoutId, testUser.getId())
                .orElseThrow();

        assertNotNull(savedWorkout.getEndTime());
        assertEquals("Great workout!", savedWorkout.getNotes());
        assertFalse(savedWorkout.getWorkoutExercises().isEmpty());
    }

    @Test
    void shouldHandleInvalidTemplateId() throws Exception {
        // Login first
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test@example.com");
        loginRequest.setPassword("password123");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        authToken = JsonPath.read(loginResult.getResponse().getContentAsString(), "$.token");

        // Try to start workout with invalid template
        mockMvc.perform(post("/api/workouts/start")
                        .header("Authorization", "Bearer " + authToken)
                        .param("templateId", "999999"))
                .andExpect(status().isNotFound());
    }
}