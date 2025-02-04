package gymnote.gymnoteapi;

import gymnote.gymnoteapi.controller.TemplateController;
import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.exception.template.TemplateCreationException;
import gymnote.gymnoteapi.exception.template.TemplateNotFoundException;
import gymnote.gymnoteapi.model.dto.TemplateDTO;
import gymnote.gymnoteapi.model.template.CreateTemplateRequest;
import gymnote.gymnoteapi.model.template.TemplatesResponse;
import gymnote.gymnoteapi.security.service.UserDetailsImpl;
import gymnote.gymnoteapi.service.TemplateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
@AutoConfigureMockMvc
class TemplateControllerTest {

    @Mock
    private TemplateService templateService; // Only mock the service

    @InjectMocks
    private TemplateController templateController; // Controller uses the mocked service

    @Test
    void getUserTemplates_Success() {
        // Arrange
        Long userId = 1L;
        UserDetailsImpl userDetails = new UserDetailsImpl(userId, "username", "email", "password", Collections.emptyList());
        User user = new User();
        user.setId(userId);
        // Create a sample template
        Template template = new Template();
        template.setId(1L);
        template.setUser(user);
        template.setTemplateName("Template 1");
        template.setDescription("Description 1");

        // Mock the service to return the sample template(s)
        when(templateService.getTemplatesByUserId(userId)).thenReturn(List.of(template));

        // Act
        ResponseEntity<TemplatesResponse> response = templateController.getUserTemplates(userDetails);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        TemplatesResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getCount());
        assertEquals(1, body.getTemplates().size());

        // Fix the assertion to use getTemplateName() instead of getName()
        assertEquals("Template 1", body.getTemplates().get(0).getName());
    }

    @Test
    void GetUserTemplateById_Success() {
        Long userId = 1L;
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "username", "test@test.com","password", Collections.emptyList());
        User user = new User();
        user.setId(userId);

        Template template = new Template();
        template.setId(1L);
        template.setUser(user);
        template.setTemplateName("Template 1");
        template.setDescription("Description 1");

        when(templateService.getUserTemplateById(1L, userId)).thenReturn(template);

        ResponseEntity<TemplateDTO> response = templateController.getUserTemplateById(userDetails, 1L);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Template 1", response.getBody().getName());
    }

    @Test
    void getUserTemplateById_NotFound() {
        // Arrange
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "username", "test@test.com", "password", Collections.emptyList());
        when(templateService.getUserTemplateById(1L, 1L)).thenThrow(
                new TemplateNotFoundException("Template not found")
        );

        // Act
        ResponseEntity<TemplateDTO> response = templateController.getUserTemplateById(userDetails, 1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createTemplate_Success() {
        // Arrange

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "username", "test@test.com","password", Collections.emptyList());
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

        // Act
        ResponseEntity<TemplateDTO> response = templateController.createTemplate(userDetails, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Template 1", response.getBody().getName());
    }

    @Test
    void createTemplate_Failure() {
        // Arrange
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "username", "test@test.com", "password", Collections.emptyList());
        CreateTemplateRequest request = new CreateTemplateRequest();
        request.setTemplateName("Template 1");
        request.setDescription("Description 1");

        when(templateService.createUserTemplate(any(Template.class), eq(1L))).thenThrow(
                new TemplateCreationException("Failed to create template", new Exception())
        );

        // Act
        ResponseEntity<TemplateDTO> response = templateController.createTemplate(userDetails, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteTemplateById_Success() {
        // Arrange
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "username", "test@test.com", "password", Collections.emptyList());
        doNothing().when(templateService).deleteUserTemplateById(1L, 1L);

        // Act
        ResponseEntity<?> response = templateController.deleteTemplateById(userDetails, 1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteTemplateById_NotFound() {
        // Arrange
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "username", "test@test.com", "password", Collections.emptyList());
        doThrow(new TemplateNotFoundException("Template not found")).when(templateService).deleteUserTemplateById(1L, 1L);

        // Act
        ResponseEntity<?> response = templateController.deleteTemplateById(userDetails, 1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}