package com.snezana.introtelecom.controller;

import com.snezana.introtelecom.dto.AddOnDTO;
import com.snezana.introtelecom.dto.PackagePlanDTO;
import com.snezana.introtelecom.dto.PhoneServiceDTO;
import com.snezana.introtelecom.service.PackageAddonPhoneServService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PackagesAddOnsPhoneServicesController.class)
@WithMockUser(username ="mika", roles="ADMIN")
class PackagesAddOnsPhoneServicesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PackageAddonPhoneServService packageAddonPhoneServService;

    @Test
    void testGetPackageByPackageCode() throws Exception {
        String packageCode = "01";
        String packageName = "prepaid1";
        String packageDescription = "description01";
        String packagePrice = "200.00";
        PackagePlanDTO packagePlanDTO = new PackagePlanDTO();
        packagePlanDTO.setPackageCode(packageCode);
        packagePlanDTO.setPackageName(packageName);
        packagePlanDTO.setPackageDescription(packageDescription);
        packagePlanDTO.setPackagePrice(packagePrice);

        when(packageAddonPhoneServService.getPackagePlanByPackageCode(packageCode)).thenReturn(packagePlanDTO);

        mockMvc.perform(get("/api/package/getPackagePlanByPackageCode/{packageCode}", packageCode))
                .andExpect(jsonPath("$.data.packageCode").value(packageCode))
                .andExpect(jsonPath("$.data.packageName").value(packageName))
                .andExpect(jsonPath("$.data.packageDescription").value(packageDescription))
                .andExpect(jsonPath("$.data.packagePrice").value(packagePrice))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAllPackages() throws Exception {
        PackagePlanDTO packagePlanDTO1 = new PackagePlanDTO();
        packagePlanDTO1.setPackageCode("01");
        packagePlanDTO1.setPackageName("prepaid1");
        packagePlanDTO1.setPackageDescription("description01");
        packagePlanDTO1.setPackagePrice("200.00");
        PackagePlanDTO packagePlanDTO2 = new PackagePlanDTO();
        packagePlanDTO2.setPackageCode("02");
        packagePlanDTO2.setPackageName("prepaid2");
        packagePlanDTO2.setPackageDescription("description02");
        packagePlanDTO2.setPackagePrice("300.00");
        List<PackagePlanDTO> packagePlanDTOList = new ArrayList<>();
        packagePlanDTOList.add(packagePlanDTO1);
        packagePlanDTOList.add(packagePlanDTO2);

        when(packageAddonPhoneServService.getAllPackagePlans()).thenReturn(packagePlanDTOList);

        mockMvc.perform(get("/api/package/getAllPackagePlans"))
                .andExpect(jsonPath("$.data.size()").value(packagePlanDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testChangePackagePrice() throws Exception {
        String packageCode = "01";
        BigDecimal newPackagePrice = BigDecimal.valueOf(300.00);//
        String message = "The package price of package code= " +packageCode + " is changed.";

        doNothing().when(packageAddonPhoneServService).changePackagePrice(packageCode, newPackagePrice);

        mockMvc.perform(patch("/api/package/changePackagePrice/{packageCode}", packageCode).param("packagePrice", newPackagePrice.toString())
                .with(csrf()))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAddOnByAddOnCode() throws Exception {
        String addonCode = "ADDCLS";
        String addonDescription = "description01";
        String addonPrice = "200.00";
        AddOnDTO addOnDTO = new AddOnDTO();
        addOnDTO.setAddonCode(addonCode);
        addOnDTO.setAddonDescription(addonDescription);
        addOnDTO.setAddonPrice(addonPrice);

        when(packageAddonPhoneServService.getAddOnByAddOnCode(addonCode)).thenReturn(addOnDTO);

        mockMvc.perform(get("/api/addon/getAddOnByAddOnCode/{addonCode}", addonCode))
                .andExpect(jsonPath("$.data.addonCode").value(addonCode))
                .andExpect(jsonPath("$.data.addonDescription").value(addonDescription))
                .andExpect(jsonPath("$.data.addonPrice").value(addonPrice))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAllAddOns() throws Exception {
        String addonCode1 = "ADDCLS";
        String addonDescription1 = "description01";
        String addonPrice1 = "200.00";
        AddOnDTO addOnDTO1 = new AddOnDTO();
        addOnDTO1.setAddonCode(addonCode1);
        addOnDTO1.setAddonDescription(addonDescription1);
        addOnDTO1.setAddonPrice(addonPrice1);
        String addonCode2 = "ADDSMS";
        String addonDescription2 = "description02";
        String addonPrice2 = "300.00";
        AddOnDTO addOnDTO2 = new AddOnDTO();
        addOnDTO2.setAddonCode(addonCode2);
        addOnDTO2.setAddonDescription(addonDescription2);
        addOnDTO2.setAddonPrice(addonPrice2);
        List<AddOnDTO> addOnDTOList = new ArrayList<>();
        addOnDTOList.add(addOnDTO1);
        addOnDTOList.add(addOnDTO2);

        when(packageAddonPhoneServService.getAllAddOns()).thenReturn(addOnDTOList);

        mockMvc.perform(get("/api/addon/getAllAddOns"))
                .andExpect(jsonPath("$.data.size()").value(addOnDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testChangeAddOnPrice() throws Exception {
        String addonCode = "ADDSMS";
        BigDecimal newAddonPrice = BigDecimal.valueOf(300.00);//
        String message = "The addon price of addon code= " +addonCode + " is changed.";

        doNothing().when(packageAddonPhoneServService).changeAddOnPrice(addonCode, newAddonPrice);

        mockMvc.perform(patch("/api/addon/changeAddOnPrice/{addonCode}", addonCode).param("addonPrice", newAddonPrice.toString())
                        .with(csrf()))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetPhoneServiceByServiceCode() throws Exception {
        String serviceCode = "SDRCLS";
        String serviceDescription = "description01";
        String servicePrice = "200.00";
        PhoneServiceDTO phoneServiceDTO = new PhoneServiceDTO();
        phoneServiceDTO.setServiceCode(serviceCode);
        phoneServiceDTO.setServiceDescription(serviceDescription);
        phoneServiceDTO.setServicePrice(servicePrice);

        when(packageAddonPhoneServService.getPhoneServiceByServiceCode(serviceCode)).thenReturn(phoneServiceDTO);

        mockMvc.perform(get("/api/service/getPhoneServiceByServiceCode/{serviceCode}", serviceCode))
                .andExpect(jsonPath("$.data.serviceCode").value(serviceCode))
                .andExpect(jsonPath("$.data.serviceDescription").value(serviceDescription))
                .andExpect(jsonPath("$.data.servicePrice").value(servicePrice))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAllPhoneServices() throws Exception {
        String serviceCode1 = "SDRCLS";
        String serviceDescription1 = "description01";
        String servicePrice1 = "200.00";
        PhoneServiceDTO phoneServiceDTO1 = new PhoneServiceDTO();
        phoneServiceDTO1.setServiceCode(serviceCode1);
        phoneServiceDTO1.setServiceDescription(serviceDescription1);
        phoneServiceDTO1.setServicePrice(servicePrice1);
        String serviceCode2 = "SDRSMS";
        String serviceDescription2 = "description02";
        String servicePrice2 = "300.00";
        PhoneServiceDTO phoneServiceDTO2 = new PhoneServiceDTO();
        phoneServiceDTO2.setServiceCode(serviceCode2);
        phoneServiceDTO2.setServiceDescription(serviceDescription2);
        phoneServiceDTO2.setServicePrice(servicePrice2);
        List<PhoneServiceDTO> phoneServiceDTOList = new ArrayList<>();
        phoneServiceDTOList.add(phoneServiceDTO1);
        phoneServiceDTOList.add(phoneServiceDTO2);

        when(packageAddonPhoneServService.getAllPhoneServices()).thenReturn(phoneServiceDTOList);

        mockMvc.perform(get("/api/service/getAllPhoneServices"))
                .andExpect(jsonPath("$.data.size()").value(phoneServiceDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testChangeServicePrice() throws Exception {
        String serviceCode = "SDRCLS";
        BigDecimal newServicePrice = BigDecimal.valueOf(300.00);//
        String message = "The service price of service code= " +serviceCode + " is changed.";

        doNothing().when(packageAddonPhoneServService).changePhoneServicePrice(serviceCode, newServicePrice);

        mockMvc.perform(patch("/api/service/changeServicePrice/{serviceCode}", serviceCode).param("servicePrice", newServicePrice.toString())
                        .with(csrf()))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }
}