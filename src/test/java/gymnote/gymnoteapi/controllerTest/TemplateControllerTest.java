package gymnote.gymnoteapi.controllerTest;

import gymnote.gymnoteapi.controller.TemplateController;
import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.exception.template.TemplateCreationException;
import gymnote.gymnoteapi.exception.template.TemplateNotFoundException;
import gymnote.gymnoteapi.model.api.ApiResponse;
import gymnote.gymnoteapi.model.dto.TemplateDTO;
import gymnote.gymnoteapi.model.template.CreateTemplateRequest;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.TemplateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class TemplateControllerTest {

    @Mock
    private TemplateService templateService;

    @InjectMocks
    private TemplateController templateController;

    @Test
    void getUserTemplates_Success() {
        Long userId = 1L;
        UserDetailsImpl userDetails = new UserDetailsImpl(userId, "username", "email", "password", Collections.emptyList());
        User user = new User();
        user.setId(userId);
        Template template = new Template();
        template.setId(1L);
        template.setUser(user);
        template.setTemplateName("Template 1");
        template.setDescription("Description 1");

        when(templateService.getTemplatesByUserId(userId)).thenReturn(List.of(template));

        ResponseEntity<ApiResponse<List<TemplateDTO>>> response = templateController.getUserTemplates(userDetails);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(1, response.getBody().getCount());
        assertEquals("Template 1", response.getBody().getData().get(0).getName());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    void GetUserTemplateById_Success() {
        Long userId = 1L;
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "username", "test@test.com", "password", Collections.emptyList());
        User user = new User();
        user.setId(userId);
        Template template = new Template();
        template.setId(1L);
        template.setUser(user);
        template.setTemplateName("Template 1");
        template.setDescription("Description 1");

        when(templateService.getUserTemplateById(1L, userId)).thenReturn(template);

        ResponseEntity<ApiResponse<TemplateDTO>> response = templateController.getUserTemplateById(userDetails, 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals("Template 1", response.getBody().getData().getName());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    void getUserTemplateById_NotFound() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "username", "test@test.com", "password", Collections.emptyList());
        when(templateService.getUserTemplateById(1L, 1L))
                .thenThrow(new TemplateNotFoundException("Template not found"));

        assertThrows(TemplateNotFoundException.class, () ->
            templateController.getUserTemplateById(userDetails, 1L));
    }

    @Test
    void createTemplate_Success() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "username", "test@test.com", "password", Collections.emptyList());
        User user = new User();
        user.setId(userDetails.getId());

        CreateTemplateRequest request = new CreateTemplateRequest();
        request.setTemplateName("Template 1");
        request.setDescription("Description 1");

        Template template = new Template();
        template.setId(1L);
        template.setTemplateName("Template 1");
        template.setDescription("Description 1");
        template.setUser(user);

        when(templateService.createUserTemplate(any(Template.class), eq(1L))).thenReturn(template);

        ResponseEntity<ApiResponse<TemplateDTO>> response = templateController.createTemplate(userDetails, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals("Template 1", response.getBody().getData().getName());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    void createTemplate_Failure() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "username", "test@test.com", "password", Collections.emptyList());
        CreateTemplateRequest request = new CreateTemplateRequest();
        request.setTemplateName("Template 1");
        request.setDescription("Description 1");

        when(templateService.createUserTemplate(any(Template.class), eq(1L)))
                .thenThrow(new TemplateCreationException("Failed to create template", new Exception()));


        assertThrows(TemplateCreationException.class, () ->
             templateController.createTemplate(userDetails, request));
    }

    @Test
    void deleteTemplateById_Success() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "username", "test@test.com", "password", Collections.emptyList());
        doNothing().when(templateService).deleteUserTemplateById(1L, 1L);

        ResponseEntity<ApiResponse<Void>> response = templateController.deleteTemplateById(userDetails, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    void deleteTemplateById_NotFound() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "username", "test@test.com", "password", Collections.emptyList());
        doThrow(new TemplateNotFoundException("Template not found"))
                .when(templateService).deleteUserTemplateById(1L, 1L);

        assertThrows(TemplateNotFoundException.class, () ->
            templateController.deleteTemplateById(userDetails, 1L));
    }
}