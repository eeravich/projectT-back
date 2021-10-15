package app.repository;

import app.generated.jooq.Sequences;
import app.generated.jooq.tables.pojos.Component;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static app.generated.jooq.Tables.COMPONENT;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ComponentRepository {
    private final DSLContext dslContext;

    public List<Component> getList() {
        return dslContext.selectFrom(COMPONENT)
                .where(COMPONENT.DELETED_DATETIME.isNull())
                .fetchInto(Component.class);
    }

    public Component getById(Long componentId) {
        return dslContext.selectFrom(COMPONENT)
                .where(COMPONENT.COMPONENT_ID.eq(componentId), COMPONENT.DELETED_DATETIME.isNull())
                .fetchOneInto(Component.class);
    }

    public void deleteById(Long componentId) {
        dslContext.update(COMPONENT)
                .set(COMPONENT.DELETED_DATETIME, LocalDateTime.now())
                .where(COMPONENT.COMPONENT_ID.eq(componentId), COMPONENT.DELETED_DATETIME.isNull())
                .execute();
    }

    public void createNewComponent(Component newComponent) {
        dslContext.insertInto(COMPONENT)
                .set(COMPONENT.COMPONENT_ID, newComponent.getComponentId())
                .set(COMPONENT.CREATED_DATETIME, LocalDateTime.now())
                .set(COMPONENT.NAME, newComponent.getName())
                .set(COMPONENT.DESCRIPTION, newComponent.getDescription())
                .execute();
    }

    public Long getNextComponentId() {
        return dslContext.nextval(Sequences.COMPONENT_COMPONENT_ID_SEQ);
    }

}
