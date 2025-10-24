package com.example.spring_boot.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "teams")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Team {

    @Id
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private AppUser createdBy;

    @ManyToMany
    @JoinTable(name = "team_members", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Builder.Default
    private Set<AppUser> members = new HashSet<>();

    public void addMember(AppUser u) {
        if (members.add(u))
            u.getTeams().add(this);
    }

    public void removeMember(AppUser u) {
        if (members.remove(u))
            u.getTeams().remove(this);
    }
}
