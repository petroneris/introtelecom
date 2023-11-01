package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.ServiceDetailRecordSaveDTO;
import com.snezana.introtelecom.dto.ServiceDetailRecordViewDTO;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.entity.PhoneService;
import com.snezana.introtelecom.entity.ServiceDetailRecord;
import com.snezana.introtelecom.repositories.PhoneRepo;
import com.snezana.introtelecom.repositories.PhoneServiceRepo;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceDetailRecordMapper {

    void serviceDetailRecordSaveDtoToServiceDetailRecord(ServiceDetailRecordSaveDTO serviceDetailRecordSaveDto, @MappingTarget ServiceDetailRecord serviceDetailRecord, @Context PhoneRepo phoneRepo, @Context PhoneServiceRepo phoneServiceRepo);

    @BeforeMapping
    default void beforeServiceDetailRecordSaveDtoToServiceDetailRecord(ServiceDetailRecordSaveDTO serviceDetailRecordSaveDto, @MappingTarget ServiceDetailRecord serviceDetailRecord, @Context PhoneRepo phoneRepo, @Context PhoneServiceRepo phoneServiceRepo) {
        Phone phone = phoneRepo.findByPhoneNumber(serviceDetailRecordSaveDto.getPhoneNumber());
        serviceDetailRecord.setPhone(phone);
        PhoneService phoneService = phoneServiceRepo.findByServiceCode(serviceDetailRecordSaveDto.getServiceCode());
        serviceDetailRecord.setPhoneService(phoneService);
    }

    ServiceDetailRecordViewDTO serviceDetailRecordToServiceDetailRecordViewDTO(ServiceDetailRecord serviceDetailRecord);

    List<ServiceDetailRecordViewDTO> serviceDetailRecordsToServiceDetailRecordsViewDTO(List<ServiceDetailRecord> serviceDetailRecordList);

    @BeforeMapping
    default void serviceDetailRecordToServiceDetailRecordViewDTO(ServiceDetailRecord serviceDetailRecord, @MappingTarget ServiceDetailRecordViewDTO serviceDetailRecordViewDTO) {
        Phone phone = serviceDetailRecord.getPhone();
        serviceDetailRecordViewDTO.setPhoneNumber(phone.getPhoneNumber());
        PhoneService phoneService = serviceDetailRecord.getPhoneService();
        serviceDetailRecordViewDTO.setServiceCode(phoneService.getServiceCode());
    }
}
