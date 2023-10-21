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

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "phone", ignore = true)
    User userSaveDtoToUser(UserSaveDTO userSaveDto, @MappingTarget User user, @Context RoleRepo roleRepo, @Context PhoneRepo phoneRepo);

    @BeforeMapping
    default void beforeUserSaveDtoToUser(UserSaveDTO userSaveDto, @MappingTarget User user,
                                         @Context RoleRepo roleRepo, @Context PhoneRepo phoneRepo) {
        if (userSaveDto.getRole() != null && (user.getRole() == null || !user.getRole().getRoleType().equals(userSaveDto.getRole()))) {
            final Role role = roleRepo.findByRoleTypeOpt(userSaveDto.getRole())
                    .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Role type = " + userSaveDto.getRole() + " is not found. Must be 'ADMIN' or 'CUSTOMER'"));
            user.setRole(role);
            final Phone phone = phoneRepo.findByPhoneNumberOpt(userSaveDto.getPhone())
                    .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Phone = " + userSaveDto.getPhone() + " is not found."));
//            phone.setUser(user);
            user.setPhone(phone);
            user.setUserStatus(StatusType.PRESENT.getStatus());
        }
    }

    @Mapping(target = "role", ignore = true)
    UserViewDTO userToUserViewDTO(User user);

    @Mapping(target = "role", ignore = true)
    List<UserViewDTO> userToUserViewDTO (List<User> userList);

    @BeforeMapping
    default void beforeUserToUserViewDTO(User user, @MappingTarget UserViewDTO userViewDTO){
        Role role = user.getRole();
        userViewDTO.setRole(role.getRoleType());
    }

}
