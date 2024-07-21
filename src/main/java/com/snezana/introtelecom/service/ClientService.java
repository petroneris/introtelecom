package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.entity.*;
import com.snezana.introtelecom.enums.AddonCode;
import com.snezana.introtelecom.enums.PackagePlanType;
import com.snezana.introtelecom.mapper.AddOnMapper;
import com.snezana.introtelecom.mapper.MonthlyBillFactsMapper;
import com.snezana.introtelecom.mapper.PackagePlanMapper;
import com.snezana.introtelecom.repository.*;
import com.snezana.introtelecom.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.snezana.introtelecom.enums.PackagePlanType.PRP01;
import static com.snezana.introtelecom.enums.PackagePlanType.PRP02;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {

    private final PhoneValidationService phoneValidationService;
    private final AddonFrameRepo addonFrameRepo;
    private final MonthlyBillFactsRepo monthlyBillFactsRepo;
    private final MonthlyBillFactsValidationService monthlyBillFactsValidationService;
    private final MonthlyBillFactsMapper monthlyBillFactsMapper;
    private final ServiceDetailRecordRepo serviceDetailRecordRepo;
    private final FramesSDRValidationService framesSDRValidationService;
    private final UserRepo userRepo;
    private final CustomerValidationService customerValidationService;
    private final PackagePlanRepo packagePlanRepo;
    private final AddOnRepo addOnRepo;
    private final PackagePlanMapper packagePlanMapper;
    private final AddOnMapper addOnMapper;
    private final UserValidationService userValidationService;
    private final PasswordEncoder passwordEncoder;


    /**
     * current info shows what amount of services is left
     * Client (customer role User) view
     */
    public CurrentInfo01ViewDTO getCurrentInfo(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String username = principal.toString();
        User user = userRepo.findByUsername(username);
        String phoneNumber = user.getPhoneNumber();
        Phone phone = phoneValidationService.returnThePhoneIfExists(phoneNumber);
        String packageName = phone.getPackagePlan().getPackageName();
        Customer customer = customerValidationService.returnTheCustomerWithThisPhoneIfExists(phone, "phone number");
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();
        CurrentInfoMonthlyBillFactsService cimbfService = new CurrentInfoMonthlyBillFactsService(phoneValidationService, addonFrameRepo, monthlyBillFactsRepo, monthlyBillFactsValidationService, monthlyBillFactsMapper, serviceDetailRecordRepo, framesSDRValidationService);
        CurrentInfo01ViewDTO ci01ViewDTO = cimbfService.getCurrentInfoByPhone(phoneNumber);
        if (ci01ViewDTO instanceof CurrentInfo02ViewDTO){
            return new ClientCurrentInfo02ViewDTO(phoneNumber, firstName, lastName, username, packageName, ci01ViewDTO.getPackageCode(), ci01ViewDTO.getCurrCls(), ci01ViewDTO.getCurrSms(), ((CurrentInfo02ViewDTO) ci01ViewDTO).getCurrInt(), ci01ViewDTO.getAddCls(), ci01ViewDTO.getAddSms(), ((CurrentInfo02ViewDTO) ci01ViewDTO).getAddInt(), ci01ViewDTO.getCurrDateTime());
        } else if (ci01ViewDTO instanceof CurrentInfo11ViewDTO) {
            return new ClientCurrentInfo11ViewDTO(phoneNumber, firstName, lastName, username, packageName, ci01ViewDTO.getPackageCode(), ci01ViewDTO.getCurrCls(), ci01ViewDTO.getCurrSms(), ((CurrentInfo11ViewDTO) ci01ViewDTO).getCurrInt(), ((CurrentInfo11ViewDTO) ci01ViewDTO).getCurrIcl(), ((CurrentInfo11ViewDTO) ci01ViewDTO).getCurrRmg(), ci01ViewDTO.getAddCls(), ci01ViewDTO.getAddSms(), ((CurrentInfo11ViewDTO) ci01ViewDTO).getAddInt(), ((CurrentInfo11ViewDTO) ci01ViewDTO).getAddIcl(), ((CurrentInfo11ViewDTO) ci01ViewDTO).getAddRmg(), ci01ViewDTO.getCurrDateTime());
        } else if (ci01ViewDTO instanceof CurrentInfo1234ViewDTO) {
            return new ClientCurrentInfo1234ViewDTO(phoneNumber, firstName, lastName, username, packageName, ci01ViewDTO.getPackageCode(), ci01ViewDTO.getCurrCls(), ci01ViewDTO.getCurrSms(), ((CurrentInfo1234ViewDTO) ci01ViewDTO).getCurrInt(), ((CurrentInfo1234ViewDTO) ci01ViewDTO).getCurrAsm(), ((CurrentInfo1234ViewDTO) ci01ViewDTO).getCurrIcl(), ((CurrentInfo1234ViewDTO) ci01ViewDTO).getCurrRmg(), ci01ViewDTO.getAddCls(), ci01ViewDTO.getAddSms(), ((CurrentInfo1234ViewDTO) ci01ViewDTO).getAddInt(), ((CurrentInfo1234ViewDTO) ci01ViewDTO).getAddAsm(),((CurrentInfo1234ViewDTO) ci01ViewDTO).getAddIcl(), ((CurrentInfo1234ViewDTO) ci01ViewDTO).getAddRmg(), ci01ViewDTO.getCurrDateTime());
        } else {
            return new ClientCurrentInfo01ViewDTO(phoneNumber, firstName, lastName, username, packageName, ci01ViewDTO.getPackageCode(), ci01ViewDTO.getCurrCls(), ci01ViewDTO.getCurrSms(), ci01ViewDTO.getAddCls(), ci01ViewDTO.getAddSms(), ci01ViewDTO.getCurrDateTime());
        }
    }

    public ClientMonthlyBillFactsPrpViewDTO getMonthlyBillFactsByYearAndMonth(Authentication authentication, int year, int month) {
        ClientMonthlyBillFactsPrpViewDTO cmbfPrpViewDTO = getClientMonthlyBillFactsPrpViewDTOdata(authentication);
        PackagePlanType packagePlanType = PackagePlanType.findByKey(cmbfPrpViewDTO.getPackageCode());
        monthlyBillFactsValidationService.checkYearAndMonth(year, month);
        LocalDate yearMonth = LocalDate.of(year, month, 1);
        if (packagePlanType == PRP01 || packagePlanType == PRP02) {
            String packageCode = cmbfPrpViewDTO.getPackageCode() + "  -> THERE IS NO MONTHLY BILL FOR PREPAID PHONE PACKAGES.";
            cmbfPrpViewDTO.setPackageCode(packageCode);
            return cmbfPrpViewDTO;
        }
        MonthlyBillFacts monthlyBillFacts = monthlyBillFactsValidationService.returnTheMonthlyBillFactsByYearMonthIfExists(cmbfPrpViewDTO.getPhoneNumber(), yearMonth);
        ClientMonthlyBillFactsPstViewDTO clientMonthlyBillFactsPstViewDTO = monthlyBillFactsMapper.monthlyBillFactsToClientMonthlyBillFactsPstViewDTO(monthlyBillFacts);
        clientMonthlyBillFactsPstViewDTO.setFirstName(cmbfPrpViewDTO.getFirstName());
        clientMonthlyBillFactsPstViewDTO.setLastName(cmbfPrpViewDTO.getLastName());
        clientMonthlyBillFactsPstViewDTO.setUsername(cmbfPrpViewDTO.getUsername());
        return clientMonthlyBillFactsPstViewDTO;
    }

    public List<? extends ClientMonthlyBillFactsPrpViewDTO> getMonthlyBillFactsFromStartDateToEndDate(Authentication authentication, int startYear, int startMonth, int endYear, int endMonth) {
        ClientMonthlyBillFactsPrpViewDTO cmbfPrpViewDTO = getClientMonthlyBillFactsPrpViewDTOdata(authentication);
        PackagePlanType packagePlanType = PackagePlanType.findByKey(cmbfPrpViewDTO.getPackageCode());
        monthlyBillFactsValidationService.checkYearAndMonth(startYear, startMonth);
        monthlyBillFactsValidationService.checkYearAndMonth(endYear, endMonth);
        LocalDate startDate = LocalDate.of(startYear, startMonth, 1);
        LocalDate endDate = LocalDate.of(endYear, endMonth, 1);
        monthlyBillFactsValidationService.controlTheStartDateIsLessThanEndDate(startDate, endDate);
        if (packagePlanType == PRP01 || packagePlanType == PRP02) {
            String packageCode = cmbfPrpViewDTO.getPackageCode() + "  -> THERE IS NO MONTHLY BILL FOR PREPAID PHONE PACKAGES.";
            cmbfPrpViewDTO.setPackageCode(packageCode);
            List<ClientMonthlyBillFactsPrpViewDTO> clientMonthlyBillFactsPrpViewDTOList = new ArrayList<>();
            clientMonthlyBillFactsPrpViewDTOList.add(cmbfPrpViewDTO);
            return clientMonthlyBillFactsPrpViewDTOList;
        }
        List<MonthlyBillFacts> monthlyBillFactsList = monthlyBillFactsRepo.findByPhone_PhoneNumberAndYearMonthStartsWithAndYearMonthEndsWith(cmbfPrpViewDTO.getPhoneNumber(), startDate, endDate);
        List<ClientMonthlyBillFactsPstViewDTO> clientMonthlyBillFactsPstViewDTOList = monthlyBillFactsMapper.monthlyBillFactsListToClientMonthlyBillFactsPstViewDTOList(monthlyBillFactsList);
        for (ClientMonthlyBillFactsPstViewDTO cmbf : clientMonthlyBillFactsPstViewDTOList) {
            cmbf.setFirstName(cmbfPrpViewDTO.getFirstName());
            cmbf.setLastName(cmbfPrpViewDTO.getLastName());
            cmbf.setUsername(cmbfPrpViewDTO.getUsername());
        }
        return clientMonthlyBillFactsPstViewDTOList;
    }

    public void changePassword(Authentication authentication, ClientChangePasswordDTO clientChangePasswordDto) {
        Object principal = authentication.getPrincipal();
        String username = principal.toString();
        User user = userRepo.findByUsername(username);
        String oldPassword = clientChangePasswordDto.getOldPassword();
        userValidationService.checkIfOldPasswordIsValid(oldPassword, user.getPassword());
        user.setPassword(passwordEncoder.encode(clientChangePasswordDto.getNewPassword()));
    }

    public List<PackagePlanDTO> getAllCustomersPackagePlans() {
        List<PackagePlan> packagePlanList = packagePlanRepo.findAllCustomersPackagePlans();
        return packagePlanMapper.packagePlanToPackagePlanDTO(packagePlanList);
    }

    public List<AddOnDTO> getAllAddOnDetails() {
        List<AddOn> addOnList = addOnRepo.findAll();
        List<AddOnDTO> addOnDTOList = addOnMapper.addOnToAddOnDTO(addOnList);
        String iclStr = "; zone1 -> 15 cu/min;  zone2 -> 20 cu/min;";
        String rmgStr = "; zone1 -> 4 cu/min;  zone2 -> 8 cu/min;";
        for (AddOnDTO addonDTO : addOnDTOList) {
            addonDTO.setAddonPrice(addonDTO.getAddonPrice() + " cu");
            if (addonDTO.getAddonCode().equals(AddonCode.ADDICL.name())) {
                addonDTO.setAddonDescription(addonDTO.getAddonDescription() + iclStr);
            }
            if (addonDTO.getAddonCode().equals(AddonCode.ADDRMG.name())) {
                addonDTO.setAddonDescription(addonDTO.getAddonDescription() + rmgStr);
            }
        }
        return addOnDTOList;
    }

    private ClientMonthlyBillFactsPrpViewDTO getClientMonthlyBillFactsPrpViewDTOdata(Authentication authentication) {
        monthlyBillFactsValidationService.controlTheTimeForScheduling();
        Object principal = authentication.getPrincipal();
        String username = principal.toString();
        User user = userRepo.findByUsername(username);
        String phoneNumber = user.getPhoneNumber();
        Phone phone = phoneValidationService.returnThePhoneIfExists(phoneNumber);
        phoneValidationService.controlThisPhoneHasCustomersPackageCode(phone);
        String packageCode = phone.getPackagePlan().getPackageCode();
        String packageName = phone.getPackagePlan().getPackageName();
        Customer customer = customerValidationService.returnTheCustomerWithThisPhoneIfExists(phone, "phone number");
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();
        return new ClientMonthlyBillFactsPrpViewDTO(phoneNumber, firstName, lastName, username, packageName, packageCode);
    }

}
