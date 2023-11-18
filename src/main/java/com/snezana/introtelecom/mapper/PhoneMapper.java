package com.snezana.introtelecom.mapper;


import com.snezana.introtelecom.dto.PhoneSaveDTO;
import com.snezana.introtelecom.dto.PhoneViewDTO;
import com.snezana.introtelecom.entity.PackagePlan;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.enums.PackagePlanType;
import com.snezana.introtelecom.enums.StatusType;
import com.snezana.introtelecom.repositories.PackagePlanRepo;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhoneMapper {
    Phone phoneSaveDtoToPhone(PhoneSaveDTO phoneSaveDto, @MappingTarget Phone phone, @Context PackagePlanRepo packagePlanRepo);

    @BeforeMapping
    default void beforePhoneSaveDtoToPhone(PhoneSaveDTO phoneSaveDto, @MappingTarget Phone phone, @Context PackagePlanRepo packagePlanRepo) {
        PackagePlan packagePlan = packagePlanRepo.findByPackageCode(phoneSaveDto.getPackageCode());
        phone.setPackagePlan(packagePlan);
        phone.setPhoneStartDateTime(LocalDateTime.now());
        phone.setPhoneStatus(StatusType.PRESENT.getStatus());
        if (phoneSaveDto.getPackageCode().equals(PackagePlanType.ADM.getPackageCode())) {
            phone.setNote("Admin phone for support");
        } else {
            phone.setNote("");
        }
    }

    PhoneViewDTO phoneToPhoneViewDTO(Phone phone);

    List<PhoneViewDTO> phonesToPhonesViewDTO(List<Phone> phoneList);

    @BeforeMapping
    default void beforePhoneToPhoneViewDTO(Phone phone, @MappingTarget PhoneViewDTO phoneViewDTO) {
        PackagePlan packagePlan = phone.getPackagePlan();
        phoneViewDTO.setPackageCode(packagePlan.getPackageCode());
    }

}