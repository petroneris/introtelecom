package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.AddonFrameSaveDTO;
import com.snezana.introtelecom.dto.AddonFrameViewDTO;
import com.snezana.introtelecom.entity.AddOn;
import com.snezana.introtelecom.entity.AddonFrame;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.enums.PackageCodeType;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.repositories.AddOnRepo;
import com.snezana.introtelecom.repositories.PhoneRepo;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddonFrameMapper {

    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "addOn", ignore = true)
    AddonFrame addonFrameSaveDtoToAddonFrame(AddonFrameSaveDTO addonFrameSaveDto, @MappingTarget AddonFrame addonFrame, @Context PhoneRepo phoneRepo, @Context AddOnRepo addOnRepo);

    @BeforeMapping
    default void beforeAddonFrameSaveDtoToAddonFrame(AddonFrameSaveDTO addonFrameSaveDto, @MappingTarget AddonFrame addonFrame,
                                                         @Context PhoneRepo phoneRepo, @Context AddOnRepo addOnRepo) {
        if (addonFrameSaveDto.getPhone() != null && (addonFrame.getPhone() == null || !addonFrame.getPhone().getPhoneNumber().equals(addonFrameSaveDto.getPhone()))) {
            final Phone phone = phoneRepo.findByPhoneNumberOpt(addonFrameSaveDto.getPhone())
                    .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Phone = " + addonFrameSaveDto.getPhone() + " is not found."));
            addonFrame.setPhone(phone);
            final AddOn addOn = addOnRepo.findByAddonCodeOpt(addonFrameSaveDto.getAddOn())
                    .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "AddOn code = " + addonFrameSaveDto.getAddOn() + " is not found."));
            addonFrame.setAddOn(addOn);
        }
    }

    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "addOn", ignore = true)
    @Mapping(target = "addfrCls", ignore = true)
    @Mapping(target = "addfrSms", ignore = true)
    @Mapping(target = "addfrInt", ignore = true)
    @Mapping(target = "addfrAsm", ignore = true)
    @Mapping(target = "addfrIcl", ignore = true)
    @Mapping(target = "addfrRmg", ignore = true)
    AddonFrameViewDTO addonFrameToAddonFrameViewDTO(AddonFrame addonFrame);

    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "addOn", ignore = true)
    @Mapping(target = "addfrCls", ignore = true)
    @Mapping(target = "addfrSms", ignore = true)
    @Mapping(target = "addfrInt", ignore = true)
    @Mapping(target = "addfrAsm", ignore = true)
    @Mapping(target = "addfrIcl", ignore = true)
    @Mapping(target = "addfrRmg", ignore = true)
    List<AddonFrameViewDTO> addonFrameToAddonFrameViewDTO (List<AddonFrame> addonFrameList);

    @BeforeMapping
    default void addonFrameToAddonFrameViewDTO(AddonFrame addonFrame, @MappingTarget AddonFrameViewDTO addonFrameViewDTO){
        Phone phone = addonFrame.getPhone();
        addonFrameViewDTO.setPhone(phone.getPhoneNumber());
        AddOn addOn = addonFrame.getAddOn();
        String addonCode = addOn.getAddonCode();
        addonFrameViewDTO.setAddOn(addonCode);
        switch (addonCode) {
            case "ADDCLS":
                addonFrameViewDTO.setAddfrCls("calls 100min -> 100cu");
                addonFrameViewDTO.setAddfrSms(" / ");
                addonFrameViewDTO.setAddfrInt(" / ");
                addonFrameViewDTO.setAddfrAsm(" / ");
                addonFrameViewDTO.setAddfrIcl(" / ");
                addonFrameViewDTO.setAddfrRmg(" / ");
                break;
            case "ADDSMS":
                addonFrameViewDTO.setAddfrCls(" / ");
                addonFrameViewDTO.setAddfrSms("sms 100msg -> 100cu");
                addonFrameViewDTO.setAddfrInt(" / ");
                addonFrameViewDTO.setAddfrAsm(" / ");
                addonFrameViewDTO.setAddfrIcl(" / ");
                addonFrameViewDTO.setAddfrRmg(" / ");
                break;
            case "ADDINT":
                addonFrameViewDTO.setAddfrCls(" / ");
                addonFrameViewDTO.setAddfrSms(" / ");
                addonFrameViewDTO.setAddfrInt("internet 5GB -> 200cu");
                addonFrameViewDTO.setAddfrAsm(" / ");
                addonFrameViewDTO.setAddfrIcl(" / ");
                addonFrameViewDTO.setAddfrRmg(" / ");
                break;
            case "ADDASM":
                addonFrameViewDTO.setAddfrCls(" / ");
                addonFrameViewDTO.setAddfrSms(" / ");
                addonFrameViewDTO.setAddfrInt(" / ");
                addonFrameViewDTO.setAddfrAsm("app and social media 5GB -> 200cu");
                addonFrameViewDTO.setAddfrIcl(" / ");
                addonFrameViewDTO.setAddfrRmg(" / ");
                break;
            case "ADDICL":
                addonFrameViewDTO.setAddfrCls(" / ");
                addonFrameViewDTO.setAddfrSms(" / ");
                addonFrameViewDTO.setAddfrInt(" / ");
                addonFrameViewDTO.setAddfrAsm(" / ");
                addonFrameViewDTO.setAddfrIcl("international calls -> 200cu");
                addonFrameViewDTO.setAddfrRmg(" / ");
                break;
            case "ADDRMG":
                addonFrameViewDTO.setAddfrCls(" / ");
                addonFrameViewDTO.setAddfrSms(" / ");
                addonFrameViewDTO.setAddfrInt(" / ");
                addonFrameViewDTO.setAddfrAsm(" / ");
                addonFrameViewDTO.setAddfrIcl(" / ");
                addonFrameViewDTO.setAddfrRmg("roaming -> 200cu");
                break;
                default:

        }
    }
}
