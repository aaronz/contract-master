 package com.contract.master;

import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.ContractNo;
import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.model.ContractAmount;
import com.contract.master.contract.domain.model.ContractParty;
import com.contract.master.contract.domain.repository.ContractRepository;

import com.contract.master.rule.domain.model.RuleConfig;
import com.contract.master.evaluation.domain.repository.RuleConfigRepository;
import com.contract.master.identity.domain.model.User;
import com.contract.master.identity.domain.repository.UserRepository;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import com.contract.master.audit.application.AuditService; // Import AuditService
import com.contract.master.audit.domain.repository.AuditLogRepository; // Import AuditLogRepository
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan(basePackages = {
    "com.contract.master.audit.domain.model",
    "com.contract.master.contract.domain.model",
    "com.contract.master.contract.metadata.domain.model",
    "com.contract.master.evaluation.domain.model",
    "com.contract.master.identity.domain.model",
    "com.contract.master.integration.domain.model",
    "com.contract.master.notification.domain.model",
    "com.contract.master.rule.domain.model",
    "com.contract.master.problemcenter.domain.model",
    "com.contract.master.shared.domain.model"
})
public class ContractManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContractManagementApplication.class, args);
    }

    // Explicitly define AuditService as a bean for diagnostic purposes
    @Bean
    public AuditService auditService(AuditLogRepository auditLogRepository) {
        return new AuditService(auditLogRepository);
    }
    

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, ContractRepository contractRepository, RuleConfigRepository ruleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println(">>> CHECKING ADMIN USER...");
            if (userRepository.findByUserName("admin").isEmpty()) {
                System.out.println(">>> CREATING ADMIN USER...");
                User admin = new User();
                admin.setUserId(UUID.randomUUID().toString());
                admin.setUserName("admin");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setTenantId(TenantId.of("tenant-1"));
                admin.setRealName("System Admin");
                admin.setStatus(1);
                userRepository.save(admin);
                System.out.println(">>> ADMIN USER CREATED SUCCESSFULLY.");
            }

            System.out.println(">>> CHECKING SAMPLE CONTRACTS...");
            if (contractRepository.count() == 0) {
                System.out.println(">>> CREATING SAMPLE CONTRACTS...");
                  Contract c1 = new Contract();
                  c1.setContractId(ContractId.of(UUID.randomUUID().toString()));
                  c1.setContractNo(new ContractNo("CON-2024-001"));
                  c1.setContractName("Enterprise License Agreement");
                  c1.setPartyA(new ContractParty(null, "Acme Corp", null, null, null));
                  c1.setPartyB(new ContractParty(null, "TechSolutions Inc", null, null, null));
                  c1.setAmount(ContractAmount.of(new BigDecimal("150000.00"), "USD"));
                  c1.setStatus("Active");
                  c1.setTenantId(TenantId.of("tenant-1"));
                  contractRepository.save(c1);

                  Contract c2 = new Contract();
                  c2.setContractId(ContractId.of(UUID.randomUUID().toString()));
                  c2.setContractNo(new ContractNo("CON-2024-002"));
                  c2.setContractName("Strategic Partnership Agreement");
                  c2.setPartyA(new ContractParty(null, "Acme Corp", null, null, null));
                  c2.setPartyB(new ContractParty(null, "TechSolutions Inc", null, null, null));
                  c2.setAmount(ContractAmount.of(new BigDecimal("1000000.00"), "USD"));
                  c2.setStatus("Active");
                  c2.setTenantId(TenantId.of("tenant-1"));
                  contractRepository.save(c2);

                  Contract c3 = new Contract();
                  c3.setContractId(ContractId.of(UUID.randomUUID().toString()));
                  c3.setContractNo(new ContractNo("CON-2026-STAB"));
                  c3.setContractName("Stabilization Contract");
                  c3.setPartyA(new ContractParty(null, "Acme Corp", null, null, null));
                  c3.setPartyB(new ContractParty(null, "TechSolutions Inc", null, null, null));
                  c3.setAmount(ContractAmount.of(new BigDecimal("2000000.00"), "USD"));
                  c3.setStatus("DRAFT");
                  c3.setTenantId(TenantId.of("tenant-1"));
                  contractRepository.save(c3);


                
                System.out.println(">>> SAMPLE CONTRACTS CREATED SUCCESSFULLY.");
            }

            System.out.println(">>> CHECKING RULES...");
            if (ruleRepository.count() == 0) {
                System.out.println(">>> CREATING DEFAULT RULES...");
                RuleConfig rule1 = new RuleConfig();
                rule1.setRuleId(UUID.randomUUID().toString());
                rule1.setRuleName("High Value Contract Review");
                rule1.setRuleType("RISK_CHECK");
                rule1.setRuleLevel("HIGH");
                rule1.setTriggerTime("BEFORE_APPROVAL");
                rule1.setRuleCondition("amount > 1000000");
                rule1.setExecutionActions("REQUIRE_LEGAL_REVIEW");
                rule1.setIsEnabled(true);
                rule1.setTenantId(TenantId.of("tenant-1"));
                ruleRepository.save(rule1);

                RuleConfig rule2 = new RuleConfig();
                rule2.setRuleId(UUID.randomUUID().toString());
                rule2.setRuleName("Missing Attachment Check");
                rule2.setRuleType("COMPLIANCE");
                rule2.setRuleLevel("MEDIUM");
                rule2.setTriggerTime("BEFORE_SUBMIT");
                rule2.setRuleCondition("attachmentCount == 0");
                rule2.setExecutionActions("BLOCK_SUBMISSION");
                rule2.setIsEnabled(true);
                rule2.setTenantId(TenantId.of("tenant-1"));
                ruleRepository.save(rule2);
                System.out.println(">>> DEFAULT RULES CREATED SUCCESSFULLY.");
            }
        };
    }
}
