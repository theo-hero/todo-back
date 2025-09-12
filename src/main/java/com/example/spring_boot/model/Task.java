package com.example.spring_boot.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    private Long teamId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "task_status")
    @Builder.Default
    private TaskStatus status = TaskStatus.active;

    @CreationTimestamp
    private Instant createdAt;
    private String estimatedTime;
    private Instant deadline;
}
