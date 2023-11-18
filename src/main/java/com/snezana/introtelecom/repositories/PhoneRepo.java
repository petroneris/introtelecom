package com.snezana.introtelecom.repositories;

import com.snezana.introtelecom.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PhoneRepo extends JpaRepository<Phone, Long> {
    Phone findByPhoneNumber (String phoneNumber);
    @Query(
            "SELECT " +
                    "phone "+
                    "FROM Phone phone "+
                    "WHERE phone.phoneNumber = :phoneNumber "
    )
    Optional<Phone> findByPhoneNumberOpt(@Param("phoneNumber") String phoneNumber);


    @Query("SELECT phone FROM Phone phone WHERE phone.packagePlan.packageCode='00'")
    public List<Phone> findAllAdminPhones();

    @Query("SELECT phone FROM Phone phone WHERE NOT phone.packagePlan.packageCode='00'")
    public List<Phone> findAllCustomerPhones();

    @Query(
            "SELECT " +
                    "phone "+
                    "FROM Phone phone "+
                    "WHERE phone.packagePlan.packageCode = :packageCode "
    )
    public List<Phone> findByPackageCode(@Param("packageCode") String packageCode);
}
