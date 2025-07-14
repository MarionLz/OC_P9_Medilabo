package com.openclassrooms.medilabo.diabetesReportService.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class DiabetesReportControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private static final Logger log = LogManager.getLogger(DiabetesReportControllerIT.class);

    @Test
    public void testGetDiabetesReport() throws Exception {
        System.out.println(System.getProperty("user.dir"));
        log.debug("Debug message for file logging");
        mockMvc.perform(get("/diabetes-report/patient/1")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("NONE")));
    }
}
