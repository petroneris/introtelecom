package com.snezana.introtelecom.repositories;

import com.snezana.introtelecom.entity.ServiceDetailRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ServiceDetailRecordRepo extends JpaRepository<ServiceDetailRecord, Long> {

    ServiceDetailRecord findBySdrId (Long sdrId);

    List<ServiceDetailRecord> findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqual (String phoneNumber, LocalDateTime sdrStartDateTime, LocalDateTime sdrEndDateTime);

    List<ServiceDetailRecord> findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqual (String phoneNumber, LocalDateTime addfrStartDateTime);

    List<ServiceDetailRecord> findByPhone_PhoneNumberAndPhoneService_ServiceCode (String phoneNumber, String serviceCode);

    List<ServiceDetailRecord> findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode (String phoneNumber, LocalDateTime sdrStartDateTime, LocalDateTime sdrEndDateTime, String serviceCode);

    List<ServiceDetailRecord> findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndPhoneService_ServiceCode (String phoneNumber, LocalDateTime sdrStartDateTime, String serviceCode);

    @Query(
            "SELECT " +
                    "serviceDetailRecord "+
                    "FROM ServiceDetailRecord serviceDetailRecord "+
                    "WHERE serviceDetailRecord.sdrId = :sdrId "
    )
    Optional<ServiceDetailRecord> findBySdrIdOpt(@Param("sdrId") Long sdrId);


}
