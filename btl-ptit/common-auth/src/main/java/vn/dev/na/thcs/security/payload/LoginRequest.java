package vn.dev.na.thcs.security.payload;

import java.util.Set;

import javax.validation.constraints.NotBlank;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {
	
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	
}