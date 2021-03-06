package org.sonatype.cs.metrics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.service.ApplicationsDataService;
import org.sonatype.cs.metrics.service.PeriodsDataService;
import org.sonatype.cs.metrics.service.SecurityDataService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class SecurityViolationsController {
    private static final Logger log = LoggerFactory.getLogger(SecurityViolationsController.class);

    @Autowired private SecurityDataService securityDataService;

    @Autowired private PeriodsDataService periodsDataService;

    @Autowired private ApplicationsDataService applicationsDataService;

    @GetMapping({"/securityviolations", "/securityviolations.html"})
    public String securityViolations(Model model) {

        log.info("In SecurityViolationsController");

        Map<String, Object> periodsData =
                periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);
        Map<String, Object> applicationData =
                applicationsDataService.getApplicationData(
                        SqlStatements.METRICTABLENAME, periodsData);

        Map<String, Object> securityViolationsData =
                securityDataService.getSecurityViolations(SqlStatements.METRICTABLENAME);
        model.mergeAttributes(securityViolationsData);
        model.mergeAttributes(applicationData);

        return "securityViolations";
    }
}
