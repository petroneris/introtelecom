package com.snezana.introtelecom.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class HowFarApartTwoLocalDateTime {

    public static void main(String[] args) {
        int resMin =0;
        LocalDateTime ldt1 = LocalDateTime.of(2023, Month.OCTOBER, 10, 5, 15, 37, 324);
        LocalDateTime ldt2 = LocalDateTime.of(2023, Month.OCTOBER, 10, 5, 30, 52, 572);
//        System.out.println(ChronoUnit.HOURS.between(ldt1, ldt2));
        System.out.println(ChronoUnit.MINUTES.between(ldt1, ldt2));
        System.out.println(ChronoUnit.SECONDS.between(ldt1, ldt2));
        long deltaSec = ChronoUnit.SECONDS.between(ldt1, ldt2);
        if ((int)deltaSec%60 == 0){
            resMin = (int)deltaSec/60;
        } else {
            resMin = (int)deltaSec/60 +1;
        }
        System.out.println("Minuta je = " + resMin);

    }

    public static int howFarApart (LocalDateTime ldt1, LocalDateTime ldt2) {
        int resMin =0;
        long deltaSec = ChronoUnit.SECONDS.between(ldt1, ldt2);
        if ((int)deltaSec%60 == 0){
            resMin = (int)deltaSec/60;
        } else {
            resMin = (int)deltaSec/60 +1;
        }
        return resMin;
    }
}
