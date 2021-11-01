package app.services;

import app.InvalidDataException;
import app.UserContext;
import app.entities.enums.Roles;
import app.generated.jooq.tables.pojos.Address;
import app.repository.AddressRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddressServiceTest {

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private UserContext userContext;

    @Autowired
    private AddressService addressService;

    @Test
    void getByIdTest_UserGetItsAddress() {
        Address address1 = new Address(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                "street",
                "house",
                "apart",
                null,
                1,
                null,
                1L
        );

        Mockito.when(addressRepository.getById(1L)).thenReturn(address1);
        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(userContext.getAccountId()).thenReturn(1L);

        assertEquals(address1, addressService.getById(1L));
    }

    @Test
    void getByIdTest_UserGetForeignAddress() {
        Address address1 = new Address(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                "street",
                "house",
                "apart",
                null,
                1,
                null,
                2L
        );

        Mockito.when(addressRepository.getById(1L)).thenReturn(address1);
        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(userContext.getAccountId()).thenReturn(1L);

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> addressService.getById(1L));
        assertNotNull(exception);
    }


    @Test
    void getByIdTest_MangerOrAdminGetAddress() {
        Address address1 = new Address(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                "street",
                "house",
                "apart",
                null,
                1,
                null,
                1L
        );

        Address address2 = new Address(
                2L,
                2L,
                LocalDateTime.now(),
                null,
                "street",
                "house",
                "apart",
                null,
                1,
                null,
                2L
        );

        Mockito.when(addressRepository.getById(1L)).thenReturn(address1);
        Mockito.when(addressRepository.getById(2L)).thenReturn(address2);
        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_MANAGER.getId().longValue(), Roles.ROLE_ADMIN.getId().longValue());
        Mockito.when(userContext.getAccountId()).thenReturn(5L, 6L);

        assertEquals(address1, addressService.getById(1L));
        assertEquals(address2, addressService.getById(2L));
    }

    @Test
    void createAddressTest_WithNullFieldsByManagerOrAdmin() {
        Address address1 = new Address();
        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_MANAGER.getId().longValue());

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> addressService.createAddress(address1));
        assertNotNull(ex);
        assertTrue(ex.getErrors().containsKey("street"));
        assertTrue(ex.getErrors().containsKey("house"));
        assertTrue(ex.getErrors().containsKey("apartment"));
        assertTrue(ex.getErrors().containsKey("account"));
        assertEquals(4, ex.getErrors().size());
    }

    @Test
    void createAddressTest_WithNullFieldsByUser() {
        Address address1 = new Address();
        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(userContext.getAccountId()).thenReturn(1L);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> addressService.createAddress(address1));
        assertNotNull(ex);
        assertTrue(ex.getErrors().containsKey("street"));
        assertTrue(ex.getErrors().containsKey("house"));
        assertTrue(ex.getErrors().containsKey("apartment"));
        assertFalse(ex.getErrors().containsKey("account"));
        assertEquals(3, ex.getErrors().size());
    }

    @Test
    void createAddressTest_WithEmptyFieldsByManagerOrAdmin() {
        Address address1 = new Address(
                1L,
                1L,
                null,
                null,
                "",
                "",
                "",
                null,
                null,
                null,
                1L
        );
        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_MANAGER.getId().longValue());

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> addressService.createAddress(address1));
        assertNotNull(ex);
        assertTrue(ex.getErrors().containsKey("street"));
        assertTrue(ex.getErrors().containsKey("house"));
        assertTrue(ex.getErrors().containsKey("apartment"));
        assertFalse(ex.getErrors().containsKey("account"));
        assertEquals(3, ex.getErrors().size());
    }

    @Test
    void createAddressTest_WithEmptyFieldsByUser() {
        Address address1 = new Address(
                1L,
                1L,
                null,
                null,
                "",
                "",
                "",
                null,
                null,
                null,
                1L
        );
        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(userContext.getAccountId()).thenReturn(1L);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> addressService.createAddress(address1));
        assertNotNull(ex);
        assertTrue(ex.getErrors().containsKey("street"));
        assertTrue(ex.getErrors().containsKey("house"));
        assertTrue(ex.getErrors().containsKey("apartment"));
        assertFalse(ex.getErrors().containsKey("account"));
        assertEquals(3, ex.getErrors().size());
    }

    @Test
    void createAddressTest_WithBlankFieldsByManagerOrAdmin() {
        Address address1 = new Address(
                1L,
                1L,
                null,
                null,
                " ",
                " ",
                "  ",
                null,
                null,
                null,
                1L
        );
        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_MANAGER.getId().longValue());

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> addressService.createAddress(address1));
        assertNotNull(ex);
        assertTrue(ex.getErrors().containsKey("street"));
        assertTrue(ex.getErrors().containsKey("house"));
        assertTrue(ex.getErrors().containsKey("apartment"));
        assertFalse(ex.getErrors().containsKey("account"));
        assertEquals(3, ex.getErrors().size());
    }

    @Test
    void createAddressTest_WithBlankFieldsByUser() {
        Address address1 = new Address(
                1L,
                1L,
                null,
                null,
                " ",
                " ",
                "  ",
                null,
                null,
                null,
                1L
        );
        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(userContext.getAccountId()).thenReturn(1L);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> addressService.createAddress(address1));
        assertNotNull(ex);
        assertTrue(ex.getErrors().containsKey("street"));
        assertTrue(ex.getErrors().containsKey("house"));
        assertTrue(ex.getErrors().containsKey("apartment"));
        assertFalse(ex.getErrors().containsKey("account"));
        assertEquals(3, ex.getErrors().size());
    }

    @Test
    void createAddressTest_ValidAddress() {
        Address address1 = new Address(
                null,
                null,
                null,
                null,
                "Baker street",
                "102",
                "221B",
                null,
                null,
                null,
                null
        );
        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(userContext.getAccountId()).thenReturn(1L);
        Mockito.when(addressRepository.getNextAddressId()).thenReturn(1L);
        Mockito.doNothing().when(addressRepository).createAddress(Mockito.any(Address.class));

        addressService.createAddress(address1);
        Mockito.verify(addressRepository, Mockito.times(1)).createAddress(address1);
    }

    @Test
    void deleteAddressTest_UserDeleteItsAddress() {
        Address address = new Address(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                "Baker street",
                "102",
                "221B",
                null,
                null,
                null,
                5L
        );

        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(addressRepository.getById(1L)).thenReturn(address);
        Mockito.when(userContext.getAccountId()).thenReturn(5L);
        Mockito.doNothing().when(addressRepository).deleteAddress(1L);

        addressService.deleteAddress(1L);

        Mockito.verify(addressRepository, Mockito.times(1)).deleteAddress(1L);
    }

    @Test
    void deleteAddressTest_UserDeleteForeignAddress() {
        Address address = new Address(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                "Baker street",
                "102",
                "221B",
                null,
                null,
                null,
                5L
        );

        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(addressRepository.getById(1L)).thenReturn(address);
        Mockito.when(userContext.getAccountId()).thenReturn(2L);
        Mockito.doNothing().when(addressRepository).deleteAddress(1L);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> addressService.deleteAddress(1L));
        assertNotNull(ex);

        Mockito.verify(addressRepository, Mockito.times(0)).deleteAddress(1L);
    }

    @Test
    void editAddressTest_UserEditForeignAddress() {
        Address address = new Address(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                "Baker street",
                "102",
                "221B",
                null,
                null,
                null,
                5L
        );
        Address oldAddress = new Address(address);

        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(addressRepository.getById(1L)).thenReturn(oldAddress);
        Mockito.when(userContext.getAccountId()).thenReturn(2L);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> addressService.editAddress(address));
        assertNotNull(ex);
        Mockito.verify(addressRepository, Mockito.times(0)).deleteAddress(1L);
        Mockito.verify(addressRepository, Mockito.times(0)).createAddress(address);
    }

    @Test
    void editAddressTest_UserEditItsAddress() {
        Address address = new Address(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                "Baker street",
                "102",
                "221B",
                null,
                null,
                null,
                5L
        );
        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_USER.getId().longValue());
        Mockito.when(addressRepository.getById(1L)).thenReturn(address);
        Mockito.when(userContext.getAccountId()).thenReturn(5L);
        Mockito.doNothing().when(addressRepository).deleteAddress(Mockito.anyLong());
        Mockito.doNothing().when(addressRepository).createAddress(address);

        addressService.editAddress(address);

        Mockito.verify(addressRepository, Mockito.times(1)).deleteAddress(1L);
        Mockito.verify(addressRepository, Mockito.times(1)).createAddress(address);
    }

    @Test
    void editAddressTest_ManagerOrAdminEditAddress() {
        Address address = new Address(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                "Baker street",
                "102",
                "221B",
                null,
                null,
                null,
                5L
        );
        Mockito.when(userContext.getRoleId()).thenReturn(Roles.ROLE_MANAGER.getId().longValue());
        Mockito.when(addressRepository.getById(1L)).thenReturn(address);
        Mockito.when(userContext.getAccountId()).thenReturn(2L);
        Mockito.doNothing().when(addressRepository).deleteAddress(Mockito.anyLong());
        Mockito.doNothing().when(addressRepository).createAddress(address);

        addressService.editAddress(address);

        Mockito.verify(addressRepository, Mockito.times(1)).deleteAddress(1L);
        Mockito.verify(addressRepository, Mockito.times(1)).createAddress(address);
    }
}