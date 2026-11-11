package vn.udn.dut.it2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public class ProcedureDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTicketRequest {
        private String studentCode;
        private String procedureName;
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EvaluationResult {
        private String studentCode;
        private String fullName;
        private BigDecimal gpa;
        private Integer trainingPoint;
        private String eligibleLevel; // LOAI_XUAT_SAC, LOAI_GIOI, LOAI_KHA, KHONG_DU_DIEU_KIEN
        private String statusMessage;
        private List<String> checklistSteps;
    }
}
