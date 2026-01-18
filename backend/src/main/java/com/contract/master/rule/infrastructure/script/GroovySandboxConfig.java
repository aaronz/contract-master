package com.contract.master.rule.infrastructure.script;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class GroovySandboxConfig {

    @Bean
    public CompilerConfiguration groovyCompilerConfiguration() {
        CompilerConfiguration config = new CompilerConfiguration();
        SecureASTCustomizer secure = new SecureASTCustomizer();

        // Whitelist allowed classes for rule evaluation
        secure.setAllowedReceiversClasses(Arrays.asList(
                Object.class,
                String.class,
                Integer.class,
                Double.class,
                Boolean.class,
                java.math.BigDecimal.class,
                java.util.List.class,
                java.util.Map.class,
                java.time.LocalDate.class,
                java.time.LocalDateTime.class
        ));

        // Disallow dangerous constructs like closures and method definitions to ensure scripts are pure logic
        secure.setClosuresAllowed(false);
        secure.setMethodDefinitionAllowed(false);
        secure.setPackageAllowed(false);

        // Disallow system-level calls to prevent unauthorized access
        secure.setDisallowedStaticImports(Arrays.asList("java.lang.System", "java.lang.Runtime"));

        config.addCompilationCustomizers(secure);
        return config;
    }
}
