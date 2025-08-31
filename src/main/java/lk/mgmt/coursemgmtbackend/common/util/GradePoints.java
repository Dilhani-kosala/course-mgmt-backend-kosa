package lk.mgmt.coursemgmtbackend.common.util;

import lk.mgmt.coursemgmtbackend.common.enums.Grade;

public final class GradePoints {
    private GradePoints() {}

    public static double toPoints(Grade g) {
        if (g == null) return 0.0;
        return switch (g) {
            case A -> 4.0;
            case B -> 3.0;
            case C -> 2.0;
            case D -> 1.0;
            case E, F -> 0.0;
            case I, W -> 0.0; // I (Incomplete), W (Withdrawn) usually do not count in GPA
        };
    }

    public static boolean earnsCredit(Grade g) {
        return g == Grade.A || g == Grade.B || g == Grade.C || g == Grade.D;
    }
}
