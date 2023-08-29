package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.AddOnDTO;
import com.snezana.introtelecom.dto.PackagePlanDTO;
import com.snezana.introtelecom.entity.AddOn;
import com.snezana.introtelecom.entity.PackagePlan;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddOnMapper {

    AddOn addOnDtoToAddOn(AddOnDTO addOnDto, @MappingTarget AddOn addOn);

    AddOnDTO addOnToAddOnDTO(AddOn addOn);

    List<AddOnDTO> addOnToAddOnDTO (List<AddOn> addOnList);
}
