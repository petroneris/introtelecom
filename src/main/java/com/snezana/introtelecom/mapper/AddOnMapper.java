package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.AddOnDTO;
import com.snezana.introtelecom.entity.AddOn;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddOnMapper {
    AddOnDTO addOnToAddOnDTO(AddOn addOn);

    List<AddOnDTO> addOnToAddOnDTO (List<AddOn> addOnList);
}
