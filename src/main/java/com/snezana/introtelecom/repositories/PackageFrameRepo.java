package com.snezana.introtelecom.repositories;

import com.snezana.introtelecom.entity.PackageFrame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PackageFrameRepo extends JpaRepository<PackageFrame, Long> {

    PackageFrame findByPackfrId (Long packfrId);

    List<PackageFrame> findByPhone_PhoneNumberAndPackfrStartDateTimeGreaterThanEqualAndPackfrEndDateTimeIsLessThanEqual (String phoneNumber, LocalDateTime packfrStartDateTime, LocalDateTime packfrEndDateTime);

    List<PackageFrame> findByPhone_PhoneNumberAndPackfrStartDateTimeGreaterThanEqual (String phoneNumber, LocalDateTime packfrStartDateTime);


    @Query(
            "SELECT " +
                    "packageFrame "+
                    "FROM PackageFrame packageFrame "+
                    "WHERE packageFrame.packfrId = :packfrId "
    )
    Optional<PackageFrame> findByPackfrIdOpt(@Param("packfrId") Long packfrId);


}
