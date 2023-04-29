package com.nutri.rest.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints={ @UniqueConstraint(columnNames = {"customerId", "dietitianId"}) })
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "customerId")
    private User customerId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "dietitianId")
    private User dietitianId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "lookupValueId", name = "frequency")
    private LookupValue frequency;

    @ManyToOne
    @JoinColumn(referencedColumnName = "lookupValueId", name = "status")
    private LookupValue status;

    @ManyToOne
    @JoinColumn(referencedColumnName = "lookupValueId", name = "preferredMealOption")
    private LookupValue preferredMealOption;

    private BigDecimal amount;

    private String customerInput;
    private String dietitianInput;
    private LocalDate subscriptionExpireDate;

    private String allergens;
    private String sex;
    private String sleep;
    private String quesres;
    private String nutrition;
    private String phyActivity;
    private String hydration;

}
