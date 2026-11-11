package vn.udn.dut.it2.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "academic_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "event_type", nullable = false)
    private String eventType; // HOC_BONG, HOC_VU, HOC_PHI, SU_KIEN_CHUNG

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "is_deadline")
    private Boolean isDeadline;

    @Column(name = "faculty_scope")
    private String facultyScope; // ALL or specific faculty

    @Column(name = "class_scope")
    private String classScope; // ALL or specific class

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
