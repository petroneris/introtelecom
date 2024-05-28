package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CustomerRepoIntegrationTest {

    @Autowired
    private CustomerRepo customerRepo;

    @Test
    void findCustomerOpt_byCustomerId_whenIsNotEmpty(){
        Long id = 1L;
        String firstName = "Lana";
        String lastName = "Jovanović";
        String email = "lana@greenphone.com";
        String personalNumber = "3277645392";
        Optional<Customer> found = customerRepo.findByCustomerIdOpt(id);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(Customer.class)
                .hasValueSatisfying(customer-> {
                    assertThat(customer.getCustomerId()).isEqualTo(1L);
                    assertThat(customer.getFirstName()).isEqualTo(firstName);
                    assertThat(customer.getLastName()).isEqualTo(lastName);
                    assertThat(customer.getEmail()).isEqualTo(email);
                    assertThat(customer.getPersonalNumber()).isEqualTo(personalNumber);
                });
    }

    @Test
    void findCustomerOpt_byCustomerId_whenIsEmpty(){
        Long id = 200L;
        Optional<Customer> found = customerRepo.findByCustomerIdOpt(id);
        assertThat(found).isEmpty();
    }

    @Test
    void findCustomerOpt_byPhoneNumber_whenIsNotEmpty(){
        String firstName = "Lana";
        String lastName = "Jovanović";
        String email = "lana@greenphone.com";
        String personalNumber = "3277645392";
        String phoneNumber = "0720123763";
        Optional<Customer> found = customerRepo.findByPhoneNumberOpt(phoneNumber);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(Customer.class)
                .hasValueSatisfying(customer-> {
                    assertThat(customer.getFirstName()).isEqualTo(firstName);
                    assertThat(customer.getLastName()).isEqualTo(lastName);
                    assertThat(customer.getEmail()).isEqualTo(email);
                    assertThat(customer.getPersonalNumber()).isEqualTo(personalNumber);
                    assertThat(customer.getPhones().stream().filter(c->c.getPhoneNumber().equals(phoneNumber)).collect(Collectors.toList())).hasSize(1);
                });
    }

    @Test
    void findCustomerOpt_byPhoneNumber_whenIsEmpty(){
        String phoneNumber = "UNKNOWN";
        Optional<Customer> found = customerRepo.findByPhoneNumberOpt(phoneNumber);
        assertThat(found).isEmpty();
    }

    @Test
    void findCustomerOpt_byPersonalNumber_whenIsNotEmpty(){
        String firstName = "Lana";
        String lastName = "Jovanović";
        String email = "lana@greenphone.com";
        String personalNumber = "3277645392";
        Optional<Customer> found = customerRepo.findByPersonalNumberOpt(personalNumber);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(Customer.class)
                .hasValueSatisfying(customer-> {
                    assertThat(customer.getFirstName()).isEqualTo(firstName);
                    assertThat(customer.getLastName()).isEqualTo(lastName);
                    assertThat(customer.getEmail()).isEqualTo(email);
                    assertThat(customer.getPersonalNumber()).isEqualTo(personalNumber);
                });
    }

    @Test
    void findCustomerOpt_byPersonalNumber_whenIsEmpty(){
        String personalNumber = "UNKNOWN";
        Optional<Customer> found = customerRepo.findByPersonalNumberOpt(personalNumber);
        assertThat(found).isEmpty();
    }

    @Test
    void findCustomerOpt_byEmail_whenIsNotEmpty(){
        String firstName = "Lana";
        String lastName = "Jovanović";
        String email = "lana@greenphone.com";
        String personalNumber = "3277645392";
        Optional<Customer> found = customerRepo.findByEmailOpt(email);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(Customer.class)
                .hasValueSatisfying(customer-> {
                    assertThat(customer.getFirstName()).isEqualTo(firstName);
                    assertThat(customer.getLastName()).isEqualTo(lastName);
                    assertThat(customer.getEmail()).isEqualTo(email);
                    assertThat(customer.getPersonalNumber()).isEqualTo(personalNumber);
                });
    }

    @Test
    void findCustomerOpt_byEmail_whenIsEmpty(){
        String email = "UNKNOWN";
        Optional<Customer> found = customerRepo.findByEmailOpt(email);
        assertThat(found).isEmpty();
    }

    @Test
    void findCustomer_byPersonalNumberOtherWay_returnCustomerIfExists(){
        String firstName = "Lana";
        String lastName = "Jovanović";
        String email = "lana@greenphone.com";
        String personalNumber = "3277645392";
        Customer found = customerRepo.findCustomerByPersonalNumberOtherWay(personalNumber);
        assertThat(found.getFirstName()).isEqualTo(firstName);
        assertThat(found.getLastName()).isEqualTo(lastName);
        assertThat(found.getEmail()).isEqualTo(email);
        assertThat(found.getPersonalNumber()).isEqualTo(personalNumber);
    }

    @Test
    void findCustomer_byPersonalNumberOtherWay_expectedNull_ifPersonalNumberDoesNotExist(){
        String personalNumber = "UNKNOWN";
        Customer found = customerRepo.findCustomerByPersonalNumberOtherWay(personalNumber);
        assertThat(found).isNull();
    }
}
