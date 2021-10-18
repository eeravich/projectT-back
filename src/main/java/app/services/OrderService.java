package app.services;

import app.InvalidDataException;
import app.UserContext;
import app.entities.pojos.OrderPojo;
import app.generated.jooq.tables.pojos.Order;
import app.repository.AccountRepository;
import app.repository.AddressRepository;
import app.repository.DiscountRepository;
import app.repository.OrderRepository;
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
    private final UserContext userContext;

    public List<OrderPojo> getList() {
        return repository.getList();
    }

    public OrderPojo getById(Long orderId) {
        OrderPojo orderPojo = repository.getById(orderId);
        orderPojo.setAccount(accountRepository.getById(orderPojo.getAccountId()));
        orderPojo.setAddress(addressRepository.getById(orderPojo.getAddressId()));

        if (orderPojo.getDiscountId() != null) {
            orderPojo.setDiscount(discountRepository.getById(orderPojo.getDiscountId()));
        }
        return orderPojo;
    }

    public void createOrder(Order order) {
        validateCreateOrder(order);
        order.setOrderId(repository.getNextOrderId());
        repository.createOrder(order);
    }

    public void deleteOrder(Long orderId) {
        repository.deleteOrder(orderId);
    }

    public void editOrder(Order order) {
        validateCreateOrder(order);
        OrderPojo oldOrder = repository.getById(order.getOrderId());
        deleteOrder(oldOrder.getOrderId());
        repository.createOrder(order);
    }

    private void validateCreateOrder(Order order) {
        Map<String, String> errors = new HashMap<>();
        if (order.getAccountId() == null) {
            errors.put("account", "Account can't be null");
        } else if (!userContext.getAccountId().equals(order.getAccountId())) {
            errors.put("account", "Accounts mismatch");
        }
        if (StringUtils.isBlank(order.getPhone())) {
            errors.put("phone", "Phone can't be null");
        }
        if (StringUtils.isBlank(order.getName())) {
            errors.put("name", "Name can't be null");
        }
        if (order.getAddonsCount() == null) {
            errors.put("addonsCount", "Addons count can't be null");
        }

        throw new InvalidDataException(errors);
    }
}
