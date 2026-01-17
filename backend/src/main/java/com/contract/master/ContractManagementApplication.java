package com.contract.master;

import com.contract.master.domain.ContractBase;
import com.contract.master.domain.ContractBaseRepository;
import com.contract.master.domain.RuleConfig;
import com.contract.master.domain.RuleConfigRepository;
import com.contract.master.domain.User;
import com.contract.master.domain.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
@EnableJpaAuditing
public class ContractManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContractManagementApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, ContractBaseRepository contractRepository, RuleConfigRepository ruleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println(">>> CHECKING ADMIN USER...");
            if (userRepository.findByUserName("admin").isEmpty()) {
                System.out.println(">>> CREATING ADMIN USER...");
                User admin = new User();
                admin.setUserId(UUID.randomUUID().toString());
                admin.setUserName("admin");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setTenantId("tenant-1");
                admin.setRealName("System Admin");
                admin.setStatus(1);
                userRepository.save(admin);
                System.out.println(">>> ADMIN USER CREATED SUCCESSFULLY.");
            }

            System.out.println(">>> CHECKING SAMPLE CONTRACTS...");
            if (contractRepository.count() == 0) {
                System.out.println(">>> CREATING SAMPLE CONTRACTS...");
                ContractBase c1 = new ContractBase();
                c1.setContractId("CON-2024-001");
                c1.setContractNo("CON-2024-001");
                c1.setContractName("Enterprise License Agreement");
                c1.setPartyAName("Acme Corp");
                c1.setPartyBName("TechSolutions Inc");
                c1.setAmount(new BigDecimal("150000.00"));
                c1.setStatus("Active");
                c1.setTenantId("tenant-1");
                c1.setCreateTime(LocalDateTime.now());
                contractRepository.save(c1);

                ContractBase c2 = new ContractBase();
                c2.setContractId("1");
                c2.setContractNo("CON-2024-002");
                c2.setContractName("Strategic Partnership Agreement");
                c2.setPartyAName("Acme Corp");
                c2.setPartyBName("TechSolutions Inc");
                c2.setAmount(new BigDecimal("1000000.00"));
                c2.setStatus("Active");
                c2.setTenantId("tenant-1");
                c2.setCreateTime(LocalDateTime.now());
                contractRepository.save(c2);

                ContractBase c3 = new ContractBase();
                c3.setContractId("CON-2026-STAB");
                c3.setContractNo("CON-2026-STAB");
                c3.setContractName("Stabilization Contract");
                c3.setPartyAName("Acme Corp");
                c3.setPartyBName("TechSolutions Inc");
                c3.setAmount(new BigDecimal("2000000.00"));
                c3.setStatus("DRAFT");
                c3.setTenantId("tenant-1");
                c3.setCreateTime(LocalDateTime.now());
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
                rule1.setTenantId("tenant-1");
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
                rule2.setTenantId("tenant-1");
                ruleRepository.save(rule2);
                System.out.println(">>> DEFAULT RULES CREATED SUCCESSFULLY.");
            }
        };
    }
}
