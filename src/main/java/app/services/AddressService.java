package app.services;

import app.InvalidDataException;
import app.UserContext;
import app.entities.enums.Roles;
import app.generated.jooq.tables.pojos.Address;
import app.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.tools.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository repository;
    private final UserContext userContext;

    public List<Address> getList() {
        return repository.getList();
    }

    public List<Address> getListByUser() {
        return repository.getListByUser(userContext.getAccountId());
    }

    public Address getById(Long addressId) {

        Address address = repository.getById(addressId);

        if (Roles.ROLE_USER.getId().equals(userContext.getRoleId().intValue())) {
            if (!address.getAccountId().equals(userContext.getAccountId())) {
                throw new InvalidDataException("Access denied");
            }
        }
        return address;
    }

    public void createAddress(Address address) {
        if (Roles.ROLE_USER.getId().equals(userContext.getRoleId().intValue())) {
            address.setAccountId(userContext.getAccountId());
        }

        validateCreateAddress(address);

        address.setAddressId(repository.getNextAddressId());
        repository.createAddress(address);
    }

    public void deleteAddress(Long addressId) {
        if (Roles.ROLE_USER.getId().equals(userContext.getRoleId().intValue())) {
            Address addressToDelete = repository.getById(addressId);
            if (!addressToDelete.getAccountId().equals(userContext.getAccountId())) {
                throw new InvalidDataException("Address in not accessible by this account");
            }
        }
        repository.deleteAddress(addressId);
    }

    public void editAddress(Address address) {
        boolean isUser = Roles.ROLE_USER.getId().equals(userContext.getRoleId().intValue());
        if (isUser) {
            address.setAccountId(userContext.getAccountId());
        }

        validateCreateAddress(address);
        Address oldAddress = repository.getById(address.getAddressId());
        if (isUser && !oldAddress.getAccountId().equals(address.getAccountId())) {
            throw new InvalidDataException("Address in not accessible by this account");
        }
        deleteAddress(oldAddress.getAddressId());
        repository.createAddress(address);
    }

    //TODO: доработать адреса и их валидацию (api yandex map)
    private void validateCreateAddress(Address address) {
        Map<String, String> errors = new HashMap<>();
        if (StringUtils.isBlank(address.getStreet())) {
            errors.put("street", "Street can't be null");
        }
        if (StringUtils.isBlank(address.getHouse())) {
            errors.put("house", "House can't be null");
        }
        if (StringUtils.isBlank(address.getApartment())) {
            errors.put("apartment", "Apartment can't be null");
        }
        if (address.getAccountId() == null) {
            errors.put("account", "Account can't be null");
        }

        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }
}
