package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackagePlanDTO {

    private String packageCode;
    private String packageName;
    private String packageDescription;
    private String packagePrice;
}
