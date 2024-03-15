package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.Admin;
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
public class AdminRepoIntegrationTest {

    @Autowired
    private AdminRepo adminRepo;

    @Test
    void findAdminOpt_byAdminId_whenIsNotEmpty(){
        Long id = 2L;
        String aFirstName = "Mihailo";
        String aLastName = "Maksić";
        String aEmail = "mika@introtelecom.com";
        String phoneNumber = "0770000001";
        Optional<Admin> found = adminRepo.findByAdminIdOpt(id);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(Admin.class)
                .hasValueSatisfying(admin-> {
                    assertThat(admin.getAdminId()).isEqualTo(2L);
                    assertThat(admin.getFirstName()).isEqualTo(aFirstName);
                    assertThat(admin.getLastName()).isEqualTo(aLastName);
                    assertThat(admin.getEmail()).isEqualTo(aEmail);
                    assertThat(admin.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
                });
    }

    @Test
    void findAdminOpt_byAdminId_whenIsEmpty(){
        Long id = 200L;
        Optional<Admin> found = adminRepo.findByAdminIdOpt(id);
        assertThat(found).isEmpty();
    }

    @Test
    void findAdminOpt_byPhoneNumber_whenIsNotEmpty(){
        String aFirstName = "Mihailo";
        String aLastName = "Maksić";
        String aEmail = "mika@introtelecom.com";
        String phoneNumber = "0770000001";
        Optional<Admin> found = adminRepo.findByPhoneNumberOpt(phoneNumber);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(Admin.class)
                .hasValueSatisfying(admin-> {
                    assertThat(admin.getFirstName()).isEqualTo(aFirstName);
                    assertThat(admin.getLastName()).isEqualTo(aLastName);
                    assertThat(admin.getEmail()).isEqualTo(aEmail);
                    assertThat(admin.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
                });
    }

    @Test
    void findAdminOpt_byPhoneNumber_whenIsEmpty(){
        String phoneNumber = "UNKNOWN";
        Optional<Admin> found = adminRepo.findByPhoneNumberOpt(phoneNumber);
        assertThat(found).isEmpty();
    }

    @Test
    void findAdminOpt_byPersonalNumber_whenIsNotEmpty(){
        String aFirstName = "Mihailo";
        String aLastName = "Maksić";
        String aEmail = "mika@introtelecom.com";
        String phoneNumber = "0770000001";
        String personalNumber = "9283478122";
        Optional<Admin> found = adminRepo.findByPersonalNumberOpt(personalNumber);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(Admin.class)
                .hasValueSatisfying(admin-> {
                    assertThat(admin.getFirstName()).isEqualTo(aFirstName);
                    assertThat(admin.getLastName()).isEqualTo(aLastName);
                    assertThat(admin.getEmail()).isEqualTo(aEmail);
                    assertThat(admin.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
                    assertThat(admin.getPersonalNumber()).isEqualTo(personalNumber);
                });
    }

    @Test
    void findAdminOpt_byPersonalNumber_whenIsEmpty(){
        String personalNumber = "UNKNOWN";
        Optional<Admin> found = adminRepo.findByPersonalNumberOpt(personalNumber);
        assertThat(found).isEmpty();
    }

    @Test
    void findAdminOpt_byEmail_whenIsNotEmpty(){
        String aFirstName = "Mihailo";
        String aLastName = "Maksić";
        String aEmail = "mika@introtelecom.com";
        String phoneNumber = "0770000001";
        Optional<Admin> found = adminRepo.findByEmailOpt(aEmail);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(Admin.class)
                .hasValueSatisfying(admin-> {
                    assertThat(admin.getFirstName()).isEqualTo(aFirstName);
                    assertThat(admin.getLastName()).isEqualTo(aLastName);
                    assertThat(admin.getEmail()).isEqualTo(aEmail);
                    assertThat(admin.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
                });
    }

    @Test
    void findAdminOpt_byEmail_whenIsEmpty(){
        String aEmail = "UNKNOWN";
        Optional<Admin> found = adminRepo.findByEmailOpt(aEmail);
        assertThat(found).isEmpty();
    }

}
