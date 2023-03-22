package com.example.cardservices.audit;

import org.springframework.data.domain.AuditorAware;
import java.util.Optional;

public class AuditAwareImpl implements AuditorAware<String> {
    /**
     * @return
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("Credit Suisse");
    }
}
