package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.entity.*;
import com.snezana.introtelecom.enums.AddonCode;
import com.snezana.introtelecom.enums.PackagePlanType;
import com.snezana.introtelecom.enums.SDRCode;
import com.snezana.introtelecom.mapper.AddOnMapper;
import com.snezana.introtelecom.mapper.MonthlyBillFactsMapper;
import com.snezana.introtelecom.mapper.PackagePlanMapper;
import com.snezana.introtelecom.repository.*;
import com.snezana.introtelecom.validation.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static com.snezana.introtelecom.enums.PackagePlanType.*;
import static com.snezana.introtelecom.enums.SDRCode.*;
import static com.snezana.introtelecom.service.FramesSDRServiceImpl.*;
import static com.snezana.introtelecom.service.FramesSDRServiceImpl.UNIT_PRICE_RMGCZ2;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ClientServiceImpl.class);

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

    @Override
    public ClientCurrentInfo01ViewDTO getCurrentInfo(Authentication authentication) {
        monthlyBillFactsValidationService.controlTheTimeForScheduling();
        String currCls;
        String currSms;
        String currInt;
        String currAsm;
        String currIcl;
        String currRmg;
        String clsNote = "";
        String smsNote = "";
        String intNote = "";
        String asmNote = "";
        String iclNote = "";
        String rmgNote = "";
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
        PackagePlanType packagePlanType = PackagePlanType.findByKey(packageCode);
        Month currentMonth = LocalDateTime.now().getMonth();
        int currentYear = LocalDateTime.now().getYear();
        LocalDateTime monthlyStartDateTime = LocalDateTime.of(currentYear, currentMonth, 1, 0, 0, 0, 0);
        LocalDateTime monthlyEndDateTime = monthlyStartDateTime.plusMonths(1);
        LocalDateTime nowDateTime = LocalDateTime.now();
        CurrentInfoMonthlyBillFactsServiceImpl cimbfServiceImpl = new CurrentInfoMonthlyBillFactsServiceImpl(phoneValidationService, addonFrameRepo, monthlyBillFactsRepo, monthlyBillFactsValidationService, monthlyBillFactsMapper, serviceDetailRecordRepo, framesSDRValidationService);
        PackageFrame monthlyPackageFrame = framesSDRValidationService.returnTheMonthlyPackageFrameIfExists(phone.getPhoneNumber(), monthlyStartDateTime, monthlyEndDateTime);
        List<AddonFrame> addonFrameList = addonFrameRepo.findByPhone_PhoneNumberAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual(phone.getPhoneNumber(), monthlyStartDateTime, monthlyEndDateTime);
        log.info("addonFrameList is size = " + addonFrameList.size());
        MonthlyFramesInputTotal monthlyFramesInputTotal = cimbfServiceImpl.inputAmountOfFramesCurrentInfo(monthlyPackageFrame, addonFrameList);
        SdrOutputTotal sdrOutputTotal = cimbfServiceImpl.outputAmountOfSDRsCurrentInfo(phoneNumber, monthlyStartDateTime, nowDateTime);
        if (packagePlanType == PackagePlanType.PST13 || packagePlanType == PackagePlanType.PST14) {
            currCls = "UNLIMITED";
            currSms = "UNLIMITED";
        } else {
            if (monthlyFramesInputTotal.getInputCls() - sdrOutputTotal.getOutputCls() > 0) {
                currCls = monthlyFramesInputTotal.getInputCls() - sdrOutputTotal.getOutputCls() + " min left";
            } else {
                currCls = monthlyFramesInputTotal.getInputCls() - sdrOutputTotal.getOutputCls() + " min";
            }
            if (monthlyFramesInputTotal.getInputSms() - sdrOutputTotal.getOutputSms() > 0) {
                currSms = monthlyFramesInputTotal.getInputSms() - sdrOutputTotal.getOutputSms() + " msg left";
            } else {
                currSms = monthlyFramesInputTotal.getInputSms() - sdrOutputTotal.getOutputSms() + " msg";
            }
        }
        if (monthlyFramesInputTotal.getNAddCls() > 0) {
            clsNote = "N= " + monthlyFramesInputTotal.getNAddCls();
        }
        if (monthlyFramesInputTotal.getNAddSms() > 0) {
            smsNote = "N= " + monthlyFramesInputTotal.getNAddSms();
        }
        if (packagePlanType == PRP01) {
            return new ClientCurrentInfo01ViewDTO(phoneNumber, firstName, lastName, username, packageName, packageCode, currCls, currSms, clsNote, smsNote, nowDateTime);
        }
        if (packagePlanType == PackagePlanType.PST14) {
            currInt = "UNLIMITED";
        } else {
            if (!(monthlyFramesInputTotal.getInputInt().subtract(sdrOutputTotal.getOutputInt()).compareTo(BigDecimal.valueOf(0)) == 0)) {
                currInt = monthlyFramesInputTotal.getInputInt().subtract(sdrOutputTotal.getOutputInt()) + " MB left";
            } else {
                currInt = monthlyFramesInputTotal.getInputInt().subtract(sdrOutputTotal.getOutputInt()) + " MB";
            }
        }
        if (monthlyFramesInputTotal.getNAddInt() > 0) {
            intNote = "N= " + monthlyFramesInputTotal.getNAddInt();
        }
        if (packagePlanType == PRP02) {
            return new ClientCurrentInfo02ViewDTO(phoneNumber, firstName, lastName, username, packageName, packageCode, currCls, currSms, currInt, clsNote, smsNote, intNote, nowDateTime);
        }
        if (!(monthlyFramesInputTotal.getInputIcl().subtract(sdrOutputTotal.getOutputIcl()).compareTo(BigDecimal.valueOf(0)) == 0)) {
            currIcl = monthlyFramesInputTotal.getInputIcl().subtract(sdrOutputTotal.getOutputIcl()) + " cu left";
        } else {
            currIcl = monthlyFramesInputTotal.getInputIcl().subtract(sdrOutputTotal.getOutputIcl()) + " cu";
        }
        if (monthlyFramesInputTotal.getNAddIcl() > 0) {
            iclNote = "N= " + monthlyFramesInputTotal.getNAddIcl();
        }
        if (!(monthlyFramesInputTotal.getInputRmg().subtract(sdrOutputTotal.getOutputRmg()).compareTo(BigDecimal.valueOf(0)) == 0)) {
            currRmg = monthlyFramesInputTotal.getInputRmg().subtract(sdrOutputTotal.getOutputRmg()) + " cu left";
        } else {
            currRmg = monthlyFramesInputTotal.getInputRmg().subtract(sdrOutputTotal.getOutputRmg()) + " cu";
        }
        if (monthlyFramesInputTotal.getNAddRmg() > 0) {
            rmgNote = "N= " + monthlyFramesInputTotal.getNAddRmg();
        }
        if (packagePlanType == PST11) {
            return new ClientCurrentInfo11ViewDTO(phoneNumber, firstName, lastName, username, packageName, packageCode, currCls, currSms, currInt, currIcl, currRmg, clsNote, smsNote, intNote, iclNote, rmgNote, nowDateTime);
        }
        if (!(monthlyFramesInputTotal.getInputAsm().subtract(sdrOutputTotal.getOutputAsm()).compareTo(BigDecimal.valueOf(0)) == 0)) {
            currAsm = monthlyFramesInputTotal.getInputAsm().subtract(sdrOutputTotal.getOutputAsm()) + " MB left";
        } else {
            currAsm = monthlyFramesInputTotal.getInputAsm().subtract(sdrOutputTotal.getOutputAsm()) + " MB";
        }
        if (monthlyFramesInputTotal.getNAddAsm() > 0) {
            asmNote = "N= " + monthlyFramesInputTotal.getNAddAsm();
        }
        return new ClientCurrentInfo1234ViewDTO(phoneNumber, firstName, lastName, username, packageName, packageCode, currCls, currSms, currInt, currAsm, currIcl, currRmg, clsNote, smsNote, intNote, asmNote, iclNote, rmgNote, nowDateTime);

    }

    @Override
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

    @Override
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

    @Override
    public List<PackagePlanDTO> getAllCustomersPackagePlans() {
        List<PackagePlan> packagePlanList = packagePlanRepo.findAllCustomersPackagePlans();
        return packagePlanMapper.packagePlanToPackagePlanDTO(packagePlanList);
    }

    @Override
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
