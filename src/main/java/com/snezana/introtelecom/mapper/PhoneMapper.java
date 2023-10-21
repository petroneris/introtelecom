package com.snezana.introtelecom.mapper;


import com.snezana.introtelecom.dto.PhoneSaveDTO;
import com.snezana.introtelecom.dto.PhoneViewDTO;
import com.snezana.introtelecom.entity.PackagePlan;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.enums.PackageCodeType;
import com.snezana.introtelecom.enums.StatusType;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.repositories.PackagePlanRepo;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhoneMapper {

    @Mapping(target = "packagePlan", ignore = true)
    Phone phoneSaveDtoToPhone(PhoneSaveDTO phoneSaveDto, @MappingTarget Phone phone,
                          @Context PackagePlanRepo packagePlanRepo);

//    @AfterMapping
//    default void afterPhoneToPhoneSaveDto(Phone phone, @MappingTarget PhoneSaveDTO phoneSaveDto) {
//        phoneSaveDto.setPackagePlan(phone.getPackagePlan() == null ? null : phone.getPackagePlan().getPackageCode());
//    }

    @BeforeMapping
    default void beforePhoneSaveDtoToPhone(PhoneSaveDTO phoneSaveDto, @MappingTarget Phone phone,
                                    @Context PackagePlanRepo packagePlanRepo) {
        if (phoneSaveDto.getPackagePlan() != null && (phone.getPackagePlan() == null || !phone.getPackagePlan().getPackageCode().equals(phoneSaveDto.getPackagePlan()))) {
            final PackagePlan packagePlan = packagePlanRepo.findByPackageCodeOpt(phoneSaveDto.getPackagePlan())
                    .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Package code = " + phoneSaveDto.getPackagePlan() + " is not found."));
            phone.setPackagePlan(packagePlan);
            phone.setPhoneStartDateTime(LocalDateTime.now());
            phone.setPhoneStatus(StatusType.PRESENT.getStatus());
            if (phoneSaveDto.getPackagePlan().equals(PackageCodeType.ADM.getPackageCode())){
                phone.setNote("Admin phone for support");
            } else {
                phone.setNote("");
            }
        }
    }

    @Mapping(target = "packagePlan", ignore = true)
    PhoneViewDTO phoneToPhoneViewDTO(Phone phone);

    @Mapping(target = "packagePlan", ignore = true)
    List<PhoneViewDTO> phoneToPhoneViewDTO (List<Phone> phoneList);

    @BeforeMapping
    default void beforePhoneToPhoneViewDTO(Phone phone, @MappingTarget PhoneViewDTO phoneViewDTO){
        PackagePlan packagePlan = phone.getPackagePlan();
        phoneViewDTO.setPackagePlan(packagePlan.getPackageCode());
    }

}