package vn.udn.dut.it2.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_code", unique = true, nullable = false)
    private String studentCode;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(nullable = false)
    private String faculty;

    @Column(precision = 3, scale = 2)
    private BigDecimal gpa;

    @Column(name = "training_point")
    private Integer trainingPoint;

    @Column(name = "accumulated_credits")
    private Integer accumulatedCredits;

    @Column(name = "total_credits")
    private Integer totalCredits;

    @Column(unique = true)
    private String email;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
