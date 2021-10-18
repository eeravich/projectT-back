package app.repository;

import app.entities.pojos.ComboPojo;
import app.generated.jooq.Sequences;
import app.generated.jooq.tables.pojos.Combo;
import app.generated.jooq.tables.pojos.Discount;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static app.generated.jooq.Tables.REF_COMBO_DISCOUNT;
import static app.generated.jooq.Tables.DISCOUNT;
import static app.generated.jooq.Tables.COMBO;

@Repository
@RequiredArgsConstructor
public class ComboRepository {
    private final DSLContext dslContext;

    public List<ComboPojo> getList() {
        return dslContext.selectFrom(COMBO)
                .where(COMBO.DELETED_DATETIME.isNull())
                .fetchInto(ComboPojo.class);
    }

    public ComboPojo getById(Long comboId) {
        return dslContext.selectFrom(COMBO)
                .where(COMBO.COMBO_ID.eq(comboId), COMBO.DELETED_DATETIME.isNull())
                .fetchOneInto(ComboPojo.class);
    }

    public void createCombo(Combo combo) {
        dslContext.insertInto(COMBO)
                .set(COMBO.COMBO_ID, combo.getComboId())
                .set(COMBO.CREATED_DATETIME, LocalDateTime.now())
                .set(COMBO.NAME, combo.getName())
                .set(COMBO.DESCRIPTION, combo.getDescription())
                .set(COMBO.PRICE, combo.getPrice())
                .set(COMBO.ATTACHMENT_ID, combo.getAttachmentId())
                .execute();
    }

    public void deleteCombo(Long comboId) {
        dslContext.update(COMBO)
                .set(COMBO.DELETED_DATETIME, LocalDateTime.now())
                .where(COMBO.COMBO_ID.eq(comboId), COMBO.DELETED_DATETIME.isNull())
                .execute();
    }

    public Long getNextComboId() {
        return dslContext.nextval(Sequences.COMBO_COMBO_ID_SEQ);
    }

    public List<Discount> getDiscountsByComboId(Long comboId) {
        return dslContext.select(DISCOUNT.fields())
                .from(REF_COMBO_DISCOUNT)
                .leftJoin(DISCOUNT).on(REF_COMBO_DISCOUNT.DISCOUNT_ID.eq(DISCOUNT.DISCOUNT_ID))
                .where(REF_COMBO_DISCOUNT.COMBO_ID.eq(comboId), DISCOUNT.DELETED_DATETIME.isNull())
                .fetchInto(Discount.class);
    }
}
