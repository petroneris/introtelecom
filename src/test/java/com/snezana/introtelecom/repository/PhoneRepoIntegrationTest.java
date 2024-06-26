package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.Phone;
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
public class PhoneRepoIntegrationTest {

    @Autowired
    private PhoneRepo phoneRepo;

    @Test
    void findPhone_byPhoneNumber_returnPhoneIfExists(){
        String phoneNumber = "0719317657";
        Phone found = phoneRepo.findByPhoneNumber(phoneNumber);
        assertThat(found.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void findPhone_byPhoneNumber_expectedNull_ifPhoneNumberDoesNotExist(){
        String notDBphoneNumber = "0718797423";
        Phone found = phoneRepo.findByPhoneNumber(notDBphoneNumber);
        assertThat(found).isNull();
    }

    @Test
    void findPhoneOpt_byPhoneNumber_whenIsNotEmpty(){
        String phoneNumber = "0719317657";
        String packageCode = "11";
        Optional<Phone> found = phoneRepo.findByPhoneNumberOpt(phoneNumber);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(Phone.class)
                .hasValueSatisfying(phone-> {
                    assertThat(phone.getPhoneNumber()).isEqualTo(phoneNumber);
                    assertThat(phone.getPackagePlan().getPackageCode()).isEqualTo(packageCode);
                });
    }

    @Test
    void findPhoneOpt_byPhoneNumber_whenIsEmpty(){
        String notDBphoneNumber = "0718797423";
        Optional<Phone> found = phoneRepo.findByPhoneNumberOpt(notDBphoneNumber);
        assertThat(found).isEmpty();
    }

    @Test
    void findPhoneList_allAdminPhones(){
        int listSize = 2;
        String packageCode = "00";
        String adminNote = "Admin phone for support";
        List<Phone> foundList = phoneRepo.findAllAdminPhones();
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(phone -> phone.getPackagePlan().getPackageCode().equals(packageCode)
                        && phone.getNote().equals(adminNote))
                .hasSize(listSize);
    }

    @Test
    void findPhoneList_allCustomerPhones(){
        int listSize = 24;
        String packageCode = "00";
        String adminNote = "Admin phone for support";
        List<Phone> foundList = phoneRepo.findAllCustomerPhones();
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(phone ->!phone.getPackagePlan().getPackageCode().equals(packageCode)
                        && !phone.getNote().equals(adminNote))
                .hasSize(listSize);
    }

    @Test
    void findPhoneList_byPackageCode(){
        int listSize = 4;
        String packageCode = "13";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 1, 1,0,0,0,0);
        List<Phone> foundList = phoneRepo.findByPackageCode(packageCode);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(phone ->phone.getPackagePlan().getPackageCode().equals(packageCode)
                        && phone.getPhoneStartDateTime().isAfter(startDateTime))
                .hasSize(listSize);
    }

}
