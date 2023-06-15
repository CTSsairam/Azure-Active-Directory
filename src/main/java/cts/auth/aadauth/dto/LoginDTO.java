package cts.auth.aadauth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    @NotEmpty(message = "Invalid Email")
    @Email(message = "Invalid Email")
    private String username;

    @Size(max = 255, min = 8, message = "Invalid Password")
    private String password;


}
