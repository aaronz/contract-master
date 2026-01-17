package com.contract.master.api;

import com.contract.master.dto.DashboardStatsDTO;
import com.contract.master.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    @Test
    @WithMockUser
    public void testGetStatsSuccess() throws Exception {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalContracts(100L);
        when(dashboardService.getStats()).thenReturn(stats);

        mockMvc.perform(get("/api/dashboard/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalContracts").value(100));
    }

    @Test
    public void testGetStatsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/dashboard/stats"))
                .andExpect(status().isForbidden());
    }
}
