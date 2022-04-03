package com.example.restful.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputProductDto {

    private Integer productId;

    private Double amount;

    private Double price;

    @Pattern(regexp = "^((0[1-9]|[12][0-9]|3[01])-(0[135789]|10|12)-((2[0-9])[0-9]{2}))$", message = "You entered the date incorrectly")
    private String expireDate;
}
