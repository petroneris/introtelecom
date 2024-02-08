package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdminRepo extends JpaRepository<Admin, Long> {

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
