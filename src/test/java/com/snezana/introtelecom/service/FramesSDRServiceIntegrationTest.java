package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.entity.AddonFrame;
import com.snezana.introtelecom.entity.PackageFrame;
import com.snezana.introtelecom.entity.ServiceDetailRecord;
import com.snezana.introtelecom.repository.AddonFrameRepo;
import com.snezana.introtelecom.repository.PackageFrameRepo;
import com.snezana.introtelecom.repository.ServiceDetailRecordRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class FramesSDRServiceIntegrationTest {

    @Autowired
    private FramesSDRService framesSDRService;

    @Autowired
    private PackageFrameRepo packageFrameRepo;

    @Autowired
    private AddonFrameRepo addonFrameRepo;

    @Autowired
    private ServiceDetailRecordRepo serviceDetailRecordRepo;

    @Test
    void testFindPackageFrameById() {
        Long id = 1L;
        String phoneNumber = "0747634418";
        PackageFrameViewDTO packageFrameViewDTO = framesSDRService.findPackageFrameById(id);
        assertThat(packageFrameViewDTO).isNotNull();
        assertThat(packageFrameViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testFindPackageFramesByPhoneNumberStartTimeEndTime(){
        int size = 2;
        String phoneNumber = "0747634418";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        List<PackageFrameViewDTO> packageFrameViewDTOList = framesSDRService.findPackageFramesByPhoneNumberStartTimeEndTime(phoneNumber, startDateTime, endDateTime);
        assertThat(packageFrameViewDTOList).isNotNull();
        assertThat(packageFrameViewDTOList.size()).isEqualTo(size);
    }

    @Test
    void testFindPackageFramesByPhoneNumberStartTime(){
        int size = 2;
        String phoneNumber = "0747634418";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        List<PackageFrameViewDTO> packageFrameViewDTOList = framesSDRService.findPackageFramesByPhoneNumberStartTime(phoneNumber, startDateTime);
        assertThat(packageFrameViewDTOList).isNotNull();
        assertThat(packageFrameViewDTOList.size()).isEqualTo(size);
    }

    @Test
    void testFindPackageFramesByPackageCodeStartTimeEndTime(){
        int size = 1;
        String packageCode = "01";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        List<PackageFrameViewDTO> packageFrameViewDTOList = framesSDRService.findPackageFramesByPackageCodeStartTimeEndTime(packageCode, startDateTime,endDateTime);
        assertThat(packageFrameViewDTOList).isNotNull();
        assertThat(packageFrameViewDTOList.size()).isEqualTo(size);
    }

    @Test
    void testFindPackageFramesByPackageCodeStartTime(){
        int size = 2;
        String packageCode = "01";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        List<PackageFrameViewDTO> packageFrameViewDTOList = framesSDRService.findPackageFramesByPackageCodeStartTime(packageCode, startDateTime);
        assertThat(packageFrameViewDTOList).isNotNull();
        assertThat(packageFrameViewDTOList.size()).isEqualTo(size);
    }

    @Test
    @Sql(scripts = {"/update_package_frame.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangePackageFrameStatus(){
        Long id = 3L;
        String changedStatus = "INACTIVE";
        framesSDRService.changePackageFrameStatus(id);
        Optional<PackageFrame> packageFrameOpt = packageFrameRepo.findById(id);
        assertThat(packageFrameOpt).isPresent();
        assertThat(packageFrameOpt.get().getPackfrStatus()).isEqualTo(changedStatus);
    }

    @Test
    @Sql(scripts = {"/create_package_frame.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testDeletePackageFrame(){
        Long maxId = packageFrameRepo.maxPackfr_id();
        int sizeBefore = packageFrameRepo.findAll().size();
        framesSDRService.deletePackageFrame(maxId);
        assertThat(packageFrameRepo.findAll().size()).isEqualTo(sizeBefore - 1);
        assertThat(packageFrameRepo.findAll().stream().filter(packageFrame -> Objects.equals(packageFrame.getPackfrId(), maxId)).count()).isEqualTo(0);
    }

    @Test
    @Sql(scripts = {"/delete_addon_frame.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testSaveNewAddonFrame(){
        int sizeBefore = addonFrameRepo.findAll().size();
        String addonCode = "ADDCLS";
        String phoneNumber = "0747634418";
        AddonFrameSaveDTO addonFrameSaveDTO = new AddonFrameSaveDTO();
        addonFrameSaveDTO.setAddonCode(addonCode);
        addonFrameSaveDTO.setPhoneNumber(phoneNumber);
        framesSDRService.saveNewAddonFrame(addonFrameSaveDTO);
        AddonFrame addonFrame = addonFrameRepo.findAll().get(sizeBefore);
        assertThat(addonFrameRepo.findAll().size()).isEqualTo(sizeBefore + 1);
        assertThat(addonFrame.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(addonFrame.getAddfrId()).isEqualTo(addonFrameRepo.maxAddfr_id());
    }

    @Test
    void testFindAddonFrameById() {
        Long id = 1L;
        String phoneNumber = "0747634418";
        AddonFrameViewDTO addonFrameViewDTO = framesSDRService.findAddonFrameById(id);
        assertThat(addonFrameViewDTO).isNotNull();
        assertThat(addonFrameViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testFindAddonFramesByPhoneNumberStartTimeEndTime(){
        int size = 2;
        String phoneNumber = "0747634418";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        List<AddonFrameViewDTO> addonFrameViewDTOList = framesSDRService.findAddonFramesByPhoneNumberStartTimeEndTime(phoneNumber, startDateTime,endDateTime);
        assertThat(addonFrameViewDTOList).isNotNull();
        assertThat(addonFrameViewDTOList.size()).isEqualTo(size);
    }

    @Test
    void testFindAddonFramesByPhoneNumberStartTime(){
        int size = 2;
        String phoneNumber = "0747634418";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        List<AddonFrameViewDTO> addonFrameViewDTOList = framesSDRService.findAddonFramesByPhoneNumberStartTime(phoneNumber, startDateTime);
        assertThat(addonFrameViewDTOList).isNotNull();
        assertThat(addonFrameViewDTOList.size()).isEqualTo(size);
    }

    @Test
    void testFindAddonFramesByAddonCodeStartTimeEndTime(){
        int size = 2;
        String addonCode = "ADDCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        List<AddonFrameViewDTO> addonFrameViewDTOList = framesSDRService.findAddonFramesByAddonCodeStartTimeEndTime(addonCode,startDateTime,endDateTime);
        assertThat(addonFrameViewDTOList).isNotNull();
        assertThat(addonFrameViewDTOList.size()).isEqualTo(size);
    }

    @Test
    void testFindAddonFramesByAddonCodeStartTime(){
        int size = 2;
        String addonCode = "ADDCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        List<AddonFrameViewDTO> addonFrameViewDTOList = framesSDRService.findAddonFramesByAddonCodeStartTime(addonCode,startDateTime);
        assertThat(addonFrameViewDTOList).isNotNull();
        assertThat(addonFrameViewDTOList.size()).isEqualTo(size);
    }

    @Test
    @Sql(scripts = {"/update_addon_frame.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangeAddonFrameStatus(){
        Long id = 3L;
        String changedStatus = "INACTIVE";
        framesSDRService.changeAddonFrameStatus(id);
        Optional<AddonFrame> addonFrameOpt = addonFrameRepo.findByAddfrIdOpt(id);
        assertThat(addonFrameOpt).isPresent();
        assertThat(addonFrameOpt.get().getAddfrStatus()).isEqualTo(changedStatus);
    }

    @Test
    @Sql(scripts = {"/create_addon_frame.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testDeleteAddonFrame(){
        Long maxId = addonFrameRepo.maxAddfr_id();
        int sizeBefore = addonFrameRepo.findAll().size();
        framesSDRService.deleteAddonFrame(maxId);
        assertThat(addonFrameRepo.findAll().size()).isEqualTo(sizeBefore - 1);
        assertThat(addonFrameRepo.findAll().stream().filter(addonFrame -> Objects.equals(addonFrame.getAddfrId(), maxId)).count()).isEqualTo(0);
    }

    /*
        it is a test for normal completion of SDR (messageEOS = "Not EOS");
        there are other six cases where SDR service is interrupted (EOS), but these tests
        demand more test data with complex relation and time dependence among them
    */
    @Test
    @Sql(scripts = {"/delete_sdr.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testSaveNewServiceDetailRecord_notEOS(){
        int sizeBefore = serviceDetailRecordRepo.findAll().size();
        String messageEOS = "Not EOS";
        String phoneNumber = "0769317426";
        String serviceCode = "SDRINT";
        String calledNumber = "-";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 10, 12, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 1, 10, 13, 10, 0, 0);
        int msgAmount = 0;
        BigDecimal mbAmount = BigDecimal.valueOf(1000.00);
        mbAmount = mbAmount.setScale( 2, RoundingMode.UP);
        ServiceDetailRecordSaveDTO serviceDetailRecordSaveDTO = new ServiceDetailRecordSaveDTO();
        serviceDetailRecordSaveDTO.setPhoneNumber(phoneNumber);
        serviceDetailRecordSaveDTO.setServiceCode(serviceCode);
        serviceDetailRecordSaveDTO.setCalledNumber(calledNumber);
        serviceDetailRecordSaveDTO.setMsgAmount(msgAmount);
        serviceDetailRecordSaveDTO.setMbAmount(mbAmount);
        serviceDetailRecordSaveDTO.setSdrStartDateTime(startDateTime);
        serviceDetailRecordSaveDTO.setSdrEndDateTime(endDateTime);
        String message = framesSDRService.saveNewServiceDetailRecord(serviceDetailRecordSaveDTO);
        ServiceDetailRecord serviceDetailRecord = serviceDetailRecordRepo.findAll().get(sizeBefore);
        assertThat(serviceDetailRecordRepo.findAll().size()).isEqualTo(sizeBefore + 1);
        assertThat(serviceDetailRecord.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(serviceDetailRecord.getSdrId()).isEqualTo(serviceDetailRecordRepo.maxSdr_id());
        assertThat(message).isEqualTo(messageEOS);
    }

    @Test
    void testFindServiceDetailRecordById(){
        Long id = 1L;
        String phoneNumber = "0769317426";
        ServiceDetailRecordViewDTO serviceDetailRecordViewDTO = framesSDRService.findServiceDetailRecordById(id);
        assertThat(serviceDetailRecordViewDTO).isNotNull();
        assertThat(serviceDetailRecordViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testFindServiceDetailRecordsByPhoneNumberStartTimeEndTime() {
        int size = 8;
        String phoneNumber = "0769317426";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        List<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOList = framesSDRService.findServiceDetailRecordsByPhoneNumberStartTimeEndTime(phoneNumber, startDateTime, endDateTime);
        assertThat(serviceDetailRecordViewDTOList).isNotEmpty();
        assertThat(serviceDetailRecordViewDTOList.size()).isEqualTo(size);
    }

    @Test
    void testFindServiceDetailRecordsByPhoneNumberStartTime() {
        int size = 8;
        String phoneNumber = "0769317426";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        List<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOList = framesSDRService.findServiceDetailRecordsByPhoneNumberStartTime(phoneNumber, startDateTime);
        assertThat(serviceDetailRecordViewDTOList).isNotEmpty();
        assertThat(serviceDetailRecordViewDTOList.size()).isEqualTo(size);
    }

    @Test
    void testFindServiceDetailRecordsByPhoneNumberStartTimeEndTimeServiceCode() {
        int size = 2;
        String phoneNumber = "0769317426";
        String serviceCode = "SDRCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        List<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOList = framesSDRService.findServiceDetailRecordsByPhoneNumberStartTimeEndTimeServiceCode(phoneNumber, startDateTime, endDateTime, serviceCode);
        assertThat(serviceDetailRecordViewDTOList).isNotEmpty();
        assertThat(serviceDetailRecordViewDTOList.size()).isEqualTo(size);
    }

    @Test
    void testFindServiceDetailRecordsByPhoneNumberStartTimeServiceCode() {
        int size = 2;
        String phoneNumber = "0769317426";
        String serviceCode = "SDRCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        List<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOList = framesSDRService.findServiceDetailRecordsByPhoneNumberStartTimeServiceCode(phoneNumber,startDateTime,serviceCode);
        assertThat(serviceDetailRecordViewDTOList).isNotEmpty();
        assertThat(serviceDetailRecordViewDTOList.size()).isEqualTo(size);
    }

    @Test
    @Sql(scripts = {"/create_sdr.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testServiceDetailRecord(){
        Long maxId = serviceDetailRecordRepo.maxSdr_id();
        int sizeBefore = serviceDetailRecordRepo.findAll().size();
        framesSDRService.deleteServiceDetailRecord(maxId);
        assertThat(serviceDetailRecordRepo.findAll().size()).isEqualTo(sizeBefore - 1);
        assertThat(serviceDetailRecordRepo.findAll().stream().filter(serviceDetailRecord -> Objects.equals(serviceDetailRecord.getSdrId(), maxId)).count()).isEqualTo(0);
    }

}
