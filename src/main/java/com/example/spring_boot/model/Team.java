package com.example.spring_boot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="teams")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Team {
    
    @Id
    private Long id;
    
    private String name;
    private Long[] userIds;
}
