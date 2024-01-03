package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddOnDTO {

    private String addonCode;
    private String addonDescription;
    private String addonPrice;
}
