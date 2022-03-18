package app.repository;

import app.Utils;
import app.entities.pojos.ComboPojo;
import app.generated.projectT.Sequences;
import app.generated.projectT.tables.daos.ComboDao;
import app.generated.projectT.tables.pojos.Combo;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static app.generated.projectT.Tables.COMBO;

@Repository
@RequiredArgsConstructor
public class ComboRepository {
    private final DSLContext dslContext;
    private final ComboDao comboDao;

    public List<ComboPojo> getList() {
        return dslContext.selectFrom(COMBO)
                .where(COMBO.DELETED_DATETIME.isNull())
                .fetchInto(ComboPojo.class);
    }

    public Optional<ComboPojo> getActualCombo(Long comboEntityId) {
        return dslContext.selectFrom(COMBO)
                .where(
                        COMBO.ENTITY_ID.eq(comboEntityId),
                        COMBO.DELETED_DATETIME.isNull()
                )
                .fetchOptionalInto(ComboPojo.class);
    }

    public void createCombo(Combo combo) {
        comboDao.insert(combo);
    }

    public void deleteCombo(Long comboId) {
        dslContext.update(COMBO)
                .set(COMBO.DELETED_DATETIME, Utils.now())
                .where(COMBO.ID.eq(comboId))
                .execute();
    }

    public Long getNextComboId() {
        return dslContext.nextval(Sequences.COMBO_ID_SEQ);
    }

//    public List<Discount> getDiscountsByComboId(Long comboId) {
//        return dslContext.select(DISCOUNT.fields())
//                .from(REF_COMBO_DISCOUNT)
//                .leftJoin(DISCOUNT).on(REF_COMBO_DISCOUNT.DISCOUNT_ID.eq(DISCOUNT.DISCOUNT_ID))
//                .where(REF_COMBO_DISCOUNT.COMBO_ID.eq(comboId), DISCOUNT.DELETED_DATETIME.isNull())
//                .fetchInto(Discount.class);
//    }

    public List<Combo> getList(List<Long> comboIdList) {
        return dslContext.select(COMBO.fields())
                .from(COMBO)
                .where(COMBO.ID.in(comboIdList))
                .fetchInto(Combo.class);
    }
}
