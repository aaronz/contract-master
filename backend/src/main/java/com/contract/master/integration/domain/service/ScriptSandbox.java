package com.contract.master.integration.domain.service;

import groovy.lang.Binding;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ScriptSandbox {
    private static final Logger log = LoggerFactory.getLogger(ScriptSandbox.class);
    
    private final Cache<String, Script> scriptCache = Caffeine.newBuilder()
            .maximumSize(500)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();

    private final CompilerConfiguration config;

    public ScriptSandbox() {
        this.config = new CompilerConfiguration();
        ImportCustomizer imports = new ImportCustomizer();
        imports.addStarImports("java.util", "java.time");
        config.addCompilationCustomizers(imports);
    }

    public Object execute(String scriptSource, Object value) {
        if (scriptSource == null || scriptSource.trim().isEmpty()) {
            return value;
        }

        try {
            Script script = scriptCache.get(scriptSource, k -> new GroovyShell(config).parse(k));
            Binding binding = new Binding();
            binding.setVariable("value", value);
            script.setBinding(binding);
            
            return script.run();
        } catch (Exception e) {
            log.error("Script execution failed for script: {}. Error: {}", scriptSource, e.getMessage());
            return value;
        }
    }
}
