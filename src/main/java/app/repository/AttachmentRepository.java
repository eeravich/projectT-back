package app.repository;

import app.generated.jooq.tables.pojos.Attachment;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static app.generated.jooq.Tables.ATTACHMENT;

@Repository
@RequiredArgsConstructor
public class AttachmentRepository {
    private final DSLContext dslContext;

    public void save(Attachment attachment) {
        dslContext.insertInto(ATTACHMENT)
                .set(ATTACHMENT.CREATED_DATETIME, LocalDateTime.now())
                .set(ATTACHMENT.NAME, attachment.getName())
                .set(ATTACHMENT.MIME_TYPE, attachment.getMimeType())
                .set(ATTACHMENT.SIZE, attachment.getSize())
                .set(ATTACHMENT.DATA, attachment.getData())
                .execute();
    }

    public void delete(Long id) {
        dslContext.update(ATTACHMENT)
                .set(ATTACHMENT.DELETED_DATETIME, LocalDateTime.now())
                .where(ATTACHMENT.ID.eq(id))
                .execute();
    }

    public Attachment getById(Long id) {
        return dslContext.selectFrom(ATTACHMENT)
                .where(ATTACHMENT.ID.eq(id))
                .fetchOneInto(Attachment.class);
    }

}
