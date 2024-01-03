package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.CustomerSaveDTO;
import com.snezana.introtelecom.dto.CustomerViewDTO;
import com.snezana.introtelecom.entity.Customer;
import com.snezana.introtelecom.entity.Phone;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    void customerSaveDtoToCustomer(CustomerSaveDTO customerSaveDto, @MappingTarget Customer customer);

    CustomerViewDTO customerToCustomerViewDTO(Customer customer);

    List<CustomerViewDTO> customersToCustomersViewDTO(List<Customer> customerList);

    @BeforeMapping
    default void customerToCustomerViewDTO(Customer customer, @MappingTarget CustomerViewDTO customerViewDTO){
        Set<Phone> phones = customer.getPhones();
        Set<String> phoneNumbers = new HashSet<>();
       for (Phone phone: phones){
           phoneNumbers.add(phone.getPhoneNumber());
       }
        customerViewDTO.setPhoneNumbers(phoneNumbers);
    }
}
