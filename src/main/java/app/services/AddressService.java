package app.services;

import app.InvalidDataException;
import app.UserContext;
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

    public Address getById(Long addressId) {
        return repository.getById(addressId);
    }

    public void createAddress(Address address) {
        validateCreateAddress(address);
        address.setAddressId(repository.getNextAddressId());
        repository.createAddress(address);
    }

    public void deleteAddress(Long addressId) {
        repository.deleteAddress(addressId);
    }

    public void editAddress(Address address) {
        validateCreateAddress(address);
        Address oldAddress = repository.getById(address.getAddressId());
        deleteAddress(oldAddress.getAddressId());
        repository.createAddress(address);
    }

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
        } else if (!userContext.getAccountId().equals(address.getAccountId())) {
            errors.put("account", "Account mismatch");
        }

        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }
}
