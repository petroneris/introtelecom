package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.PhoneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PhoneServiceRepoIntegrationTest {

    @Autowired
    private PhoneServiceRepo phoneServiceRepo;

    @Test
    void findPhoneService_byServiceCode_returnPhoneServiceIfExists(){
        String serviceCode = "SDRSMS";
        String serviceDescription = "record for one sms";
        PhoneService found = phoneServiceRepo.findByServiceCode(serviceCode);
        assertThat(found).hasFieldOrPropertyWithValue("serviceCode", serviceCode);
        assertThat(found).hasFieldOrPropertyWithValue("serviceDescription", serviceDescription);
        assertThat(found).extracting("serviceCode").isEqualTo(serviceCode);
        assertThat(found).extracting("serviceDescription").isEqualTo(serviceDescription);
    }

    @Test
    void findPhoneService_byServiceCode_expectedNull_ifThisPhoneServiceDoesNotExist(){
        String serviceCode = "UNKNOWN";
        PhoneService found = phoneServiceRepo.findByServiceCode(serviceCode);
        assertThat(found).isNull();
    }

    @Test
    void findPhoneServiceOpt_byServiceCode_whenIsNotEmpty(){
        String serviceCode = "SDRSMS";
        String serviceDescription = "record for one sms";
        Optional<PhoneService> found = phoneServiceRepo.findByServiceCodeOpt(serviceCode);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(PhoneService.class)
                .hasValueSatisfying(packagePlan-> {
                    assertThat(packagePlan.getServiceCode()).isEqualTo(serviceCode);
                    assertThat(packagePlan.getServiceDescription()).isEqualTo(serviceDescription);
                });
    }

    @Test
    void findPhoneServiceOpt_byServiceCode_whenIsEmpty(){
        String serviceCode = "UNKNOWN";
        Optional<PhoneService> found = phoneServiceRepo.findByServiceCodeOpt(serviceCode);
        assertThat(found).isEmpty();
    }



}
