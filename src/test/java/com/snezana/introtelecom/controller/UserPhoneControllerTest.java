package com.snezana.introtelecom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.service.UserPhoneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(UserPhoneController.class)
@WithMockUser(username ="mika", roles="ADMIN")
class UserPhoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserPhoneService usersPhonesService;

    @Test
    void testSavePhone() throws Exception {
        String phoneNumber = "0739899865";
        String checkPhoneNumber = "0739899865";
        String packageCode = "01";
        PhoneSaveDTO phoneSaveDTO = new PhoneSaveDTO();
        phoneSaveDTO.setPhoneNumber(phoneNumber);
        phoneSaveDTO.setCheckPhoneNumber(checkPhoneNumber);
        phoneSaveDTO.setPackageCode(packageCode);
        String message = "The phone '" + phoneSaveDTO.getPhoneNumber() + "' is saved.";

        doNothing().when(usersPhonesService).saveNewPhone(phoneSaveDTO);

        mockMvc.perform(post("/api/phone/saveNewPhone")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phoneSaveDTO)))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testChangePackageCode() throws Exception {
        String phoneNumber = "0739899865";
        String newPackageCode = "01";
        String message = "The package code is changed.";

        doNothing().when(usersPhonesService).changePackageCode(phoneNumber, newPackageCode);

        mockMvc.perform(patch("/api/phone/changePackageCode/{phoneNumber}", phoneNumber).param("packageCode", newPackageCode.toString())
                        .with(csrf()))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testChangePhoneStatus() throws Exception {
        String phoneNumber = "0739899865";
        String message = "The phone status is changed.";

        doNothing().when(usersPhonesService).changePhoneStatus(phoneNumber);

        mockMvc.perform(patch("/api/phone/changePhoneStatus/{phoneNumber}", phoneNumber)
                        .with(csrf()))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetPhone() throws Exception {
        String phoneNumber = "0739899865";
        String packageCode = "01";
        LocalDateTime phoneStartDateTime = LocalDateTime.now();
        String phoneStatus = "ACTIVE";
        String note = "";
        PhoneViewDTO phoneViewDTO = new PhoneViewDTO();
        phoneViewDTO.setPhoneNumber(phoneNumber);
        phoneViewDTO.setPackageCode(packageCode);
        phoneViewDTO.setPhoneStatus(phoneStatus);
        phoneViewDTO.setNote(note);
        phoneViewDTO.setPhoneStartDateTime(phoneStartDateTime);

        when(usersPhonesService.getPhone(phoneNumber)).thenReturn(phoneViewDTO);

        mockMvc.perform(get("/api/phone/getPhone/{phoneNumber}", phoneNumber))
                .andExpect(jsonPath("$.data.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.data.packageCode").value(packageCode))
                .andExpect(jsonPath("$.data.phoneStatus").value(phoneStatus))
                .andExpect(jsonPath("$.data.note").value(note))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetPhonesByPackageCode() throws Exception {
        String phoneNumber1 = "0739899865";
        String packageCode = "01";;
        LocalDateTime phoneStartDateTime1 = LocalDateTime.now();
        String phoneStatus1 = "ACTIVE";
        String note1 = "";
        PhoneViewDTO phoneViewDTO1 = new PhoneViewDTO();
        phoneViewDTO1.setPhoneNumber(phoneNumber1);
        phoneViewDTO1.setPackageCode(packageCode);
        phoneViewDTO1.setPhoneStatus(phoneStatus1);
        phoneViewDTO1.setNote(note1);
        phoneViewDTO1.setPhoneStartDateTime(phoneStartDateTime1);
        String phoneNumber2 = "0739899867";
        LocalDateTime phoneStartDateTime2 = LocalDateTime.now();
        String phoneStatus2 = "ACTIVE";
        String note2 = "";
        PhoneViewDTO phoneViewDTO2 = new PhoneViewDTO();
        phoneViewDTO2.setPhoneNumber(phoneNumber2);
        phoneViewDTO2.setPackageCode(packageCode);
        phoneViewDTO2.setPhoneStatus(phoneStatus2);
        phoneViewDTO2.setNote(note2);
        phoneViewDTO2.setPhoneStartDateTime(phoneStartDateTime2);
        List<PhoneViewDTO> phoneViewDTOList = new ArrayList<>();
        phoneViewDTOList.add(phoneViewDTO1);
        phoneViewDTOList.add(phoneViewDTO2);

        when(usersPhonesService.getPhonesByPackageCode(packageCode)).thenReturn(phoneViewDTOList);

        mockMvc.perform(get("/api/phone/getPhonesByPackageCode/{packageCode}", packageCode))
                .andExpect(jsonPath("$.data.size()").value(phoneViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAllAdminPhones() throws Exception {
        String phoneNumber1 = "0730000003";
        String packageCode = "00";;
        LocalDateTime phoneStartDateTime1 = LocalDateTime.now();
        String phoneStatus1 = "ACTIVE";
        String note1 = "";
        PhoneViewDTO phoneViewDTO1 = new PhoneViewDTO();
        phoneViewDTO1.setPhoneNumber(phoneNumber1);
        phoneViewDTO1.setPackageCode(packageCode);
        phoneViewDTO1.setPhoneStatus(phoneStatus1);
        phoneViewDTO1.setNote(note1);
        phoneViewDTO1.setPhoneStartDateTime(phoneStartDateTime1);
        String phoneNumber2 = "0730000005";
        LocalDateTime phoneStartDateTime2 = LocalDateTime.now();
        String phoneStatus2 = "ACTIVE";
        String note2 = "";
        PhoneViewDTO phoneViewDTO2 = new PhoneViewDTO();
        phoneViewDTO2.setPhoneNumber(phoneNumber2);
        phoneViewDTO2.setPackageCode(packageCode);
        phoneViewDTO2.setPhoneStatus(phoneStatus2);
        phoneViewDTO2.setNote(note2);
        phoneViewDTO2.setPhoneStartDateTime(phoneStartDateTime2);
        List<PhoneViewDTO> phoneViewDTOList = new ArrayList<>();
        phoneViewDTOList.add(phoneViewDTO1);
        phoneViewDTOList.add(phoneViewDTO2);

        when(usersPhonesService.getAllAdminPhones()).thenReturn(phoneViewDTOList);

        mockMvc.perform(get("/api/phone/getAllAdminPhones"))
                .andExpect(jsonPath("$.data.size()").value(phoneViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAllCustomersPhones() throws Exception {
        String phoneNumber1 = "0739899865";
        String packageCode1 = "01";
        LocalDateTime phoneStartDateTime1 = LocalDateTime.now();
        String phoneStatus1 = "ACTIVE";
        String note1 = "";
        PhoneViewDTO phoneViewDTO1 = new PhoneViewDTO();
        phoneViewDTO1.setPhoneNumber(phoneNumber1);
        phoneViewDTO1.setPackageCode(packageCode1);
        phoneViewDTO1.setPhoneStatus(phoneStatus1);
        phoneViewDTO1.setNote(note1);
        phoneViewDTO1.setPhoneStartDateTime(phoneStartDateTime1);
        String phoneNumber2 = "0739899867";
        String packageCode2 = "02";
        LocalDateTime phoneStartDateTime2 = LocalDateTime.now();
        String phoneStatus2 = "ACTIVE";
        String note2 = "";
        PhoneViewDTO phoneViewDTO2 = new PhoneViewDTO();
        phoneViewDTO2.setPhoneNumber(phoneNumber2);
        phoneViewDTO2.setPackageCode(packageCode2);
        phoneViewDTO2.setPhoneStatus(phoneStatus2);
        phoneViewDTO2.setNote(note2);
        phoneViewDTO2.setPhoneStartDateTime(phoneStartDateTime2);
        List<PhoneViewDTO> phoneViewDTOList = new ArrayList<>();
        phoneViewDTOList.add(phoneViewDTO1);
        phoneViewDTOList.add(phoneViewDTO2);

        when(usersPhonesService.getAllCustomersPhones()).thenReturn(phoneViewDTOList);

        mockMvc.perform(get("/api/phone/getAllCustomersPhones"))
                .andExpect(jsonPath("$.data.size()").value(phoneViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testSaveUser() throws Exception {
        String phoneNumber = "0739899867";
        String username = "panta1";
        String password = "newWorld";
        String checkPassword ="newWorld";
        String roleType = "CUSTOMER";
        UserSaveDTO userSaveDTO = new UserSaveDTO();
        userSaveDTO.setPhoneNumber(phoneNumber);
        userSaveDTO.setUsername(username);
        userSaveDTO.setPassword(password);
        userSaveDTO.setRoleType(roleType);
        userSaveDTO.setCheckPassword(checkPassword);
        String message = "The user '" + userSaveDTO.getUsername() + "' is saved.";

        doNothing().when(usersPhonesService).saveNewUser(userSaveDTO);

        mockMvc.perform(post("/api/user/saveNewUser")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSaveDTO)))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetUserByPhoneNumber() throws Exception {
        String phoneNumber = "0739899867";
        String username = "panta1";
        String roleType = "CUSTOMER";
        String userStatus = "ACTIVE";
        UserViewDTO userViewDTO = new UserViewDTO();
        userViewDTO.setPhoneNumber(phoneNumber);
        userViewDTO.setUsername(username);
        userViewDTO.setRoleType(roleType);
        userViewDTO.setUserStatus(userStatus);

        when(usersPhonesService.getUserByPhoneNumber(phoneNumber)).thenReturn(userViewDTO);

        mockMvc.perform(get("/api/user/getUserByPhoneNumber/{phoneNumber}", phoneNumber))
                .andExpect(jsonPath("$.data.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.data.username").value(username))
                .andExpect(jsonPath("$.data.roleType").value(roleType))
                .andExpect(jsonPath("$.data.userStatus").value(userStatus))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetUserByUsername() throws Exception {
        String phoneNumber = "0739899867";
        String username = "panta1";
        String roleType = "CUSTOMER";
        String userStatus = "ACTIVE";
        UserViewDTO userViewDTO = new UserViewDTO();
        userViewDTO.setPhoneNumber(phoneNumber);
        userViewDTO.setUsername(username);
        userViewDTO.setRoleType(roleType);
        userViewDTO.setUserStatus(userStatus);

        when(usersPhonesService.getUserByUsername(username)).thenReturn(userViewDTO);

        mockMvc.perform(get("/api/user/getUserByUsername/{username}", username))
                .andExpect(jsonPath("$.data.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.data.username").value(username))
                .andExpect(jsonPath("$.data.roleType").value(roleType))
                .andExpect(jsonPath("$.data.userStatus").value(userStatus))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAllAdminUsers() throws Exception {
        String phoneNumber1 = "0730000003";
        String username1 = "admin1";
        String roleType = "ADMIN";
        String userStatus = "ACTIVE";
        UserViewDTO userViewDTO1 = new UserViewDTO();
        userViewDTO1.setPhoneNumber(phoneNumber1);
        userViewDTO1.setUsername(username1);
        userViewDTO1.setRoleType(roleType);
        userViewDTO1.setUserStatus(userStatus);
        String phoneNumber2 = "0730000005";
        String username2 = "admin2";
        UserViewDTO userViewDTO2 = new UserViewDTO();
        userViewDTO2.setPhoneNumber(phoneNumber2);
        userViewDTO2.setUsername(username2);
        userViewDTO2.setRoleType(roleType);
        userViewDTO2.setUserStatus(userStatus);
        List<UserViewDTO> userViewDTOList = new ArrayList<>();
        userViewDTOList.add(userViewDTO1);
        userViewDTOList.add(userViewDTO2);

        when(usersPhonesService.getAllAdminsUsers()).thenReturn(userViewDTOList);

        mockMvc.perform(get("/api/user/getAllAdminUsers"))
                .andExpect(jsonPath("$.data.size()").value(userViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAllCustomersUsers() throws Exception {
        String phoneNumber1 = "0739899867";
        String username1 = "panta1";
        String roleType = "CUSTOMER";
        String userStatus = "ACTIVE";
        UserViewDTO userViewDTO1 = new UserViewDTO();
        userViewDTO1.setPhoneNumber(phoneNumber1);
        userViewDTO1.setUsername(username1);
        userViewDTO1.setRoleType(roleType);
        userViewDTO1.setUserStatus(userStatus);
        String phoneNumber2 = "0739899865";
        String username2 = "panta2";
        UserViewDTO userViewDTO2 = new UserViewDTO();
        userViewDTO2.setPhoneNumber(phoneNumber2);
        userViewDTO2.setUsername(username2);
        userViewDTO2.setRoleType(roleType);
        userViewDTO2.setUserStatus(userStatus);
        List<UserViewDTO> userViewDTOList = new ArrayList<>();
        userViewDTOList.add(userViewDTO1);
        userViewDTOList.add(userViewDTO2);

        when(usersPhonesService.getAllCustomersUsers()).thenReturn(userViewDTOList);

        mockMvc.perform(get("/api/user/getAllCustomersUsers"))
                .andExpect(jsonPath("$.data.size()").value(userViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testChangeUserStatus() throws Exception {
        String phoneNumber = "0739899865";
        String message = "The user status is changed.";

        doNothing().when(usersPhonesService).changeUserStatus(phoneNumber);

        mockMvc.perform(patch("/api/user/changeUserStatus/{phoneNumber", phoneNumber)
                        .with(csrf()))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testChangeUserPassword() throws Exception {
        String username = "panta1";
        String oldPassword = "newWorld";
        String newPassword = "freshNewWorld";
        String checkNewPassword = "freshNewWorld";
        UserChangePasswordDTO userChangePasswordDTO = new UserChangePasswordDTO();
        userChangePasswordDTO.setUsername(username);
        userChangePasswordDTO.setOldPassword(oldPassword);
        userChangePasswordDTO.setNewPassword(newPassword);
        userChangePasswordDTO.setCheckNewPassword(checkNewPassword);
        String message = "The user password is changed.";

        doNothing().when(usersPhonesService).changeUserPassword(userChangePasswordDTO);

        mockMvc.perform(patch("/api/user/changeUserPassword")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userChangePasswordDTO)))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testDeleteUser() throws Exception {
        String username = "panta1";
        String message = "The user '" +username + "' is deleted!";

        doNothing().when(usersPhonesService).deleteUser(username);

        mockMvc.perform(delete("/api/user/deleteUser/{username}", username)
                        .with(csrf()))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }
}