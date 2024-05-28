package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.Role;
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
public class RoleRepoIntegrationTest {

    @Autowired
    private RoleRepo roleRepo;

    @Test
    void findRoleOpt_byRoleType_admin(){
        String roleType = "ADMIN";
        Long id = 1L;
        Optional<Role> found = roleRepo.findByRoleTypeOpt(roleType);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(Role.class)
                .hasValueSatisfying(role-> {
                    assertThat(role.getRoleType()).isEqualTo(roleType);
                    assertThat(role.getRoleId()).isEqualTo(id);
                });
    }

    @Test
    void findRoleOpt_byRoleType_customer(){
        String roleType = "CUSTOMER";
        Long id = 2L;
        Optional<Role> found = roleRepo.findByRoleTypeOpt(roleType);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(Role.class)
                .hasValueSatisfying(role-> {
                    assertThat(role.getRoleType()).isEqualTo(roleType);
                    assertThat(role.getRoleId()).isEqualTo(id);
                });
    }

    @Test
    void findRoleOpt_byRoleType_whenIsEmpty(){
        String roleType = "UNKNOWN";
        Optional<Role> found = roleRepo.findByRoleTypeOpt(roleType);
        assertThat(found).isEmpty();
    }

}
