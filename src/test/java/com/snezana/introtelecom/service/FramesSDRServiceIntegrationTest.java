package com.snezana.introtelecom.service;


import com.snezana.introtelecom.dto.AddonFrameSaveDTO;
import com.snezana.introtelecom.dto.PackageFrameViewDTO;
import com.snezana.introtelecom.entity.AddonFrame;
import com.snezana.introtelecom.repository.AddonFrameRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class FramesSDRServiceIntegrationTest {

    @Autowired
    private FramesSDRService framesSDRService;

    @Autowired
    private AddonFrameRepo addonFrameRepo;


    @Test
    void testFindPackageFrameById() {
        Long id = 1L;
        String phoneNumber = "0747634418";
        PackageFrameViewDTO packageFrameViewDTO = framesSDRService.findPackageFrameById(id);
        assertThat(packageFrameViewDTO).isNotNull();
        assertThat(packageFrameViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
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
    @Sql(scripts = {"/create_addon_frame.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testDeleteAddonFrame(){
        Long maxId = addonFrameRepo.maxAddfr_id();
        int sizeBefore = addonFrameRepo.findAll().size();
        framesSDRService.deleteAddonFrame(maxId);
        assertThat(addonFrameRepo.findAll().size()).isEqualTo(sizeBefore - 1);
        assertThat(addonFrameRepo.findAll().stream().filter(addonFrame -> Objects.equals(addonFrame.getAddfrId(), maxId)).count()).isEqualTo(0);
    }



}
