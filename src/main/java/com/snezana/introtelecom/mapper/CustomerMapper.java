package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.AdminSaveDTO;
import com.snezana.introtelecom.dto.AdminViewDTO;
import com.snezana.introtelecom.dto.CustomerSaveDTO;
import com.snezana.introtelecom.dto.CustomerViewDTO;
import com.snezana.introtelecom.entity.Admin;
import com.snezana.introtelecom.entity.Customer;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.repositories.PhoneRepo;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    Customer customerSaveDtoToCustomer(CustomerSaveDTO customerSaveDto, @MappingTarget Customer customer);

    @Mapping(target = "phones", ignore = true)
    CustomerViewDTO customerToCustomerViewDTO(Customer customer);

    @Mapping(target = "phones", ignore = true)
    List<CustomerViewDTO> customerToCustomerViewDTO (List<Customer> customerList);

    @BeforeMapping
    default void customerToCustomerViewDTO(Customer customer, @MappingTarget CustomerViewDTO customerViewDTO){
        Set<Phone> phones = customer.getPhones();
        Set<String> phoneNumbers = new HashSet<>();
       for (Phone phone: phones){
           phoneNumbers.add(phone.getPhoneNumber());
       }
        customerViewDTO.setPhones(phoneNumbers);
    }
}
