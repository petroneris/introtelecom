package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.AddonFrameSaveDTO;
import com.snezana.introtelecom.dto.AddonFrameViewDTO;
import com.snezana.introtelecom.entity.AddOn;
import com.snezana.introtelecom.entity.AddonFrame;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.repositories.AddOnRepo;
import com.snezana.introtelecom.repositories.PhoneRepo;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddonFrameMapper {

    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "addOn", ignore = true)
    AddonFrame addonFrameSaveDtoToAddonFrame(AddonFrameSaveDTO addonFrameSaveDto, @MappingTarget AddonFrame addonFrame, @Context PhoneRepo phoneRepo, @Context AddOn addOn);

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
    AddonFrameViewDTO addonFrameToAddonFrameViewDTO(AddonFrame addonFrame);

    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "addOn", ignore = true)
    List<AddonFrameViewDTO> addonFrameToAddonFrameViewDTO (List<AddonFrame> addonFrameList);

    @BeforeMapping
    default void addonFrameToAddonFrameViewDTO(AddonFrame addonFrame, @MappingTarget AddonFrameViewDTO addonFrameViewDTO){
        Phone phone = addonFrame.getPhone();
        addonFrameViewDTO.setPhone(phone.getPhoneNumber());
        AddOn addOn = addonFrame.getAddOn();
        addonFrameViewDTO.setAddOn(addOn.getAddonCode());
    }
}
