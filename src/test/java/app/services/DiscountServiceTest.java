package app.services;

import app.InvalidDataException;
import app.generated.jooq.tables.pojos.Discount;
import app.repository.DiscountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DiscountServiceTest {

    @MockBean
    DiscountRepository discountRepository;

    @Autowired
    DiscountService discountService;

    @Test
    void deleteDiscountTest() {
        Mockito.doNothing().when(discountRepository).deleteDiscount(Mockito.anyLong());

        discountService.deleteDiscount(2L);

        Mockito.verify(discountRepository, Mockito.times(1)).deleteDiscount(2L);
    }

    @Test
    void createDiscountTest_WithNullFields() {
        Discount discount = new Discount();

        Mockito.when(discountRepository.getNextDiscountId()).thenReturn(2L);
        Mockito.doNothing().when(discountRepository).createDiscount(Mockito.any(Discount.class));

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> discountService.createNewDiscount(discount));
        assertNotNull(ex);
        assertEquals(3, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("name"));
        assertTrue(ex.getErrors().containsKey("description"));
        assertTrue(ex.getErrors().containsKey("percent"));
        assertFalse(ex.getErrors().containsKey("startDate"));

        Mockito.verify(discountRepository, Mockito.times(0)).createDiscount(Mockito.any(Discount.class));
    }

    @Test
    void createDiscountTest_WithEmptyFieldsAndNegativePercent() {
        Discount discount = new Discount(
                null,
                null,
                null,
                null,
                "",
                "",
                -100,
                null,
                false,
                null,
                null
        );

        Mockito.when(discountRepository.getNextDiscountId()).thenReturn(2L);
        Mockito.doNothing().when(discountRepository).createDiscount(Mockito.any(Discount.class));

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> discountService.createNewDiscount(discount));
        assertNotNull(ex);
        assertEquals(3, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("name"));
        assertTrue(ex.getErrors().containsKey("description"));
        assertTrue(ex.getErrors().containsKey("percent"));
        assertFalse(ex.getErrors().containsKey("startDate"));

        Mockito.verify(discountRepository, Mockito.times(0)).createDiscount(Mockito.any(Discount.class));
    }

    @Test
    void createDiscountTest_WithBlankFieldsAndGreaterThan100Percent() {
        Discount discount = new Discount(
                null,
                null,
                null,
                null,
                "  ",
                " ",
                101,
                null,
                false,
                null,
                null
        );

        Mockito.when(discountRepository.getNextDiscountId()).thenReturn(2L);
        Mockito.doNothing().when(discountRepository).createDiscount(Mockito.any(Discount.class));

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> discountService.createNewDiscount(discount));
        assertNotNull(ex);
        assertEquals(3, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("name"));
        assertTrue(ex.getErrors().containsKey("description"));
        assertTrue(ex.getErrors().containsKey("percent"));
        assertFalse(ex.getErrors().containsKey("startDate"));

        Mockito.verify(discountRepository, Mockito.times(0)).createDiscount(Mockito.any(Discount.class));
    }

    @Test
    void createDiscountTest_WithInvalidDates() {
        Discount discount = new Discount(
                null,
                null,
                null,
                null,
                "discount",
                "-10% if birthday",
                10,
                null,
                false,
                LocalDateTime.MAX,
                LocalDateTime.MIN
        );

        Mockito.when(discountRepository.getNextDiscountId()).thenReturn(2L);
        Mockito.doNothing().when(discountRepository).createDiscount(Mockito.any(Discount.class));

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> discountService.createNewDiscount(discount));
        assertNotNull(ex);
        assertEquals(1, ex.getErrors().size());
        assertFalse(ex.getErrors().containsKey("name"));
        assertFalse(ex.getErrors().containsKey("description"));
        assertFalse(ex.getErrors().containsKey("percent"));
        assertTrue(ex.getErrors().containsKey("startDate"));

        Mockito.verify(discountRepository, Mockito.times(0)).createDiscount(Mockito.any(Discount.class));
    }

    @Test
    void createDiscountTest_WithValidFields() {
        Discount discount = new Discount(
                null,
                null,
                null,
                null,
                "discount",
                "-10% if birthday",
                10,
                null,
                false,
                LocalDateTime.MIN,
                LocalDateTime.MAX
        );

        Mockito.when(discountRepository.getNextDiscountId()).thenReturn(2L);
        Mockito.doNothing().when(discountRepository).createDiscount(Mockito.any(Discount.class));

        discountService.createNewDiscount(discount);

        Mockito.verify(discountRepository, Mockito.times(1)).createDiscount(Mockito.any(Discount.class));
    }

    @Test
    void editDiscountTest_WithValidFields() {
        Discount discount = new Discount(
                null,
                2L,
                null,
                null,
                "discount",
                "-10% if birthday",
                10,
                null,
                true,
                LocalDateTime.MIN,
                LocalDateTime.MAX
        );
        Discount oldDiscount = new Discount(
                null,
                2L,
                null,
                null,
                "discount",
                "-10% if birthday",
                10,
                null,
                false,
                LocalDateTime.MIN,
                LocalDateTime.MAX
        );

        Mockito.when(discountRepository.getById(2L)).thenReturn(oldDiscount);
        Mockito.doNothing().when(discountRepository).deleteDiscount(2L);
        Mockito.doNothing().when(discountRepository).createDiscount(Mockito.any(Discount.class));

        discountService.editDiscount(discount);

        Mockito.verify(discountRepository, Mockito.times(1)).deleteDiscount(2L);
        Mockito.verify(discountRepository, Mockito.times(1)).createDiscount(Mockito.any(Discount.class));

    }


}