package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.PhoneServiceDTO;
import com.snezana.introtelecom.entity.PhoneService;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhoneServiceMapper {
    PhoneServiceDTO phoneServiceToPhoneServiceDTO(PhoneService phoneService);

    List<PhoneServiceDTO> phoneServiceToPhoneServiceDTO (List<PhoneService> phoneServiceList);
}
