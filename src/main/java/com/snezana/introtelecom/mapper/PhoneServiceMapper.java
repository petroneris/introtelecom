package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.AddOnDTO;
import com.snezana.introtelecom.dto.PhoneServiceDTO;
import com.snezana.introtelecom.entity.AddOn;
import com.snezana.introtelecom.entity.PhoneService;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PhoneServiceMapper {

    PhoneService phoneServiceDtoToPhoneService(PhoneServiceDTO phoneServiceDto, @MappingTarget PhoneService phoneService);

    PhoneServiceDTO phoneServiceToPhoneServiceDTO(PhoneService phoneService);

    List<PhoneServiceDTO> phoneServiceToPhoneServiceDTO (List<PhoneService> phoneServiceList);
}
