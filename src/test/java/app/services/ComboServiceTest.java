package app.services;

import app.InvalidDataException;
import app.entities.pojos.ComboPojo;
import app.generated.jooq.tables.pojos.Combo;
import app.generated.jooq.tables.pojos.Discount;
import app.repository.ComboRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComboServiceTest {

    @MockBean
    ComboRepository comboRepository;

    @Autowired
    ComboService comboService;

    @Test
    void createComboTest_WithNullFields() {
        Combo combo = new Combo();
        Mockito.when(comboRepository.getNextComboId()).thenReturn(1L);
        Mockito.doNothing().when(comboRepository).createCombo(combo);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> comboService.createCombo(combo));
        assertNotNull(ex);
        assertEquals(3, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("name"));
        assertTrue(ex.getErrors().containsKey("description"));
        assertTrue(ex.getErrors().containsKey("price"));

        Mockito.verify(comboRepository, Mockito.times(0)).createCombo(combo);
    }

    @Test
    void createComboTest_WithEmptyFieldsAndNegativePrice() {
        Combo combo = new Combo(
                null,
                null,
                null,
                null,
                "",
                "",
                BigDecimal.ONE.negate(),
                null
        );
        Mockito.when(comboRepository.getNextComboId()).thenReturn(1L);
        Mockito.doNothing().when(comboRepository).createCombo(combo);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> comboService.createCombo(combo));
        assertNotNull(ex);
        assertEquals(3, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("name"));
        assertTrue(ex.getErrors().containsKey("description"));
        assertTrue(ex.getErrors().containsKey("price"));

        Mockito.verify(comboRepository, Mockito.times(0)).createCombo(combo);
    }

    @Test
    void createComboTest_WithBlankFields() {
        Combo combo = new Combo(
                null,
                null,
                null,
                null,
                "   ",
                " ",
                BigDecimal.ONE,
                null
        );
        Mockito.when(comboRepository.getNextComboId()).thenReturn(1L);
        Mockito.doNothing().when(comboRepository).createCombo(combo);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> comboService.createCombo(combo));
        assertNotNull(ex);
        assertEquals(2, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("name"));
        assertTrue(ex.getErrors().containsKey("description"));
        assertFalse(ex.getErrors().containsKey("price"));

        Mockito.verify(comboRepository, Mockito.times(0)).createCombo(combo);
    }

    @Test
    void createComboTest_WithValidFields() {
        Combo combo = new Combo(
                null,
                null,
                null,
                null,
                "combo_1",
                "50 Togo, 100 e togo",
                BigDecimal.ONE,
                null
        );
        Mockito.when(comboRepository.getNextComboId()).thenReturn(1L);
        Mockito.doNothing().when(comboRepository).createCombo(combo);

        comboService.createCombo(combo);

        Mockito.verify(comboRepository, Mockito.times(1)).createCombo(combo);
    }

    @Test
    void deleteComboTest() {
        Mockito.doNothing().when(comboRepository).deleteCombo(Mockito.anyLong());

        comboService.deleteCombo(1L);

        Mockito.verify(comboRepository, Mockito.times(1)).deleteCombo(1L);
    }

    @Test
    void getByIdTest() {
        ComboPojo comboPojo = new ComboPojo();
        comboPojo.setId(1L);
        comboPojo.setComboId(2L);
        comboPojo.setCreatedDatetime(LocalDateTime.now());
        comboPojo.setName("combo_1");
        comboPojo.setDescription("50 Togo, 100 e togo");
        comboPojo.setPrice(BigDecimal.ONE);

        List<Discount> discounts = new ArrayList<>();
        discounts.add(new Discount());

        Mockito.when(comboRepository.getById(2L)).thenReturn(comboPojo);
        Mockito.when(comboRepository.getDiscountsByComboId(2L)).thenReturn(discounts);

        ComboPojo comboById = comboService.getById(2L);

        assertEquals(discounts, comboById.getDiscounts());
    }

    @Test
    void editComboTest() {
        Combo editCombo = new Combo(
                3L,
                2L,
                null,
                null,
                "combo_1v2",
                "50 Togo, 100 e togo",
                BigDecimal.ONE,
                null
        );

        ComboPojo comboPojo = new ComboPojo();
        comboPojo.setId(1L);
        comboPojo.setComboId(2L);
        comboPojo.setCreatedDatetime(LocalDateTime.now());
        comboPojo.setName("combo_1");
        comboPojo.setDescription("50 Togo, 100 e togo");
        comboPojo.setPrice(BigDecimal.ONE);

        Mockito.when(comboRepository.getById(2L)).thenReturn(comboPojo);
        Mockito.doNothing().when(comboRepository).deleteCombo(2L);
        Mockito.doNothing().when(comboRepository).createCombo(editCombo);

        comboService.editCombo(editCombo);

        Mockito.verify(comboRepository, Mockito.times(1)).deleteCombo(2L);
        Mockito.verify(comboRepository, Mockito.times(1)).createCombo(editCombo);
    }
}