package com.nufemit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private String lastname;
    private String secondLastname;
    @Column(nullable = false, unique = true, length = 50)
    private String email;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "participants")
    @JsonBackReference
    public List<Activity> activitiesJoined = new ArrayList<>();
    private String phone;
    private LocalDate birthDate;
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    @JsonBackReference
    private List<Activity> activitiesCreated;
}
