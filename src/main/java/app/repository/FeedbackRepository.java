package app.repository;

import app.Utils;
import app.generated.projectT.Sequences;
import app.generated.projectT.tables.daos.FeedbackDao;
import app.generated.projectT.tables.pojos.Feedback;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static app.generated.projectT.Tables.FEEDBACK;

@Repository
@RequiredArgsConstructor
public class FeedbackRepository {
    private final DSLContext dslContext;
    private final FeedbackDao feedbackDao;

    public List<Feedback> getList() {
        return dslContext.selectFrom(FEEDBACK)
                .where(FEEDBACK.DELETED_DATETIME.isNull())
                .fetchInto(Feedback.class);
    }

    public Optional<Feedback> getActualFeedback(Long feedbackEntityId) {
        return dslContext.selectFrom(FEEDBACK)
                .where(
                        FEEDBACK.ENTITY_ID.eq(feedbackEntityId),
                        FEEDBACK.DELETED_DATETIME.isNull()
                )
                .fetchOptionalInto(Feedback.class);
    }

    public void createFeedback(Feedback feedback) {
        feedbackDao.insert(feedback);
    }

    public void deleteFeedback(Long feedbackId) {
        dslContext.update(FEEDBACK)
                .set(FEEDBACK.DELETED_DATETIME, Utils.now())
                .where(FEEDBACK.ID.eq(feedbackId))
                .execute();
    }

    public Long getNextFeedbackId() {
        return dslContext.nextval(Sequences.FEEDBACK_ID_SEQ);
    }
}
