package com.snezana.introtelecom.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Utility method for Local-Date-Time manipulation.
 */
public class SomeUtils {

    public static int howFarApartTwoLocalDateTime (LocalDateTime ldt1, LocalDateTime ldt2) {
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
