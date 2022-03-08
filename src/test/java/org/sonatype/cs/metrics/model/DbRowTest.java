package org.sonatype.cs.metrics.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DbRowTest {
    @Test
    void testToString() {
        DbRow dbRow = new DbRow("row1", 1, 2, 3, 4);
        assertEquals(
                "DbRow [label=row1, pointA=1, pointB=2, pointC=3, pointD=4]", dbRow.toString());
    }
}
