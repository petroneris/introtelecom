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

    List<AddonFrame> findByPhone_PhoneNumberAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeIsLessThanEqual (String phoneNumber, LocalDateTime addfrStartDateTime, LocalDateTime addfrEndDateTime);

    List<AddonFrame> findByPhone_PhoneNumberAndAddfrStartDateTimeGreaterThanEqual(String phoneNumber, LocalDateTime addfrStartDateTime);

    List<AddonFrame> findByPhone_PhoneNumberAndAddOn_AddonCode (String phoneNumber, String addonCode);

    List<AddonFrame> findByPhone_PhoneNumberAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeIsLessThanEqualAndAddOn_AddonCode (String phoneNumber, LocalDateTime addfrStartDateTime, LocalDateTime addfrEndDateTime, String addonCode);

    List<AddonFrame> findByPhone_PhoneNumberAndAddfrStartDateTimeGreaterThanEqualAndAddOn_AddonCode (String phoneNumber, LocalDateTime addfrStartDateTime, String addonCode);

    @Query(
            "SELECT " +
                    "addonFrame "+
                    "FROM AddonFrame addonFrame "+
                    "WHERE addonFrame.addfrId = :addfrId "
    )
    Optional<AddonFrame> findByAddfrIdOpt(@Param("addfrId") Long addfrId);

}
