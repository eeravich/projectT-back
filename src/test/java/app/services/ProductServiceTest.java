package app.services;

import app.InvalidDataException;
import app.entities.pojos.ProductPojo;
import app.generated.jooq.tables.pojos.Product;
import app.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @MockBean
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Test
    void getListTest() {
        List<ProductPojo> list = new ArrayList<>();
        list.add(new ProductPojo());
        list.add(new ProductPojo());
        list.add(new ProductPojo());

        Mockito.when(productRepository.getList()).thenReturn(list);
        Mockito.when(productRepository.getProductComponents(Mockito.anyLong())).thenReturn(new ArrayList<>());
        Mockito.when(productRepository.getDiscountsByProductId(Mockito.anyLong())).thenReturn(new ArrayList<>());

        List<ProductPojo> productList = productService.getList();

        assertEquals(list.size(), productList.size());
        for (ProductPojo product : productList) {
            assertNotNull(product.getDiscounts());
            assertNotNull(product.getComponents());
        }
    }

    @Test
    void getByIdTest() {
        ProductPojo productPojo = new ProductPojo();

        Mockito.when(productRepository.getById(Mockito.anyLong())).thenReturn(productPojo);
        Mockito.when(productRepository.getProductComponents(Mockito.anyLong())).thenReturn(new ArrayList<>());
        Mockito.when(productRepository.getDiscountsByProductId(Mockito.anyLong())).thenReturn(new ArrayList<>());

        ProductPojo productById = productService.getById(1L);

        assertEquals(productById, productPojo);
        assertNotNull(productById.getComponents());
        assertNotNull(productById.getDiscounts());
    }

    @Test
    void createProductTest_WithNullFields() {
        Product product = new Product();

        Mockito.when(productRepository.getNextProductId()).thenReturn(2L);
        Mockito.doNothing().when(productRepository).createProduct(product);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> productService.createProduct(product));
        assertNotNull(ex);
        assertEquals(5, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("name"));
        assertTrue(ex.getErrors().containsKey("price"));
        assertTrue(ex.getErrors().containsKey("portion"));
        assertTrue(ex.getErrors().containsKey("weight"));
        assertTrue(ex.getErrors().containsKey("productType"));

        Mockito.verify(productRepository, Mockito.times(0)).createProduct(product);
    }

    @Test
    void createProductTest_WithEmptyTextFieldAndNegativePricePortionWeight() {
        Product product = new Product(
                null,
                null,
                null,
                null,
                "",
                "",
                BigDecimal.ONE.negate(),
                -1,
                -2,
                1L,
                null
        );

        Mockito.when(productRepository.getNextProductId()).thenReturn(2L);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> productService.createProduct(product));
        assertNotNull(ex);
        assertEquals(4, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("name"));
        assertTrue(ex.getErrors().containsKey("price"));
        assertTrue(ex.getErrors().containsKey("portion"));
        assertTrue(ex.getErrors().containsKey("weight"));
        assertFalse(ex.getErrors().containsKey("productType"));
    }

    @Test
    void createProductTest_WithBlankTextFieldAndZeroPricePortionWeight() {
        Product product = new Product(
                null,
                null,
                null,
                null,
                "   ",
                "   ",
                BigDecimal.ZERO,
                0,
                0,
                1L,
                null
        );

        Mockito.when(productRepository.getNextProductId()).thenReturn(2L);
        Mockito.doNothing().when(productRepository).createProduct(product);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> productService.createProduct(product));
        assertNotNull(ex);
        assertEquals(4, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("name"));
        assertTrue(ex.getErrors().containsKey("price"));
        assertTrue(ex.getErrors().containsKey("portion"));
        assertTrue(ex.getErrors().containsKey("weight"));
        assertFalse(ex.getErrors().containsKey("productType"));

        Mockito.verify(productRepository, Mockito.times(0)).createProduct(product);
    }

    @Test
    void createProductTest_WithValidFields() {
        Product product = new Product(
                null,
                null,
                null,
                null,
                "chebupel",
                "rolling",
                BigDecimal.TEN,
                10,
                1000,
                1L,
                null
        );

        Mockito.when(productRepository.getNextProductId()).thenReturn(2L);
        Mockito.doNothing().when(productRepository).createProduct(product);

        productService.createProduct(product);

        Mockito.verify(productRepository, Mockito.times(1)).createProduct(product);
    }

    @Test
    void deleteProductTest() {
        Mockito.doNothing().when(productRepository).deleteProduct(Mockito.anyLong());

        productService.deleteProduct(1L);

        Mockito.verify(productRepository, Mockito.times(1)).deleteProduct(1L);
    }

    @Test
    void editProductTest_WithValidFields() {
        Product product = new Product(
                null,
                2L,
                null,
                null,
                "chebupel",
                "rolling",
                BigDecimal.TEN,
                10,
                1000,
                1L,
                null
        );
        ProductPojo oldProduct = new ProductPojo(product);

        Mockito.when(productRepository.getById(2L)).thenReturn(oldProduct);
        Mockito.doNothing().when(productRepository).deleteProduct(Mockito.anyLong());
        Mockito.doNothing().when(productRepository).createProduct(product);

        productService.editProduct(product);

        Mockito.verify(productRepository, Mockito.times(1)).deleteProduct(2L);
        Mockito.verify(productRepository, Mockito.times(1)).createProduct(product);
    }

    @Test
    void addComponentsTest() {
        List<Long> componentsIds = Arrays.asList(1L, 2L, 3L);

        Mockito.doNothing().when(productRepository).addComponent(Mockito.any());

        productService.addComponents(2L, componentsIds);

        Mockito.verify(productRepository, Mockito.times(componentsIds.size())).addComponent(Mockito.any());
    }

    @Test
    void deleteComponentsTest() {
        List<Long> componentsIds = Arrays.asList(1L, 2L, 3L);

        Mockito.doNothing().when(productRepository).deleteComponent(Mockito.anyLong(), Mockito.anyLong());

        productService.deleteComponents(2L, componentsIds);

        Mockito.verify(productRepository, Mockito.times(componentsIds.size())).deleteComponent(Mockito.anyLong(), Mockito.anyLong());
    }
}