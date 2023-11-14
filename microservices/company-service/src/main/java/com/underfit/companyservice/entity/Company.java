package com.underfit.companyservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "company", schema = "company-schema")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "msrn", nullable = false)
    private Long msrn;

    @Column(name = "description")
    private String description;

    @Column(name = "user_id")
    private Long userId;

}