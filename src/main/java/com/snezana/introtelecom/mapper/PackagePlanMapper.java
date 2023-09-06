package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.PackagePlanDTO;
import com.snezana.introtelecom.entity.PackagePlan;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PackagePlanMapper {

    PackagePlan packagePlanDtoToPackagePlan(PackagePlanDTO packagePlanDto, @MappingTarget PackagePlan packagePlan);

    PackagePlanDTO packagePlanToPackagePlanDTO(PackagePlan packagePlan);

    List<PackagePlanDTO> packagePlanToPackagePlanDTO (List<PackagePlan> packagePlanList);
}
