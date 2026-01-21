 package com.contract.master;

import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.ContractNo;
import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.model.ContractAmount;
import com.contract.master.contract.domain.model.ContractParty;
import com.contract.master.contract.domain.repository.ContractRepository;

import com.contract.master.rule.domain.model.RuleConfig;
import com.contract.master.rule.domain.model.Rule;
import com.contract.master.rule.domain.model.RuleLogicType;
import com.contract.master.rule.domain.model.Severity;
import com.contract.master.rule.domain.model.RuleStatus;
import com.contract.master.rule.domain.repository.RuleRepository;
import com.contract.master.evaluation.domain.repository.RuleConfigRepository;
import com.contract.master.identity.domain.model.User;
import com.contract.master.identity.domain.model.Role;
import com.contract.master.identity.domain.repository.UserRepository;
import com.contract.master.identity.domain.repository.RoleRepository;
import com.contract.master.ai.domain.model.AISetting;
import com.contract.master.ai.domain.repository.AISettingRepository;
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
    "com.contract.master.ai.domain.model",
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
    public CommandLineRunner initData(UserRepository userRepository, RoleRepository roleRepository, ContractRepository contractRepository, RuleConfigRepository ruleConfigRepository, RuleRepository ruleRepository, AISettingRepository aiSettingRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println(">>> CHECKING AI SETTINGS...");
            TenantId t1 = TenantId.of("tenant-1");
            com.contract.master.security.TenantContext.setCurrentTenant(t1.getId());
            try {
                if (aiSettingRepository.findAll().isEmpty()) {
                    AISetting setting = new AISetting();
                    setting.setProvider("SILICONFLOW");
                    setting.setModelName("Qwen/Qwen2.5-7B-Instruct");
                    setting.setEndpointUrl("https://api.siliconflow.cn/v1/chat/completions");
                    setting.setExtractionPrompt("You are an expert legal document analyzer. Your task is to extract specific information from the contract text provided (which could be in English or Chinese). \n\n" +
                        "Return ONLY a valid JSON object with the following keys: \n" +
                        "- contractNo: The contract reference number or ID.\n" +
                        "- contractName: The official title of the contract.\n" +
                        "- partyAName: The legal name of the first party (Party A/甲方).\n" +
                        "- partyBName: The legal name of the second party (Party B/乙方).\n" +
                        "- contractAmount: The total contract value as a number.\n" +
                        "- contractType: The classification of the contract.\n\n" +
                        "Guidelines:\n" +
                        "1. If a value is missing, return null for that key.\n" +
                        "2. Return only the JSON object without any additional text or formatting explanation.");
                    aiSettingRepository.save(setting);
                }

                System.out.println(">>> CHECKING ROLES...");
                if (roleRepository.findAll().isEmpty()) {
                    System.out.println(">>> CREATING DEFAULT ROLES...");
                    String[][] defaultRoles = {
                        {"admin", "System Admin", "SYSTEM"},
                        {"legal_mgr", "Legal Manager", "STANDARD"},
                        {"sales_lead", "Sales Director", "STANDARD"},
                        {"sales", "Sales Rep", "STANDARD"},
                        {"finance", "Finance Audit", "STANDARD"}
                    };
                    for (String[] r : defaultRoles) {
                        Role role = new Role();
                        role.setRoleId(r[0]);
                        role.setRoleName(r[1]);
                        role.setRoleType(r[2]);
                        role.setStatus(1);
                        roleRepository.save(role);
                    }
                }

                System.out.println(">>> CHECKING ADMIN USER...");
                if (userRepository.findByUserName("admin").isEmpty()) {
                    System.out.println(">>> CREATING ADMIN USER...");
                    User admin = new User();
                    admin.setUserId(UUID.randomUUID().toString());
                    admin.setUserName("admin");
                    admin.setPassword(passwordEncoder.encode("password"));
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
                      contractRepository.save(c1);

                      Contract c2 = new Contract();
                      c2.setContractId(ContractId.of(UUID.randomUUID().toString()));
                      c2.setContractNo(new ContractNo("CON-2024-002"));
                      c2.setContractName("Strategic Partnership Agreement");
                      c2.setPartyA(new ContractParty(null, "Acme Corp", null, null, null));
                      c2.setPartyB(new ContractParty(null, "TechSolutions Inc", null, null, null));
                      c2.setAmount(ContractAmount.of(new BigDecimal("1000000.00"), "USD"));
                      c2.setStatus("Active");
                      contractRepository.save(c2);

                      Contract c3 = new Contract();
                      c3.setContractId(ContractId.of(UUID.randomUUID().toString()));
                      c3.setContractNo(new ContractNo("CON-2026-STAB"));
                      c3.setContractName("Stabilization Contract");
                      c3.setPartyA(new ContractParty(null, "Internal HQ", null, null, null));
                      c3.setPartyB(new ContractParty(null, "TechSolutions Inc", null, null, null));
                      c3.setAmount(ContractAmount.of(new BigDecimal("2000000.00"), "USD"));
                      c3.setStatus("DRAFT");
                      contractRepository.save(c3);

                    System.out.println(">>> SAMPLE CONTRACTS CREATED SUCCESSFULLY.");
                }

                System.out.println(">>> CHECKING RULE CONFIGS...");
                if (ruleConfigRepository.count() == 0) {
                    System.out.println(">>> CREATING DEFAULT RULE CONFIGS...");
                    RuleConfig rule1 = new RuleConfig();
                    rule1.setRuleId(UUID.randomUUID().toString());
                    rule1.setRuleName("High Value Contract Review");
                    rule1.setRuleType("RISK_CHECK");
                    rule1.setRuleLevel("HIGH");
                    rule1.setTriggerTime("BEFORE_APPROVAL");
                    rule1.setRuleCondition("contractAmount > 500000");
                    rule1.setExecutionActions("REQUIRE_LEGAL_REVIEW");
                    rule1.setIsEnabled(true);
                    ruleConfigRepository.save(rule1);

                    RuleConfig rule2 = new RuleConfig();
                    rule2.setRuleId(UUID.randomUUID().toString());
                    rule2.setRuleName("Internal Party Compliance Check");
                    rule2.setRuleType("COMPLIANCE");
                    rule2.setRuleLevel("MEDIUM");
                    rule2.setTriggerTime("BEFORE_SUBMIT");
                    rule2.setRuleCondition("partyAName == 'Internal HQ'");
                    rule2.setExecutionActions("NOTIFY");
                    rule2.setIsEnabled(true);
                    ruleConfigRepository.save(rule2);
                    System.out.println(">>> DEFAULT RULE CONFIGS CREATED SUCCESSFULLY.");
                }

                System.out.println(">>> CHECKING RULES...");
                if (ruleRepository.count() == 0) {
                    System.out.println(">>> CREATING DEFAULT RULES...");
                    Rule rule1 = new Rule();
                    rule1.setName("High Value Contract Check");
                    rule1.setDescription("Check if contract amount is greater than 500,000");
                    rule1.setLogicType(RuleLogicType.LOGIC);
                    rule1.setLogicContent("{\"type\":\"group\",\"operator\":\"AND\",\"children\":[{\"type\":\"rule\",\"field\":\"contractAmount\",\"operator\":\"gt\",\"value\":\"500000\"}]}");
                    rule1.setSeverity(Severity.SEVERE);
                    rule1.setStatus(RuleStatus.ACTIVE);
                    ruleRepository.save(rule1);

                    Rule rule2 = new Rule();
                    rule2.setName("Internal Entity Match");
                    rule2.setDescription("Check if Party A is Internal HQ");
                    rule2.setLogicType(RuleLogicType.LOGIC);
                    rule2.setLogicContent("{\"type\":\"group\",\"operator\":\"AND\",\"children\":[{\"type\":\"rule\",\"field\":\"partyAName\",\"operator\":\"eq\",\"value\":\"Internal HQ\"}]}");
                    rule2.setSeverity(Severity.WARNING);
                    rule2.setStatus(RuleStatus.ACTIVE);
                    ruleRepository.save(rule2);

                    Rule rule3 = new Rule();
                    rule3.setName("Acme Corp Monitoring");
                    rule3.setDescription("Flag all contracts with Acme Corp as Party A");
                    rule3.setLogicType(RuleLogicType.LOGIC);
                    rule3.setLogicContent("{\"type\":\"group\",\"operator\":\"AND\",\"children\":[{\"type\":\"rule\",\"field\":\"partyAName\",\"operator\":\"eq\",\"value\":\"Acme Corp\"}]}");
                    rule3.setSeverity(Severity.INFO);
                    rule3.setStatus(RuleStatus.ACTIVE);
                    ruleRepository.save(rule3);

                    Rule rule4 = new Rule();
                    rule4.setName("Contract Present Check");
                    rule4.setDescription("Always hit if contract number exists");
                    rule4.setLogicType(RuleLogicType.LOGIC);
                    rule4.setLogicContent("{\"type\":\"group\",\"operator\":\"AND\",\"children\":[{\"type\":\"rule\",\"field\":\"contractNo\",\"operator\":\"neq\",\"value\":\"\"}]}");
                    rule4.setSeverity(Severity.INFO);
                    rule4.setStatus(RuleStatus.ACTIVE);
                    ruleRepository.save(rule4);

                    System.out.println(">>> DEFAULT RULES CREATED SUCCESSFULLY.");
                }
            } finally {
                com.contract.master.security.TenantContext.clear();
            }
        };
    }
}
