package com.nufemit.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    @ManyToMany(mappedBy = "participants")
    @JsonIgnore
    public List<Activity> activitiesJoined = new ArrayList<>();
    private String phone;
    private LocalDate birthDate;
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    @OneToMany(mappedBy = "creator")
    @JsonIgnore
    private List<Activity> activitiesCreated;
}
