package com.snezana.introtelecom.validations;

import com.snezana.introtelecom.entity.Customer;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.exceptions.IllegalItemFieldException;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.repositories.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerValidationService {

    private final CustomerRepo customerRepo;

    public void controlThePersonalNumberIsUnique(String personalNumber) {
        Optional<Customer> customerOptional = customerRepo.findByPersonalNumberOpt(personalNumber);
        customerOptional.ifPresent(customer ->  {
            throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "This personal number already exists in database!");
        });
    }

    public Customer returnTheCustomerWithThatPersonalNumberIfExists(String personalNumber) {
        return customerRepo.findByPersonalNumberOpt(personalNumber)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The customer with that personalNumber doesn't exist in database!"));

    }

    public Customer returnTheCustomerWithThatIdIfExists (Long customerId){
        return customerRepo.findByCustomerIdOpt(customerId)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The customer with that Id doesn't exist in database!"));
    }

    public Customer returnTheCustomerWithThatEmailIfExists (String email){
        return customerRepo.findByEmailOpt(email)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The customer with that email doesn't exist in database!"));
    }

    public void controlTheEmailIsUnique (String email){
        Optional<Customer> customerOptional = customerRepo.findByEmailOpt(email);
        customerOptional.ifPresent(customer ->  {
                throw new ItemNotFoundException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The customer with that email already exists in database!");
        });
    }

    public void controlTheOtherCustomerHasThisPersonalNumber(String personalNumber, Long customerId){
        Optional<Customer> customerOptional = customerRepo.findByPersonalNumberOpt(personalNumber);
        customerOptional.ifPresent(customer ->  {
            if(!customer.getCustomerId().equals(customerId)){
                throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The other customer with that personal number already exists in database!");
            }
        });
    }

    public void controlTheOtherCustomerHasThisEmail(String email, Long customerId){
        Optional<Customer> customerOptional = customerRepo.findByEmailOpt(email);
        customerOptional.ifPresent(customer ->  {
            if(!customer.getCustomerId().equals(customerId)){
                throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The other customer with that email already exists in database!");
            }
        });
    }

    public void checkThatPhoneBelongsToCustomerWithThisId (Customer customer, Phone phone){
        if(!customer.getPhones().contains(phone)){
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "This phone number doesn't belong to this customer!");
        }
    }

    public void controlThatPhoneAlreadyBelongsToSomeCustomer (String phoneNumber){
        Optional<Customer> customerOptional = customerRepo.findByPhoneNumberOpt(phoneNumber);
        customerOptional.ifPresent(customer ->  {
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "This phone number already belongs to some customer!");
        });
    }

    public Customer returnTheCustomerWithThisPhoneIfExists (Phone phone, String text){
        Optional<Customer> customerOpt = Optional.empty();
        List<Customer> customerList = customerRepo.findAll();
        for (Customer custm: customerList){
            Set<Phone> phones = custm.getPhones();
            if(phones.contains(phone)) {
                customerOpt = Optional.of(custm);
                break;
            }
        }
        return customerOpt
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The customer with that " +text + " doesn't exist in database!"));
    }

}
