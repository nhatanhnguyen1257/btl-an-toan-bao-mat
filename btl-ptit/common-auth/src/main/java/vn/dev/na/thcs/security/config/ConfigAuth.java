package vn.dev.na.thcs.security.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages ={"vn.dev.na.thcs.security"})
@EntityScan(basePackages = {"vn.dev.na.thcs.security"})
@ComponentScan(basePackages = {"vn.dev.na.thcs.security"})
public class ConfigAuth {

}
