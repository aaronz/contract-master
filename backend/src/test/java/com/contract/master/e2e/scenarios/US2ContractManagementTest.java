package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import com.contract.master.e2e.pages.LoginPage;
import com.contract.master.e2e.pages.ContractListPage;
import com.contract.master.e2e.pages.ContractDetailPage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class US2ContractManagementTest extends E2ETestBase {

    @Test
    void testSearchAndViewDetail() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login("admin", "password", "tenant-1");
        
        page.navigate("http://localhost:5173/contract/list");
        ContractListPage listPage = new ContractListPage(page);
        listPage.search("SN-2024-001");
        
        ContractDetailPage detailPage = listPage.viewFirstDetail();
        assertNotNull(detailPage.getFieldValue("contract_no"));
    }
}
