package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.UserSaveDTO;
import com.snezana.introtelecom.dto.UserViewDTO;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.entity.Role;
import com.snezana.introtelecom.entity.User;
import com.snezana.introtelecom.enums.StatusType;
import com.snezana.introtelecom.exception.ItemNotFoundException;
import com.snezana.introtelecom.exception.RestAPIErrorMessage;
import com.snezana.introtelecom.repository.PhoneRepo;
import com.snezana.introtelecom.repository.RoleRepo;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "phoneNumber", ignore = true)
    void userSaveDtoToUser(UserSaveDTO userSaveDto, @MappingTarget User user, @Context RoleRepo roleRepo, @Context PhoneRepo phoneRepo);

    @BeforeMapping
    default void beforeUserSaveDtoToUser(UserSaveDTO userSaveDto, @MappingTarget User user, @Context RoleRepo roleRepo, @Context PhoneRepo phoneRepo) {
            Role role = roleRepo.findByRoleTypeOpt(userSaveDto.getRoleType())
                    .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Role type = " + userSaveDto.getRoleType() + " is not found. Must be 'ADMIN' or 'CUSTOMER'"));
            user.setRole(role);
            Phone phone = phoneRepo.findByPhoneNumber(userSaveDto.getPhoneNumber());
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
