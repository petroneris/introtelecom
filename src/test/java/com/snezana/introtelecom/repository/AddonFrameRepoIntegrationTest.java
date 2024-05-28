package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.AddonFrame;
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
public class AddonFrameRepoIntegrationTest {

    @Autowired
    private AddonFrameRepo addonFrameRepo;

    @Test
    void findAddonFrameOpt_byId_whenIsNotEmpty(){
        Long id = 1L;
        String phoneNumber = "0747634418";
        int addfrCls = 100;
        Optional<AddonFrame> found = addonFrameRepo.findByAddfrIdOpt(id);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(AddonFrame.class)
                .hasValueSatisfying(addonFrame-> {
                    assertThat(addonFrame.getAddfrId()).isEqualTo(1L);
                    assertThat(addonFrame.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
                    assertThat(addonFrame.getAddfrCls()).isEqualTo(addfrCls);
                });
    }

    @Test
    void findAddonFrameOpt_byId_whenIsEmpty(){
        Long id = 200L;
        Optional<AddonFrame> found = addonFrameRepo.findByAddfrIdOpt(id);
        assertThat(found).isEmpty();
    }

    @Test
    void findAddonFrameOpt_byPhoneNumber_addonCode_endDateTime_whenIsNotEmpty(){
        String phoneNumber = "0747634418";
        String addonCode = "ADDCLS";
        LocalDateTime endDateTime = LocalDateTime.of(2023, 2,1,0,0,0,0);
        Optional<AddonFrame> found = addonFrameRepo.findByPhone_PhoneNumberAndAddOn_AddonCodeAndAddfrEndDateTimeEquals (phoneNumber,addonCode, endDateTime);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(AddonFrame.class)
                .hasValueSatisfying(addonFrame-> {
                    assertThat(addonFrame.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
                    assertThat(addonFrame.getAddOn().getAddonCode()).isEqualTo(addonCode);
                    assertThat(addonFrame.getAddfrEndDateTime()).isEqualTo(endDateTime);
                });
    }

    @Test
    void findAddonFrameOpt_byPhoneNumber_addonCode_endDateTime_whenIsEmpty(){
        String phoneNumber = "UNKNOWN";
        String addonCode = "ADDCLS";
        LocalDateTime endDateTime = LocalDateTime.of(2023, 2,1,0,0,0,0);
        Optional<AddonFrame> found = addonFrameRepo.findByPhone_PhoneNumberAndAddOn_AddonCodeAndAddfrEndDateTimeEquals (phoneNumber, addonCode, endDateTime);
        assertThat(found).isEmpty();
    }

    @Test
    void findAddonFrameList_byPhoneNumber_startDateTime_endDateTime() {
        int listSize = 2;
        String phoneNumber = "0747634418";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        List<AddonFrame> foundList = addonFrameRepo.findByPhone_PhoneNumberAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual (phoneNumber, startDateTime, endDateTime);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(addonFrame -> addonFrame.getPhone().getPhoneNumber().equals(phoneNumber)
                        && (addonFrame.getAddfrStartDateTime().equals(startDateTime)|| addonFrame.getAddfrStartDateTime().isAfter(startDateTime))
                        && (addonFrame.getAddfrEndDateTime().equals(endDateTime)|| addonFrame.getAddfrEndDateTime().isBefore(endDateTime)))
                .hasSize(listSize);
    }

    @Test
    void findAddonFrameList_byPhoneNumber_startDateTime() {
        int listSize = 2;
        String phoneNumber = "0747634418";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        List<AddonFrame> foundList = addonFrameRepo.findByPhone_PhoneNumberAndAddfrStartDateTimeGreaterThanEqual(phoneNumber, startDateTime);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(addonFrame -> addonFrame.getPhone().getPhoneNumber().equals(phoneNumber)
                        && (addonFrame.getAddfrStartDateTime().equals(startDateTime)|| addonFrame.getAddfrStartDateTime().isAfter(startDateTime)))
                .hasSize(listSize);
    }

    @Test
    void findAddonFrameList_byAddonCode_startDateTime_endDateTime() {
        int listSize = 2;
        String addonCode = "ADDCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        List<AddonFrame> foundList = addonFrameRepo.findByAddOn_AddonCodeAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual (addonCode, startDateTime, endDateTime);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(addonFrame -> addonFrame.getAddOn().getAddonCode().equals(addonCode)
                        && (addonFrame.getAddfrStartDateTime().equals(startDateTime)|| addonFrame.getAddfrStartDateTime().isAfter(startDateTime))
                        && (addonFrame.getAddfrEndDateTime().equals(endDateTime)|| addonFrame.getAddfrEndDateTime().isBefore(endDateTime)))
                .hasSize(listSize);
    }

    @Test
    void findAddonFrameList_byAddonCode_startDateTime() {
        int listSize = 2;
        String addonCode = "ADDCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        List<AddonFrame> foundList = addonFrameRepo.findByAddOn_AddonCodeAndAddfrStartDateTimeGreaterThanEqual (addonCode, startDateTime);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(addonFrame -> addonFrame.getAddOn().getAddonCode().equals(addonCode)
                        && (addonFrame.getAddfrStartDateTime().equals(startDateTime)|| addonFrame.getAddfrStartDateTime().isAfter(startDateTime)))
                .hasSize(listSize);
    }

    @Test
    void findAddonFrameList_byPhoneNumber_addonCode_startDateTime_endDateTime() {
        int listSize = 1;
        String phoneNumber = "0747634418";
        String addonCode = "ADDCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        List<AddonFrame> foundList = addonFrameRepo.findAddonFramesByPhone_PhoneNumberAndAddOn_AddonCodeAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual (phoneNumber, addonCode, startDateTime, endDateTime);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(addonFrame -> addonFrame.getAddOn().getAddonCode().equals(addonCode)
                        && addonFrame.getPhone().getPhoneNumber().equals(phoneNumber)
                        && (addonFrame.getAddfrStartDateTime().equals(startDateTime)|| addonFrame.getAddfrStartDateTime().isAfter(startDateTime))
                        && (addonFrame.getAddfrEndDateTime().equals(endDateTime)|| addonFrame.getAddfrEndDateTime().isBefore(endDateTime)))
                .hasSize(listSize);
    }

    @Test
    void findAddonFrameList_byStartDateTime_endDateTime() {
        int listSize = 6;
        LocalDateTime startDateTime = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        List<AddonFrame> foundList = addonFrameRepo.findByAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeEquals(startDateTime, endDateTime);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(addonFrame -> (addonFrame.getAddfrStartDateTime().equals(startDateTime)|| addonFrame.getAddfrStartDateTime().isAfter(startDateTime))
                        && addonFrame.getAddfrEndDateTime().equals(endDateTime))
                .hasSize(listSize);
    }

}
