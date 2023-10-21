package com.snezana.introtelecom.repositories;

import com.snezana.introtelecom.entity.AddonFrame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AddonFrameRepo extends JpaRepository<AddonFrame, Long> {

    AddonFrame findByAddfrId (Long addfrId);

    List<AddonFrame> findByPhone_PhoneNumberAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual (String phoneNumber, LocalDateTime addfrStartDateTime, LocalDateTime addfrEndDateTime);

    List<AddonFrame> findByPhone_PhoneNumberAndAddfrStartDateTimeGreaterThanEqual(String phoneNumber, LocalDateTime addfrStartDateTime);

    List<AddonFrame> findByAddOn_AddonCodeAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual (String addonCode, LocalDateTime addfrStartDateTime, LocalDateTime addfrEndDateTime);

    List<AddonFrame> findByAddOn_AddonCodeAndAddfrStartDateTimeGreaterThanEqual (String addonCode, LocalDateTime addfrStartDateTime);

    Optional<AddonFrame> findByPhone_PhoneNumberAndAddOn_AddonCodeAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual (String phoneNumber, String addonCode, LocalDateTime addfrStartDateTime, LocalDateTime addfrEndDateTime);

    List<AddonFrame> findAddonFramesByPhone_PhoneNumberAndAddOn_AddonCodeAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual (String phoneNumber, String addonCode, LocalDateTime addfrStartDateTime, LocalDateTime addfrEndDateTime);

    @Query(
            "SELECT " +
                    "addonFrame "+
                    "FROM AddonFrame addonFrame "+
                    "WHERE addonFrame.addfrId = :addfrId "
    )
    Optional<AddonFrame> findByAddfrIdOpt(@Param("addfrId") Long addfrId);

}
