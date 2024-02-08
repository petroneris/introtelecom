package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.AddonFrameSaveDTO;
import com.snezana.introtelecom.dto.AddonFrameViewDTO;
import com.snezana.introtelecom.entity.AddOn;
import com.snezana.introtelecom.entity.AddonFrame;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.enums.AddonCode;
import com.snezana.introtelecom.enums.StatusType;
import com.snezana.introtelecom.repository.AddOnRepo;
import com.snezana.introtelecom.repository.PhoneRepo;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AddonFrameMapper {

    public abstract AddonFrame addonFrameSaveDtoToAddonFrame(AddonFrameSaveDTO addonFrameSaveDto, @Context PhoneRepo phoneRepo, @Context AddOnRepo addOnRepo);

    @BeforeMapping
    protected void beforeAddonFrameSaveDtoToAddonFrame(AddonFrameSaveDTO addonFrameSaveDto, @MappingTarget AddonFrame addonFrame,
                                                         @Context PhoneRepo phoneRepo, @Context AddOnRepo addOnRepo) {
        Phone phone = phoneRepo.findByPhoneNumber(addonFrameSaveDto.getPhoneNumber());
        addonFrame.setPhone(phone);
        AddOn addOn = addOnRepo.findByAddonCode(addonFrameSaveDto.getAddonCode());
        addonFrame.setAddOn(addOn);
        addonFrame.setAddfrStatus(StatusType.PRESENT.getStatus());
    }

    @AfterMapping
    protected void afterAddonFrameSaveDtoToAddonFrame(@MappingTarget AddonFrame addonFrame) {
        String addonCodeStr = addonFrame.getAddOn().getAddonCode();
        AddonCode addonCode = AddonCode.valueOf(addonCodeStr);
        switch (addonCode) {
            case ADDCLS:
                setAddonFrameQuota(addonFrame, 100, 0, "0.00", "0.00", "0.00", "0.00");
                break;
            case ADDSMS:
                setAddonFrameQuota(addonFrame, 0, 100, "0.00", "0.00", "0.00", "0.00");
                break;
            case ADDINT:
                setAddonFrameQuota(addonFrame, 0, 0, "5000.00", "0.00", "0.00", "0.00");
                break;
            case ADDASM:
                setAddonFrameQuota(addonFrame, 0, 0, "0.00", "5000.00", "0.00", "0.00");
                break;
            case ADDICL:
                setAddonFrameQuota(addonFrame, 0, 0, "0.00", "0.00", "200.00", "0.00");
                break;
            case ADDRMG:
                setAddonFrameQuota(addonFrame, 0, 0, "0.00", "0.00", "0.00", "200.00");
                break;
            default:
        }
    }

    private void setAddonFrameQuota (AddonFrame addonFrame, int quotaCls, int quotaSms, String quotaInt, String quotaAsm, String quotaIcl, String quotaRmg){
        addonFrame.setAddfrCls(quotaCls);
        addonFrame.setAddfrSms(quotaSms);
        addonFrame.setAddfrInt(new BigDecimal(quotaInt));
        addonFrame.setAddfrAsm(new BigDecimal(quotaAsm));
        addonFrame.setAddfrIcl(new BigDecimal(quotaIcl));
        addonFrame.setAddfrRmg(new BigDecimal(quotaRmg));
    }

    public abstract AddonFrameViewDTO addonFrameToAddonFrameViewDTO(AddonFrame addonFrame);


    public abstract List<AddonFrameViewDTO> addonFramesToAddonFramesViewDTO(List<AddonFrame> addonFrameList);

    @BeforeMapping
    protected void beforeAddonFrameToAddonFrameViewDTO(AddonFrame addonFrame, @MappingTarget AddonFrameViewDTO addonFrameViewDTO) {
        addonFrameViewDTO.setPhoneNumber(addonFrame.getPhone().getPhoneNumber());
        addonFrameViewDTO.setAddonCode(addonFrame.getAddOn().getAddonCode());
        addonFrameViewDTO.setAddfrStatus(addonFrame.getAddfrStatus());
    }


    @AfterMapping
    protected void afterAddonFrameToAddonFrameViewDTO(AddonFrame addonFrame, @MappingTarget AddonFrameViewDTO addonFrameViewDTO) {
        AddonCode addonCode = AddonCode.valueOf(addonFrameViewDTO.getAddonCode());
        switch (addonCode) {
            case ADDCLS:
                addonFrameViewDTO.setAddfrCls("calls " + addonFrame.getAddfrCls() + "min -> 100.00cu");
                break;
            case ADDSMS:
                addonFrameViewDTO.setAddfrSms("sms " + addonFrame.getAddfrSms() + "msg -> 100.00cu");
                break;
            case ADDINT:
                BigDecimal gbInternet = addonFrame.getAddfrInt().divide(new BigDecimal("1000"), 2, RoundingMode.UP);
                addonFrameViewDTO.setAddfrInt("internet " + gbInternet + "GB -> 200.00cu");
                break;
            case ADDASM:
                BigDecimal gbAppSocialMedia = addonFrame.getAddfrAsm().divide(new BigDecimal("1000"), 2, RoundingMode.UP);
                addonFrameViewDTO.setAddfrAsm("app and social media " + gbAppSocialMedia + "GB -> 200.00cu");
                break;
            case ADDICL:
                addonFrameViewDTO.setAddfrIcl("international calls -> " + addonFrame.getAddfrIcl() + "cu");
                break;
            case ADDRMG:
                addonFrameViewDTO.setAddfrRmg("roaming -> " + addonFrame.getAddfrRmg() + "cu");
                break;
            default:
        }
    }

}
