package com.nutri.rest.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Table(uniqueConstraints={ @UniqueConstraint(columnNames = {"dietitianProfileId", "qualifiedDegree"}) })
public class DietitianExtraEducationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "dietitianProfileId")
    private DietitianProfile dietitianProfileId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "lookupValueId", name = "qualifiedDegree")
    private LookupValue qualifiedDegree;

    private String degreeUniversity;
    private Long degreeYear;
}
