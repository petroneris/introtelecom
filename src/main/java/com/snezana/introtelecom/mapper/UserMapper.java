package com.snezana.introtelecom.mapper;


import com.snezana.introtelecom.dto.UserSaveDTO;
import com.snezana.introtelecom.dto.UserViewDTO;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.entity.Role;
import com.snezana.introtelecom.entity.User;
import com.snezana.introtelecom.enums.StatusType;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.repositories.PhoneRepo;
import com.snezana.introtelecom.repositories.RoleRepo;
import org.mapstruct.*;
import org.slf4j.Logger;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    Logger log = org.slf4j.LoggerFactory.getLogger(UserMapper.class);


    @Mapping(target = "phoneNumber", ignore = true)
    void userSaveDtoToUser(UserSaveDTO userSaveDto, @MappingTarget User user, @Context RoleRepo roleRepo, @Context PhoneRepo phoneRepo);

    @BeforeMapping
    default void beforeUserSaveDtoToUser(UserSaveDTO userSaveDto, @MappingTarget User user, @Context RoleRepo roleRepo, @Context PhoneRepo phoneRepo) {
            Role role = roleRepo.findByRoleTypeOpt(userSaveDto.getRoleType())
                    .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Role type = " + userSaveDto.getRoleType() + " is not found. Must be 'ADMIN' or 'CUSTOMER'"));
            user.setRole(role);
            Phone phone = phoneRepo.findByPhoneNumber(userSaveDto.getPhoneNumber());
//            phone.setUser(user);
            user.setPhone(phone);
            user.setUserStatus(StatusType.PRESENT.getStatus());
    }

    UserViewDTO userToUserViewDTO(User user);

    List<UserViewDTO> usersToUsersViewDTO(List<User> userList);

    @BeforeMapping
    default void beforeUserToUserViewDTO(User user, @MappingTarget UserViewDTO userViewDTO){
        Role role = user.getRole();
        userViewDTO.setRoleType(role.getRoleType());
    }
}
