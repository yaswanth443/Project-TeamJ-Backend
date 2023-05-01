package com.nutri.rest.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Table(uniqueConstraints={ @UniqueConstraint(columnNames = {"userId", "organization", "fromYear", "toYear"}) })
public class DietitianExperienceDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "userId")
    private User userId;
    private Long fromYear;
    private Long toYear;
    private String organization;
}
