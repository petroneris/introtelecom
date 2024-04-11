package com.snezana.introtelecom.validation;

import com.snezana.introtelecom.entity.Customer;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.exception.IllegalItemFieldException;
import com.snezana.introtelecom.exception.ItemNotFoundException;
import com.snezana.introtelecom.exception.RestAPIErrorMessage;
import com.snezana.introtelecom.repository.CustomerRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import org.assertj.core.api.Condition;

@ExtendWith(MockitoExtension.class)
class CustomerValidationServiceTest {

    @Mock
    private CustomerRepo customerRepo;

    @InjectMocks
    private CustomerValidationService customerValidationService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testControlThePersonalNumberIsUnique_isUnique() {
        String personalNumber = "3998734833";
        when(customerRepo.findByPersonalNumberOpt(personalNumber)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> {
            customerValidationService.controlThePersonalNumberIsUnique(personalNumber);
        });
    }

    @Test
    void testControlThePersonalNumberIsUnique_isNotUnique() {
        String personalNumber = "3998734833";
        Customer customer = new Customer();
        customer.setPersonalNumber(personalNumber);
        String description = "This personal number already exists in database!";
        when(customerRepo.findByPersonalNumberOpt(personalNumber)).thenReturn(Optional.of(customer));
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            customerValidationService.controlThePersonalNumberIsUnique(personalNumber);
        });
        assertEquals(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testReturnTheCustomerWithThatPersonalNumberIfExists_exists() {
        String personalNumber = "3998734833";
        Customer customer = new Customer();
        customer.setPersonalNumber(personalNumber);
        when(customerRepo.findByPersonalNumberOpt(personalNumber)).thenReturn(Optional.of(customer));
        Customer newCustomer = customerValidationService.returnTheCustomerWithThatPersonalNumberIfExists(personalNumber);
        assertThat(newCustomer.getPersonalNumber()).isEqualTo(personalNumber);
    }

    @Test
    void testReturnTheCustomerWithThatPersonalNumberIfExists_doesNotExist() {
        String personalNumber = "3998734833";
        String description = "The customer with that personal number doesn't exist in database!";
        when(customerRepo.findByPersonalNumberOpt(personalNumber)).thenReturn(Optional.empty());
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            customerValidationService.returnTheCustomerWithThatPersonalNumberIfExists(personalNumber);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testReturnTheCustomerWithThatIdIfExists_exists() {
        Long id = 3L;
        Customer customer = new Customer();
        customer.setCustomerId(id);
        when(customerRepo.findByCustomerIdOpt(id)).thenReturn(Optional.of(customer));
        Customer newCustomer = customerValidationService.returnTheCustomerWithThatIdIfExists(id);
        assertThat(newCustomer.getCustomerId()).isEqualTo(id);
    }

    @Test
    void testReturnTheCustomerWithThatIdIfExists_doesNotExist() {
        Long id = 3L;
        String description = "The customer with that id doesn't exist in database!";
        when(customerRepo.findByCustomerIdOpt(id)).thenReturn(Optional.empty());
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            customerValidationService.returnTheCustomerWithThatIdIfExists(id);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testReturnTheCustomerWithThatEmailIfExists_exists() {
        String email = "sneza@bluephone.com";
        Customer customer = new Customer();
        customer.setEmail(email);
        when(customerRepo.findByEmailOpt(email)).thenReturn(Optional.of(customer));
        Customer newCustomer = customerValidationService.returnTheCustomerWithThatEmailIfExists(email);
        assertThat(newCustomer.getEmail()).isEqualTo(email);
    }

    @Test
    void testReturnTheCustomerWithThatEmailIfExists_doesNotExist() {
        String email = "sneza@bluephone.com";
        String description = "The customer with that email doesn't exist in database!";
        when(customerRepo.findByEmailOpt(email)).thenReturn(Optional.empty());
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            customerValidationService.returnTheCustomerWithThatEmailIfExists(email);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlTheEmailIsUnique_isUnique() {
        String email = "sneza@bluephone.com";
        when(customerRepo.findByEmailOpt(email)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> {
            customerValidationService.controlTheEmailIsUnique(email);
        });
    }

    @Test
    void testControlTheEmailIsUnique_isNotUnique() {
        String email = "sneza@bluephone.com";
        String description = "The customer with that email already exists in database!";
        Customer customer = new Customer();
        customer.setEmail(email);
        when(customerRepo.findByEmailOpt(email)).thenReturn(Optional.of(customer));
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            customerValidationService.controlTheEmailIsUnique(email);
        });
        assertEquals(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlTheOtherCustomerHasThisPersonalNumber_itHasNot() {
        String personalNumber = "3998734833";
        Long id = 3L;
        when(customerRepo.findByPersonalNumberOpt(personalNumber)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> {
            customerValidationService.controlTheOtherCustomerHasThisPersonalNumber(personalNumber, id);
        });
    }

    @Test
    void testControlTheOtherCustomerHasThisPersonalNumber_itHasNot2() {
        String personalNumber = "3998734833";
        Long id = 3L;
        Customer customer = new Customer();
        customer.setPersonalNumber(personalNumber);
        customer.setCustomerId(id);
        when(customerRepo.findByPersonalNumberOpt(personalNumber)).thenReturn(Optional.of(customer));
        assertDoesNotThrow(() -> {
            customerValidationService.controlTheOtherCustomerHasThisPersonalNumber(personalNumber, id);
        });
    }

    @Test
    void testControlTheOtherCustomerHasThisPersonalNumber_itHas() {
        String personalNumber = "3998734833";
        Long customerId = 3L;
        Long otherId = 4L;
        Customer customer = new Customer();
        customer.setPersonalNumber(personalNumber);
        customer.setCustomerId(customerId);
        String description = "Another customer with that personal number already exists in database!";
        when(customerRepo.findByPersonalNumberOpt(personalNumber)).thenReturn(Optional.of(customer));
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            customerValidationService.controlTheOtherCustomerHasThisPersonalNumber(personalNumber, otherId);
        });
        assertEquals(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlTheOtherCustomerHasThisEmail_itHasNot() {
        String email = "sneza@bluephone.com";
        Long id = 3L;
        when(customerRepo.findByEmailOpt(email)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> {
            customerValidationService.controlTheOtherCustomerHasThisEmail(email, id);
        });
    }

    @Test
    void testControlTheOtherCustomerHasThisEmail_itHasNot2() {
        String email = "sneza@bluephone.com";
        Long id = 3L;
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setCustomerId(id);
        when(customerRepo.findByEmailOpt(email)).thenReturn(Optional.of(customer));
        assertDoesNotThrow(() -> {
            customerValidationService.controlTheOtherCustomerHasThisEmail(email, id);
        });
    }

    @Test
    void testControlTheOtherCustomerHasThisEmail_itHas() {
        String email = "sneza@bluephone.com";
        Long customerId = 3L;
        Long otherId = 4L;
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setCustomerId(customerId);
        String description = "Another customer with that email already exists in database!";
        when(customerRepo.findByEmailOpt(email)).thenReturn(Optional.of(customer));
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            customerValidationService.controlTheOtherCustomerHasThisEmail(email, otherId);
        });
        assertEquals(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testCheckThePhoneBelongsToThisCustomer_itBelongs() {
        String phoneNumber1 = "0732283498";
        Phone phone1 = new Phone();
        phone1.setPhoneNumber(phoneNumber1);
        String phoneNumber2 = "0732283499";
        Phone phone2 = new Phone();
        phone2.setPhoneNumber(phoneNumber2);
        Set<Phone> phones = new HashSet<>();
        phones.add(phone1);
        phones.add(phone2);
        Customer customer = new Customer();
        customer.setPhones(phones);
        assertDoesNotThrow(() -> {
            customerValidationService.checkThePhoneBelongsToThisCustomer(customer, phone1);
        });
    }

    @Test
    void testCheckThePhoneBelongsToThisCustomer_doesNotBelong() {
        String phoneNumber1 = "0732283498";
        Phone phone1 = new Phone();
        phone1.setPhoneNumber(phoneNumber1);
        String phoneNumber2 = "0732283499";
        Phone phone2 = new Phone();
        phone2.setPhoneNumber(phoneNumber2);
        Set<Phone> phones = new HashSet<>();
        phones.add(phone1);
        phones.add(phone2);
        Customer customer = new Customer();
        customer.setPhones(phones);
        String phoneNumber3 = "0732283497";
        Phone phone3 = new Phone();
        phone3.setPhoneNumber(phoneNumber3);
        String description = "This phone doesn't belong to this customer!";
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            customerValidationService.checkThePhoneBelongsToThisCustomer(customer, phone3);
        });
        assertEquals(RestAPIErrorMessage.WRONG_ITEM, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlThatPhoneAlreadyBelongsToSomeCustomer_alreadyBelongs() {
        String phoneNumber = "0732283498";
        Phone phone = new Phone();
        phone.setPhoneNumber(phoneNumber);
        Set<Phone> phones = new HashSet<>();
        phones.add(phone);
        Customer customer = new Customer();
        customer.setPhones(phones);
        when(customerRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.of(customer));
        String description = "This phone number already belongs to another customer!";
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            customerValidationService.controlThatPhoneAlreadyBelongsToSomeCustomer(phoneNumber);
        });
        assertEquals(RestAPIErrorMessage.WRONG_ITEM, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());

    }

    @Test
    void testControlThatPhoneAlreadyBelongsToSomeCustomer_doesNotBelong() {
        String phoneNumber = "0732283498";
        when(customerRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> {
            customerValidationService.controlThatPhoneAlreadyBelongsToSomeCustomer(phoneNumber);
        });
    }

    @Test
    void testReturnTheCustomerWithThisPhoneIfExists_exists() {
        String phoneNumber = "0732283498";
        String text = "phoneNumber";
        Phone phone = new Phone();
        phone.setPhoneNumber(phoneNumber);
        String phoneNumber1 = "0732283499";
        Phone phone1 = new Phone();
        phone1.setPhoneNumber(phoneNumber1);
        Set<Phone> phones = new HashSet<>();
        phones.add(phone);
        phones.add(phone1);
        Customer customer = new Customer();
        customer.setPhones(phones);
        Condition<Phone> phone0732283498 = new Condition<>(
                p -> p.getPhoneNumber().equals("0732283498") , "thatPhone0732283498");
        when(customerRepo.findAll()).thenReturn(Collections.singletonList(customer));
        Customer newCustomer = customerValidationService.returnTheCustomerWithThisPhoneIfExists(phone, text);
        assertThat(newCustomer.getPhones().contains(phone)).isTrue();
        assertThat(newCustomer.getPhones()).haveExactly(1, phone0732283498);

    }

    @Test
    void testReturnTheCustomerWithThisPhoneIfExists_doesNotExists() {
        String text = "phoneNumber";
        Phone phone = new Phone();
        when(customerRepo.findAll()).thenReturn(Collections.emptyList());
        String description = "The customer with that " +text + " doesn't exist in database!";
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            customerValidationService.returnTheCustomerWithThisPhoneIfExists(phone, text);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }
}