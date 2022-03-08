package org.sonatype.cs.metrics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.model.DbRowStr;
import org.sonatype.cs.metrics.service.DbService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ComponentWaiversController {

    private static final Logger log = LoggerFactory.getLogger(ComponentWaiversController.class);

    @Autowired private DbService dbService;

    @GetMapping({"/waivers", "/waivers.html"})
    public String componentWaivers(Model model) {

        log.info("In ComponentWaiversController");

        List<DbRowStr> componentWaivers = dbService.runSqlStr(SqlStatements.COMPONENTWAIVERS);
        model.addAttribute("componentWaivers", componentWaivers);

        return "componentWaivers";
    }
}
