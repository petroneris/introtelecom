package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.PackageFrameViewDTO;
import com.snezana.introtelecom.entity.PackageFrame;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PackageFrameMapper {

    @Mapping(source = "packageFrame", target = "packfrCls", qualifiedByName = "calls")
    @Mapping(source = "packageFrame", target = "packfrSms", qualifiedByName = "messages")
    @Mapping(source = "packageFrame", target = "packfrInt", qualifiedByName = "internet")
    @Mapping(source = "packageFrame", target = "packfrAsm", qualifiedByName = "appSocialMedia")
    @Mapping(source = "packageFrame", target = "packfrIcl", qualifiedByName = "internationalCalls")
    @Mapping(source = "packageFrame", target = "packfrRmg", qualifiedByName = "roaming")
    public abstract PackageFrameViewDTO packageFrameToPackageFrameViewDTO(PackageFrame packageFrame);

    public abstract List<PackageFrameViewDTO> packageFramesToPackageFramesViewDTO(List<PackageFrame> packageFrameList);


    @Named("calls")
    public static String clsConversion (PackageFrame packageFrame) {
        if (packageFrame.getPackfrCls() < 0) {
            return "UNLIMITED";
        } else {
            return packageFrame.getPackfrCls() + " min";
        }
    }

    @Named("messages")
    public static String smsConversion (PackageFrame packageFrame) {
        if (packageFrame.getPackfrSms() < 0) {
            return "UNLIMITED";
        } else {
            return packageFrame.getPackfrSms() + " msg";
        }
    }

    @Named("internet")
    public static String intConversion (PackageFrame packageFrame) {
        BigDecimal divisor = new BigDecimal("1000");
        if (packageFrame.getPackfrInt().compareTo(new BigDecimal(0)) < 0) {
            return "UNLIMITED";
        } else {
            BigDecimal gbInternet = packageFrame.getPackfrInt().divide(divisor, 2, RoundingMode.UP);
            return gbInternet + " GB";
        }
    }

    @Named("appSocialMedia")
    public static String asmConversion (PackageFrame packageFrame) {
        BigDecimal divisor = new BigDecimal("1000");
        BigDecimal gbAppSocialMedia = packageFrame.getPackfrAsm().divide(divisor, 2, RoundingMode.UP);
        return gbAppSocialMedia + " GB";
    }

    @Named("internationalCalls")
    public static String iclConversion (PackageFrame packageFrame) {
        return packageFrame.getPackfrIcl() + " cu";
    }

    @Named("roaming")
    public static String rmgConversion (PackageFrame packageFrame) {
        return packageFrame.getPackfrRmg() + " cu";
    }


    @BeforeMapping
    protected void beforePackageFrameToPackageFrameViewDTO(PackageFrame packageFrame, @MappingTarget PackageFrameViewDTO packageFrameViewDTO){
        packageFrameViewDTO.setPhoneNumber(packageFrame.getPhone().getPhoneNumber());
        packageFrameViewDTO.setPackageCode(packageFrame.getPhone().getPackagePlan().getPackageCode());
    }

}
