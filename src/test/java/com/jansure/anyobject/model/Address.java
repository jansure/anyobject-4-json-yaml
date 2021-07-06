package com.jansure.anyobject.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
    private String detail;
    private int countryCode;
    private double doorCode;
}
