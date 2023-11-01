package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.PackageFrameSaveDTO;
import com.snezana.introtelecom.dto.PackageFrameViewDTO;
import com.snezana.introtelecom.entity.PackageFrame;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.enums.PackagePlanType;
import com.snezana.introtelecom.enums.StatusType;
import com.snezana.introtelecom.repositories.PhoneRepo;
import org.mapstruct.*;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PackageFrameMapper {
    Logger log = org.slf4j.LoggerFactory.getLogger(PackageFrameMapper.class);

    public abstract PackageFrame packageFrameSaveDtoToPackageFrame(PackageFrameSaveDTO packageFrameSaveDto, @Context PhoneRepo phoneRepo);

    @AfterMapping
    protected void afterPackageFrameSaveDtoToPackageFrame(PackageFrameSaveDTO packageFrameSaveDto, @MappingTarget PackageFrame packageFrame, @Context PhoneRepo phoneRepo) {
        Phone phone = phoneRepo.findByPhoneNumber(packageFrameSaveDto.getPhoneNumber());
        packageFrame.setPhone(phone);
        packageFrame.setPackfrStatus(StatusType.PRESENT.getStatus());
        setPackageFrameByPackageCode(packageFrame);
    }

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

    private void setMonthlyPackageFrameQuota (PackageFrame packageFrame, int monthlyQuotaCls, int monthlyQuotaSms, String monthlyQuotaInt, String monthlyQuotaAsm, String monthlyQuotaIcl, String monthlyQuotaRmg){
        packageFrame.setPackfrCls(monthlyQuotaCls);
        packageFrame.setPackfrSms(monthlyQuotaSms);
        packageFrame.setPackfrInt(new BigDecimal(monthlyQuotaInt));
        packageFrame.setPackfrAsm(new BigDecimal(monthlyQuotaAsm));
        packageFrame.setPackfrIcl(new BigDecimal(monthlyQuotaIcl));
        packageFrame.setPackfrRmg(new BigDecimal(monthlyQuotaRmg));

    }

    private void setPackageFrameByPackageCode(PackageFrame packageFrame) {
        String packageCode = packageFrame.getPhone().getPackagePlan().getPackageCode();
        PackagePlanType packagePlanType = PackagePlanType.findByKey(packageCode);
        switch (packagePlanType) {
            case PRP01:
                setMonthlyPackageFrameQuota(packageFrame, 200, 200, "0.00", "0.00", "0.00", "0.00");
                break;
            case PRP02:
                setMonthlyPackageFrameQuota(packageFrame, 200, 200, "10000.00", "0.00", "0.00", "0.00");
                break;
            case PST11:
                setMonthlyPackageFrameQuota(packageFrame, 300, 300, "10000.00", "0.00", "200.00", "200.00");
                break;
            case PST12:
                setMonthlyPackageFrameQuota(packageFrame, 400, 400, "10000.00", "5000.00", "200.00", "200.00");
                break;
            case PST13:
                setMonthlyPackageFrameQuota(packageFrame, -1, -1, "15000.00", "5000.00", "200.00", "200.00");
                break;
            case PST14:
                setMonthlyPackageFrameQuota(packageFrame, -1, -1, "-1.00", "10000.00", "200.00", "200.00");
                break;
            default:
        }
    }

}
