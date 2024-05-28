package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.PackagePlan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PackagePlanRepoIntegrationTest {

    @Autowired
    private PackagePlanRepo packagePlanRepo;

    @Test
    void findPackagePlan_byPackageCode_returnPackagePlanIfExists(){
        String packageCode = "01";
        String packageName = "prepaid1";
        PackagePlan found = packagePlanRepo.findByPackageCode(packageCode);
        assertThat(found).hasFieldOrPropertyWithValue("packageCode", packageCode);
        assertThat(found).hasFieldOrPropertyWithValue("packageName", packageName);
        assertThat(found).extracting("packageCode").isEqualTo(packageCode);
        assertThat(found).extracting("packageName").isEqualTo(packageName);
    }

    @Test
    void findPackagePlan_byPackageCode_expectedNull_ifPackageCodeDoesNotExist(){
        String packageCode = "UNKNOWN";
        PackagePlan found = packagePlanRepo.findByPackageCode(packageCode);
        assertThat(found).isNull();
    }

    @Test
    void findPackagePlanOpt_byPackageCode_whenIsNotEmpty(){
        String packageCode = "01";
        String packageName = "prepaid1";
        Optional<PackagePlan> found = packagePlanRepo.findByPackageCodeOpt(packageCode);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(PackagePlan.class)
                .hasValueSatisfying(packagePlan-> {
                    assertThat(packagePlan.getPackageCode()).isEqualTo(packageCode);
                    assertThat(packagePlan.getPackageName()).isEqualTo(packageName);
                });
    }

    @Test
    void findPackagePlanOpt_byPackageCode_whenIsEmpty(){
        String packageCode = "UNKNOWN";
        Optional<PackagePlan> found = packagePlanRepo.findByPackageCodeOpt(packageCode);
        assertThat(found).isEmpty();
    }

    @Test
    void findPackagePlanList_allCustomersPackagePlans(){
        int listSize = 6;
        String packageCode = "00";
        String packageName = "admin";
        List<PackagePlan> foundList = packagePlanRepo.findAllCustomersPackagePlans();
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(packagePlan ->!packagePlan.getPackageCode().equals(packageCode)
                        && !packagePlan.getPackageName().equals(packageName))
                .hasSize(listSize);
    }

}
