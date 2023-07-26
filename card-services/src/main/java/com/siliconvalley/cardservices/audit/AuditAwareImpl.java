package com.siliconvalley.cardservices.audit;

import org.springframework.data.domain.AuditorAware;
import java.util.Optional;

public class AuditAwareImpl implements AuditorAware<String> {
    /**
     * @returnType String
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("Silicon Valley Corporation pvt ltd.");
    }
}
