package org.sonatype.cs.metrics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.service.ApplicationsDataService;
import org.sonatype.cs.metrics.service.LicenseDataService;
import org.sonatype.cs.metrics.service.PeriodsDataService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class LicenseViolationsController {
    private static final Logger log = LoggerFactory.getLogger(LicenseViolationsController.class);

    @Autowired private LicenseDataService licenseDataService;

    @Autowired private PeriodsDataService periodsDataService;

    @Autowired private ApplicationsDataService applicationsDataService;

    @GetMapping({"/licenseviolations", "/licenseviolations.html"})
    public String licenseViolations(Model model) {

        log.info("In LicenseViolationsController");

        Map<String, Object> periodsData =
                periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);
        Map<String, Object> applicationData =
                applicationsDataService.getApplicationData(
                        SqlStatements.METRICTABLENAME, periodsData);

        Map<String, Object> licenseViolationsData =
                licenseDataService.getLicenseViolations(SqlStatements.METRICTABLENAME);
        model.mergeAttributes(licenseViolationsData);
        model.mergeAttributes(applicationData);

        return "licenseViolations";
    }
}
