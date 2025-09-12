package com.example.spring_boot.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="intervals")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Interval {
    
    @Id
    private Long id;

    private Long taskOfFocus;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long userId;
    private int rate;
}
