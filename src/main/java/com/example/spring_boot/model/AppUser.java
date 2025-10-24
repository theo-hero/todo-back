package com.example.spring_boot.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password")
@Table(name = "users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String username;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    @Builder.Default
    @JsonIgnore
    private boolean enabled = true;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    @JsonIgnore
    @Builder.Default
    private Set<Task> tasks = new LinkedHashSet<>();

    @JsonIgnore
    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @JsonIgnore
    @Column(name = "failed_attempts")
    private int failedAttempts;

    @JsonIgnore
    @ManyToMany(mappedBy = "members")
    @Builder.Default
    private Set<Team> teams = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private TelegramLink telegramLink;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserSettings settings;
}
