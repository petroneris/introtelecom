package com.snezana.introtelecom.validations;

import com.snezana.introtelecom.entity.Customer;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.exceptions.IllegalItemFieldException;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.repositories.CustomerRepo;
import com.snezana.introtelecom.repositories.PhoneRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerValidationService {

    private final PhoneRepo phoneRepo;
    private final CustomerRepo customerRepo;

    public void controlThePersonalNumberIsUnique(String personalNumber) {
        Optional<Customer> customerOptional = customerRepo.findByPersonalNumberOpt(personalNumber);
        if (customerOptional.isPresent()) {
            throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "This personal number exists in database!");
        }
    }

    public void controlThePersonalNumberExists(String personalNumber) {
        customerRepo.findByPersonalNumberOpt(personalNumber)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The customer with that personalNumber doesn't exist in database!"));

    }

    public void controlTheCustomerWithThatIdExists (Long customerId){
        customerRepo.findByCustomerIdOpt(customerId)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The customer with that Id doesn't exist in database!"));
    }

    public void controlTheEmailExists (String email){
        customerRepo.findByEmailOpt(email)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The customer with that email doesn't exist in database!"));
    }

    public void controlTheEmailIsUnique (String email){
        Optional<Customer> customerOptional = customerRepo.findByEmailOpt(email);
        if (customerOptional.isPresent()) {
                throw new ItemNotFoundException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The customer with that email exists in database!");
        }
    }

    public void controlUpdateTheOtherCustomerHasThisPersonalNumber(String personalNumber, Long customerId){
        Optional<Customer> customerOptional = customerRepo.findByPersonalNumberOpt(personalNumber);
        if (customerOptional.isPresent()) {
            Customer customer = customerRepo.findByPersonalNumber(personalNumber);
            if(!customer.getCustomerId().equals(customerId)){
                throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The other customer with that personal number exists in database!");
            }
        }
    }

    public void controlUpdateTheOtherCustomerHasThisEmail(String email, Long customerId){
        Optional<Customer> customerOptional = customerRepo.findByEmailOpt(email);
        if (customerOptional.isPresent()) {
            Customer customer = customerRepo.findCustomerByEmail(email);
            if(!customer.getCustomerId().equals(customerId)){
                throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The other customer with that email exists in database!");
            }
        }
    }

    public void checkThatPhoneBelongsToCustomerWithThisId (Long customerId, String phoneNumber){
        Phone phone = phoneRepo.findByPhoneNumber(phoneNumber);
        Customer customer = customerRepo.findByCustomerId(customerId);
        if(!customer.getPhones().contains(phone)){
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "This phone number doesn't belong to this customer!");
        }
    }

    public void controlThatPhoneAlreadyBelongsToSomeCustomer (String phoneNumber){
        Optional<Customer> customerOptional = customerRepo.findByPhoneNumberOpt(phoneNumber);
        if (customerOptional.isPresent()) {
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "This phone number already belongs to some customer!");
        }
    }

}
