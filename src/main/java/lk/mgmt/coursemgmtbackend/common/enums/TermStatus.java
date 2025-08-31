package lk.mgmt.coursemgmtbackend.common.enums;

public enum TermStatus {
    PLANNED,      // created; not yet open
    ENROLLING,    // students can enroll
    IN_PROGRESS,  // teaching happening
    COMPLETED,    // finished; grades posted
    ARCHIVED      // hidden from normal listings
}
