package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import com.contract.master.e2e.pages.ContractDetailPO;
import com.contract.master.e2e.pages.ContractListPO;
import com.contract.master.e2e.pages.DashboardPO;
import com.contract.master.e2e.pages.LoginPO;
import com.contract.master.e2e.utils.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Temporarily disabled")
public class ContractLifecycleTest extends E2ETestBase {
    private DashboardPO dashboard;

    @BeforeEach
    void login() {
        TestDataGenerator generator = new TestDataGenerator(playwright);
        generator.seedUser("admin", "password", "tenant-1");
        
        LoginPO loginPage = new LoginPO(page, getBaseUrl());
        loginPage.navigate();
        dashboard = loginPage.login("admin", "password", "tenant-1");
    }

    @Test
    void testContractCreation() {
        String contractNo = "CT-" + System.nanoTime();
        ContractListPO listPage = dashboard.navigateToContracts();
        ContractDetailPO detailPage = listPage.createContract();
        
        Map<String, String> data = new HashMap<>();
        data.put("Contract No", contractNo);
        data.put("Contract Name", "Test Creation");
        data.put("PARTY A NAME", "Internal HQ"); 
        data.put("PARTY B NAME", "Internal HQ"); 
        
        detailPage.fillForm(data);
        detailPage.save();
        
        listPage = dashboard.navigateToContracts();
        listPage.search(contractNo);
        // Wait for the specific contract to be present in table
        page.waitForSelector("tr:has-text('" + contractNo + "')");
        assertThat(listPage.getRowCount()).isEqualTo(1);
    }

    @Test
    void testContractEditing() {
        String contractNo = "ED-" + System.nanoTime();
        ContractListPO listPage = dashboard.navigateToContracts();
        ContractDetailPO detailPage = listPage.createContract();
        
        Map<String, String> createData = new HashMap<>();
        createData.put("Contract No", contractNo);
        createData.put("Contract Name", "Test Edit");
        detailPage.fillForm(createData);
        detailPage.save();

        listPage = dashboard.navigateToContracts();
        listPage.search(contractNo);
        detailPage = listPage.openContract(contractNo);
        
        detailPage.switchToEditMode();
        Map<String, String> data = new HashMap<>();
        data.put("CONTRACT AMOUNT", "5000");
        detailPage.fillForm(data);
        detailPage.save();
        
        assertThat(detailPage.getFieldValue("Amount")).contains("5,000");
    }
}
