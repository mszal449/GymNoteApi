package gymnote.gymnoteapi.serviceTest;

import gymnote.gymnoteapi.entity.Template;
import gymnote.gymnoteapi.entity.User;
import gymnote.gymnoteapi.exception.template.TemplateCreationException;
import gymnote.gymnoteapi.exception.template.TemplateDeletionException;
import gymnote.gymnoteapi.exception.template.TemplateNotFoundException;
import gymnote.gymnoteapi.exception.user.UserNotFoundException;
import gymnote.gymnoteapi.repository.TemplateRepository;
import gymnote.gymnoteapi.service.TemplateService;
import gymnote.gymnoteapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TemplateServiceTest {

  @Mock
  private TemplateRepository templateRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private TemplateService templateService;

  private Template template;
  private User user;
  private final Long USER_ID = 1L;
  private final Long TEMPLATE_ID = 1L;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(USER_ID);
    
    template = new Template();
    template.setId(TEMPLATE_ID);
    template.setTemplateName("Test Template");
    template.setDescription("Test Description");
    template.setUser(user);
  }

  @Test
  void getTemplatesByUserId_Success() {
    when(templateRepository.findByUserId(USER_ID)).thenReturn(List.of(template));

    List<Template> result = templateService.getTemplatesByUserId(USER_ID);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(template.getTemplateName(), result.get(0).getTemplateName());
    verify(templateRepository).findByUserId(USER_ID);
  }

  @Test
  void getUserTemplateById_Success() {
    when(templateRepository.findByIdAndUserId(TEMPLATE_ID, USER_ID))
        .thenReturn(Optional.of(template));

    Template result = templateService.getUserTemplateById(TEMPLATE_ID, USER_ID);

    assertNotNull(result);
    assertEquals(template.getId(), result.getId());
    verify(templateRepository).findByIdAndUserId(TEMPLATE_ID, USER_ID);
  }

  @Test
  void getUserTemplateById_ThrowsTemplateNotFoundException() {
    when(templateRepository.findByIdAndUserId(TEMPLATE_ID, USER_ID))
        .thenReturn(Optional.empty());

    assertThrows(TemplateNotFoundException.class, () ->
        templateService.getUserTemplateById(TEMPLATE_ID, USER_ID));
  }

  @Test
  void createUserTemplate_Success() {
    when(userService.findById(USER_ID)).thenReturn(user);
    when(templateRepository.save(any(Template.class))).thenReturn(template);

    Template result = templateService.createUserTemplate(template, USER_ID);

    assertNotNull(result);
    assertEquals(template.getTemplateName(), result.getTemplateName());
    verify(templateRepository).save(template);
  }

  @Test
  void createUserTemplate_ThrowsUserNotFoundException() {
    when(userService.findById(USER_ID)).thenThrow(new UserNotFoundException(""));

    assertThrows(UserNotFoundException.class, () ->
        templateService.createUserTemplate(template, USER_ID));
  }

  @Test
  void createUserTemplate_ThrowsTemplateCreationException() {
    when(userService.findById(USER_ID)).thenReturn(user);
    when(templateRepository.save(any(Template.class)))
        .thenThrow(new DataIntegrityViolationException(""));

    assertThrows(TemplateCreationException.class, () ->
        templateService.createUserTemplate(template, USER_ID));
  }

  @Test
  void updateUserTemplate_Success() {
    Template updateData = new Template();
    updateData.setId(TEMPLATE_ID);
    updateData.setTemplateName("Updated Name");
    
    when(templateRepository.findByIdAndUserId(TEMPLATE_ID, USER_ID))
        .thenReturn(Optional.of(template));
    when(templateRepository.save(any(Template.class))).thenReturn(template);

    Template result = templateService.updateUserTemplate(TEMPLATE_ID, USER_ID, updateData);

    assertNotNull(result);
    verify(templateRepository).save(any(Template.class));
  }

  @Test
  void updateUserTemplate_ThrowsTemplateNotFoundException() {
    when(templateRepository.findByIdAndUserId(TEMPLATE_ID, USER_ID))
        .thenReturn(Optional.empty());

    assertThrows(TemplateNotFoundException.class, () ->
        templateService.updateUserTemplate(TEMPLATE_ID, USER_ID, template));
  }

  @Test
  void deleteUserTemplateById_Success() {
    when(templateRepository.findByIdAndUserId(TEMPLATE_ID, USER_ID))
        .thenReturn(Optional.of(template));
    doNothing().when(templateRepository).delete(template);

    assertDoesNotThrow(() -> templateService.deleteUserTemplateById(TEMPLATE_ID, USER_ID));
    verify(templateRepository).delete(template);
  }

  @Test
  void deleteUserTemplateById_ThrowsTemplateNotFoundException() {
    when(templateRepository.findByIdAndUserId(TEMPLATE_ID, USER_ID))
        .thenReturn(Optional.empty());

    assertThrows(TemplateNotFoundException.class, () ->
        templateService.deleteUserTemplateById(TEMPLATE_ID, USER_ID));
  }

  @Test
  void deleteUserTemplateById_ThrowsTemplateDeletionException() {
    when(templateRepository.findByIdAndUserId(TEMPLATE_ID, USER_ID))
        .thenReturn(Optional.of(template));
    doThrow(new RuntimeException()).when(templateRepository).delete(template);

    assertThrows(TemplateDeletionException.class, () ->
        templateService.deleteUserTemplateById(TEMPLATE_ID, USER_ID));
  }
}
