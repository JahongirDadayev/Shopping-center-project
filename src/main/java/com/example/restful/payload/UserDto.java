package com.example.restful.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String firstName;

    private String lastName;

    @Pattern(regexp = "^998[389][012345789][0-9]{7}$", message = "You entered the phone number incorrectly")
    private String phoneNumber;

    @Email(message = "You entered the email incorrectly")
    private String email;

    private String password;

    private List<Integer> warehousesId;
}
