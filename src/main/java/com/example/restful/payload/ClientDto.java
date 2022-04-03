package com.example.restful.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
    private String name;

    @Pattern(regexp = "^998[389][012345789][0-9]{7}$", message = "You entered the phone number incorrectly")
    private String phoneNumber;
}
