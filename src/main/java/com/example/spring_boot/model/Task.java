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
    private AppUser createdBy;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "task_status")
    @Builder.Default
    private TaskStatus status = TaskStatus.active;

    @CreationTimestamp
    private Instant createdAt;
    private Long estimatedTime;
    private Instant deadline;
    private Long duration;
    private int difficulty;
    private int urgency;
    private int importance;

    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;
}
