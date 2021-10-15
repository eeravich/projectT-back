package app.controllers;

import app.generated.jooq.tables.pojos.Feedback;
import app.services.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService service;

    @GetMapping("/list")
    public List<Feedback> getList() {
        return service.getList();
    }

    @GetMapping("/{feedbackId}")
    public Feedback getById(@PathVariable Long feedbackId) {
        return service.getById(feedbackId);
    }

    @PostMapping("/create")
    @Transactional
    public void createFeedback(@RequestBody Feedback feedback) {
        service.createFeedback(feedback);
    }

    @PutMapping("/edit")
    @Transactional
    public void editFeedback(@RequestBody Feedback feedback) {
        service.editFeedback(feedback);
    }

    @DeleteMapping("/delete/{feedbackId}")
    @Transactional
    public void deleteFeedback(@PathVariable Long feedbackId) {
        service.deleteFeedback(feedbackId);
    }
}
