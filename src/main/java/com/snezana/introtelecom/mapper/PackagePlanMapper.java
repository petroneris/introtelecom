package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.CustomerSaveDTO;
import com.snezana.introtelecom.dto.CustomerViewDTO;
import com.snezana.introtelecom.dto.PackagePlanDTO;
import com.snezana.introtelecom.entity.Customer;
import com.snezana.introtelecom.entity.PackagePlan;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PackagePlanMapper {

    PackagePlan packagePlanDtoToPackagePlan(PackagePlanDTO packagePlanDto, @MappingTarget PackagePlan packagePlan);

    PackagePlanDTO packagePlanToPackagePlanDTO(PackagePlan packagePlan);

    List<PackagePlanDTO> packagePlanToPackagePlanDTO (List<PackagePlan> packagePlanList);
}
