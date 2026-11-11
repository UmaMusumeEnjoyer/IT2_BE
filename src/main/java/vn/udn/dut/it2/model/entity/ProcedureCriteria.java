package vn.udn.dut.it2.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "procedure_criteria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedureCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "procedure_name", nullable = false)
    private String procedureName;

    @Column(name = "grade_level", nullable = false)
    private String gradeLevel; // LOAI_XUAT_SAC, LOAI_GIOI, LOAI_KHA

    @Column(name = "min_gpa", precision = 3, scale = 2, nullable = false)
    private BigDecimal minGpa;

    @Column(name = "max_gpa", precision = 3, scale = 2)
    private BigDecimal maxGpa;

    @Column(name = "min_training_point", nullable = false)
    private Integer minTrainingPoint;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
