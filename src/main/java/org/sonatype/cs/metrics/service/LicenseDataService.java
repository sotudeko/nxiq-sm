package org.sonatype.cs.metrics.service;

import org.sonatype.cs.metrics.model.DbRow;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LicenseDataService {

    @Autowired private DbService dbService;

    public Map<String, Object> getLicenseViolations(String tableName) {
        Map<String, Object> model = new HashMap<>();

        List<DbRow> licenseViolations =
                dbService.runSql(tableName, SqlStatements.LICENSEVIOLATIONS);
        List<DbRow> discoveredLicenseViolations =
                dbService.runSql(tableName, SqlStatements.DISCOVEREDLICENSEVIOLATIONS);
        List<DbRow> openLicenseViolations =
                dbService.runSql(tableName, SqlStatements.OPENLICENSEVIOLATIONS);
        List<DbRow> fixedLicenseViolations =
                dbService.runSql(tableName, SqlStatements.FIXEDLICENSEVIOLATIONS);
        List<DbRow> waivedLicenseViolations =
                dbService.runSql(tableName, SqlStatements.WAIVEDLICENSEVIOLATIONS);

        model.put("LicenseViolations", licenseViolations);
        model.put("discoveredLicenseViolations", discoveredLicenseViolations);
        model.put("openLicenseViolations", openLicenseViolations);
        model.put("fixedLicenseViolations", fixedLicenseViolations);
        model.put("waivedLicenseViolations", waivedLicenseViolations);

        DbRow discoveredLicenseViolationsTotal =
                dbService.runSql(tableName, SqlStatements.DISCOVEREDLICENSEVIOLATIONSTOTAL).get(0);
        DbRow openLicenseViolationsTotal =
                dbService.runSql(tableName, SqlStatements.OPENLICENSEVIOLATIONSTOTAL).get(0);
        DbRow fixedLicenseViolationsTotal =
                dbService.runSql(tableName, SqlStatements.FIXEDLICENSEVIOLATIONSTOTAL).get(0);
        DbRow waivedLicenseViolationsTotal =
                dbService.runSql(tableName, SqlStatements.WAIVEDLICENSEVIOLATIONSTOTAL).get(0);

        model.put("discoveredLicenseViolationsTotal", discoveredLicenseViolationsTotal);
        model.put("openLicenseViolationsTotal", openLicenseViolationsTotal);
        model.put("fixedLicenseViolationsTotal", fixedLicenseViolationsTotal);
        model.put("waivedLicenseViolationsTotal", waivedLicenseViolationsTotal);

        int discoveredLicenseTotal =
                discoveredLicenseViolationsTotal.getPointA()
                        + discoveredLicenseViolationsTotal.getPointB()
                        + discoveredLicenseViolationsTotal.getPointC();
        int fixedLicenseTotal =
                fixedLicenseViolationsTotal.getPointA()
                        + fixedLicenseViolationsTotal.getPointB()
                        + fixedLicenseViolationsTotal.getPointC();
        int waivedLicenseTotal =
                waivedLicenseViolationsTotal.getPointA()
                        + waivedLicenseViolationsTotal.getPointB()
                        + waivedLicenseViolationsTotal.getPointC();

        model.put("discoveredLicenseTotal", discoveredLicenseTotal);
        model.put("fixedLicenseTotal", fixedLicenseTotal);
        model.put("waivedLicenseTotal", waivedLicenseTotal);

        int discoveredLicenseCriticalTotal = discoveredLicenseViolationsTotal.getPointA();
        int fixedLicenseCriticalTotal = fixedLicenseViolationsTotal.getPointA();
        int waivededLicenseCriticalTotal = waivedLicenseViolationsTotal.getPointA();

        model.put("discoveredLicenseCriticalTotal", discoveredLicenseCriticalTotal);
        model.put("fixedLicenseCriticalTotal", fixedLicenseCriticalTotal);
        model.put("waivededLicenseCriticalTotal", waivededLicenseCriticalTotal);

        return model;
    }
}
