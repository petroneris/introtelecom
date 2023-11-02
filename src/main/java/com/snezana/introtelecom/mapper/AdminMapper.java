package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.entity.Admin;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.repositories.PhoneRepo;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminMapper {

    void adminSaveDtoToAdmin(AdminSaveDTO adminSaveDto, @MappingTarget Admin admin, @Context PhoneRepo phoneRepo);

    @BeforeMapping
    default void beforeAdminSaveDtoToAdmin(AdminSaveDTO adminSaveDto, @MappingTarget Admin admin, @Context PhoneRepo phoneRepo) {
           Phone phone = phoneRepo.findByPhoneNumber(adminSaveDto.getPhoneNumber());
           admin.setPhone(phone);
    }

    AdminViewDTO adminToAdminViewDTO(Admin admin);

    List<AdminViewDTO> adminsToAdminsViewDTO(List<Admin> adminList);

    @BeforeMapping
    default void beforeAdminToAdminViewDTO(Admin admin, @MappingTarget AdminViewDTO adminViewDTO){
        Phone phone = admin.getPhone();
        adminViewDTO.setPhoneNumber(phone.getPhoneNumber());
    }

}
