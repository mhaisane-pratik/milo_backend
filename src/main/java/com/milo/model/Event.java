package com.milo.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String area; // Wakad, Baner, Hinjewadi, Tatwade, Kothrud

    @Column(name = "event_date", nullable = false)
    private String eventDate;

    @Column(name = "event_time", nullable = false)
    private String eventTime;

    @Column(name = "total_spots", nullable = false)
    private Integer totalSpots;

    @Column(name = "joined_spots", nullable = false)
    private Integer joinedSpots;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status; // PENDING, APPROVED, REJECTED, CANCELLED

    @Column(name = "is_featured")
    private Boolean isFeatured;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.totalSpots == null) this.totalSpots = 10;
        if (this.joinedSpots == null) this.joinedSpots = 1;
        if (this.price == null) this.price = BigDecimal.ZERO;
        if (this.status == null) this.status = EventStatus.APPROVED;
        if (this.isFeatured == null) this.isFeatured = false;
    }

    public enum EventStatus {
        PENDING, APPROVED, REJECTED, CANCELLED
    }
}
