package app.services;

import app.InvalidDataException;
import app.UserContext;
import app.entities.enums.OrderStatuses;
import app.entities.enums.Roles;
import app.entities.pojos.OrderPojo;
import app.generated.jooq.tables.pojos.Combo;
import app.generated.jooq.tables.pojos.Order;
import app.generated.jooq.tables.pojos.Product;
import app.repository.AccountRepository;
import app.repository.AddressRepository;
import app.repository.DiscountRepository;
import app.repository.OrderRepository;
import app.repository.ProductRepository;
import app.repository.ComboRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.tools.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final DiscountRepository discountRepository;
    private final AddressRepository addressRepository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final ComboRepository comboRepository;
    private final UserContext userContext;

    public List<OrderPojo> getList() {
        return repository.getList();
    }

    public OrderPojo getById(Long orderId) {
        OrderPojo orderPojo = repository.getById(orderId);

        validateAccountMatch(orderPojo.getAccountId());

        orderPojo.setAccount(accountRepository.getActualAccountById(orderPojo.getAccountId()));

        List<Long> productIds = repository.getProductIdsByOrderId(orderId);
        List<Long> comboIds = repository.getComboIdsByOrderId(orderId);
        List<Product> productList = productRepository.getListByProductIds(productIds);
        List<Combo> comboList = comboRepository.getListByComboIds(comboIds);
        orderPojo.setProductList(productList);
        orderPojo.setComboList(comboList);

        if (orderPojo.getAddressId() != null) {
            orderPojo.setAddress(addressRepository.getById(orderPojo.getAddressId()));
        }
        if (orderPojo.getDiscountId() != null) {
            orderPojo.setDiscount(discountRepository.getById(orderPojo.getDiscountId()));
        }

        return orderPojo;
    }

    public void createOrder(Order order) {
        validateCreateOrder(order);
        order.setStatusId(OrderStatuses.NEW.getId().longValue());
        order.setOrderId(repository.getNextOrderId());
        order.setAccountId(userContext.getAccountId());
        repository.createOrder(order);
    }

    public void deleteOrder(Long orderId) {
        repository.deleteOrder(orderId);
    }

    public void editOrder(Order order) {
        validateCreateOrder(order);
        validateAccountMatch(order.getAccountId());

        OrderPojo oldOrder = repository.getById(order.getOrderId());
        deleteOrder(oldOrder.getOrderId());
        repository.createOrder(order);
    }

    private void validateCreateOrder(Order order) {
        Map<String, String> errors = new HashMap<>();
        if (StringUtils.isBlank(order.getPhone())) {
            //TODO: check phone with ?regex?
            errors.put("phone", "Phone can't be null");
        }
        if (StringUtils.isBlank(order.getName())) {
            errors.put("name", "Name can't be null");
        }
        if (order.getAddonsCount() == null) {
            errors.put("addonsCount", "Addons count can't be null");
        }
        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }

    public void changeStatus(Long orderId, Integer statusId) {
        OrderPojo orderPojo = repository.getById(orderId);

        validateAccountMatch(orderPojo.getAccountId());

        orderPojo.setStatusId(statusId.longValue());
        deleteOrder(orderPojo.getOrderId());
        repository.createOrder(orderPojo);
    }

    private void validateAccountMatch(Long accountId) {
        if (Roles.ROLE_USER.getId().equals(userContext.getRoleId().intValue()) && !accountId.equals(userContext.getAccountId())) {
            throw new InvalidDataException("Order isn't accessible for this account");
        }
    }
}
