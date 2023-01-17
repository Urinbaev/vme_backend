package ru.sibintek.vme.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import ru.sibintek.vme.common.security.AuthenticationFacade;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider(AuthenticationFacade authenticationFacade) {
        return () -> Optional.ofNullable(authenticationFacade.getCurrentUser().getId());
    }

}
