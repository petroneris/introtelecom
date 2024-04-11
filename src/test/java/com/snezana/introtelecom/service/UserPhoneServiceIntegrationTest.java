package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.PhoneSaveDTO;
import com.snezana.introtelecom.dto.PhoneViewDTO;
import com.snezana.introtelecom.dto.UserSaveDTO;
import com.snezana.introtelecom.dto.UserViewDTO;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.entity.User;
import com.snezana.introtelecom.repository.PhoneRepo;
import com.snezana.introtelecom.repository.UserRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserPhoneServiceIntegrationTest {

    @Autowired
    private UserPhoneService userPhoneService;

    @Autowired
    private PhoneRepo phoneRepo;

    @Autowired
    private UserRepo userRepo;

    @Test
    @Sql(scripts = {"/delete_phone.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testSaveNewPhone() {
        int sizeBefore = phoneRepo.findAll().size();
        String phoneNumber = "0788995677";
        String checkPhoneNumber = "0788995677";
        String packageCode = "11";
        PhoneSaveDTO phoneSaveDTO = new PhoneSaveDTO();
        phoneSaveDTO.setPhoneNumber(phoneNumber);
        phoneSaveDTO.setCheckPhoneNumber(checkPhoneNumber);
        phoneSaveDTO.setPackageCode(packageCode);
        userPhoneService.saveNewPhone(phoneSaveDTO);
        Phone phone = phoneRepo.findAll().get(sizeBefore);
        assertThat(phoneRepo.findAll().size()).isEqualTo(sizeBefore + 1);
        assertThat(phone.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(phone.getPackagePlan().getPackageCode()).isEqualTo(packageCode);
    }

    @Test
    @Sql(scripts = {"/update_phone.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangePackageCode(){
        String phoneNumber = "0742347426";
        String packageCode = "14";
        userPhoneService.changePackageCode(phoneNumber, packageCode);
        Phone phone = phoneRepo.findByPhoneNumber(phoneNumber);
        assertThat(phone.getPackagePlan().getPackageCode()).isEqualTo(packageCode);
        assertThat(phone.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    @Sql(scripts = {"/update_phone.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangePhoneStatus(){
        String phoneNumber = "0742347426";
        String changedStatus = "INACTIVE";
        String noteSequence = "Phone status changed at ";
        userPhoneService.changePhoneStatus(phoneNumber);
        Phone phone = phoneRepo.findByPhoneNumber(phoneNumber);
        assertThat(phone.getPhoneStatus()).isEqualTo(changedStatus);
        assertThat(phone.getNote()).contains(noteSequence);
    }

    @Test
    void testGetPhone() {
        String phoneNumber = "0742347426";
        String packageCode = "13";
        PhoneViewDTO phoneViewDTO = userPhoneService.getPhone(phoneNumber);
        assertThat(phoneViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(phoneViewDTO.getPackageCode()).isEqualTo(packageCode);
    }

    @Test
    void testGetAllAdminPhones(){
        int size = 2;
        List<PhoneViewDTO> phoneViewDTOList = userPhoneService.getAllAdminPhones();
        assertThat(phoneViewDTOList).isNotEmpty();
        assertThat(phoneViewDTOList.size()).isEqualTo(size);
    }

    @Test
    void testGetAllCustomerPhones(){
        int size = 24;
        List<PhoneViewDTO> phoneViewDTOList = userPhoneService.getAllCustomersPhones();
        assertThat(phoneViewDTOList).isNotEmpty();
        assertThat(phoneViewDTOList.size()).isEqualTo(size);
    }

    @Test
    void testGetPhonesByPackageCode(){
        int size = 4;
        String packageCode = "13";
        List<PhoneViewDTO> phoneViewDTOList = userPhoneService.getPhonesByPackageCode(packageCode);
        assertThat(phoneViewDTOList).isNotEmpty();
        assertThat(phoneViewDTOList.size()).isEqualTo(size);
    }

    @Test
    @Sql(scripts = {"/create_phone.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/delete_user.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = {"/delete_phone.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testSaveNewUser() {
        String phoneNumber = "0788995677";
        String username = "sneza5";
        String password = "newWorld";
        String checkPassword = "newWorld";
        String roleType = "CUSTOMER";
        UserSaveDTO userSaveDTO = new UserSaveDTO();
        userSaveDTO.setPhoneNumber(phoneNumber);
        userSaveDTO.setUsername(username);
        userSaveDTO.setPassword(password);
        userSaveDTO.setCheckPassword(checkPassword);
        userSaveDTO.setRoleType(roleType);
        userPhoneService.saveNewUser(userSaveDTO);
        User user = userRepo.findByUsername(username);
        assertThat(user.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getRole().getRoleType()).isEqualTo(roleType);
    }

    @Test
    void testGetAllAdminsUsers(){
        int size = 2;
        List<UserViewDTO> userViewDTOList = userPhoneService.getAllAdminsUsers();
        assertThat(userViewDTOList).isNotEmpty();
        assertThat(userViewDTOList.size()).isEqualTo(size);
    }

    @Test
    void testGetAllCustomersUsers(){
        int size = 24;
        List<UserViewDTO> userViewDTOList = userPhoneService.getAllCustomersUsers();
        assertThat(userViewDTOList).isNotEmpty();
        assertThat(userViewDTOList.size()).isEqualTo(size);
    }

    @Test
    void testGetUserByUsername(){
        String username = "sava3";
        String phoneNumber = "0758519203";
        UserViewDTO userViewDTO = userPhoneService.getUserByUsername(username);
        assertThat(userViewDTO.getUsername()).isEqualTo(username);
        assertThat(userViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testGetUserByPhoneNumber(){
        String phoneNumber = "0758519203";
        String username = "sava3";
        UserViewDTO userViewDTO = userPhoneService.getUserByPhoneNumber(phoneNumber);
        assertThat(userViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(userViewDTO.getUsername()).isEqualTo(username);
    }

    @Test
    @Sql(scripts = {"/update_user_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangeUserStatus(){
        String phoneNumber = "0758519203";
        String changedStatus = "INACTIVE";
        userPhoneService.changeUserStatus(phoneNumber);
        Optional<User> userOpt = userRepo.findByPhoneNumberOpt(phoneNumber);
        assertThat(userOpt).isNotEmpty();
        assertThat(userOpt.get().getUserStatus()).isEqualTo(changedStatus);
    }

    @Test
    @Transactional
    @Sql(scripts = {"/create_phone.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/create_user_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/delete_phone.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testDeleteUser(){
        String username = "sneza5";
        int size = userRepo.findAll().size();
        User user = userRepo.findByUsername(username);
        userPhoneService.deleteUser(user.getUsername());
        Optional<User> userOpt = userRepo.findByUsernameOpt(username);
        int newSize = userRepo.findAll().size();
        assertThat(userOpt).isEmpty();
        assertThat(newSize).isNotEqualTo(size);
    }

}
