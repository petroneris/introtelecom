package com.snezana.introtelecom.repositories;

import com.snezana.introtelecom.entity.Admin;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AdminRepo extends JpaRepository<Admin, Long> {
    Admin findByAdminId (Long adminId);
    Admin findByPhone (Phone phone);
    Admin findByPersonalNumber (String personalNumber);
    Admin findAdminByEmail (String email);
    List<Admin> findAll ();

    @Query(
            "SELECT " +
                    "admin "+
                    "FROM Admin admin "+
                    "WHERE admin.adminId = :adminId "
    )
    Optional<Admin> findByAdminIdOpt(@Param("adminId") Long adminId);

    @Query(
            "SELECT " +
                    "admin "+
                    "FROM Admin admin "+
                    "WHERE admin.phone.phoneNumber = :phoneNumber "
    )
    Optional<Admin> findByPhoneNumberOpt(@Param("phoneNumber") String phoneNumber);

    @Query(
            "SELECT " +
                    "admin "+
                    "FROM Admin admin "+
                    "WHERE admin.personalNumber = :personalNumber "
    )
    Optional<Admin> findByPersonalNumberOpt(@Param("personalNumber") String personalNumber);

    @Query(
            "SELECT " +
                    "admin "+
                    "FROM Admin admin "+
                    "WHERE admin.email = :email "
    )
    Optional<Admin> findByEmailOpt(@Param("email") String email);


}
