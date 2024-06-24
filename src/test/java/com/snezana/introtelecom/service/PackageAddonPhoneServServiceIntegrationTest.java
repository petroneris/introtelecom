package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.AddOnDTO;
import com.snezana.introtelecom.dto.PackagePlanDTO;
import com.snezana.introtelecom.dto.PhoneServiceDTO;
import com.snezana.introtelecom.entity.AddOn;
import com.snezana.introtelecom.entity.PackagePlan;
import com.snezana.introtelecom.entity.PhoneService;
import com.snezana.introtelecom.repository.AddOnRepo;
import com.snezana.introtelecom.repository.PackagePlanRepo;
import com.snezana.introtelecom.repository.PhoneServiceRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PackageAddonPhoneServServiceIntegrationTest {

    @Autowired
    private PackageAddonPhoneServService packageAddonPhoneServService;

    @Autowired
    private PackagePlanRepo packagePlanRepo;

    @Autowired
    private AddOnRepo addOnRepo;

    @Autowired
    private PhoneServiceRepo phoneServiceRepo;

    @Test
    void testGetPackagePlanByPackageCode(){
        String packageCode = "01";
        String packageName = "prepaid1";
        PackagePlanDTO packagePlanDTO  = packageAddonPhoneServService.getPackagePlanByPackageCode(packageCode);
        assertThat(packagePlanDTO).isNotNull();
        assertThat(packagePlanDTO.getPackageName()).isEqualTo(packageName);
        assertThat(packagePlanDTO.getPackageCode()).isEqualTo(packageCode);
    }

    @Test
    void testGetAllPackagePlans(){
        List<PackagePlanDTO> packagePlanDTOList = packageAddonPhoneServService.getAllPackagePlans();
        assertThat(packagePlanDTOList).isNotNull();
        assertThat(packagePlanDTOList.size()).isGreaterThan(0);
        assertThat(packagePlanDTOList.size()).isEqualTo(7);
    }

    @Test
    @Sql(scripts = {"/update_package_plan.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangePackagePrice() {
        String packageCode = "01";
        BigDecimal packagePrice = BigDecimal.valueOf(300.00);
        packagePrice = packagePrice.setScale( 2, RoundingMode.UP);
        packageAddonPhoneServService.changePackagePrice(packageCode, packagePrice);
        PackagePlan packagePlan = packagePlanRepo.findByPackageCode(packageCode);
        assertThat(packagePlan.getPackagePrice()).isEqualTo(packagePrice);
    }

    @Test
    void testGetAddOnByAddOnCode(){
        String addOnCode = "ADDCLS";
        String addOnDescription = "calls 100 min";
        AddOnDTO addOnDTO = packageAddonPhoneServService.getAddOnByAddOnCode(addOnCode);
        assertThat(addOnDTO).isNotNull();
        assertThat(addOnDTO.getAddonCode()).isEqualTo(addOnCode);
        assertThat(addOnDTO.getAddonDescription()).isEqualTo(addOnDescription);
    }

    @Test
    void testGetAllAddOns(){
        List<AddOnDTO> addOnDTOList = packageAddonPhoneServService.getAllAddOns();
        assertThat(addOnDTOList).isNotNull();
        assertThat(addOnDTOList.size()).isGreaterThan(0);
        assertThat(addOnDTOList.size()).isEqualTo(6);
    }

    @Test
    @Sql(scripts = {"/update_addon.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangeAddOnPrice(){
        String addonCode = "ADDCLS";
        BigDecimal addonPrice = BigDecimal.valueOf(150.00);
        addonPrice = addonPrice.setScale( 2, RoundingMode.UP);
        packageAddonPhoneServService.changeAddOnPrice(addonCode, addonPrice);
        AddOn addOn = addOnRepo.findByAddonCode(addonCode);
        assertThat(addOn.getAddonPrice()).isEqualTo(addonPrice);
    }

    @Test
    void testGetPhoneServiceByServiceCode(){
        String serviceCode = "SDRINT";
        PhoneServiceDTO phoneServiceDTO = packageAddonPhoneServService.getPhoneServiceByServiceCode(serviceCode);
        assertThat(phoneServiceDTO).isNotNull();
        assertThat(phoneServiceDTO.getServiceCode()).isEqualTo(serviceCode);
    }

    @Test
    void testGetAllPhoneServices(){
        List<PhoneServiceDTO> phoneServiceDTOList = packageAddonPhoneServService.getAllPhoneServices();
        assertThat(phoneServiceDTOList).isNotNull();
        assertThat(phoneServiceDTOList.size()).isGreaterThan(0);
        assertThat(phoneServiceDTOList.size()).isEqualTo(13);
    }

    @Test
    @Sql(scripts = {"/update_service.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangePhoneServicePrice(){
        String serviceCode = "SDRRMGSZ1";
        BigDecimal servicePrice = BigDecimal.valueOf(3.00);
        servicePrice = servicePrice.setScale( 2, RoundingMode.UP);
        packageAddonPhoneServService.changePhoneServicePrice(serviceCode, servicePrice);
        PhoneService phoneService = phoneServiceRepo.findByServiceCode(serviceCode);
        assertThat(phoneService.getServicePrice()).isEqualTo(servicePrice);
    }

}
