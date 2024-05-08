package vn.dev.na.auth;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import vn.dev.na.thcs.security.config.ConfigAuth;


@SpringBootApplication
@Import(value = {ConfigAuth.class})
@OpenAPIDefinition(info = @Info(title = "Service AUTH", version = "1.0", description = "Sử dụng để quản lý tài khoản người dùng"))
@SecurityScheme(name = "", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class Auth {
	
	public static void main(String []args) {
		SpringApplication.run(Auth.class, args);
	}

}
