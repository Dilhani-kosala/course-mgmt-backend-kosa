package lk.mgmt.coursemgmtbackend.modules.grades.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class TranscriptDto {
    // per-term: termCode -> { gpa, creditsAttempted, creditsEarned }
    private Map<String, TermGpaDto> termGpa;
    private double cumulativeGpa;
    private double totalCreditsAttempted;
    private double totalCreditsEarned;
    private List<TranscriptLineDto> lines;

    @Setter
    @Getter
    public static class TermGpaDto {
        private double gpa;
        private double creditsAttempted;
        private double creditsEarned;

    }
}
