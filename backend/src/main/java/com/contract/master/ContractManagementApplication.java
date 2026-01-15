package com.contract.master;

import com.contract.master.domain.ContractBase;
import com.contract.master.domain.ContractBaseRepository;
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
    public CommandLineRunner initData(UserRepository userRepository, ContractBaseRepository contractRepository, PasswordEncoder passwordEncoder) {
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
        };
    }
}
