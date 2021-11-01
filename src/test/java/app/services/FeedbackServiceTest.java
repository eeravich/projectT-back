package app.services;

import app.InvalidDataException;
import app.UserContext;
import app.entities.enums.Roles;
import app.generated.jooq.tables.pojos.Feedback;
import app.repository.FeedbackRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FeedbackServiceTest {

    @MockBean
    FeedbackRepository feedbackRepository;

    @MockBean
    UserContext userContext;

    @Autowired
    FeedbackService feedbackService;

    @Test
    void createFeedbackTest_WithNullFields() {
        Feedback feedback = new Feedback();

        Mockito.when(feedbackRepository.getNextFeedbackId()).thenReturn(2L);
        Mockito.when(userContext.getAccountId()).thenReturn(5L);
        Mockito.doNothing().when(feedbackRepository).createFeedback(feedback);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> feedbackService.createFeedback(feedback));
        assertNotNull(ex);
        assertEquals(1, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("comment"));

        Mockito.verify(feedbackRepository, Mockito.times(0)).createFeedback(feedback);
    }

    @Test
    void createFeedbackTest_WithEmptyFields() {
        Feedback feedback = new Feedback(
                1L,
                2L,
                LocalDateTime.now(),
                null,
                "",
                null
        );

        Mockito.when(feedbackRepository.getNextFeedbackId()).thenReturn(3L);
        Mockito.when(userContext.getAccountId()).thenReturn(5L);
        Mockito.doNothing().when(feedbackRepository).createFeedback(feedback);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> feedbackService.createFeedback(feedback));
        assertNotNull(ex);
        assertEquals(1, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("comment"));

        Mockito.verify(feedbackRepository, Mockito.times(0)).createFeedback(feedback);
    }

    @Test
    void createFeedbackTest_WithBlankFields() {
        Feedback feedback = new Feedback(
                1L,
                2L,
                LocalDateTime.now(),
                null,
                "   ",
                null
        );

        Mockito.when(feedbackRepository.getNextFeedbackId()).thenReturn(3L);
        Mockito.when(userContext.getAccountId()).thenReturn(5L);
        Mockito.doNothing().when(feedbackRepository).createFeedback(feedback);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> feedbackService.createFeedback(feedback));
        assertNotNull(ex);
        assertEquals(1, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("comment"));

        Mockito.verify(feedbackRepository, Mockito.times(0)).createFeedback(feedback);
    }

    @Test
    void createFeedbackTest_WithValidFields() {
        Feedback feedback = new Feedback(
                1L,
                2L,
                LocalDateTime.now(),
                null,
                "11/10",
                null
        );

        Mockito.when(feedbackRepository.getNextFeedbackId()).thenReturn(3L);
        Mockito.when(userContext.getAccountId()).thenReturn(5L);
        Mockito.doNothing().when(feedbackRepository).createFeedback(feedback);

        feedbackService.createFeedback(feedback);

        Mockito.verify(feedbackRepository, Mockito.times(1)).createFeedback(feedback);
    }

    @Test
    void deleteFeedbackTest_UserDeleteItsFeedback() {
        Feedback feedback = new Feedback(
                1L,
                2L,
                LocalDateTime.now(),
                null,
                "11/10",
                5L
        );

        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(feedbackRepository.getById(2L)).thenReturn(feedback);
        Mockito.when(userContext.getAccountId()).thenReturn(5L);
        Mockito.doNothing().when(feedbackRepository).deleteFeedback(2L);

        feedbackService.deleteFeedback(2L);

        Mockito.verify(feedbackRepository, Mockito.times(1)).deleteFeedback(2L);
    }

    @Test
    void deleteFeedbackTest_UserDeleteForeignFeedback() {
        Feedback feedback = new Feedback(
                1L,
                2L,
                LocalDateTime.now(),
                null,
                "11/10",
                5L
        );

        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(feedbackRepository.getById(2L)).thenReturn(feedback);
        Mockito.when(userContext.getAccountId()).thenReturn(3L);
        Mockito.doNothing().when(feedbackRepository).deleteFeedback(2L);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> feedbackService.deleteFeedback(2L));
        assertNotNull(ex);

        Mockito.verify(feedbackRepository, Mockito.times(0)).deleteFeedback(2L);
    }

    @Test
    void deleteFeedbackTest_ManagerOrAdminDeleteForeignFeedback() {
        Feedback feedback = new Feedback(
                1L,
                2L,
                LocalDateTime.now(),
                null,
                "11/10",
                5L
        );

        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_MANAGER.getId().longValue());
        Mockito.when(feedbackRepository.getById(2L)).thenReturn(feedback);
        Mockito.when(userContext.getAccountId()).thenReturn(3L);
        Mockito.doNothing().when(feedbackRepository).deleteFeedback(2L);

        feedbackService.deleteFeedback(2L);

        Mockito.verify(feedbackRepository, Mockito.times(1)).deleteFeedback(2L);
    }

    @Test
    void editFeedbackTest_UserEditItsFeedback() {
        Feedback feedback = new Feedback(
                1L,
                2L,
                LocalDateTime.now(),
                null,
                "1/10",
                5L
        );

        Feedback oldFeedback = new Feedback(
                1L,
                2L,
                LocalDateTime.now(),
                null,
                "11/10",
                5L
        );

        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(feedbackRepository.getById(2L)).thenReturn(oldFeedback);
        Mockito.when(userContext.getAccountId()).thenReturn(5L);
        Mockito.doNothing().when(feedbackRepository).deleteFeedback(2L);
        Mockito.doNothing().when(feedbackRepository).createFeedback(feedback);

        feedbackService.editFeedback(feedback);

        Mockito.verify(feedbackRepository, Mockito.times(1)).deleteFeedback(2L);
        Mockito.verify(feedbackRepository, Mockito.times(1)).createFeedback(feedback);
    }

    @Test
    void editFeedbackTest_UserEditForeignFeedback() {
        Feedback feedback = new Feedback(
                1L,
                2L,
                LocalDateTime.now(),
                null,
                "1/10",
                5L
        );

        Feedback oldFeedback = new Feedback(
                1L,
                2L,
                LocalDateTime.now(),
                null,
                "11/10",
                9L
        );

        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(feedbackRepository.getById(2L)).thenReturn(oldFeedback);
        Mockito.when(userContext.getAccountId()).thenReturn(5L);
        Mockito.doNothing().when(feedbackRepository).deleteFeedback(2L);
        Mockito.doNothing().when(feedbackRepository).createFeedback(feedback);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> feedbackService.editFeedback(feedback));
        assertNotNull(ex);

        Mockito.verify(feedbackRepository, Mockito.times(0)).deleteFeedback(2L);
        Mockito.verify(feedbackRepository, Mockito.times(0)).createFeedback(feedback);
    }

    @Test
    void editFeedbackTest_ManagerOrAdminEditForeignFeedback() {
        Feedback feedback = new Feedback(
                1L,
                2L,
                LocalDateTime.now(),
                null,
                "11/10",
                5L
        );

        Feedback oldFeedback = new Feedback(
                1L,
                2L,
                LocalDateTime.now(),
                null,
                "1/10",
                9L
        );

        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_MANAGER.getId().longValue());
        Mockito.when(feedbackRepository.getById(2L)).thenReturn(oldFeedback);
        Mockito.when(userContext.getAccountId()).thenReturn(5L);
        Mockito.doNothing().when(feedbackRepository).deleteFeedback(2L);

        feedbackService.editFeedback(feedback);

        Mockito.verify(feedbackRepository, Mockito.times(1)).deleteFeedback(2L);
        Mockito.verify(feedbackRepository, Mockito.times(1)).createFeedback(feedback);
    }
}