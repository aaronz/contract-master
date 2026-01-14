package com.contract.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ContractManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContractManagementApplication.class, args);
    }
}
