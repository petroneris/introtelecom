package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.entity.User;
import com.snezana.introtelecom.repository.UserRepo;
import com.snezana.introtelecom.securitycontext.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.context.jdbc.Sql;

import javax.validation.constraints.NotBlank;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ClientServiceIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;


    @Test
    @WithMockCustomUser(username = "dana2", password = "pwd")
    void testGetCurrentInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = "0769317426";
        String packageCode = "12";
        String currCls = "392 min left";
        String currSms = "398 msg left";
        String currInt = "8000.00 MB left";
        String currAsm = "3000.00 MB left";
        String currIcl = "200.00 cu left";
        String currRmg = "200.00 cu left";
        LocalDateTime fixedLocalDateTime = LocalDateTime.of(2023, Month.FEBRUARY, 22, 15, 10, 0, 0);
        try (MockedStatic<LocalDateTime> mockedStatic = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(() -> LocalDateTime.now(ArgumentMatchers.any(Clock.class))).thenReturn(fixedLocalDateTime);
            ClientCurrentInfo1234ViewDTO clientCurrentInfo1234ViewDTO = (ClientCurrentInfo1234ViewDTO) clientService.getCurrentInfo(authentication);
            assertThat(clientCurrentInfo1234ViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
            assertThat(clientCurrentInfo1234ViewDTO.getPackageCode()).isEqualTo(packageCode);
            assertThat(clientCurrentInfo1234ViewDTO.getCurrCls()).isEqualTo(currCls);
            assertThat(clientCurrentInfo1234ViewDTO.getCurrSms()).isEqualTo(currSms);
            assertThat(clientCurrentInfo1234ViewDTO.getCurrInt()).isEqualTo(currInt);
            assertThat(clientCurrentInfo1234ViewDTO.getCurrAsm()).isEqualTo(currAsm);
            assertThat(clientCurrentInfo1234ViewDTO.getCurrIcl()).isEqualTo(currIcl);
            assertThat(clientCurrentInfo1234ViewDTO.getCurrRmg()).isEqualTo(currRmg);
            assertThat(clientCurrentInfo1234ViewDTO.getCurrDateTime()).isEqualTo(fixedLocalDateTime);
        }
    }

    @Test
    @WithMockCustomUser(username = "lana1", password = "pwd1")
    void testGetMonthlyBillFactsByYearAndMonth_prepaid(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = "0720123763";
        String packageCode = "01";
        String username = "lana1";
        String packageCodeView = packageCode + "  -> THERE IS NO MONTHLY BILL FOR PREPAID PHONE PACKAGES.";
        int month = 12;
        int year = 2023;
        ClientMonthlyBillFactsPrpViewDTO clientMonthlyBillFactsPrpViewDTO = clientService.getMonthlyBillFactsByYearAndMonth(authentication, year, month);
        assertThat(clientMonthlyBillFactsPrpViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(clientMonthlyBillFactsPrpViewDTO.getUsername()).isEqualTo(username);
        assertThat(clientMonthlyBillFactsPrpViewDTO.getPackageCode()).isEqualTo(packageCodeView);
    }

    @Test
    @WithMockCustomUser(username = "dana2", password = "pwd")
    void testGetMonthlyBillFactsByYearAndMonth_postpaid(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = "0769317426";
        String packageCode = "12";
        String username = "dana2";
        String monthStr = "DECEMBER";
        int month = 12;
        int year = 2023;
        String monthlybillTotalprice = "1000.00 cu";
        LocalDateTime monthlybillDateTime = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0, 0);
        ClientMonthlyBillFactsPstViewDTO clientMonthlyBillFactsPstViewDTO = (ClientMonthlyBillFactsPstViewDTO) clientService.getMonthlyBillFactsByYearAndMonth(authentication, year, month);
        assertThat(clientMonthlyBillFactsPstViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(clientMonthlyBillFactsPstViewDTO.getUsername()).isEqualTo(username);
        assertThat(clientMonthlyBillFactsPstViewDTO.getPackageCode()).isEqualTo(packageCode);
        assertThat(clientMonthlyBillFactsPstViewDTO.getMonth()).isEqualTo(monthStr);
        assertThat(clientMonthlyBillFactsPstViewDTO.getYear()).isEqualTo(year);
        assertThat(clientMonthlyBillFactsPstViewDTO.getMonthlybillTotalprice()).isEqualTo(monthlybillTotalprice);
        assertThat(clientMonthlyBillFactsPstViewDTO.getMonthlybillDateTime()).isEqualTo(monthlybillDateTime);
    }

    @Test
    @WithMockCustomUser(username = "lana1", password = "pwd1")
    void testGetMonthlyBillFactsFromStartDateToEndDate_prepaid(){
        int size = 1;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = "0720123763";
        String packageCode = "01";
        String username = "lana1";
        String packageCodeView = packageCode + "  -> THERE IS NO MONTHLY BILL FOR PREPAID PHONE PACKAGES.";
        int startMonth = 12;
        int startYear = 2023;
        int endMonth = 2;
        int endYear = 2024;
        List<? extends ClientMonthlyBillFactsPrpViewDTO> clientMonthlyBillFactsPrpViewDTOList = clientService.getMonthlyBillFactsFromStartDateToEndDate(authentication, startYear, startMonth, endYear, endMonth);
        assertThat(clientMonthlyBillFactsPrpViewDTOList.size()).isEqualTo(size);
        assertThat(clientMonthlyBillFactsPrpViewDTOList.get(0).getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(clientMonthlyBillFactsPrpViewDTOList.get(0).getUsername()).isEqualTo(username);
        assertThat(clientMonthlyBillFactsPrpViewDTOList.get(0).getPackageCode()).isEqualTo(packageCodeView);
    }

    @Test
    @WithMockCustomUser(username = "dana2", password = "pwd")
    void testGetMonthlyBillFactsFromStartDateToEndDate_postpaid(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int size = 3;
        int startMonth = 12;
        int startYear = 2023;
        int endMonth = 2;
        int endYear = 2024;
        List<? extends ClientMonthlyBillFactsPrpViewDTO> clientMonthlyBillFactsPstViewDTOList = clientService.getMonthlyBillFactsFromStartDateToEndDate(authentication, startYear, startMonth, endYear, endMonth);
        assertThat(clientMonthlyBillFactsPstViewDTOList).isNotEmpty();
        assertThat(clientMonthlyBillFactsPstViewDTOList.size()).isEqualTo(size);
    }

    @Test
    @WithMockCustomUser(username = "sava3", password = "sava3")
    @Sql(scripts = {"/update_user_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangePassword(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String oldPassword = "sava3";
        String rawNewPassword = "newWorld";
        String checkNewPassword = "newWorld";
        ClientChangePasswordDTO clientChangePasswordDTO = new ClientChangePasswordDTO();
        clientChangePasswordDTO.setOldPassword(oldPassword);
        clientChangePasswordDTO.setNewPassword(rawNewPassword);
        clientChangePasswordDTO.setCheckNewPassword(checkNewPassword);
        clientService.changePassword(authentication, clientChangePasswordDTO);
        User user = userRepo.findByUsername(authentication.getPrincipal().toString());
        String encodedPassword = user.getPassword();
        boolean passwordMatches = passwordEncoder.matches(rawNewPassword, encodedPassword);
        assertThat(passwordMatches).isTrue();
    }

    @Test
    void testGetAllCustomersPackagePlans(){
        int size = 6;
        List<PackagePlanDTO> packagePlanDTOList = clientService.getAllCustomersPackagePlans();
        assertThat(packagePlanDTOList).isNotEmpty();
        assertThat(packagePlanDTOList.size()).isEqualTo(size);
    }

    @Test
    void testGetAllAddOnDetails(){
        int size = 6;
        List<AddOnDTO> addOnDTOList = clientService.getAllAddOnDetails();
        assertThat(addOnDTOList).isNotEmpty();
        assertThat(addOnDTOList.size()).isEqualTo(size);
    }

}
