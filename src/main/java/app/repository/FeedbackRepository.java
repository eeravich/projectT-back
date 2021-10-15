package app.repository;

import app.generated.jooq.Sequences;
import app.generated.jooq.tables.pojos.Feedback;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static app.generated.jooq.Tables.FEEDBACK;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FeedbackRepository {
    private final DSLContext dslContext;

    public List<Feedback> getList() {
        return dslContext.selectFrom(FEEDBACK)
                .where(FEEDBACK.DELETED_DATETIME.isNull())
                .fetchInto(Feedback.class);
    }

    public Feedback getById(Long feedbackId) {
        return dslContext.selectFrom(FEEDBACK)
                .where(FEEDBACK.FEEDBACK_ID.eq(feedbackId), FEEDBACK.DELETED_DATETIME.isNull())
                .fetchOneInto(Feedback.class);
    }

    public void createFeedback(Feedback feedback) {
        dslContext.insertInto(FEEDBACK)
                .set(FEEDBACK.FEEDBACK_ID, feedback.getFeedbackId())
                .set(FEEDBACK.ACCOUNT_ID, feedback.getAccountId())
                .set(FEEDBACK.CREATED_DATETIME, LocalDateTime.now())
                .set(FEEDBACK.COMMENT, feedback.getComment())
                .execute();
    }

    public void deleteFeedback(Long feedbackId) {
        dslContext.update(FEEDBACK)
                .set(FEEDBACK.DELETED_DATETIME, LocalDateTime.now())
                .where(FEEDBACK.FEEDBACK_ID.eq(feedbackId), FEEDBACK.DELETED_DATETIME.isNull())
                .execute();
    }

    public Long getNextFeedbackId() {
        return dslContext.nextval(Sequences.FEEDBACK_FEEDBACK_ID_SEQ);
    }
}
