package app.repository;

import app.Utils;
import app.generated.projectT.Sequences;
import app.generated.projectT.tables.daos.ComponentDao;
import app.generated.projectT.tables.pojos.Component;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static app.generated.projectT.Tables.COMPONENT;

@Repository
@RequiredArgsConstructor
public class ComponentRepository {
    private final DSLContext dslContext;
    private final ComponentDao componentDao;

    public List<Component> getList() {
        return dslContext.selectFrom(COMPONENT)
                .where(COMPONENT.DELETED_DATETIME.isNull())
                .fetchInto(Component.class);
    }

    public Optional<Component> getActualComponent(Long componentEntityId) {
        return dslContext.selectFrom(COMPONENT)
                .where(
                        COMPONENT.ENTITY_ID.eq(componentEntityId),
                        COMPONENT.DELETED_DATETIME.isNull()
                )
                .fetchOptionalInto(Component.class);
    }

    public void deleteById(Long componentId) {
        dslContext.update(COMPONENT)
                .set(COMPONENT.DELETED_DATETIME, Utils.now())
                .where(COMPONENT.ID.eq(componentId))
                .execute();
    }

    public void createNewComponent(Component newComponent) {
        componentDao.insert(newComponent);
    }

    public Long getNextComponentId() {
        return dslContext.nextval(Sequences.COMPONENT_ID_SEQ);
    }

}
