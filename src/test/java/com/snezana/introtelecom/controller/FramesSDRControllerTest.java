package com.snezana.introtelecom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.service.FramesSDRService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FramesSDRController.class)
@WithMockUser(username ="mika", roles="ADMIN")   // to eliminate status 401(Unauthorized) and enable testing
class FramesSDRControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FramesSDRService framesSDRService;

    @Test
    void testGetPackageFrameById() throws Exception {
        Long packfrId = 1L;
        String phoneNumber = "0747634418";
        String packageCode = "01";
        String packfrCls = "200";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        String startDateTimeStr = startDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        String endDateTimeStr = endDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        PackageFrameViewDTO packageFrameViewDTO = new PackageFrameViewDTO();
        packageFrameViewDTO.setPackfrId(packfrId);
        packageFrameViewDTO.setPackageCode(packageCode);
        packageFrameViewDTO.setPhoneNumber(phoneNumber);
        packageFrameViewDTO.setPackfrCls(packfrCls);
        packageFrameViewDTO.setPackfrStartDateTime(startDateTime);
        packageFrameViewDTO.setPackfrEndDateTime(endDateTime);

        when(framesSDRService.findPackageFrameById(packfrId)).thenReturn(packageFrameViewDTO);

        mockMvc.perform(get("/api/packageFrame/getPackageFrameById/{packfrId}", packfrId))
                .andExpect(jsonPath("$.data.packfrId").value(packfrId))
                .andExpect(jsonPath("$.data.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.data.packageCode").value(packageCode))
                .andExpect(jsonPath("$.data.packfrCls").value(packfrCls))
                .andExpect(jsonPath("$.data.packfrStartDateTime").value(startDateTimeStr))
                .andExpect(jsonPath("$.data.packfrEndDateTime").value(endDateTimeStr))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetPackageFramesByPhoneNumberStartTimeEndTime() throws Exception {
        String phoneNumber = "0747634418";
        String packageCode = "01";
        String packfrCls = "200";
        LocalDateTime startDateTime1 = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime1 = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        PackageFrameViewDTO packageFrameViewDTO1 = new PackageFrameViewDTO();
        packageFrameViewDTO1.setPackageCode(packageCode);
        packageFrameViewDTO1.setPhoneNumber(phoneNumber);
        packageFrameViewDTO1.setPackfrCls(packfrCls);
        packageFrameViewDTO1.setPackfrStartDateTime(startDateTime1);
        packageFrameViewDTO1.setPackfrEndDateTime(endDateTime1);
        LocalDateTime startDateTime2 = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime2 = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        PackageFrameViewDTO packageFrameViewDTO2 = new PackageFrameViewDTO();
        packageFrameViewDTO2.setPackageCode(packageCode);
        packageFrameViewDTO2.setPhoneNumber(phoneNumber);
        packageFrameViewDTO2.setPackfrCls(packfrCls);
        packageFrameViewDTO2.setPackfrStartDateTime(startDateTime2);
        packageFrameViewDTO2.setPackfrEndDateTime(endDateTime2);
        List<PackageFrameViewDTO> packageFrameViewDTOList = new ArrayList<>();
        packageFrameViewDTOList.add(packageFrameViewDTO1);
        packageFrameViewDTOList.add(packageFrameViewDTO2);

        when(framesSDRService.findPackageFramesByPhoneNumberStartTimeEndTime(phoneNumber, startDateTime1, endDateTime2)).thenReturn(packageFrameViewDTOList);

        mockMvc.perform(get("/api/packageFrame/getPackageFramesByPhoneNumberStartTimeEndTime/{phoneNumber}", phoneNumber).param("packfrStartDateTime", startDateTime1.toString()).param("packfrEndDateTime", endDateTime2.toString()))
                .andExpect(jsonPath("$.data.size()").value(packageFrameViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetPackageFramesByPhoneNumberStartTime() throws Exception {
        String phoneNumber = "0747634418";
        String packageCode = "01";
        String packfrCls = "200";
        LocalDateTime startDateTime1 = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime1 = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        PackageFrameViewDTO packageFrameViewDTO1 = new PackageFrameViewDTO();
        packageFrameViewDTO1.setPackageCode(packageCode);
        packageFrameViewDTO1.setPhoneNumber(phoneNumber);
        packageFrameViewDTO1.setPackfrCls(packfrCls);
        packageFrameViewDTO1.setPackfrStartDateTime(startDateTime1);
        packageFrameViewDTO1.setPackfrEndDateTime(endDateTime1);
        LocalDateTime startDateTime2 = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime2 = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        PackageFrameViewDTO packageFrameViewDTO2 = new PackageFrameViewDTO();
        packageFrameViewDTO2.setPackageCode(packageCode);
        packageFrameViewDTO2.setPhoneNumber(phoneNumber);
        packageFrameViewDTO2.setPackfrCls(packfrCls);
        packageFrameViewDTO2.setPackfrStartDateTime(startDateTime2);
        packageFrameViewDTO2.setPackfrEndDateTime(endDateTime2);
        List<PackageFrameViewDTO> packageFrameViewDTOList = new ArrayList<>();
        packageFrameViewDTOList.add(packageFrameViewDTO1);
        packageFrameViewDTOList.add(packageFrameViewDTO2);

        when(framesSDRService.findPackageFramesByPhoneNumberStartTime(phoneNumber, startDateTime1)).thenReturn(packageFrameViewDTOList);

        mockMvc.perform(get("/api/packageFrame/getPackageFramesByPhoneNumberStartTime/{phoneNumber}", phoneNumber).param("packfrStartDateTime", startDateTime1.toString()))
                .andExpect(jsonPath("$.data.size()").value(packageFrameViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetPackageFramesByPackageCodeStartTimeEndTime() throws Exception {
        String phoneNumber = "0747634418";
        String packageCode = "01";
        String packfrCls = "200";
        LocalDateTime startDateTime1 = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime1 = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        PackageFrameViewDTO packageFrameViewDTO1 = new PackageFrameViewDTO();
        packageFrameViewDTO1.setPackageCode(packageCode);
        packageFrameViewDTO1.setPhoneNumber(phoneNumber);
        packageFrameViewDTO1.setPackfrCls(packfrCls);
        packageFrameViewDTO1.setPackfrStartDateTime(startDateTime1);
        packageFrameViewDTO1.setPackfrEndDateTime(endDateTime1);
        LocalDateTime startDateTime2 = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime2 = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        PackageFrameViewDTO packageFrameViewDTO2 = new PackageFrameViewDTO();
        packageFrameViewDTO2.setPackageCode(packageCode);
        packageFrameViewDTO2.setPhoneNumber(phoneNumber);
        packageFrameViewDTO2.setPackfrCls(packfrCls);
        packageFrameViewDTO2.setPackfrStartDateTime(startDateTime2);
        packageFrameViewDTO2.setPackfrEndDateTime(endDateTime2);
        List<PackageFrameViewDTO> packageFrameViewDTOList = new ArrayList<>();
        packageFrameViewDTOList.add(packageFrameViewDTO1);
        packageFrameViewDTOList.add(packageFrameViewDTO2);

        when(framesSDRService.findPackageFramesByPackageCodeStartTimeEndTime(packageCode, startDateTime1, endDateTime2)).thenReturn(packageFrameViewDTOList);

        mockMvc.perform(get("/api/packageFrame/getPackageFramesByPackageCodeStartTimeEndTime/{packageCode}", packageCode).param("packfrStartDateTime", startDateTime1.toString()).param("packfrEndDateTime", endDateTime2.toString()))
                .andExpect(jsonPath("$.data.size()").value(packageFrameViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetPackageFramesByPackageCodeStartTime() throws Exception {
        String phoneNumber = "0747634418";
        String packageCode = "01";
        String packfrCls = "200";
        LocalDateTime startDateTime1 = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime1 = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        PackageFrameViewDTO packageFrameViewDTO1 = new PackageFrameViewDTO();
        packageFrameViewDTO1.setPackageCode(packageCode);
        packageFrameViewDTO1.setPhoneNumber(phoneNumber);
        packageFrameViewDTO1.setPackfrCls(packfrCls);
        packageFrameViewDTO1.setPackfrStartDateTime(startDateTime1);
        packageFrameViewDTO1.setPackfrEndDateTime(endDateTime1);
        LocalDateTime startDateTime2 = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime2 = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        PackageFrameViewDTO packageFrameViewDTO2 = new PackageFrameViewDTO();
        packageFrameViewDTO2.setPackageCode(packageCode);
        packageFrameViewDTO2.setPhoneNumber(phoneNumber);
        packageFrameViewDTO2.setPackfrCls(packfrCls);
        packageFrameViewDTO2.setPackfrStartDateTime(startDateTime2);
        packageFrameViewDTO2.setPackfrEndDateTime(endDateTime2);
        List<PackageFrameViewDTO> packageFrameViewDTOList = new ArrayList<>();
        packageFrameViewDTOList.add(packageFrameViewDTO1);
        packageFrameViewDTOList.add(packageFrameViewDTO2);

        when(framesSDRService.findPackageFramesByPackageCodeStartTime(packageCode, startDateTime1)).thenReturn(packageFrameViewDTOList);

        mockMvc.perform(get("/api/packageFrame/getPackageFramesByPackageCodeStartTime/{packageCode}", packageCode).param("packfrStartDateTime", startDateTime1.toString()))
                .andExpect(jsonPath("$.data.size()").value(packageFrameViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testChangePackageFrameStatus() throws Exception {
        Long packfrId = 1L;
        String message = "The package frame status is changed.";

        doNothing().when(framesSDRService).changePackageFrameStatus(packfrId);

        mockMvc.perform(patch("/api/packageFrame/changePackageFrameStatus/{packfrId}", packfrId)
                        .with(csrf()))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testDeletePackageFrame() throws Exception {
        Long packfrId = 1L;
        String message = "The package frame '" +packfrId + "' is deleted!";

        doNothing().when(framesSDRService).deletePackageFrame(packfrId);

        mockMvc.perform(delete("/api/packageFrame/deletePackageFrame/{packfrId}", packfrId)
                        .with(csrf()))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testSaveAddonFrame() throws Exception {
        String phoneNumber = "0747634418";
        String addonCode = "ADDCLS";
        String message = "The new addon frame is saved.";
        AddonFrameSaveDTO addonFrameSaveDTO = new AddonFrameSaveDTO();
        addonFrameSaveDTO.setPhoneNumber(phoneNumber);
        addonFrameSaveDTO.setAddonCode(addonCode);

        doNothing().when(framesSDRService).saveNewAddonFrame(addonFrameSaveDTO);

        mockMvc.perform(post("/api/addon/saveNewAddonFrame")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addonFrameSaveDTO)))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAddonFrameById() throws Exception {
        Long addfrId = 1L;
        String phoneNumber = "0747634418";
        String addonCode = "ADDCLS";
        String addfrCls = "200";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 22, 0, 0, 0, 0);
        String startDateTimeStr = startDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        String endDateTimeStr = endDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        AddonFrameViewDTO addonFrameViewDTO = new AddonFrameViewDTO();
        addonFrameViewDTO.setAddfrId(addfrId);
        addonFrameViewDTO.setPhoneNumber(phoneNumber);
        addonFrameViewDTO.setAddonCode(addonCode);
        addonFrameViewDTO.setAddfrCls(addfrCls);
        addonFrameViewDTO.setAddfrStartDateTime(startDateTime);
        addonFrameViewDTO.setAddfrEndDateTime(endDateTime);

        when(framesSDRService.findAddonFrameById(addfrId)).thenReturn(addonFrameViewDTO);

        mockMvc.perform(get("/api/addonFrame/getAddonFrameById/{addfrId}", addfrId))
                .andExpect(jsonPath("$.data.addfrId").value(addfrId))
                .andExpect(jsonPath("$.data.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.data.addonCode").value(addonCode))
                .andExpect(jsonPath("$.data.addfrCls").value(addfrCls))
                .andExpect(jsonPath("$.data.addfrStartDateTime").value(startDateTimeStr))
                .andExpect(jsonPath("$.data.addfrEndDateTime").value(endDateTimeStr))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAddonFramesByPhoneNumberStartTimeEndTime() throws Exception {
        String phoneNumber = "0747634418";
        String addonCode = "ADDCLS";
        String addfrCls = "200";
        LocalDateTime startDateTime1 = LocalDateTime.of(2023, 1, 22, 0, 0, 0, 0);
        LocalDateTime endDateTime1 = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        AddonFrameViewDTO addonFrameViewDTO1 = new AddonFrameViewDTO();
        addonFrameViewDTO1.setPhoneNumber(phoneNumber);
        addonFrameViewDTO1.setAddonCode(addonCode);
        addonFrameViewDTO1.setAddfrCls(addfrCls);
        addonFrameViewDTO1.setAddfrStartDateTime(startDateTime1);
        addonFrameViewDTO1.setAddfrEndDateTime(endDateTime1);
        LocalDateTime startDateTime2 = LocalDateTime.of(2023, 2, 22, 0, 0, 0, 0);
        LocalDateTime endDateTime2 = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        AddonFrameViewDTO addonFrameViewDTO2 = new AddonFrameViewDTO();
        addonFrameViewDTO2.setPhoneNumber(phoneNumber);
        addonFrameViewDTO2.setAddonCode(addonCode);
        addonFrameViewDTO2.setAddfrCls(addfrCls);
        addonFrameViewDTO2.setAddfrStartDateTime(startDateTime2);
        addonFrameViewDTO2.setAddfrEndDateTime(endDateTime2);
        List<AddonFrameViewDTO> addonFrameViewDTOList = new ArrayList<>();
        addonFrameViewDTOList.add(addonFrameViewDTO1);
        addonFrameViewDTOList.add(addonFrameViewDTO2);

        when(framesSDRService.findAddonFramesByPhoneNumberStartTimeEndTime(phoneNumber, startDateTime1, endDateTime2)).thenReturn(addonFrameViewDTOList);

        mockMvc.perform(get("/api/addonFrame/getAddonFramesByPhoneNumberStartTimeEndTime/{phoneNumber}", phoneNumber).param("addfrStartDateTime", startDateTime1.toString()).param("addfrEndDateTime", endDateTime2.toString()))
                .andExpect(jsonPath("$.data.size()").value(addonFrameViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAddonFramesByPhoneNumberStartTime() throws Exception {
        String phoneNumber = "0747634418";
        String addonCode = "ADDCLS";
        String addfrCls = "200";
        LocalDateTime startDateTime1 = LocalDateTime.of(2023, 1, 22, 0, 0, 0, 0);
        LocalDateTime endDateTime1 = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        AddonFrameViewDTO addonFrameViewDTO1 = new AddonFrameViewDTO();
        addonFrameViewDTO1.setPhoneNumber(phoneNumber);
        addonFrameViewDTO1.setAddonCode(addonCode);
        addonFrameViewDTO1.setAddfrCls(addfrCls);
        addonFrameViewDTO1.setAddfrStartDateTime(startDateTime1);
        addonFrameViewDTO1.setAddfrEndDateTime(endDateTime1);
        LocalDateTime startDateTime2 = LocalDateTime.of(2023, 2, 22, 0, 0, 0, 0);
        LocalDateTime endDateTime2 = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        AddonFrameViewDTO addonFrameViewDTO2 = new AddonFrameViewDTO();
        addonFrameViewDTO2.setPhoneNumber(phoneNumber);
        addonFrameViewDTO2.setAddonCode(addonCode);
        addonFrameViewDTO2.setAddfrCls(addfrCls);
        addonFrameViewDTO2.setAddfrStartDateTime(startDateTime2);
        addonFrameViewDTO2.setAddfrEndDateTime(endDateTime2);
        List<AddonFrameViewDTO> addonFrameViewDTOList = new ArrayList<>();
        addonFrameViewDTOList.add(addonFrameViewDTO1);
        addonFrameViewDTOList.add(addonFrameViewDTO2);

        when(framesSDRService.findAddonFramesByPhoneNumberStartTime(phoneNumber, startDateTime1)).thenReturn(addonFrameViewDTOList);

        mockMvc.perform(get("/api/addonFrame/getAddonFramesByPhoneNumberStartTime/{phoneNumber}", phoneNumber).param("addfrStartDateTime", startDateTime1.toString()))
                .andExpect(jsonPath("$.data.size()").value(addonFrameViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAddonFramesByAddonCodeStartTimeEndTime() throws Exception {
        String phoneNumber = "0747634418";
        String addonCode = "ADDCLS";
        String addfrCls = "200";
        LocalDateTime startDateTime1 = LocalDateTime.of(2023, 1, 22, 0, 0, 0, 0);
        LocalDateTime endDateTime1 = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        AddonFrameViewDTO addonFrameViewDTO1 = new AddonFrameViewDTO();
        addonFrameViewDTO1.setPhoneNumber(phoneNumber);
        addonFrameViewDTO1.setAddonCode(addonCode);
        addonFrameViewDTO1.setAddfrCls(addfrCls);
        addonFrameViewDTO1.setAddfrStartDateTime(startDateTime1);
        addonFrameViewDTO1.setAddfrEndDateTime(endDateTime1);
        LocalDateTime startDateTime2 = LocalDateTime.of(2023, 2, 22, 0, 0, 0, 0);
        LocalDateTime endDateTime2 = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        AddonFrameViewDTO addonFrameViewDTO2 = new AddonFrameViewDTO();
        addonFrameViewDTO2.setPhoneNumber(phoneNumber);
        addonFrameViewDTO2.setAddonCode(addonCode);
        addonFrameViewDTO2.setAddfrCls(addfrCls);
        addonFrameViewDTO2.setAddfrStartDateTime(startDateTime2);
        addonFrameViewDTO2.setAddfrEndDateTime(endDateTime2);
        List<AddonFrameViewDTO> addonFrameViewDTOList = new ArrayList<>();
        addonFrameViewDTOList.add(addonFrameViewDTO1);
        addonFrameViewDTOList.add(addonFrameViewDTO2);

        when(framesSDRService.findAddonFramesByAddonCodeStartTimeEndTime(addonCode, startDateTime1, endDateTime2)).thenReturn(addonFrameViewDTOList);

        mockMvc.perform(get("/api/addonFrame/getAddonFramesByAddonCodeStartTimeEndTime/{addonCode}", addonCode).param("addfrStartDateTime", startDateTime1.toString()).param("addfrEndDateTime", endDateTime2.toString()))
                .andExpect(jsonPath("$.data.size()").value(addonFrameViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAddonFramesByAddonCodeStartTime() throws Exception {
        String phoneNumber = "0747634418";
        String addonCode = "ADDCLS";
        String addfrCls = "200";
        LocalDateTime startDateTime1 = LocalDateTime.of(2023, 1, 22, 0, 0, 0, 0);
        LocalDateTime endDateTime1 = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        AddonFrameViewDTO addonFrameViewDTO1 = new AddonFrameViewDTO();
        addonFrameViewDTO1.setPhoneNumber(phoneNumber);
        addonFrameViewDTO1.setAddonCode(addonCode);
        addonFrameViewDTO1.setAddfrCls(addfrCls);
        addonFrameViewDTO1.setAddfrStartDateTime(startDateTime1);
        addonFrameViewDTO1.setAddfrEndDateTime(endDateTime1);
        LocalDateTime startDateTime2 = LocalDateTime.of(2023, 2, 22, 0, 0, 0, 0);
        LocalDateTime endDateTime2 = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);
        AddonFrameViewDTO addonFrameViewDTO2 = new AddonFrameViewDTO();
        addonFrameViewDTO2.setPhoneNumber(phoneNumber);
        addonFrameViewDTO2.setAddonCode(addonCode);
        addonFrameViewDTO2.setAddfrCls(addfrCls);
        addonFrameViewDTO2.setAddfrStartDateTime(startDateTime2);
        addonFrameViewDTO2.setAddfrEndDateTime(endDateTime2);
        List<AddonFrameViewDTO> addonFrameViewDTOList = new ArrayList<>();
        addonFrameViewDTOList.add(addonFrameViewDTO1);
        addonFrameViewDTOList.add(addonFrameViewDTO2);

        when(framesSDRService.findAddonFramesByAddonCodeStartTime(addonCode, startDateTime1)).thenReturn(addonFrameViewDTOList);

        mockMvc.perform(get("/api/addonFrame/getAddonFramesByAddonCodeStartTime/{addonCode}", addonCode).param("addfrStartDateTime", startDateTime1.toString()))
                .andExpect(jsonPath("$.data.size()").value(addonFrameViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testChangeAddonFrameStatus() throws Exception {
        Long addfrId = 1L;
        String message = "The addon frame status is changed.";

        doNothing().when(framesSDRService).changeAddonFrameStatus(addfrId);

        mockMvc.perform(patch("/api/addonFrame/changeAddonFrameStatus/{addfrId}", addfrId)
                        .with(csrf()))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testDeleteAddonFrame() throws Exception {
        Long addfrId = 1L;
        String message = "The addon frame '" +addfrId + "' is deleted!";

        doNothing().when(framesSDRService).deleteAddonFrame(addfrId);

        mockMvc.perform(delete("/api/addonFrame/deleteAddonFrame/{addfrId}", addfrId)
                        .with(csrf()))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    // when service is completed without interruption (no End of Service)
    @Test
    void testSaveServiceDetailRecord_notEOS() throws Exception {
        String phoneNumber = "0747634418";
        String serviceCode = "SDRINT";
        String calledNumber = "-";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 10, 12, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 1, 10, 13, 10, 0, 0);
        int msgAmount = 0;
        BigDecimal mbAmount = BigDecimal.valueOf(1000.00);
        ServiceDetailRecordSaveDTO serviceDetailRecordSaveDTO = new ServiceDetailRecordSaveDTO();
        serviceDetailRecordSaveDTO.setPhoneNumber(phoneNumber);
        serviceDetailRecordSaveDTO.setServiceCode(serviceCode);
        serviceDetailRecordSaveDTO.setCalledNumber(calledNumber);
        serviceDetailRecordSaveDTO.setMsgAmount(msgAmount);
        serviceDetailRecordSaveDTO.setMbAmount(mbAmount);
        serviceDetailRecordSaveDTO.setSdrStartDateTime(startDateTime);
        serviceDetailRecordSaveDTO.setSdrEndDateTime(endDateTime);
        String message = "Not EOS";
        String message1 = "The new Service Detail Record is saved.";

        when(framesSDRService.saveNewServiceDetailRecord(serviceDetailRecordSaveDTO)).thenReturn(message);

        mockMvc.perform(post("/api//sdr/saveNewServiceDetailRecord")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceDetailRecordSaveDTO)))
                .andExpect(jsonPath("$.data.message").value(message1))
                .andExpect(jsonPath("$.success").value(true));
    }

    //  one service interrupt example (SDRINT - Internet) of six possibilities for EOS
    @Test
    void testSaveServiceDetailRecord_EOS_forINT() throws Exception {
        String phoneNumber = "0747634418";
        String serviceCode = "SDRINT";
        String calledNumber = "-";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 10, 12, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 1, 10, 13, 10, 0, 0);
        int msgAmount = 0;
        BigDecimal mbAmount = BigDecimal.valueOf(1000.00);
        ServiceDetailRecordSaveDTO serviceDetailRecordSaveDTO = new ServiceDetailRecordSaveDTO();
        serviceDetailRecordSaveDTO.setPhoneNumber(phoneNumber);
        serviceDetailRecordSaveDTO.setServiceCode(serviceCode);
        serviceDetailRecordSaveDTO.setCalledNumber(calledNumber);
        serviceDetailRecordSaveDTO.setMsgAmount(msgAmount);
        serviceDetailRecordSaveDTO.setMbAmount(mbAmount);
        serviceDetailRecordSaveDTO.setSdrStartDateTime(startDateTime);
        serviceDetailRecordSaveDTO.setSdrEndDateTime(endDateTime);
        String message = "Internet service is interrupted by EOS";

        when(framesSDRService.saveNewServiceDetailRecord(serviceDetailRecordSaveDTO)).thenReturn(message);

        mockMvc.perform(post("/api//sdr/saveNewServiceDetailRecord")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceDetailRecordSaveDTO)))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetServiceDetailRecordById() throws Exception {
        Long sdrId = 1L;
        String phoneNumber = "0747634418";
        String serviceCode = "SDRINT";
        String calledNumber = "-";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 10, 12, 1, 0, 0);
        String startDateTimeStr = startDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 1, 10, 13, 10, 0, 0);
        String endDateTimeStr = endDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        int msgAmount = 0;
        BigDecimal mbAmount = BigDecimal.valueOf(1000.00);
        ServiceDetailRecordViewDTO serviceDetailRecordViewDTO = new ServiceDetailRecordViewDTO();
        serviceDetailRecordViewDTO.setSdrId(sdrId);
        serviceDetailRecordViewDTO.setPhoneNumber(phoneNumber);
        serviceDetailRecordViewDTO.setServiceCode(serviceCode);
        serviceDetailRecordViewDTO.setCalledNumber(calledNumber);
        serviceDetailRecordViewDTO.setMsgAmount(msgAmount);
        serviceDetailRecordViewDTO.setMbAmount(mbAmount);
        serviceDetailRecordViewDTO.setSdrStartDateTime(startDateTime);
        serviceDetailRecordViewDTO.setSdrEndDateTime(endDateTime);

        when(framesSDRService.findServiceDetailRecordById(sdrId)).thenReturn(serviceDetailRecordViewDTO);

        mockMvc.perform(get("/api/sdr/getServiceDetailRecordById/{sdrId}", sdrId))
                .andExpect(jsonPath("$.data.sdrId").value(sdrId))
                .andExpect(jsonPath("$.data.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.data.serviceCode").value(serviceCode))
                .andExpect(jsonPath("$.data.calledNumber").value(calledNumber))
                .andExpect(jsonPath("$.data.msgAmount").value(msgAmount))
                .andExpect(jsonPath("$.data.mbAmount").value(mbAmount))
                .andExpect(jsonPath("$.data.sdrStartDateTime").value(startDateTimeStr))
                .andExpect(jsonPath("$.data.sdrEndDateTime").value(endDateTimeStr))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetServiceDetailRecordsByPhoneNumberStartTimeEndTime() throws Exception {
        String phoneNumber = "0747634418";
        String serviceCode1 = "SDRINT";
        String calledNumber1 = "-";
        LocalDateTime startDateTime1 = LocalDateTime.of(2023, 1, 10, 12, 1, 0, 0);
        LocalDateTime endDateTime1 = LocalDateTime.of(2023, 1, 10, 13, 10, 0, 0);
        int msgAmount1 = 0;
        BigDecimal mbAmount1 = BigDecimal.valueOf(1000.00);
        ServiceDetailRecordViewDTO serviceDetailRecordViewDTO1 = new ServiceDetailRecordViewDTO();
        serviceDetailRecordViewDTO1.setPhoneNumber(phoneNumber);
        serviceDetailRecordViewDTO1.setServiceCode(serviceCode1);
        serviceDetailRecordViewDTO1.setCalledNumber(calledNumber1);
        serviceDetailRecordViewDTO1.setMsgAmount(msgAmount1);
        serviceDetailRecordViewDTO1.setMbAmount(mbAmount1);
        serviceDetailRecordViewDTO1.setSdrStartDateTime(startDateTime1);
        serviceDetailRecordViewDTO1.setSdrEndDateTime(endDateTime1);
        String serviceCode2 = "SDRCLS";
        String calledNumber2 = "0749934554";
        LocalDateTime startDateTime2 = LocalDateTime.of(2023, 1, 11, 12, 1, 0, 0);
        LocalDateTime endDateTime2 = LocalDateTime.of(2023, 1, 11, 12, 10, 0, 0);
        int msgAmount2 = 0;
        BigDecimal mbAmount2 = BigDecimal.valueOf(0.00);
        ServiceDetailRecordViewDTO serviceDetailRecordViewDTO2 = new ServiceDetailRecordViewDTO();
        serviceDetailRecordViewDTO2.setPhoneNumber(phoneNumber);
        serviceDetailRecordViewDTO2.setServiceCode(serviceCode2);
        serviceDetailRecordViewDTO2.setCalledNumber(calledNumber2);
        serviceDetailRecordViewDTO2.setMsgAmount(msgAmount2);
        serviceDetailRecordViewDTO2.setMbAmount(mbAmount2);
        serviceDetailRecordViewDTO2.setSdrStartDateTime(startDateTime2);
        serviceDetailRecordViewDTO2.setSdrEndDateTime(endDateTime2);
        List<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOList = new ArrayList<>();
        serviceDetailRecordViewDTOList.add(serviceDetailRecordViewDTO1);
        serviceDetailRecordViewDTOList.add(serviceDetailRecordViewDTO2);
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 9, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 1, 12, 0, 0, 0, 0);

        when(framesSDRService.findServiceDetailRecordsByPhoneNumberStartTimeEndTime(phoneNumber, startDateTime, endDateTime)).thenReturn(serviceDetailRecordViewDTOList);

        mockMvc.perform(get("/api/sdr/getServiceDetailRecordsByPhoneNumberStartTimeEndTime/{phoneNumber}", phoneNumber).param("sdrStartDateTime", startDateTime.toString()).param("sdrEndDateTime", endDateTime.toString()))
                .andExpect(jsonPath("$.data.size()").value(serviceDetailRecordViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetServiceDetailRecordsByPhoneNumberStartTime() throws Exception {
        String phoneNumber = "0747634418";
        String serviceCode1 = "SDRINT";
        String calledNumber1 = "-";
        LocalDateTime startDateTime1 = LocalDateTime.of(2023, 1, 10, 12, 1, 0, 0);
        LocalDateTime endDateTime1 = LocalDateTime.of(2023, 1, 10, 13, 10, 0, 0);
        int msgAmount1 = 0;
        BigDecimal mbAmount1 = BigDecimal.valueOf(1000.00);
        ServiceDetailRecordViewDTO serviceDetailRecordViewDTO1 = new ServiceDetailRecordViewDTO();
        serviceDetailRecordViewDTO1.setPhoneNumber(phoneNumber);
        serviceDetailRecordViewDTO1.setServiceCode(serviceCode1);
        serviceDetailRecordViewDTO1.setCalledNumber(calledNumber1);
        serviceDetailRecordViewDTO1.setMsgAmount(msgAmount1);
        serviceDetailRecordViewDTO1.setMbAmount(mbAmount1);
        serviceDetailRecordViewDTO1.setSdrStartDateTime(startDateTime1);
        serviceDetailRecordViewDTO1.setSdrEndDateTime(endDateTime1);
        String serviceCode2 = "SDRCLS";
        String calledNumber2 = "0749934554";
        LocalDateTime startDateTime2 = LocalDateTime.of(2023, 1, 11, 12, 1, 0, 0);
        LocalDateTime endDateTime2 = LocalDateTime.of(2023, 1, 11, 12, 10, 0, 0);
        int msgAmount2 = 0;
        BigDecimal mbAmount2 = BigDecimal.valueOf(0.00);
        ServiceDetailRecordViewDTO serviceDetailRecordViewDTO2 = new ServiceDetailRecordViewDTO();
        serviceDetailRecordViewDTO2.setPhoneNumber(phoneNumber);
        serviceDetailRecordViewDTO2.setServiceCode(serviceCode2);
        serviceDetailRecordViewDTO2.setCalledNumber(calledNumber2);
        serviceDetailRecordViewDTO2.setMsgAmount(msgAmount2);
        serviceDetailRecordViewDTO2.setMbAmount(mbAmount2);
        serviceDetailRecordViewDTO2.setSdrStartDateTime(startDateTime2);
        serviceDetailRecordViewDTO2.setSdrEndDateTime(endDateTime2);
        List<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOList = new ArrayList<>();
        serviceDetailRecordViewDTOList.add(serviceDetailRecordViewDTO1);
        serviceDetailRecordViewDTOList.add(serviceDetailRecordViewDTO2);
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 9, 0, 0, 0, 0);

        when(framesSDRService.findServiceDetailRecordsByPhoneNumberStartTime(phoneNumber, startDateTime)).thenReturn(serviceDetailRecordViewDTOList);

        mockMvc.perform(get("/api/sdr/getServiceDetailRecordsByPhoneNumberStartTime/{phoneNumber}", phoneNumber).param("sdrStartDateTime", startDateTime.toString()))
                .andExpect(jsonPath("$.data.size()").value(serviceDetailRecordViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetServiceDetailRecordsByPhoneNumberSdrCodeStartTimeEndTime() throws Exception {
        String phoneNumber = "0747634418";
        String serviceCode = "SDRINT";
        String calledNumber = "-";
        LocalDateTime startDateTime1 = LocalDateTime.of(2023, 1, 10, 12, 1, 0, 0);
        LocalDateTime endDateTime1 = LocalDateTime.of(2023, 1, 10, 13, 10, 0, 0);
        int msgAmount = 0;
        BigDecimal mbAmount1 = BigDecimal.valueOf(1000.00);
        ServiceDetailRecordViewDTO serviceDetailRecordViewDTO1 = new ServiceDetailRecordViewDTO();
        serviceDetailRecordViewDTO1.setPhoneNumber(phoneNumber);
        serviceDetailRecordViewDTO1.setServiceCode(serviceCode);
        serviceDetailRecordViewDTO1.setCalledNumber(calledNumber);
        serviceDetailRecordViewDTO1.setMsgAmount(msgAmount);
        serviceDetailRecordViewDTO1.setMbAmount(mbAmount1);
        serviceDetailRecordViewDTO1.setSdrStartDateTime(startDateTime1);
        serviceDetailRecordViewDTO1.setSdrEndDateTime(endDateTime1);
        LocalDateTime startDateTime2 = LocalDateTime.of(2023, 1, 11, 12, 1, 0, 0);
        LocalDateTime endDateTime2 = LocalDateTime.of(2023, 1, 11, 14, 10, 0, 0);
        BigDecimal mbAmount2 = BigDecimal.valueOf(2000.00);
        ServiceDetailRecordViewDTO serviceDetailRecordViewDTO2 = new ServiceDetailRecordViewDTO();
        serviceDetailRecordViewDTO2.setPhoneNumber(phoneNumber);
        serviceDetailRecordViewDTO2.setServiceCode(serviceCode);
        serviceDetailRecordViewDTO2.setCalledNumber(calledNumber);
        serviceDetailRecordViewDTO2.setMsgAmount(msgAmount);
        serviceDetailRecordViewDTO2.setMbAmount(mbAmount2);
        serviceDetailRecordViewDTO2.setSdrStartDateTime(startDateTime2);
        serviceDetailRecordViewDTO2.setSdrEndDateTime(endDateTime2);
        List<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOList = new ArrayList<>();
        serviceDetailRecordViewDTOList.add(serviceDetailRecordViewDTO1);
        serviceDetailRecordViewDTOList.add(serviceDetailRecordViewDTO2);
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 9, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 1, 12, 0, 0, 0, 0);

        when(framesSDRService.findServiceDetailRecordsByPhoneNumberStartTimeEndTimeServiceCode(phoneNumber, startDateTime, endDateTime, serviceCode)).thenReturn(serviceDetailRecordViewDTOList);

        mockMvc.perform(get("/api/sdr/getServiceDetailRecordsByPhoneNumberSdrCodeStartTimeEndTime/{phoneNumber}", phoneNumber).param("sdrCode", serviceCode).param("sdrStartDateTime", startDateTime.toString()).param("sdrEndDateTime", endDateTime.toString()))
                .andExpect(jsonPath("$.data.size()").value(serviceDetailRecordViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetServiceDetailRecordsByPhoneNumberSdrCodeStartTime() throws Exception {
        String phoneNumber = "0747634418";
        String serviceCode = "SDRINT";
        String calledNumber = "-";
        LocalDateTime startDateTime1 = LocalDateTime.of(2023, 1, 10, 12, 1, 0, 0);
        LocalDateTime endDateTime1 = LocalDateTime.of(2023, 1, 10, 13, 10, 0, 0);
        int msgAmount = 0;
        BigDecimal mbAmount1 = BigDecimal.valueOf(1000.00);
        ServiceDetailRecordViewDTO serviceDetailRecordViewDTO1 = new ServiceDetailRecordViewDTO();
        serviceDetailRecordViewDTO1.setPhoneNumber(phoneNumber);
        serviceDetailRecordViewDTO1.setServiceCode(serviceCode);
        serviceDetailRecordViewDTO1.setCalledNumber(calledNumber);
        serviceDetailRecordViewDTO1.setMsgAmount(msgAmount);
        serviceDetailRecordViewDTO1.setMbAmount(mbAmount1);
        serviceDetailRecordViewDTO1.setSdrStartDateTime(startDateTime1);
        serviceDetailRecordViewDTO1.setSdrEndDateTime(endDateTime1);
        LocalDateTime startDateTime2 = LocalDateTime.of(2023, 1, 11, 12, 1, 0, 0);
        LocalDateTime endDateTime2 = LocalDateTime.of(2023, 1, 11, 14, 10, 0, 0);
        BigDecimal mbAmount2 = BigDecimal.valueOf(2000.00);
        ServiceDetailRecordViewDTO serviceDetailRecordViewDTO2 = new ServiceDetailRecordViewDTO();
        serviceDetailRecordViewDTO2.setPhoneNumber(phoneNumber);
        serviceDetailRecordViewDTO2.setServiceCode(serviceCode);
        serviceDetailRecordViewDTO2.setCalledNumber(calledNumber);
        serviceDetailRecordViewDTO2.setMsgAmount(msgAmount);
        serviceDetailRecordViewDTO2.setMbAmount(mbAmount2);
        serviceDetailRecordViewDTO2.setSdrStartDateTime(startDateTime2);
        serviceDetailRecordViewDTO2.setSdrEndDateTime(endDateTime2);
        List<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOList = new ArrayList<>();
        serviceDetailRecordViewDTOList.add(serviceDetailRecordViewDTO1);
        serviceDetailRecordViewDTOList.add(serviceDetailRecordViewDTO2);
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 9, 0, 0, 0, 0);

        when(framesSDRService.findServiceDetailRecordsByPhoneNumberStartTimeServiceCode(phoneNumber, startDateTime, serviceCode)).thenReturn(serviceDetailRecordViewDTOList);

        mockMvc.perform(get("/api/sdr/getServiceDetailRecordsByPhoneNumberSdrCodeStartTime/{phoneNumber}", phoneNumber).param("sdrCode", serviceCode).param("sdrStartDateTime", startDateTime.toString()))
                .andExpect(jsonPath("$.data.size()").value(serviceDetailRecordViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testDeleteServiceDetailRecord() throws Exception {
        Long sdrId = 1L;
        String message = "The Service Detail Record '" +sdrId + "' is deleted!";

        doNothing().when(framesSDRService).deleteServiceDetailRecord(sdrId);

        mockMvc.perform(delete("/api/sdr/deleteServiceDetailRecord/{sdrId}", sdrId)
                        .with(csrf()))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }
}