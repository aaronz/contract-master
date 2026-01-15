package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import com.contract.master.e2e.pages.LoginPage;
import com.contract.master.e2e.pages.ContractListPage;
import com.contract.master.e2e.pages.ContractDetailPage;
import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class US2ContractManagementTest extends E2ETestBase {

    @Test
    void testSearchAndViewDetail() {
        login("admin", "password", "tenant-1");
        
        page.navigate(baseUrl + "/contracts");
        page.waitForSelector(".search-input input");
        ContractListPage listPage = new ContractListPage(page);
        listPage.search("CON-2024-001");
        
        ContractDetailPage detailPage = listPage.viewFirstDetail();
        assertNotNull(detailPage);
    }

    @Test
    void testNewContractCreation() {
        login("admin", "password", "tenant-1");
        page.navigate(baseUrl + "/contracts");
        
        page.click("button:has-text('New Contract')");
        page.fill("input[placeholder='e.g. CON-2026-001']", "E2E-NEW-001");
        page.fill(".el-form-item:has-text('Contract Name') input", "E2E Automated Contract");
        page.fill(".el-form-item:has-text('Party A') input", "Test Party A");
        page.fill(".el-form-item:has-text('Party B') input", "Test Party B");
        page.fill(".el-form-item:has-text('Contract Type') input", "Sales");
        page.click("button:has-text('Create')");
        
        assertThat(page.locator(".el-message--success")).isVisible();
    }

    @Test
    void testPaginationNavigation() {
        login("admin", "password", "tenant-1");
        page.navigate(baseUrl + "/contracts");
        
        page.waitForSelector(".el-pagination");
        assertThat(page.locator(".el-pagination")).isVisible();
        page.locator(".el-pager li.number").first().click();
        assertThat(page.locator(".el-table__row").first()).isVisible();
    }
}
