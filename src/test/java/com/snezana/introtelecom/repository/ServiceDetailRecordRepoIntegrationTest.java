package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.ServiceDetailRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ServiceDetailRecordRepoIntegrationTest {

    @Autowired
    private ServiceDetailRecordRepo serviceDetailRecordRepo;

    @Test
    void findServiceDetailRecordOpt_byId_whenIsNotEmpty(){
        Long id = 1L;
        String phoneNumber = "0769317426";
        String sdrCode = "SDRCLS";
        Optional<ServiceDetailRecord> found = serviceDetailRecordRepo.findBySdrIdOpt(id);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(ServiceDetailRecord.class)
                .hasValueSatisfying(sdr-> {
                    assertThat(sdr.getSdrId()).isEqualTo(1L);
                    assertThat(sdr.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
                    assertThat(sdr.getPhoneService().getServiceCode()).isEqualTo(sdrCode);
                });
    }

    @Test
    void findServiceDetailRecordOpt_byId_whenIsEmpty(){
        Long id = 200L;
        Optional<ServiceDetailRecord> found = serviceDetailRecordRepo.findBySdrIdOpt(id);
        assertThat(found).isEmpty();
    }

    @Test
    void findServiceDetailRecordOpt_byPhoneNumber_startDateTime_endDateTime_serviceCode_whenIsNotEmpty(){
        Long id = 1L;
        String phoneNumber = "0769317426";
        String sdrCode = "SDRCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 2,10,10,2,0,0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 2,10,10,5,0,0);
        Optional<ServiceDetailRecord> found = serviceDetailRecordRepo.findServiceDetailRecordByPhone_PhoneNumberAndSdrStartDateTimeEqualsAndSdrEndDateTimeEqualsAndPhoneService_ServiceCode (phoneNumber, startDateTime, endDateTime, sdrCode);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(ServiceDetailRecord.class)
                .hasValueSatisfying(sdr-> {
                    assertThat(sdr.getSdrId()).isEqualTo(id);
                    assertThat(sdr.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
                    assertThat(sdr.getSdrStartDateTime()).isEqualTo(startDateTime);
                    assertThat(sdr.getSdrEndDateTime()).isEqualTo(endDateTime);
                    assertThat(sdr.getPhoneService().getServiceCode()).isEqualTo(sdrCode);
                });
    }

    @Test
    void findServiceDetailRecordOpt_byPhoneNumber_startDateTime_endDateTime_serviceCode_whenIsEmpty(){
        Long id = 1L;
        String phoneNumber = "UNKNOWN";
        String sdrCode = "SDRCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 2,10,10,2,0,0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 2,10,10,5,0,0);
        Optional<ServiceDetailRecord> found = serviceDetailRecordRepo.findServiceDetailRecordByPhone_PhoneNumberAndSdrStartDateTimeEqualsAndSdrEndDateTimeEqualsAndPhoneService_ServiceCode (phoneNumber, startDateTime, endDateTime, sdrCode);
        assertThat(found).isEmpty();
    }

    @Test
    void findServiceDetailRecordList_byPhoneNumber_startDateTime_endDateTime(){
        int listSize = 8;
        String phoneNumber = "0769317426";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 2,10,10,2,0,0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 2,17,11,2,0,0);
        List<ServiceDetailRecord> foundList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqual (phoneNumber, startDateTime, endDateTime);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(sdr -> sdr.getPhone().getPhoneNumber().equals(phoneNumber)
                        && (sdr.getSdrStartDateTime().equals(startDateTime)|| sdr.getSdrStartDateTime().isAfter(startDateTime))
                        && (sdr.getSdrEndDateTime().equals(endDateTime)|| sdr.getSdrEndDateTime().isBefore(endDateTime)))
                .hasSize(listSize);
    }

    @Test
    void findServiceDetailRecordList_byPhoneNumber_startDateTime(){
        int listSize = 8;
        String phoneNumber = "0769317426";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 2,10,10,2,0,0);
        List<ServiceDetailRecord> foundList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqual (phoneNumber, startDateTime);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(sdr -> sdr.getPhone().getPhoneNumber().equals(phoneNumber)
                        && (sdr.getSdrStartDateTime().equals(startDateTime)|| sdr.getSdrStartDateTime().isAfter(startDateTime)))
                .hasSize(listSize);
    }

    @Test
    void findServiceDetailRecordList_byPhoneNumber_startDateTime_endDateTime_serviceCode(){
        int listSize = 2;
        String phoneNumber = "0769317426";
        String sdrCode = "SDRCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 2,10,10,2,0,0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 2,17,11,2,0,0);
        List<ServiceDetailRecord> foundList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode (phoneNumber, startDateTime, endDateTime, sdrCode);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(sdr -> sdr.getPhone().getPhoneNumber().equals(phoneNumber)
                        && (sdr.getPhoneService().getServiceCode().equals(sdrCode))
                        && (sdr.getSdrStartDateTime().equals(startDateTime)|| sdr.getSdrStartDateTime().isAfter(startDateTime))
                        && (sdr.getSdrEndDateTime().equals(endDateTime)|| sdr.getSdrEndDateTime().isBefore(endDateTime)))
                .hasSize(listSize);
    }

    @Test
    void findServiceDetailRecordList_byPhoneNumber_startDateTime_serviceCode(){
        int listSize = 2;
        String phoneNumber = "0769317426";
        String sdrCode = "SDRCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 2,10,10,2,0,0);
        List<ServiceDetailRecord> foundList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndPhoneService_ServiceCode (phoneNumber, startDateTime, sdrCode);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(sdr -> sdr.getPhone().getPhoneNumber().equals(phoneNumber)
                        && (sdr.getPhoneService().getServiceCode().equals(sdrCode))
                        && (sdr.getSdrStartDateTime().equals(startDateTime)|| sdr.getSdrStartDateTime().isAfter(startDateTime)))
                .hasSize(listSize);
    }

}
