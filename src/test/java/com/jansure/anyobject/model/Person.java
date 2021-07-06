package com.jansure.anyobject.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Person {
    private String name;
    private int age;
    private LocalDateTime bornDate;
    private Address address;
}
