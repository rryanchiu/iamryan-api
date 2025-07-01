package me.rryan.tinyurl.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment_analyze_plan_v4")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentAnalyzePlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String deviceId;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private String planId;
    @Column(nullable = false)
    private Integer month;
    @Column(nullable = false)
    private BigDecimal totalAmount;
    @Column(nullable = false)
    private BigDecimal showAmount;
    @Column(nullable = false)
    private String planName;
    @Column(nullable = false)
    private String recoveryCode;
    @Column(nullable = false)
    private String outTradeNo;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private String remark;
    @Column(nullable = false)
    private Integer totalLimit;
    @Column(nullable = false)
    private Boolean isBeta;
    @Column(nullable = false)
    private Integer todayUsedCount;
    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;



}
