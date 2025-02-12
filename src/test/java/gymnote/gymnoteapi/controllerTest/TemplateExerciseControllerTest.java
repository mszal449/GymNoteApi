package gymnote.gymnoteapi.controllerTest;

import gymnote.gymnoteapi.controller.TemplateExerciseController;
import gymnote.gymnoteapi.entity.*;
import gymnote.gymnoteapi.exception.template.TemplateNotFoundException;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.TemplateExerciseDTO;
import gymnote.gymnoteapi.model.templateExercise.CreateTemplateExerciseRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.TemplateExerciseService;
import gymnote.gymnoteapi.service.TemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateExerciseControllerTest {

    @Mock
    private TemplateExerciseService templateExerciseService;

    @Mock
    private TemplateService templateService;

    @InjectMocks
    private TemplateExerciseController templateExerciseController;

    private UserDetailsImpl userDetails;
    private Template template;
    private Exercise exercise;
    private TemplateExercise templateExercise;
    private CreateTemplateExerciseRequest createRequest;

    @BeforeEach
    void setUp() {
        // Setup UserDetails
        userDetails = new UserDetailsImpl(1L, "testUser", "test@test.com", "password", Collections.emptyList());
        
        // Setup User
        User user = new User();
        user.setId(1L);
        
        // Setup Template
        template = new Template();
        template.setId(1L);
        template.setUser(user);
        template.setTemplateName("Test Template");
        
        // Setup Exercise
        exercise = new Exercise();
        exercise.setId(1L);
        exercise.setExerciseName("Push Up");
        exercise.setType(EExerciseType.REPS);
        
        // Setup TemplateExercise
        templateExercise = new TemplateExercise();
        templateExercise.setId(1L);
        templateExercise.setTemplate(template);
        templateExercise.setExercise(exercise);
        templateExercise.setExerciseOrder(1);
        
        // Setup CreateRequest
        createRequest = new CreateTemplateExerciseRequest();
        createRequest.setTemplateId(1L);
        createRequest.setExerciseId(1L);
        createRequest.setExerciseOrder(1);
    }

    @Test
    void getTemplateExercises_Success() {
        when(templateExerciseService.getUserTemplateExercises(1L, 1L))
            .thenReturn(List.of(templateExercise));

        ResponseEntity<ApiResponse<List<TemplateExerciseDTO>>> response =
            templateExerciseController.getTemplateExercises(userDetails, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(1, response.getBody().getData().size());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    void getTemplateExercises_NotFound() {
        when(templateExerciseService.getUserTemplateExercises(1L, 1L))
            .thenThrow(new TemplateNotFoundException("Template not found"));

        ResponseEntity<ApiResponse<List<TemplateExerciseDTO>>> response =
            templateExerciseController.getTemplateExercises(userDetails, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Template not found", response.getBody().getMessage());
    }

    @Test
    void addTemplateExercise_Success() {
        when(templateExerciseService.addUserTemplateExercise(eq(1L), eq(1L), any(TemplateExercise.class)))
            .thenReturn(templateExercise);

        ResponseEntity<ApiResponse<TemplateExerciseDTO>> response =
            templateExerciseController.addTemplateExercise(userDetails, 1L, createRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(1L, response.getBody().getData().getExerciseId());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    void addTemplateExercise_NotFound() {
        when(templateExerciseService.addUserTemplateExercise(eq(1L), eq(1L), any(TemplateExercise.class)))
            .thenThrow(new TemplateNotFoundException("Template not found"));

        ResponseEntity<ApiResponse<TemplateExerciseDTO>> response =
            templateExerciseController.addTemplateExercise(userDetails, 1L, createRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Template not found", response.getBody().getMessage());
    }

    @Test
    void updateTemplateExercise_Success() {
        when(templateExerciseService.updateUserTemplateExercise(eq(1L), eq(1L), eq(1L), any(TemplateExercise.class)))
            .thenReturn(templateExercise);

        ResponseEntity<ApiResponse<TemplateExerciseDTO>> response =
            templateExerciseController.updateTemplateExercise(userDetails, 1L, 1L, createRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(1L, response.getBody().getData().getExerciseId());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    void deleteTemplateExercise_Success() {
        doNothing().when(templateExerciseService)
            .deleteUserTemplateExercise(1L, 1L, 1L);

        ResponseEntity<ApiResponse<Void>> response =
            templateExerciseController.deleteTemplateExercise(userDetails, 1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getMessage());
        verify(templateExerciseService).deleteUserTemplateExercise(1L, 1L, 1L);
    }

    @Test
    void deleteTemplateExercise_NotFound() {
        doThrow(new TemplateNotFoundException("Template or exercise not found"))
            .when(templateExerciseService)
            .deleteUserTemplateExercise(1L, 1L, 1L);

        ResponseEntity<ApiResponse<Void>> response =
            templateExerciseController.deleteTemplateExercise(userDetails, 1L, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Template or exercise not found", response.getBody().getMessage());
    }
}