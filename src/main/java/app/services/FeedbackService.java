package app.services;

import app.InvalidDataException;
import app.UserContext;
import app.generated.jooq.tables.pojos.Feedback;
import app.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.tools.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository repository;
    private final UserContext userContext;

    public List<Feedback> getList() {
        return repository.getList();
    }

    public Feedback getById(Long feedbackId) {
        return repository.getById(feedbackId);
    }

    public void createFeedback(Feedback feedback) {
        validateCreateFeedback(feedback);
        feedback.setFeedbackId(repository.getNextFeedbackId());
        repository.createFeedback(feedback);
    }

    public void deleteFeedback(Long feedbackId) {
        repository.deleteFeedback(feedbackId);
    }

    public void editFeedback(Feedback feedback) {
        validateCreateFeedback(feedback);
        Feedback oldFeedback = repository.getById(feedback.getFeedbackId());
        deleteFeedback(oldFeedback.getFeedbackId());
        repository.createFeedback(feedback);
    }

    private void validateCreateFeedback(Feedback feedback) {
        Map<String, String> errors = new HashMap<>();
        if (StringUtils.isBlank(feedback.getComment())) {
            errors.put("comment", "Comment can't be null");
        }

        if (feedback.getAccountId() == null) {
            errors.put("account", "Account can't be null");
        } else if (!feedback.getAccountId().equals(userContext.getAccountId())) {
            errors.put("account", "Account id mismatch");
        }

        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }
}
