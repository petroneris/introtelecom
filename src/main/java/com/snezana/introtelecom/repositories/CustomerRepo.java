package com.snezana.introtelecom.repositories;

import com.snezana.introtelecom.entity.Admin;
import com.snezana.introtelecom.entity.Customer;
import com.snezana.introtelecom.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Customer findByCustomerId (Long customerId);
//    Customer findByPhones (Set<Phone> phones);
    Customer findByPersonalNumber (String personalNumber);
    Customer findCustomerByEmail (String email);
    List<Customer> findAll ();

    @Query(
            "SELECT " +
                    "customer "+
                    "FROM Customer customer "+
                    "WHERE customer.customerId = :customerId "
    )
    Optional<Customer> findByCustomerIdOpt(@Param("customerId") Long customerId);

    @Query(
            "SELECT " +
                    "customer "+
                    "FROM Customer customer "+
                    "JOIN customer.phones phone "+
                    "WHERE phone.phoneNumber = :phoneNumber "
    )
    Optional<Customer> findByPhoneNumberOpt(@Param("phoneNumber") String phoneNumber);

    @Query(
            "SELECT " +
                    "customer "+
                    "FROM Customer customer "+
                    "WHERE customer.personalNumber = :personalNumber "
    )
    Optional<Customer> findByPersonalNumberOpt(@Param("personalNumber") String personalNumber);

    @Query(
            "SELECT " +
                    "customer "+
                    "FROM Customer customer "+
                    "WHERE customer.email = :email "
    )
    Optional<Customer> findByEmailOpt(@Param("email") String email);
}
