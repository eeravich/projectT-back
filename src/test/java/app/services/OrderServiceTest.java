package app.services;

import app.InvalidDataException;
import app.UserContext;
import app.entities.enums.Roles;
import app.entities.pojos.OrderPojo;
import app.generated.jooq.tables.pojos.Account;
import app.generated.jooq.tables.pojos.Address;
import app.generated.jooq.tables.pojos.Discount;
import app.generated.jooq.tables.pojos.Order;
import app.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    DiscountRepository discountRepository;

    @MockBean
    AddressRepository addressRepository;

    @MockBean
    AccountRepository accountRepository;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    ComboRepository comboRepository;

    @MockBean
    UserContext userContext;

    @Autowired
    OrderService orderService;

    @Test
    void getByIdTest_UserGetItsOrder() {
        Long userAccountId = 5L;
        Long orderAccountId = 5L;
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setAccountId(orderAccountId);
        orderPojo.setAddressId(1L);
        orderPojo.setDiscountId(2L);
        Mockito.when(orderRepository.getById(Mockito.anyLong())).thenReturn(orderPojo);
        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(userContext.getAccountId()).thenReturn(userAccountId);
        Mockito.when(accountRepository.getActualAccountById(orderAccountId)).thenReturn(new Account());
        Mockito.when(addressRepository.getById(1L)).thenReturn(new Address());
        Mockito.when(discountRepository.getById(2L)).thenReturn(new Discount());
        Mockito.when(orderRepository.getProductIdsByOrderId(Mockito.anyLong())).thenReturn(new ArrayList<>());
        Mockito.when(orderRepository.getComboIdsByOrderId(Mockito.anyLong())).thenReturn(new ArrayList<>());
        Mockito.when(productRepository.getListByProductIds(Mockito.anyList())).thenReturn(new ArrayList<>());
        Mockito.when(comboRepository.getListByComboIds(Mockito.anyList())).thenReturn(new ArrayList<>());

        OrderPojo orderById = orderService.getById(10L);

        assertNotNull(orderById.getAccount());
        assertNotNull(orderById.getAddress());
        assertNotNull(orderById.getDiscount());
        assertNotNull(orderById.getProductList());
        assertNotNull(orderById.getComboList());
    }

    @Test
    void getByIdTest_UserGetForeignOrder() {
        Long userAccountId = 5L;
        Long orderAccountId = 3L;
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setAccountId(orderAccountId);
        orderPojo.setAddressId(1L);
        orderPojo.setDiscountId(2L);
        Mockito.when(orderRepository.getById(Mockito.anyLong())).thenReturn(orderPojo);
        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(userContext.getAccountId()).thenReturn(userAccountId);
        Mockito.when(accountRepository.getActualAccountById(orderAccountId)).thenReturn(new Account());
        Mockito.when(addressRepository.getById(1L)).thenReturn(new Address());
        Mockito.when(discountRepository.getById(2L)).thenReturn(new Discount());
        Mockito.when(orderRepository.getProductIdsByOrderId(Mockito.anyLong())).thenReturn(new ArrayList<>());
        Mockito.when(orderRepository.getComboIdsByOrderId(Mockito.anyLong())).thenReturn(new ArrayList<>());
        Mockito.when(productRepository.getListByProductIds(Mockito.anyList())).thenReturn(new ArrayList<>());
        Mockito.when(comboRepository.getListByComboIds(Mockito.anyList())).thenReturn(new ArrayList<>());

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> orderService.getById(10L));
        assertNotNull(ex);
    }

    @Test
    void getByIdTest_ManagerOrAdminGetForeignOrder() {
        Long userAccountId = 5L;
        Long orderAccountId = 3L;
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setAccountId(orderAccountId);
        orderPojo.setAddressId(1L);
        orderPojo.setDiscountId(2L);
        Mockito.when(orderRepository.getById(Mockito.anyLong())).thenReturn(orderPojo);
        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_MANAGER.getId().longValue());
        Mockito.when(userContext.getAccountId()).thenReturn(userAccountId);
        Mockito.when(accountRepository.getActualAccountById(orderAccountId)).thenReturn(new Account());
        Mockito.when(addressRepository.getById(1L)).thenReturn(new Address());
        Mockito.when(discountRepository.getById(2L)).thenReturn(new Discount());
        Mockito.when(orderRepository.getProductIdsByOrderId(Mockito.anyLong())).thenReturn(new ArrayList<>());
        Mockito.when(orderRepository.getComboIdsByOrderId(Mockito.anyLong())).thenReturn(new ArrayList<>());
        Mockito.when(productRepository.getListByProductIds(Mockito.anyList())).thenReturn(new ArrayList<>());
        Mockito.when(comboRepository.getListByComboIds(Mockito.anyList())).thenReturn(new ArrayList<>());

        OrderPojo orderById = orderService.getById(10L);

        assertNotNull(orderById.getAccount());
        assertNotNull(orderById.getAddress());
        assertNotNull(orderById.getDiscount());
        assertNotNull(orderById.getProductList());
        assertNotNull(orderById.getComboList());
    }

    @Test
    void createOrderTest_WithNullFields() {
        Order order = new Order();

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> orderService.createOrder(order));

        assertNotNull(ex);
        assertEquals(3, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("phone"));
        assertTrue(ex.getErrors().containsKey("name"));
        assertTrue(ex.getErrors().containsKey("addonsCount"));
    }

    @Test
    void createOrderTest_WithEmptyFields() {
        Order order = new Order(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                null,
                "",
                "",
                false,
                null,
                null,
                10,
                null,
                null,
                null,
                null,
                null
        );

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> orderService.createOrder(order));

        assertNotNull(ex);
        assertEquals(2, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("phone"));
        assertTrue(ex.getErrors().containsKey("name"));
        assertFalse(ex.getErrors().containsKey("addonsCount"));
    }

    @Test
    void createOrderTest_WithBlankFields() {
        Order order = new Order(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                null,
                "  ",
                "  ",
                false,
                null,
                null,
                10,
                null,
                null,
                null,
                null,
                null
        );

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> orderService.createOrder(order));

        assertNotNull(ex);
        assertEquals(2, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("phone"));
        assertTrue(ex.getErrors().containsKey("name"));
        assertFalse(ex.getErrors().containsKey("addonsCount"));
    }

    @Test
    void createOrderTest_WithInvalidPhone() {
        Order order1 = new Order(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                null,
                "+7903912030",
                "fwefwefwe",
                false,
                null,
                null,
                10,
                null,
                null,
                null,
                null,
                null
        );

        Order order2 = new Order(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                null,
                "+7rfewrwer",
                "rewrfwewe",
                false,
                null,
                null,
                10,
                null,
                null,
                null,
                null,
                null
        );

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> orderService.createOrder(order1));

        assertNotNull(ex);
        assertEquals(1, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("phone"));
        assertFalse(ex.getErrors().containsKey("name"));
        assertFalse(ex.getErrors().containsKey("addonsCount"));

        InvalidDataException ex2 = assertThrows(InvalidDataException.class, () -> orderService.createOrder(order2));

        assertNotNull(ex2);
        assertEquals(1, ex2.getErrors().size());
        assertTrue(ex2.getErrors().containsKey("phone"));
        assertFalse(ex2.getErrors().containsKey("name"));
        assertFalse(ex2.getErrors().containsKey("addonsCount"));
    }

    @Test
    void createOrderTest_WithValidFields() {
        Order order = new Order(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                1L,
                "+79039120303",
                "Andrey Golubev",
                false,
                null,
                null,
                10,
                null,
                null,
                null,
                1L,
                null
        );

        Mockito.when(orderRepository.getNextOrderId()).thenReturn(2L);
        Mockito.when(userContext.getAccountId()).thenReturn(1L);
        Mockito.doNothing().when(orderRepository).createOrder(Mockito.any(Order.class));

        orderService.createOrder(order);

        Mockito.verify(orderRepository, Mockito.times(1)).createOrder(order);
    }

    @Test
    void deleteOrderTest() {
        Mockito.doNothing().when(orderRepository).deleteOrder(Mockito.anyLong());

        orderService.deleteOrder(1L);

        Mockito.verify(orderRepository, Mockito.times(1)).deleteOrder(1L);
    }

    @Test
    void editOrderTest_UserEditItsOrder() {
        Order newOrder = new Order(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                5L,
                "+79039120312",
                "Andrey Golubev",
                false,
                null,
                null,
                10,
                null,
                null,
                null,
                1L,
                null
        );
        OrderPojo oldOrder = new OrderPojo(newOrder);

        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(userContext.getAccountId()).thenReturn(5L);
        Mockito.when(orderRepository.getById(1L)).thenReturn(oldOrder);
        Mockito.doNothing().when(orderRepository).deleteOrder(1L);
        Mockito.doNothing().when(orderRepository).createOrder(newOrder);

        orderService.editOrder(newOrder);

        Mockito.verify(orderRepository, Mockito.times(1)).deleteOrder(1L);
        Mockito.verify(orderRepository, Mockito.times(1)).createOrder(newOrder);
    }

    @Test
    void editOrderTest_UserEditForeignOrder() {
        Order newOrder = new Order(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                3L,
                "+90391203123",
                "Andrey Golubev",
                false,
                null,
                null,
                10,
                null,
                null,
                null,
                1L,
                null
        );
        OrderPojo oldOrder = new OrderPojo(newOrder);

        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(userContext.getAccountId()).thenReturn(5L);
        Mockito.when(orderRepository.getById(1L)).thenReturn(oldOrder);
        Mockito.doNothing().when(orderRepository).deleteOrder(1L);
        Mockito.doNothing().when(orderRepository).createOrder(newOrder);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> orderService.editOrder(newOrder));
        assertNotNull(ex);

        Mockito.verify(orderRepository, Mockito.times(0)).deleteOrder(1L);
        Mockito.verify(orderRepository, Mockito.times(0)).createOrder(newOrder);
    }

    @Test
    void editOrderTest_ManagerOrAdminEditForeignOrder() {
        Order newOrder = new Order(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                3L,
                "+79039120312",
                "Andrey Golubev",
                false,
                null,
                null,
                10,
                null,
                null,
                null,
                1L,
                null
        );
        OrderPojo oldOrder = new OrderPojo(newOrder);

        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_MANAGER.getId().longValue());
        Mockito.when(userContext.getAccountId()).thenReturn(5L);
        Mockito.when(orderRepository.getById(1L)).thenReturn(oldOrder);
        Mockito.doNothing().when(orderRepository).deleteOrder(1L);
        Mockito.doNothing().when(orderRepository).createOrder(newOrder);

        orderService.editOrder(newOrder);

        Mockito.verify(orderRepository, Mockito.times(1)).deleteOrder(1L);
        Mockito.verify(orderRepository, Mockito.times(1)).createOrder(newOrder);
    }
}