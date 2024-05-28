package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

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

    @Query(value = "SELECT MAX(customer.customerId) FROM Customer customer")
    Long maxCustomer_id();

    @Query(
            "SELECT " +
                    "customer "+
                    "FROM Customer customer "+
                    "JOIN FETCH customer.phones "+                      //  to handle LazyInitializationException
                    "WHERE customer.personalNumber = :personalNumber "
    )
    Customer findCustomerByPersonalNumberOtherWay(String personalNumber);

}
