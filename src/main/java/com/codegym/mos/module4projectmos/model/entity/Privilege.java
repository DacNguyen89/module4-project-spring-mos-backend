package com.codegym.mos.module4projectmos.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties(value = "roles", allowGetters = true)
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    @JsonBackReference("role-privilege")
    @ManyToMany(mappedBy = "privileges", fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    private Collection<Role> roles;

    public Privilege(String name) {
        this.name = name;
    }
}