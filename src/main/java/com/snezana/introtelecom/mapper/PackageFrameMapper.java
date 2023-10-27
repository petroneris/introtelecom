package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.PackageFrameSaveDTO;
import com.snezana.introtelecom.dto.PackageFrameViewDTO;
import com.snezana.introtelecom.entity.PackageFrame;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.enums.PackagePlanType;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.repositories.PhoneRepo;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PackageFrameMapper {

    @Mapping(target = "phone", ignore = true)
    void packageFrameSaveDtoToPackageFrame(PackageFrameSaveDTO packageFrameSaveDto, @MappingTarget PackageFrame packageFrame, @Context PhoneRepo phoneRepo);

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
    @Mapping(target = "packfrCls", ignore = true)
    @Mapping(target = "packfrSms", ignore = true)
    @Mapping(target = "packfrInt", ignore = true)
    @Mapping(target = "packfrAsm", ignore = true)
    @Mapping(target = "packfrIcl", ignore = true)
    @Mapping(target = "packfrRmg", ignore = true)
    PackageFrameViewDTO packageFrameToPackageFrameViewDTO(PackageFrame packageFrame);

    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "packfrCls", ignore = true)
    @Mapping(target = "packfrSms", ignore = true)
    @Mapping(target = "packfrInt", ignore = true)
    @Mapping(target = "packfrAsm", ignore = true)
    @Mapping(target = "packfrIcl", ignore = true)
    @Mapping(target = "packfrRmg", ignore = true)
    List<PackageFrameViewDTO> packageFrameToPackageFrameViewDTO (List<PackageFrame> packageFrameList);

    @BeforeMapping
    default void packageFrameToPackageFrameViewDTO(PackageFrame packageFrame, @MappingTarget PackageFrameViewDTO packageFrameViewDTO){
        Phone phone = packageFrame.getPhone();
        packageFrameViewDTO.setPhone(phone.getPhoneNumber());
        String packageCode = phone.getPackagePlan().getPackageCode();
        PackagePlanType packagePlanType = PackagePlanType.findByKey(packageCode);
        packageFrameViewDTO.setPackageCode(packageCode);
        BigDecimal divisor = new BigDecimal("1000");
        if (packagePlanType==PackagePlanType.PST13 || packagePlanType==PackagePlanType.PST14){
            packageFrameViewDTO.setPackfrCls("UNLIMITED");
            packageFrameViewDTO.setPackfrSms("UNLIMITED");
        } else {
            packageFrameViewDTO.setPackfrCls(packageFrame.getPackfrCls() + " min");
            packageFrameViewDTO.setPackfrSms(packageFrame.getPackfrSms() + " msg");
        }
        if (packagePlanType==PackagePlanType.PST14){
            packageFrameViewDTO.setPackfrInt("UNLIMITED");
        } else {
            BigDecimal gbInternet = packageFrame.getPackfrInt().divide(divisor);
            packageFrameViewDTO.setPackfrInt(gbInternet + " GB");
        }
        packageFrameViewDTO.setPackfrIcl(packageFrame.getPackfrIcl().toString() + " cu");
        packageFrameViewDTO.setPackfrRmg(packageFrame.getPackfrRmg().toString() + " cu");
        BigDecimal gbAppSocialMedia = packageFrame.getPackfrAsm().divide(divisor);
        packageFrameViewDTO.setPackfrAsm(gbAppSocialMedia + " GB");
    }
}
