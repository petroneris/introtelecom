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

    List<PackageFrame> findByPhone_PhoneNumberAndPackfrStartDateTimeGreaterThanEqualAndPackfrEndDateTimeLessThanEqual (String phoneNumber, LocalDateTime packfrStartDateTime, LocalDateTime packfrEndDateTime);

    List<PackageFrame> findByPhone_PhoneNumberAndPackfrStartDateTimeGreaterThanEqual (String phoneNumber, LocalDateTime packfrStartDateTime);

    List<PackageFrame> findByPhone_PackagePlan_PackageCodeAndPackfrStartDateTimeGreaterThanEqualAndPackfrEndDateTimeLessThanEqual (String packageCode, LocalDateTime packfrStartDateTime, LocalDateTime packfrEndDateTime);

    List<PackageFrame> findByPhone_PackagePlan_PackageCodeAndPackfrStartDateTimeGreaterThanEqual (String packageCode, LocalDateTime packfrStartDateTime);

    Optional<PackageFrame> findPackageFrameByPhone_PhoneNumberAndPackfrStartDateTimeGreaterThanEqualAndPackfrEndDateTimeLessThanEqual (String phoneNumber, LocalDateTime packfrStartDateTime, LocalDateTime packfrEndDateTime);

    Optional<PackageFrame> findPackageFrameByPhone_PhoneNumberAndPackfrStartDateTimeEqualsAndPackfrEndDateTimeEquals (String phoneNumber, LocalDateTime packfrStartDateTime, LocalDateTime packfrEndDateTime);

    List<PackageFrame> findByPackfrStartDateTimeEqualsAndPackfrEndDateTimeEquals (LocalDateTime packfrStartDateTime, LocalDateTime packfrEndDateTime);

    @Query(
            "SELECT " +
                    "packageFrame "+
                    "FROM PackageFrame packageFrame "+
                    "WHERE packageFrame.packfrId = :packfrId "
    )
    Optional<PackageFrame> findByPackfrIdOpt(@Param("packfrId") Long packfrId);

    Long countAllByPackfrId(Long packfrId);

}
