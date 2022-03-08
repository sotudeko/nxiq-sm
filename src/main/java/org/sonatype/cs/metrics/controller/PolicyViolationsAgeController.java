package org.sonatype.cs.metrics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.model.DbRowStr;
import org.sonatype.cs.metrics.service.DbService;
import org.sonatype.cs.metrics.util.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class PolicyViolationsAgeController {
    private static final Logger log = LoggerFactory.getLogger(PolicyViolationsAgeController.class);

    @Autowired private DbService dbService;

    @GetMapping({"/violationsage", "/violationsage.html"})
    public String policyViolationsAge(
            Model model, @RequestParam(name = "date", required = false) String comparisonDate) {

        log.info("In PolicyViolationsAgeController");
        if (comparisonDate == null) {
            LocalDate dateObj = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            comparisonDate = dateObj.format(formatter);
        }
        final String baseQuery =
                "select policy_name as pointA, application_name as pointB, open_time as"
                        + " pointC, component as pointD, stage as pointE, reason as pointF from"
                        + " policy_violation where parsedatetime(open_time, 'yyyy-MM-dd', 'en')";

        String policyViolationsAge7 =
                String.format(
                        "%s >= PARSEDATETIME('%s', 'yyyy-MM-dd', 'en') - INTERVAL '7' DAY",
                        baseQuery, comparisonDate);
        String policyViolationsAge30 =
                String.format(
                        "%s > PARSEDATETIME('%s', 'yyyy-MM-dd', 'en') - INTERVAL '30' DAY and"
                            + " parsedatetime(open_time, 'yyyy-MM-dd', 'en') < PARSEDATETIME('%s',"
                            + " 'yyyy-MM-dd', 'en') - INTERVAL '7' DAY",
                        baseQuery, comparisonDate, comparisonDate);
        String policyViolationsAge60 =
                String.format(
                        "%s > PARSEDATETIME('%s', 'yyyy-MM-dd', 'en') - INTERVAL '90' DAY and"
                            + " parsedatetime(open_time, 'yyyy-MM-dd', 'en') < PARSEDATETIME('%s',"
                            + " 'yyyy-MM-dd', 'en') - INTERVAL '30' DAY",
                        baseQuery, comparisonDate, comparisonDate);
        String policyViolationsAge90 =
                String.format(
                        "%s <= PARSEDATETIME('%s', 'yyyy-MM-dd', 'en') - INTERVAL '90' DAY",
                        baseQuery, comparisonDate);

        List<DbRowStr> age7Data = dbService.runSqlStr(policyViolationsAge7);
        List<DbRowStr> age30Data = dbService.runSqlStr(policyViolationsAge30);
        List<DbRowStr> age60Data = dbService.runSqlStr(policyViolationsAge60);
        List<DbRowStr> age90Data = dbService.runSqlStr(policyViolationsAge90);

        Map<String, Object> age7Map = HelperService.dataMap("age7", age7Data);
        Map<String, Object> age30Map = HelperService.dataMap("age30", age30Data);
        Map<String, Object> age60Map = HelperService.dataMap("age60", age60Data);
        Map<String, Object> age90Map = HelperService.dataMap("age90", age90Data);

        model.mergeAttributes(age7Map);
        model.mergeAttributes(age30Map);
        model.mergeAttributes(age60Map);
        model.mergeAttributes(age90Map);

        model.addAttribute("status", true);

        return "policyViolationsAge";
    }
}
