package com.nutri.rest.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Table(uniqueConstraints={ @UniqueConstraint(columnNames = {"userId", "awardsOrRecognitions", "yearOfRecognition"}) })
public class DietitianRecognitions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "userId")
    private User userId;
    private String awardsOrRecognitions;
    private Long yearOfRecognition;
}
