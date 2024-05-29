package com.snezana.introtelecom.service;

import com.snezana.introtelecom.entity.*;
import com.snezana.introtelecom.enums.PackagePlanType;
import com.snezana.introtelecom.enums.StatusType;
import com.snezana.introtelecom.repository.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.snezana.introtelecom.enums.AddonCode.*;
import static com.snezana.introtelecom.enums.PackagePlanType.PRP01;
import static com.snezana.introtelecom.enums.PackagePlanType.PRP02;

// In reality this is the process that takes some time, so it would be good to create some service

/**
 * automatic generation of package frames and monthly bills at the beginning of the month
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "spring.scheduler.enabled", matchIfMissing = true)
@RequiredArgsConstructor
@Transactional
public class SchedulingJobs {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SchedulingJobs.class);

    private final PackageFrameRepo packageFrameRepo;
    private final AddonFrameRepo addonFrameRepo;
    private final PhoneRepo phoneRepo;
    private final PackagePlanRepo packagePlanRepo;
    private final AddOnRepo addOnRepo;
    private final MonthlyBillFactsRepo monthlyBillFactsRepo;

    /**
     * automatic generation of package frames at the beginning of the month - “At minute 1 on day-of-month 1"
     */
   @Scheduled(cron = "0 1 0 1 * *")
    public void createMonthlyPackageFrame() {
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDateTime nextMonthDateTime = nowDateTime.plusMonths(1);
        LocalDateTime startDateTime = LocalDateTime.of(nowDateTime.getYear(), nowDateTime.getMonth(), 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(nextMonthDateTime.getYear(), nextMonthDateTime.getMonth(), 1, 0, 0, 0, 0);
        List<Phone> phoneList = phoneRepo.findAllCustomerPhones();
        log.info("START create monthly packageFrame");
        for (Phone phone : phoneList) {
            Set<Customer> customers = phone.getCustomers();
            if (!customers.isEmpty()) {
                PackageFrame packageFrame = new PackageFrame();
                packageFrame.setPhone(phone);
                packageFrame.setPackfrStatus(StatusType.PRESENT.getStatus());
                packageFrame.setPackfrStartDateTime(startDateTime);
                packageFrame.setPackfrEndDateTime(endDateTime);
                setPackageFrameByPackageCode(packageFrame);
                packageFrameRepo.save(packageFrame);
            }
        }
        log.info("END create monthly packageFrame");
    }

    private void setMonthlyPackageFrameQuota(PackageFrame packageFrame, int monthlyQuotaCls, int monthlyQuotaSms, String monthlyQuotaInt, String monthlyQuotaAsm, String monthlyQuotaIcl, String monthlyQuotaRmg) {
        packageFrame.setPackfrCls(monthlyQuotaCls);
        packageFrame.setPackfrSms(monthlyQuotaSms);
        packageFrame.setPackfrInt(new BigDecimal(monthlyQuotaInt));
        packageFrame.setPackfrAsm(new BigDecimal(monthlyQuotaAsm));
        packageFrame.setPackfrIcl(new BigDecimal(monthlyQuotaIcl));
        packageFrame.setPackfrRmg(new BigDecimal(monthlyQuotaRmg));
    }

    private void setPackageFrameByPackageCode(PackageFrame packageFrame) {
        String packageCode = packageFrame.getPhone().getPackagePlan().getPackageCode();
        PackagePlanType packagePlanType = PackagePlanType.findByKey(packageCode);
        switch (packagePlanType) {
            case PRP01:
                setMonthlyPackageFrameQuota(packageFrame, 200, 200, "0.00", "0.00", "0.00", "0.00");
                break;
            case PRP02:
                setMonthlyPackageFrameQuota(packageFrame, 200, 200, "10000.00", "0.00", "0.00", "0.00");
                break;
            case PST11:
                setMonthlyPackageFrameQuota(packageFrame, 300, 300, "10000.00", "0.00", "200.00", "200.00");
                break;
            case PST12:
                setMonthlyPackageFrameQuota(packageFrame, 400, 400, "10000.00", "5000.00", "200.00", "200.00");
                break;
            case PST13:
                setMonthlyPackageFrameQuota(packageFrame, -1, -1, "15000.00", "5000.00", "200.00", "200.00");
                break;
            case PST14:
                setMonthlyPackageFrameQuota(packageFrame, -1, -1, "-1.00", "10000.00", "200.00", "200.00");
                break;
            default:
        }
    }

    /**
     * automatic generation of monthly bills for the previous month, at the beginning of the month - “At every minute past hour 10 on day-of-month 1”
     */
    @Scheduled(cron = "0 0 10 1 * *")
    public void createMonthlyBillFacts() {
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDateTime previousMonthDateTime = nowDateTime.minusMonths(1);
        LocalDateTime startDateTime = LocalDateTime.of( previousMonthDateTime.getYear(), previousMonthDateTime.getMonth(), 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(nowDateTime.getYear(), nowDateTime.getMonth(), 1, 0, 0, 0, 0);
        List<PackageFrame> packageFrameList = packageFrameRepo.findByPackfrStartDateTimeEqualsAndPackfrEndDateTimeEquals(startDateTime, endDateTime);
        List<AddonFrame> addonFrameList = addonFrameRepo.findByAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeEquals(startDateTime, endDateTime);
        List<MonthlyData> pfYMList = new ArrayList<>();
        List<MonthlyData> afYMlist = new ArrayList<>();
        log.info("START create monthly bill");
        for (PackageFrame pf : packageFrameList) {
            if (!(pf.getPhone().getPackagePlan().getPackageCode().equals(PRP01.getPackageCode()) || pf.getPhone().getPackagePlan().getPackageCode().equals(PRP02.getPackageCode()))) {
                pfYMList.add(new MonthlyData(pf.getPackfrId(), pf.getPhone().getPhoneNumber(), pf.getPhone().getPackagePlan().getPackageCode()));
            }
            pf.setPackfrStatus(StatusType.NOT_IN_USE.getStatus());
        }
        for (AddonFrame af : addonFrameList) {
            if (!(af.getPhone().getPackagePlan().getPackageCode().equals(PRP01.getPackageCode()) || af.getPhone().getPackagePlan().getPackageCode().equals(PRP02.getPackageCode()))) {
                afYMlist.add(new MonthlyData(af.getAddfrId(), af.getPhone().getPhoneNumber(), af.getAddOn().getAddonCode()));
            }
            af.setAddfrStatus(StatusType.NOT_IN_USE.getStatus());
        }
        for (MonthlyData md : pfYMList) {
            String phoneNumb = md.getPhoneNumber();
            List<MonthlyData> afYMgroup = afYMlist.stream().filter(x -> x.getPhoneNumber().equals(phoneNumb)).collect(Collectors.toList());
            saveNewMonthlyBillFacts(md, afYMgroup, startDateTime, endDateTime);
        }
        log.info("END create monthly bill");
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
        monthlyBillFactsRepo.save(mbf);
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class MonthlyData {
    private Long id;
    private String phoneNumber;
    private String code;
}