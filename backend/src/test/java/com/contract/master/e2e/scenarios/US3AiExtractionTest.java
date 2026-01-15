package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import com.contract.master.e2e.pages.LoginPage;
import com.contract.master.e2e.pages.ContractDetailPage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class US3AiExtractionTest extends E2ETestBase {

    @Test
    void testAiExtractionVerification() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login("admin", "password", "tenant-1");
        
        page.navigate("http://localhost:5173/contract/detail/1");
        ContractDetailPage detailPage = new ContractDetailPage(page);
        
        detailPage.triggerAiAnalysis();
        assertTrue(detailPage.isProgressVisible());
    }
}
