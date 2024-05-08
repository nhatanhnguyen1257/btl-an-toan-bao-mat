package vn.dev.na.thcs.security.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    @NotBlank
    @Length(max = 100)
    private String username;
    @Email
    @NotBlank
    @Length(max = 200)
    private String email;
    @NotBlank
    @Length(max = 60)
    private String fullName;
    @Length(min = 8, max = 50)
    private String password;
}
