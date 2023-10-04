package com.snezana.introtelecom.validations;

import com.snezana.introtelecom.entity.Admin;
import com.snezana.introtelecom.entity.PackageFrame;
import com.snezana.introtelecom.exceptions.IllegalItemFieldException;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.repositories.PackageFrameRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.snezana.introtelecom.validations.JsonDateTimeDeserializer.DATE_TIME_FORMAT;

@Service
@RequiredArgsConstructor
public class FramesSDRValidationService {

    @Autowired
    PackageFrameRepo packageFrameRepo;

    public static final String LOCAL_DATE_TIME_FORMAT = "'yyyy-MM-ddTHH:mm:ss.SSSZ'";

    public void controlTheLocalDateTimeInputIsValid(LocalDateTime localDateTime) {
        LocalDateTime dateTime = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
        if (localDateTime.equals(dateTime)) {
            throw new IllegalItemFieldException(RestAPIErrorMessage.INVALID_DATE_TIME_FORMAT, String.format("Local DateTime format should be %s", LOCAL_DATE_TIME_FORMAT));
        }
    }

    public void controlThePackageFrameExists (Long packfrId){
     packageFrameRepo.findByPackfrIdOpt(packfrId)
             .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Package frame is not found."));
    }

//    public LocalDateTime StringDateTimeToLocalDateTime (String dateTime){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT);
//        try {
//            return LocalDateTime.parse(dateTime, formatter);
//        } catch (Exception e) {
//            LocalDateTime dateTime1 = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
//            return dateTime1;
//        }
//    }

}
