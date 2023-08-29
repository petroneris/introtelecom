package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.entity.Admin;
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
public interface AdminMapper {

    @Mapping(target = "phone", ignore = true)
    Admin adminSaveDtoToAdmin(AdminSaveDTO adminSaveDto, @MappingTarget Admin admin, @Context PhoneRepo phoneRepo);

    @BeforeMapping
    default void beforeAdminSaveDtoToAdmin(AdminSaveDTO adminSaveDto, @MappingTarget Admin admin,
                                        @Context PhoneRepo phoneRepo) {
        if (adminSaveDto.getPhone() != null && (admin.getPhone() == null || !admin.getPhone().getPhoneNumber().equals(adminSaveDto.getPhone()))) {
            final Phone phone = phoneRepo.findByPhoneNumberOpt(adminSaveDto.getPhone())
                    .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Phone = " + adminSaveDto.getPhone() + " is not found."));
            admin.setPhone(phone);
        }
    }

    @Mapping(target = "phone", ignore = true)
    AdminViewDTO adminToAdminViewDTO(Admin admin);

    @Mapping(target = "phone", ignore = true)
    List<AdminViewDTO> adminToAdminViewDTO (List<Admin> adminList);

    @BeforeMapping
    default void adminToAdminViewDTO(Admin admin, @MappingTarget AdminViewDTO adminViewDTO){
        Phone phone = admin.getPhone();
        adminViewDTO.setPhone(phone.getPhoneNumber());
    }

}
