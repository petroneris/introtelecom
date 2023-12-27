package com.snezana.introtelecom.mapper;

import com.snezana.introtelecom.dto.ClientMonthlyBillFactsPstViewDTO;
import com.snezana.introtelecom.dto.MonthlyBillFactsViewDTO;
import com.snezana.introtelecom.dto.PackageFrameViewDTO;
import com.snezana.introtelecom.entity.MonthlyBillFacts;
import com.snezana.introtelecom.entity.PackageFrame;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MonthlyBillFactsMapper {
    @Mapping(source = "monthlyBillFacts", target = "packagePrice", qualifiedByName = "package")
    @Mapping(source = "monthlyBillFacts", target = "addclsPrice", qualifiedByName = "calls")
    @Mapping(source = "monthlyBillFacts", target = "addsmsPrice", qualifiedByName = "messages")
    @Mapping(source = "monthlyBillFacts", target = "addintPrice", qualifiedByName = "internet")
    @Mapping(source = "monthlyBillFacts", target = "addasmPrice", qualifiedByName = "appSocialMedia")
    @Mapping(source = "monthlyBillFacts", target = "addiclPrice", qualifiedByName = "internationalCalls")
    @Mapping(source = "monthlyBillFacts", target = "addrmgPrice", qualifiedByName = "roaming")
    @Mapping(source = "monthlyBillFacts", target = "monthlybillTotalprice", qualifiedByName = "total")
    public abstract MonthlyBillFactsViewDTO monthlyBillFactsToMonthlyBillFactsViewDTO(MonthlyBillFacts monthlyBillFacts);

    public abstract List<MonthlyBillFactsViewDTO> monthlyBillFactsListToMonthlyBillFactsViewDTOList(List<MonthlyBillFacts> monthlyBillFactsList);


    @Named("package")
    public static String packageConversion(MonthlyBillFacts monthlyBillFacts) {
       if ((monthlyBillFacts.getPackagePrice().compareTo(BigDecimal.valueOf(0))) == 0){
           return "";
       } else {
           return monthlyBillFacts.getPackagePrice() + " cu";
       }
    }

    @Named("calls")
    public static String clsConversion(MonthlyBillFacts monthlyBillFacts) {
        if ((monthlyBillFacts.getAddclsPrice().compareTo(BigDecimal.valueOf(0))) == 0){
            return "";
        } else {
            return monthlyBillFacts.getAddclsPrice() + " cu";
        }
    }

    @Named("messages")
    public static String smsConversion(MonthlyBillFacts monthlyBillFacts) {
        if ((monthlyBillFacts.getAddsmsPrice().compareTo(BigDecimal.valueOf(0))) == 0){
            return "";
        } else {
            return monthlyBillFacts.getAddsmsPrice() + " cu";
        }
    }

    @Named("internet")
    public static String intConversion(MonthlyBillFacts monthlyBillFacts) {
        if ((monthlyBillFacts.getAddintPrice().compareTo(BigDecimal.valueOf(0))) == 0){
            return "";
        } else {
            return monthlyBillFacts.getAddintPrice() + " cu";
        }
    }

    @Named("appSocialMedia")
    public static String asmConversion(MonthlyBillFacts monthlyBillFacts) {
        if ((monthlyBillFacts.getAddasmPrice().compareTo(BigDecimal.valueOf(0))) == 0){
            return "";
        } else {
            return monthlyBillFacts.getAddasmPrice() + " cu";
        }
    }

    @Named("internationalCalls")
    public static String iclConversion(MonthlyBillFacts monthlyBillFacts) {
        if ((monthlyBillFacts.getAddiclPrice().compareTo(BigDecimal.valueOf(0))) == 0){
            return "";
        } else {
            return monthlyBillFacts.getAddiclPrice() + " cu";
        }
    }

    @Named("roaming")
    public static String rmgConversion(MonthlyBillFacts monthlyBillFacts) {
        if ((monthlyBillFacts.getAddrmgPrice().compareTo(BigDecimal.valueOf(0))) == 0){
            return "";
        } else {
            return monthlyBillFacts.getAddrmgPrice() + " cu";
        }
    }

    @Named("total")
    public static String totalConversion(MonthlyBillFacts monthlyBillFacts) {
        return monthlyBillFacts.getMonthlybillTotalprice() + " cu";
    }

    @BeforeMapping
    protected void beforeMonthlyBillFactsToMonthlyBillFactsViewDTO(MonthlyBillFacts monthlyBillFacts, @MappingTarget MonthlyBillFactsViewDTO monthlyBillFactsViewDTO) {
        monthlyBillFactsViewDTO.setPhoneNumber(monthlyBillFacts.getPhone().getPhoneNumber());
        monthlyBillFactsViewDTO.setMonth(monthlyBillFacts.getYearMonth().getMonth().name());
        monthlyBillFactsViewDTO.setYear(monthlyBillFacts.getYearMonth().getYear());
        monthlyBillFactsViewDTO.setPackageName(monthlyBillFacts.getPhone().getPackagePlan().getPackageName());
        monthlyBillFactsViewDTO.setPackageCode(monthlyBillFacts.getPhone().getPackagePlan().getPackageCode());
    }

    @Mapping(source = "monthlyBillFacts", target = "packagePrice", qualifiedByName = "package")
    @Mapping(source = "monthlyBillFacts", target = "addclsPrice", qualifiedByName = "calls")
    @Mapping(source = "monthlyBillFacts", target = "addsmsPrice", qualifiedByName = "messages")
    @Mapping(source = "monthlyBillFacts", target = "addintPrice", qualifiedByName = "internet")
    @Mapping(source = "monthlyBillFacts", target = "addasmPrice", qualifiedByName = "appSocialMedia")
    @Mapping(source = "monthlyBillFacts", target = "addiclPrice", qualifiedByName = "internationalCalls")
    @Mapping(source = "monthlyBillFacts", target = "addrmgPrice", qualifiedByName = "roaming")
    @Mapping(source = "monthlyBillFacts", target = "monthlybillTotalprice", qualifiedByName = "total")
    public abstract ClientMonthlyBillFactsPstViewDTO monthlyBillFactsToClientMonthlyBillFactsPstViewDTO(MonthlyBillFacts monthlyBillFacts);

    public abstract List<ClientMonthlyBillFactsPstViewDTO> monthlyBillFactsListToClientMonthlyBillFactsPstViewDTOList(List<MonthlyBillFacts> monthlyBillFactsList);

    @BeforeMapping
    protected void beforeMonthlyBillFactsToClientMonthlyBillFactsPstViewDTO(MonthlyBillFacts monthlyBillFacts, @MappingTarget ClientMonthlyBillFactsPstViewDTO clientMonthlyBillFactsPstViewDTO) {
        clientMonthlyBillFactsPstViewDTO.setPhoneNumber(monthlyBillFacts.getPhone().getPhoneNumber());
        clientMonthlyBillFactsPstViewDTO.setMonth(monthlyBillFacts.getYearMonth().getMonth().name());
        clientMonthlyBillFactsPstViewDTO.setYear(monthlyBillFacts.getYearMonth().getYear());
        clientMonthlyBillFactsPstViewDTO.setPackageName(monthlyBillFacts.getPhone().getPackagePlan().getPackageName());
        clientMonthlyBillFactsPstViewDTO.setPackageCode(monthlyBillFacts.getPhone().getPackagePlan().getPackageCode());
    }

}
