package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.PackageFrameSaveDTO;
import com.snezana.introtelecom.dto.PackageFrameViewDTO;
import com.snezana.introtelecom.entity.PackageFrame;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.repositories.PhoneRepo;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PackageFrameMapper {

    @Mapping(target = "phone", ignore = true)
    PackageFrame packageFrameSaveDtoToPackageFrame(PackageFrameSaveDTO packageFrameSaveDto, @MappingTarget PackageFrame packageFrame, @Context PhoneRepo phoneRepo);

    @BeforeMapping
    default void beforePackageFrameSaveDtoToPackageFrame(PackageFrameSaveDTO packageFrameSaveDto, @MappingTarget PackageFrame packageFrame,
                                           @Context PhoneRepo phoneRepo) {
        if (packageFrameSaveDto.getPhone() != null && (packageFrame.getPhone() == null || !packageFrame.getPhone().getPhoneNumber().equals(packageFrameSaveDto.getPhone()))) {
            final Phone phone = phoneRepo.findByPhoneNumberOpt(packageFrameSaveDto.getPhone())
                    .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Phone = " + packageFrameSaveDto.getPhone() + " is not found."));
            packageFrame.setPhone(phone);
        }
    }

    @Mapping(target = "phone", ignore = true)
    PackageFrameViewDTO packageFrameToPackageFrameViewDTO(PackageFrame packageFrame);

    @Mapping(target = "phone", ignore = true)
    List<PackageFrameViewDTO> packageFrameToPackageFrameViewDTO (List<PackageFrame> packageFrameList);

    @BeforeMapping
    default void packageFrameToPackageFrameViewDTO(PackageFrame packageFrame, @MappingTarget PackageFrameViewDTO packageFrameViewDTO){
        Phone phone = packageFrame.getPhone();
        packageFrameViewDTO.setPhone(phone.getPhoneNumber());
    }
}
