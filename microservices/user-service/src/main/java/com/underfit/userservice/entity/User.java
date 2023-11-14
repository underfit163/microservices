package com.underfit.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user", schema = "user-schema")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "login", nullable = false, length = 256)
    private String login;

    @JsonIgnore
    @Column(name = "password", nullable = false, length = 256)
    private String password;

    @Column(name = "email", nullable = false, length = 256)
    private String email;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "company_id")
    private Long companyId;
}