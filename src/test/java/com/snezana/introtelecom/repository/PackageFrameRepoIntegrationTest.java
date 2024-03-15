package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.PackageFrame;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PackageFrameRepoIntegrationTest {

    @Autowired
    private PackageFrameRepo packageFrameRepo;

    @Test
    void findPackageFrameOpt_byId_whenIsNotEmpty(){
        Long id = 1L;
        String phoneNumber = "0747634418";
        int packfrCls = 200;
        Optional<PackageFrame> found = packageFrameRepo.findByPackfrIdOpt(id);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(PackageFrame.class)
                .hasValueSatisfying(packageFrame-> {
                    assertThat(packageFrame.getPackfrId()).isEqualTo(1L);
                    assertThat(packageFrame.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
                    assertThat(packageFrame.getPackfrCls()).isEqualTo(packfrCls);
                });
    }

    @Test
    void findPackageFrameOpt_byId_whenIsEmpty(){
        Long id = 200L;
        Optional<PackageFrame> found = packageFrameRepo.findByPackfrIdOpt(id);
        assertThat(found).isEmpty();
    }

    @Test
    void findPackageFrameOpt_byPhoneNumber_startDateTime_endDateTime_whenIsNotEmpty(){
        String phoneNumber = "0747634418";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1,1,0,0,0,0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 2,1,0,0,0,0);
        Optional<PackageFrame> found = packageFrameRepo.findPackageFrameByPhone_PhoneNumberAndPackfrStartDateTimeEqualsAndPackfrEndDateTimeEquals(phoneNumber, startDateTime, endDateTime);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(PackageFrame.class)
                .hasValueSatisfying(packageFrame-> {
                    assertThat(packageFrame.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
                    assertThat(packageFrame.getPackfrStartDateTime()).isEqualTo(startDateTime);
                    assertThat(packageFrame.getPackfrEndDateTime()).isEqualTo(endDateTime);
                });
    }

    @Test
    void findPackageFrameOpt_byPhoneNumber_startDateTime_endDateTime_whenIsEmpty(){
        String phoneNumber = "UNKNOWN";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1,1,0,0,0,0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 2,1,0,0,0,0);
        Optional<PackageFrame> found = packageFrameRepo.findPackageFrameByPhone_PhoneNumberAndPackfrStartDateTimeEqualsAndPackfrEndDateTimeEquals(phoneNumber, startDateTime, endDateTime);
        assertThat(found).isEmpty();
    }

    @Test
    void findPackageFrameList_byPhoneNumber_startDateTime_endDateTime() {
        int listSize = 2;
        String phoneNumber = "0747634418";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        List<PackageFrame> foundList = packageFrameRepo.findByPhone_PhoneNumberAndPackfrStartDateTimeGreaterThanEqualAndPackfrEndDateTimeLessThanEqual(phoneNumber, startDateTime, endDateTime);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(packageFrame -> packageFrame.getPhone().getPhoneNumber().equals(phoneNumber)
                        && (packageFrame.getPackfrStartDateTime().equals(startDateTime)|| packageFrame.getPackfrStartDateTime().isAfter(startDateTime))
                        && (packageFrame.getPackfrEndDateTime().equals(endDateTime)|| packageFrame.getPackfrEndDateTime().isBefore(endDateTime)))
                .hasSize(listSize);
    }

    @Test
    void findPackageFrameList_byPhoneNumber_startDateTime(){
        int listSize = 2;
        String phoneNumber = "0747634418";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1,1,0,0,0,0);
        List<PackageFrame> foundList = packageFrameRepo.findByPhone_PhoneNumberAndPackfrStartDateTimeGreaterThanEqual(phoneNumber, startDateTime);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(packageFrame ->packageFrame.getPhone().getPhoneNumber().equals(phoneNumber)
                        && (packageFrame.getPackfrStartDateTime().equals(startDateTime)|| packageFrame.getPackfrStartDateTime().isAfter(startDateTime)))
                .hasSize(listSize);
    }

    @Test
    void findPackageFrameList_byPackageCode_startDateTime_endDateTime(){
        int listSize = 2;
        String packageCode = "01";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1,1,0,0,0,0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3,1,0,0,0,0);
        List<PackageFrame> foundList = packageFrameRepo.findByPhone_PackagePlan_PackageCodeAndPackfrStartDateTimeGreaterThanEqualAndPackfrEndDateTimeLessThanEqual (packageCode, startDateTime, endDateTime);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(packageFrame ->packageFrame.getPhone().getPackagePlan().getPackageCode().equals(packageCode)
                        && (packageFrame.getPackfrStartDateTime().equals(startDateTime)|| packageFrame.getPackfrStartDateTime().isAfter(startDateTime))
                        && (packageFrame.getPackfrEndDateTime().equals(endDateTime)|| packageFrame.getPackfrEndDateTime().isBefore(endDateTime)))
                .hasSize(listSize);
    }

    @Test
    void findPackageFrameList_byPackageCode_startDateTime(){
        int listSize = 2;
        String packageCode = "01";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1,1,0,0,0,0);
        List<PackageFrame> foundList = packageFrameRepo.findByPhone_PackagePlan_PackageCodeAndPackfrStartDateTimeGreaterThanEqual(packageCode, startDateTime);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(packageFrame ->packageFrame.getPhone().getPackagePlan().getPackageCode().equals(packageCode)
                        && (packageFrame.getPackfrStartDateTime().equals(startDateTime)|| packageFrame.getPackfrStartDateTime().isAfter(startDateTime)))
                .hasSize(listSize);
    }

    @Test
    void findPackageFrameList_byStartDateTime_endDateTime(){
        int listSize = 6;
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1,1,0,0,0,0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 2,1,0,0,0,0);
        List<PackageFrame> foundList = packageFrameRepo.findByPackfrStartDateTimeEqualsAndPackfrEndDateTimeEquals(startDateTime, endDateTime);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(packageFrame ->packageFrame.getPackfrStartDateTime().equals(startDateTime)
                        && packageFrame.getPackfrEndDateTime().equals(endDateTime))
                .hasSize(listSize);
    }

}
