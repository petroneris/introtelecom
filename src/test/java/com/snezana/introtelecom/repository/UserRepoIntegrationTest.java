package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.User;
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
public class UserRepoIntegrationTest {

    @Autowired
    private UserRepo userRepo;

    @Test
    void findUser_byUsername_returnUserIfExists(){
        String username = "lana1";
        User found = userRepo.findByUsername(username);
        assertThat(found.getUsername()).isEqualTo(username);
    }

    @Test
    void findUser_byUsername_expectedNull_ifUsernameDoesNotExist(){
        String username = "UNKNOWN";
        User found = userRepo.findByUsername(username);
        assertThat(found).isNull();
    }

    @Test
    void findUserOpt_byPhoneNumber_whenIsNotEmpty(){
        String phoneNumber = "0720123763";
        String username = "lana1";
        Optional<User> found = userRepo.findByPhoneNumberOpt(phoneNumber);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(User.class)
                .hasValueSatisfying(user-> {
                    assertThat(user.getUsername()).isEqualTo(username);
                    assertThat(user.getPhoneNumber()).isEqualTo(phoneNumber);
                });
    }

    @Test
    void findUserOpt_byPhoneNumber_whenIsEmpty(){
        String phoneNumber = "UNKNOWN";
        Optional<User> found = userRepo.findByPhoneNumberOpt(phoneNumber);
        assertThat(found).isEmpty();
    }

    @Test
    void findUserOpt_byUsername_whenIsNotEmpty(){
        String username = "lana1";
        String phoneNumber = "0720123763";
        Optional<User> found = userRepo.findByUsernameOpt(username);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(User.class)
                .hasValueSatisfying(user-> {
                    assertThat(user.getUsername()).isEqualTo(username);
                    assertThat(user.getPhoneNumber()).isEqualTo(phoneNumber);
                });
    }

    @Test
    void findUserOpt_byUsername_whenIsEmpty(){
        String username = "UNKNOWN";
        Optional<User> found = userRepo.findByUsernameOpt(username);
        assertThat(found).isEmpty();
    }

    @Test
    void findUserList_allAdminUsers(){
        int listSize = 2;
        Long roleId = 1L;
        String roleType = "ADMIN";
        List<User> foundList = userRepo.findAllAdminsUsers();
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(user ->user.getRole().getRoleType().equals(roleType)
                && user.getRole().getRoleId().equals(roleId))
                .hasSize(listSize);
    }

    @Test
    void findUserList_allCustomerUsers(){
        int listSize = 24;
        Long roleId = 2L;
        String roleType = "CUSTOMER";
        List<User> foundList = userRepo.findAllCustomersUsers();
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(user ->user.getRole().getRoleType().equals(roleType)
                        && user.getRole().getRoleId().equals(roleId))
                .hasSize(listSize);
    }

}
