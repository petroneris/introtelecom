package com.snezana.introtelecom.repositories;

import com.snezana.introtelecom.entity.PhoneService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PhoneServiceRepo extends JpaRepository<PhoneService, Long> {
    PhoneService findByServiceCode (String serviceCode);
    @Query(
            "SELECT " +
                    "phoneService "+
                    "FROM PhoneService phoneService "+
                    "WHERE phoneService.serviceCode = :serviceCode "
    )
    Optional<PhoneService> findByServiceCodeOpt(@Param("serviceCode") String serviceCode);
}
