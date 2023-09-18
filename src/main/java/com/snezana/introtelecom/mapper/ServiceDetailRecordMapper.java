package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.ServiceDetailRecordSaveDTO;
import com.snezana.introtelecom.dto.ServiceDetailRecordViewDTO;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.entity.PhoneService;
import com.snezana.introtelecom.entity.ServiceDetailRecord;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.repositories.PhoneRepo;
import com.snezana.introtelecom.repositories.PhoneServiceRepo;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceDetailRecordMapper {

    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "phoneService", ignore = true)
    ServiceDetailRecord serviceDetailRecordSaveDtoToServiceDetailRecord(ServiceDetailRecordSaveDTO serviceDetailRecordSaveDto, @MappingTarget ServiceDetailRecord serviceDetailRecord, @Context PhoneRepo phoneRepo, @Context PhoneService phoneService);

    @BeforeMapping
    default void beforeServiceDetailRecordSaveDtoToServiceDetailRecord(ServiceDetailRecordSaveDTO serviceDetailRecordSaveDto, @MappingTarget ServiceDetailRecord serviceDetailRecord,
                                                     @Context PhoneRepo phoneRepo, @Context PhoneServiceRepo phoneServiceRepo) {
        if (serviceDetailRecordSaveDto.getPhone() != null && (serviceDetailRecord.getPhone() == null || !serviceDetailRecord.getPhone().getPhoneNumber().equals(serviceDetailRecordSaveDto.getPhone()))) {
            final Phone phone = phoneRepo.findByPhoneNumberOpt(serviceDetailRecordSaveDto.getPhone())
                    .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Phone = " + serviceDetailRecordSaveDto.getPhone() + " is not found."));
            serviceDetailRecord.setPhone(phone);
            final PhoneService phoneService = phoneServiceRepo.findByServiceCodeOpt(serviceDetailRecordSaveDto.getPhoneService())
                    .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Service code = " + serviceDetailRecordSaveDto.getPhoneService() + " is not found."));
            serviceDetailRecord.setPhoneService(phoneService);
        }
    }

    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "phoneService", ignore = true)
    ServiceDetailRecordViewDTO serviceDetailRecordToServiceDetailRecordViewDTO(ServiceDetailRecord serviceDetailRecord);

    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "phoneService", ignore = true)
    List<ServiceDetailRecordViewDTO> serviceDetailRecordToServiceDetailRecordViewDTO (List<ServiceDetailRecord> serviceDetailRecordList);

    @BeforeMapping
    default void serviceDetailRecordToServiceDetailRecordViewDTO(ServiceDetailRecord serviceDetailRecord, @MappingTarget ServiceDetailRecordViewDTO serviceDetailRecordViewDTO){
        Phone phone = serviceDetailRecord.getPhone();
        serviceDetailRecordViewDTO.setPhone(phone.getPhoneNumber());
        PhoneService phoneService = serviceDetailRecord.getPhoneService();
        serviceDetailRecordViewDTO.setPhoneService(phoneService.getServiceCode());
    }
}
