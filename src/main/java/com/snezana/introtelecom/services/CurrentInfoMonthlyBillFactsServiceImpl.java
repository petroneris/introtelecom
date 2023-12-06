package com.snezana.introtelecom.services;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.entity.*;
import com.snezana.introtelecom.enums.AddonCode;
import com.snezana.introtelecom.enums.PackagePlanType;
import com.snezana.introtelecom.enums.SDRCode;
import com.snezana.introtelecom.mapper.MonthlyBillFactsMapper;
import com.snezana.introtelecom.repositories.*;
import com.snezana.introtelecom.validations.MonthlyBillFactsValidationService;
import com.snezana.introtelecom.validations.PhoneValidationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.snezana.introtelecom.enums.AddonCode.*;
import static com.snezana.introtelecom.enums.PackagePlanType.*;
import static com.snezana.introtelecom.enums.SDRCode.*;
import static com.snezana.introtelecom.services.FramesSDRServiceImpl.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CurrentInfoMonthlyBillFactsServiceImpl implements CurrentInfoMonthlyBillFactsService{
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CurrentInfoMonthlyBillFactsServiceImpl.class);

    private final PhoneValidationService phoneValidationService;
    private final PackageFrameRepo packageFrameRepo;
    private final AddonFrameRepo addonFrameRepo;
    private final MonthlyBillFactsRepo monthlyBillFactsRepo;
    private final MonthlyBillFactsValidationService monthlyBillFactsValidationService;
    private final MonthlyBillFactsMapper monthlyBillFactsMapper;
    private final ServiceDetailRecordRepo serviceDetailRecordRepo;
    private final PhoneRepo phoneRepo;
    private final PackagePlanRepo packagePlanRepo;
    private final AddOnRepo addOnRepo;

    @Override
    public CurrentInfo01ViewDTO getCurrentInfoByPhone(String phoneNumber) {
        String currCls;
        String currSms;
        String currInt;
        String currAsm;
        String currIcl;
        String currRmg;
        String clsNote= "";
        String smsNote= "";
        String intNote= "";
        String asmNote= "";
        String iclNote= "";
        String rmgNote= "";
        Phone phone = phoneValidationService.returnThePhoneIfExists(phoneNumber);
        phoneValidationService.controlThisPhoneHasCustomersPackageCode(phone);
        String packageCode = phone.getPackagePlan().getPackageCode();
        PackagePlanType packagePlanType = PackagePlanType.findByKey(packageCode);
        Month currentMonth = LocalDateTime.now().getMonth();
        int currentYear = LocalDateTime.now().getYear();
        LocalDateTime monthlyStartDateTime = LocalDateTime.of(currentYear, currentMonth, 1, 0, 0, 0, 0);
        LocalDateTime monthlyEndDateTime = monthlyStartDateTime.plusMonths(1);
        LocalDateTime nowDateTime = LocalDateTime.now();
        PackageFrame monthlyPackageFrame = packageFrameRepo.findPackageFrameByPhone_PhoneNumberAndPackfrStartDateTimeEqualsAndPackfrEndDateTimeEquals(phone.getPhoneNumber(), monthlyStartDateTime, monthlyEndDateTime);
        List<AddonFrame> addonFrameList = addonFrameRepo.findByPhone_PhoneNumberAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual(phone.getPhoneNumber(), monthlyStartDateTime, monthlyEndDateTime);
        log.info("addonFrameList is size = " + addonFrameList.size());
        MonthlyFramesInputTotal monthlyFramesInputTotal = inputAmountOfFramesCurrentInfo(monthlyPackageFrame, addonFrameList);
        SdrOutputTotal sdrOutputTotal = outputAmountOfSDRsCurrentInfo(phoneNumber, monthlyStartDateTime, nowDateTime);
        if (packagePlanType == PackagePlanType.PST13 || packagePlanType == PackagePlanType.PST14) {
            currCls = "UNLIMITED";
            currSms = "UNLIMITED";
        } else {
            if (monthlyFramesInputTotal.getInputCls() - sdrOutputTotal.getOutputCls() > 0) {
                currCls = monthlyFramesInputTotal.getInputCls() - sdrOutputTotal.getOutputCls() + " min left";
            } else {
                currCls = monthlyFramesInputTotal.getInputCls() - sdrOutputTotal.getOutputCls() + " min";
            }
            if (monthlyFramesInputTotal.getInputSms() - sdrOutputTotal.getOutputSms() > 0){
                currSms = monthlyFramesInputTotal.getInputSms() - sdrOutputTotal.getOutputSms() + " msg left";
            } else {
                currSms = monthlyFramesInputTotal.getInputSms() - sdrOutputTotal.getOutputSms() + " msg";
            }
        }
        if (monthlyFramesInputTotal.getNAddCls()>0) {
            clsNote = "N= " + monthlyFramesInputTotal.getNAddCls();
        }
        if (monthlyFramesInputTotal.getNAddSms()>0) {
            smsNote = "N= " + monthlyFramesInputTotal.getNAddSms();
        }
        if (packagePlanType == PRP01){
            return new CurrentInfo01ViewDTO(phoneNumber, packagePlanType, currCls, currSms, clsNote, smsNote, nowDateTime);
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
        if (monthlyFramesInputTotal.getNAddInt()>0) {
            intNote = "N= " + monthlyFramesInputTotal.getNAddInt();
        }
        if (packagePlanType == PRP02){
            return new CurrentInfo02ViewDTO(phoneNumber, packagePlanType, currCls, currSms, currInt, clsNote, smsNote, intNote, nowDateTime);
        }
        if (!(monthlyFramesInputTotal.getInputIcl().subtract(sdrOutputTotal.getOutputIcl()).compareTo(BigDecimal.valueOf(0)) == 0)) {
            currIcl = monthlyFramesInputTotal.getInputIcl().subtract(sdrOutputTotal.getOutputIcl()) + " cu left";
        } else {
            currIcl = monthlyFramesInputTotal.getInputIcl().subtract(sdrOutputTotal.getOutputIcl()) + " cu";
        }
        if (monthlyFramesInputTotal.getNAddIcl()>0) {
            iclNote = "N= " + monthlyFramesInputTotal.getNAddIcl();
        }
        if (!(monthlyFramesInputTotal.getInputRmg().subtract(sdrOutputTotal.getOutputRmg()).compareTo(BigDecimal.valueOf(0)) == 0)) {
            currRmg = monthlyFramesInputTotal.getInputRmg().subtract(sdrOutputTotal.getOutputRmg()) + " cu left";
        } else {
            currRmg = monthlyFramesInputTotal.getInputRmg().subtract(sdrOutputTotal.getOutputRmg()) + " cu";
        }
        if (monthlyFramesInputTotal.getNAddRmg()>0) {
            rmgNote = "N= " + monthlyFramesInputTotal.getNAddRmg();
        }
        if (packagePlanType == PST11){
            return new CurrentInfo11ViewDTO(phoneNumber, packagePlanType, currCls, currSms, currInt, currIcl, currRmg, clsNote, smsNote, intNote, iclNote, rmgNote, nowDateTime);
        }
        if (!(monthlyFramesInputTotal.getInputAsm().subtract(sdrOutputTotal.getOutputAsm()).compareTo(BigDecimal.valueOf(0)) == 0)) {
            currAsm = monthlyFramesInputTotal.getInputAsm().subtract(sdrOutputTotal.getOutputAsm()) + " MB left";
        } else {
            currAsm = monthlyFramesInputTotal.getInputAsm().subtract(sdrOutputTotal.getOutputAsm()) + " MB";
        }
        if (monthlyFramesInputTotal.getNAddAsm()>0) {
            asmNote = "N= " + monthlyFramesInputTotal.getNAddAsm();
        }
        return new CurrentInfoAllViewDTO(phoneNumber, packagePlanType, currCls, currSms, currInt, currAsm, currIcl, currRmg, clsNote, smsNote, intNote, asmNote, iclNote, rmgNote, nowDateTime);
    }

    @Override
    public MonthlyBillFactsViewDTO getMonthlyBillFactsById(Long monthlybillId) {
        checkMonthlyBillFactsIfNotExistsThenCreate();
        MonthlyBillFacts monthlyBillFacts = monthlyBillFactsValidationService.returnTheMonthlyBillFactsByIdIfExists(monthlybillId);
        return monthlyBillFactsMapper.monthlyBillFactsToMonthlyBillFactsViewDTO(monthlyBillFacts);
    }

    @Override
    public MonthlyBillFactsViewDTO getMontlyBillFactsByPhoneAndYearAndMonth(String phoneNumber, int year, int month) {
        monthlyBillFactsValidationService.checkYearAndMonth(year, month);
        LocalDate yearMonth = LocalDate.of(year, month, 1);
        checkMonthlyBillFactsIfNotExistsThenCreate();
        MonthlyBillFacts monthlyBillFacts = monthlyBillFactsValidationService.returnTheMonthlyBillFactsByYearMonthIfExists(phoneNumber, yearMonth);
        return monthlyBillFactsMapper.monthlyBillFactsToMonthlyBillFactsViewDTO(monthlyBillFacts);
    }

    @Override
    public List<MonthlyBillFactsViewDTO> getMonthlyBillFactsByPhoneFromStartDateToEndDate(String phoneNumber, int startYear, int startMonth, int endYear, int endMonth) {
        monthlyBillFactsValidationService.checkYearAndMonth(startYear, startMonth);
        monthlyBillFactsValidationService.checkYearAndMonth(endYear, endMonth);
        LocalDate startDate = LocalDate.of(startYear, startMonth, 1);
        LocalDate endDate = LocalDate.of(endYear, endMonth, 1);
        monthlyBillFactsValidationService.controlTheStartDateIsLessThanEndDate(startDate, endDate);
        checkMonthlyBillFactsIfNotExistsThenCreate();
        List <MonthlyBillFacts> monthlyBillFactsList = monthlyBillFactsRepo.findByPhone_PhoneNumberAndYearMonthStartsWithAndYearMonthEndsWith (phoneNumber, startDate, endDate);
        return monthlyBillFactsMapper.monthlyBillFactsListToMonthlyBillFactsViewDTOList(monthlyBillFactsList);
    }


    MonthlyFramesInputTotal inputAmountOfFramesCurrentInfo(PackageFrame packageFrame, List<AddonFrame> addonFrameList) {
        MonthlyFramesInputTotal inputResult = new MonthlyFramesInputTotal(0,0,new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"), 0,0,0,0,0,0);
        int inputCls = packageFrame.getPackfrCls();
        int inputSms = packageFrame.getPackfrSms();
        BigDecimal inputInt = packageFrame.getPackfrInt();
        BigDecimal inputAsm = packageFrame.getPackfrAsm();
        BigDecimal inputIcl = packageFrame.getPackfrIcl();
        BigDecimal inputRmg = packageFrame.getPackfrRmg();
        if (!addonFrameList.isEmpty()) {
            for (AddonFrame af : addonFrameList) {
                AddonCode addonCode = AddonCode.valueOf(af.getAddOn().getAddonCode());
                switch (addonCode) {
                    case ADDCLS:
                        inputCls = inputCls + af.getAddfrCls();
                        inputResult.setNAddCls(inputResult.getNAddCls()+1);
                        continue;
                    case ADDSMS:
                        inputSms = inputSms + af.getAddfrSms();
                        inputResult.setNAddSms(inputResult.getNAddSms()+1);
                        continue;
                    case ADDINT:
                        inputInt = inputInt.add(af.getAddfrInt());
                        inputResult.setNAddInt(inputResult.getNAddInt()+1);
                        continue;
                    case ADDASM:
                        inputAsm = inputAsm.add(af.getAddfrAsm());
                        inputResult.setNAddAsm(inputResult.getNAddAsm()+1);
                        continue;
                    case ADDICL:
                        inputIcl = inputIcl.add(af.getAddfrIcl());
                        inputResult.setNAddIcl(inputResult.getNAddIcl()+1);
                        continue;
                    case ADDRMG:
                        inputRmg = inputRmg.add(af.getAddfrRmg());
                        inputResult.setNAddRmg(inputResult.getNAddRmg()+1);
                }
            }
        }
        inputResult.setInputCls(inputCls);
        log.info("inputAm CLS = " + inputCls);
        inputResult.setInputSms(inputSms);
        log.info("inputAm SMS = " + inputSms);
        inputResult.setInputInt(inputInt);
        log.info("inputAm INT = " + inputInt);
        inputResult.setInputAsm(inputAsm);
        log.info("inputAm ASM = " + inputAsm);
        inputResult.setInputIcl(inputIcl);
        log.info("inputAm ICL = " + inputIcl);
        inputResult.setInputRmg(inputRmg);
        log.info("inputAm RMG = " + inputRmg);
        return inputResult;
    }

    SdrOutputTotal outputAmountOfSDRsCurrentInfo(String phoneNumber, LocalDateTime monthlyStartDateTime, LocalDateTime nowDateTime) {
        SdrOutputTotal outputResult = new SdrOutputTotal(0, 0, new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"));
        int duration = 0;
        int msgAmount = 0;
        int durationIcl1 = 0;
        int durationIcl2 = 0;
        int durationRmg1 = 0;
        int durationRmg2 = 0;
        BigDecimal mbAmountInt = new BigDecimal("0.00");
        BigDecimal mbAmountAsm = new BigDecimal("0.00");

        BigDecimal priceAmountICL = new BigDecimal("0.00");
        BigDecimal priceAmountRMG = new BigDecimal("0.00");
        BigDecimal priceICLCZ1 = new BigDecimal(UNIT_PRICE_ICLCZ1);
        BigDecimal priceICLCZ2 = new BigDecimal(UNIT_PRICE_ICLCZ2);
        BigDecimal priceRMGCZ1 = new BigDecimal(UNIT_PRICE_RMGCZ1);
        BigDecimal priceRMGCZ2 = new BigDecimal(UNIT_PRICE_RMGCZ2);

        List<ServiceDetailRecord> serviceDetailRecordListCls = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, nowDateTime, SDRCLS.name());
        if (!serviceDetailRecordListCls.isEmpty()) {
            for (ServiceDetailRecord sdr : serviceDetailRecordListCls) {
                duration = duration + sdr.getDuration();
            }
            outputResult.setOutputCls(duration);
        }
        log.info(" CLS duration = " + duration);
        List<ServiceDetailRecord> serviceDetailRecordListSms = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, nowDateTime, SDRSMS.name());
        if (!serviceDetailRecordListSms.isEmpty()) {
            for (ServiceDetailRecord sdr : serviceDetailRecordListSms) {
                msgAmount = msgAmount + sdr.getMsgAmount();
            }
            outputResult.setOutputSms(msgAmount);
        }
        log.info(" SMS msgAmount = " + msgAmount);
        List<ServiceDetailRecord> serviceDetailRecordListInt = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, nowDateTime, SDRINT.name());
        if (!serviceDetailRecordListInt.isEmpty()) {
            for (ServiceDetailRecord sdr : serviceDetailRecordListInt) {
                mbAmountInt = mbAmountInt.add(sdr.getMbAmount());
            }
            outputResult.setOutputInt(mbAmountInt);
        }
        log.info(" INT mbAmountInt = " + mbAmountInt);
        List<ServiceDetailRecord> serviceDetailRecordListAsm = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, nowDateTime, SDRASM.name());
        if (!serviceDetailRecordListAsm.isEmpty()) {
            for (ServiceDetailRecord sdr : serviceDetailRecordListAsm) {
                mbAmountAsm = mbAmountAsm.add(sdr.getMbAmount());
            }
            outputResult.setOutputAsm(mbAmountAsm);
        }
        log.info(" ASM mbAmountAsm = " + mbAmountAsm);
        List<ServiceDetailRecord> serviceDetailRecordListIcl1 = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, nowDateTime, SDRICLCZ1.name());
        if (!serviceDetailRecordListIcl1.isEmpty()) {
            for (ServiceDetailRecord sdr : serviceDetailRecordListIcl1) {
                durationIcl1 = durationIcl1 + sdr.getDuration();
            }
        }
        log.info("ICL durationIclCZ1= " + durationIcl1);
        priceICLCZ1 = priceICLCZ1.multiply(new BigDecimal(durationIcl1));
        log.info("ICL priceAmountICLCZ1= " + priceICLCZ1);

        List<ServiceDetailRecord> serviceDetailRecordListIcl2 = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, nowDateTime, SDRCode.SDRICLCZ2.name());
        if (!serviceDetailRecordListIcl2.isEmpty()) {
            for (ServiceDetailRecord sdr : serviceDetailRecordListIcl2) {
                durationIcl2 = durationIcl2 + sdr.getDuration();
            }
        }
        log.info("ICL durationIclCZ2= " + durationIcl2);
        priceICLCZ2 = priceICLCZ2.multiply(new BigDecimal(durationIcl2));
        log.info("ICL priceAmountICLCZ2= " + priceICLCZ2);
        priceAmountICL = priceAmountICL.add(priceICLCZ1).add(priceICLCZ2);
        log.info("ICL priceAmountICL= " + priceAmountICL);
        outputResult.setOutputIcl(priceAmountICL);

        List<ServiceDetailRecord> serviceDetailRecordListRmg1 = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, nowDateTime, SDRRMGCZ1.name());
        if (!serviceDetailRecordListRmg1.isEmpty()) {
            for (ServiceDetailRecord sdr : serviceDetailRecordListRmg1) {
                durationRmg1 = durationRmg1 + sdr.getDuration();
            }
        }
        log.info("RMG durationRmgCZ1= " + durationRmg1);
        priceRMGCZ1 = priceRMGCZ1.multiply(new BigDecimal(durationRmg1));
        log.info("RMG priceAmountRMGCZ1= " + priceRMGCZ1);
        List<ServiceDetailRecord> serviceDetailRecordListRmg2 = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, nowDateTime, SDRRMGCZ2.name());
        if (!serviceDetailRecordListRmg2.isEmpty()) {
            for (ServiceDetailRecord sdr : serviceDetailRecordListRmg2) {
                durationRmg2 = durationRmg2 + sdr.getDuration();
            }
        }
        log.info("RMG durationRmgCZ2= " + durationRmg2);
        priceRMGCZ2 = priceRMGCZ2.multiply(new BigDecimal(durationRmg2));
        log.info("RMG priceAmountRMGCZ2= " + priceRMGCZ2);
        priceAmountRMG = priceAmountRMG.add(priceRMGCZ1).add(priceRMGCZ2);
        log.info("RMG priceAmountRMG= " + priceAmountRMG);
        outputResult.setOutputRmg(priceAmountRMG);

        return outputResult;
    }

    void checkMonthlyBillFactsIfNotExistsThenCreate() {
        LocalDateTime startDateTime = LocalDateTime.of(2023, Month.MAY, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, Month.JUNE, 1, 0, 0, 0, 0);
        List<PackageFrame> packageFrameList = packageFrameRepo.findByPackfrStartDateTimeEqualsAndPackfrEndDateTimeEquals(startDateTime, endDateTime);
        List<AddonFrame> addonFrameList = addonFrameRepo.findByAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeEquals(startDateTime, endDateTime);
        List<MonthlyData> pfYMList = new ArrayList<>();
        List<MonthlyData> afYMlist = new ArrayList<>();
        for (PackageFrame pf : packageFrameList) {
            if (!(pf.getPhone().getPackagePlan().getPackageCode().equals(PRP01.getPackageCode()) || pf.getPhone().getPackagePlan().getPackageCode().equals(PRP02.getPackageCode()))) {
                pfYMList.add(new MonthlyData(pf.getPackfrId(), pf.getPhone().getPhoneNumber(), pf.getPhone().getPackagePlan().getPackageCode()));
//                log.info("pfYMList with: phoneNumber= {}, packageCode= {},", pf.getPhone().getPhoneNumber(), pf.getPhone().getPackagePlan().getPackageCode());
            }
        }
        for (AddonFrame af : addonFrameList) {
            if (!(af.getPhone().getPackagePlan().getPackageCode().equals(PRP01.getPackageCode()) || af.getPhone().getPackagePlan().getPackageCode().equals(PRP02.getPackageCode()))) {
                afYMlist.add(new MonthlyData(af.getAddfrId(), af.getPhone().getPhoneNumber(), af.getAddOn().getAddonCode()));
//                log.info("afYMList with: phoneNumber= {}, addonCode= {},", af.getPhone().getPhoneNumber(), af.getAddOn().getAddonCode());
            }
        }
        for (MonthlyData md : pfYMList) {
            String phoneNumb = md.getPhoneNumber();
            List<MonthlyData> afYMgroup = afYMlist.stream().filter(x -> x.getPhoneNumber().equals(phoneNumb)).collect(Collectors.toList());
            saveNewMonthlyBillFacts(md, afYMgroup, startDateTime, endDateTime);
        }

    }

    void saveNewMonthlyBillFacts(MonthlyData md, List<MonthlyData> afYMgroup, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        MonthlyBillFacts mbf = new MonthlyBillFacts();
        Phone phone = phoneRepo.findByPhoneNumber(md.getPhoneNumber());
        PackagePlan packagePlan = packagePlanRepo.findByPackageCode(md.getCode());
        mbf.setPackagePrice(packagePlan.getPackagePrice());
        BigDecimal addClsPrice = new BigDecimal("0.00");
        BigDecimal addSmsPrice = new BigDecimal("0.00");
        BigDecimal addIntPrice = new BigDecimal("0.00");
        BigDecimal addAsmPrice = new BigDecimal("0.00");
        BigDecimal addIclPrice = new BigDecimal("0.00");
        BigDecimal addRmgPrice = new BigDecimal("0.00");
        BigDecimal totalPrice;
        mbf.setPhone(phone);
        mbf.setYearMonth(LocalDate.of(startDateTime.getYear(), startDateTime.getMonth(), 1));
        mbf.setMonthlybillDateTime(LocalDateTime.of(endDateTime.getYear(), endDateTime.getMonth(), 1, 0, 0, 0, 0));
        for (MonthlyData af : afYMgroup) {
            AddOn addOn = addOnRepo.findByAddonCode(af.getCode());
            if (addOn.getAddonCode().equals(ADDCLS.name())) {
                addClsPrice = addClsPrice.add(addOn.getAddonPrice());
            } else if (addOn.getAddonCode().equals(ADDSMS.name())) {
                addSmsPrice = addSmsPrice.add(addOn.getAddonPrice());
            } else if (addOn.getAddonCode().equals(ADDINT.name())) {
                addIntPrice = addIntPrice.add(addOn.getAddonPrice());
            } else if (addOn.getAddonCode().equals(ADDASM.name())) {
                addAsmPrice = addAsmPrice.add(addOn.getAddonPrice());
            } else if (addOn.getAddonCode().equals(ADDICL.name())) {
                addIclPrice = addIclPrice.add(addOn.getAddonPrice());
            } else if (addOn.getAddonCode().equals(ADDRMG.name())) {
                addRmgPrice = addRmgPrice.add(addOn.getAddonPrice());
            }
        }
        totalPrice = packagePlan.getPackagePrice().add(addClsPrice).add(addSmsPrice).add(addIntPrice).add(addAsmPrice).add(addIclPrice).add(addRmgPrice);
        mbf.setAddclsPrice(addClsPrice);
        mbf.setAddsmsPrice(addSmsPrice);
        mbf.setAddintPrice(addIntPrice);
        mbf.setAddasmPrice(addAsmPrice);
        mbf.setAddiclPrice(addIclPrice);
        mbf.setAddrmgPrice(addRmgPrice);
        mbf.setMonthlybillTotalprice(totalPrice);
//        monthlyBillFactsRepo.save(mbf);
        log.info("MonthlyBillFacts with: phoneNumber= {},  yearMonth= {}, packagePrice= {}, addClsPrice= {}, addSmsPrice= {}, addIntPrice= {}, addAsmPrice= {}, addIclPrice= {}, addRmgPrice= {}, monthlybillTotalprice= {}, MonthlybillDateTime= {},", mbf.getPhone().getPhoneNumber(), mbf.getYearMonth(), mbf.getPackagePrice(), mbf.getAddclsPrice(), mbf.getAddsmsPrice(), mbf.getAddintPrice(), mbf.getAddasmPrice(), mbf.getAddiclPrice(), mbf.getAddrmgPrice(), mbf.getMonthlybillTotalprice(), mbf.getMonthlybillDateTime());
//        log.info("save new monthly bill facts" + LocalTime.now());
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class SdrOutputTotal {
    private int outputCls;
    private int outputSms;
    private BigDecimal outputInt;
    private BigDecimal outputAsm;
    private BigDecimal outputIcl;
    private BigDecimal outputRmg;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class MonthlyFramesInputTotal {
    private int inputCls;
    private int inputSms;
    private BigDecimal inputInt;
    private BigDecimal inputAsm;
    private BigDecimal inputIcl;
    private BigDecimal inputRmg;
    private int nAddCls;
    private int nAddSms;
    private int nAddInt;
    private int nAddAsm;
    private int nAddIcl;
    private int nAddRmg;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class MonthlyData {
    private Long id;
    private String phoneNumber;
    private String code;
}