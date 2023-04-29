package com.nutri.rest.model;

import com.nutri.rest.config.AuditableEntity;
import lombok.*;

import javax.persistence.*;
@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints={ @UniqueConstraint(columnNames = {"childItemId", "menuName", "dietitianId"}) })
public class DietitianMenus extends AuditableEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuItemId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "itemId", name = "childItemId")
    private ChildItem childItem;
    private Long quantity;
    private String menuName;
    private String instructions;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "dietitianId")
    private User dietitianId;
    @ManyToOne
    @JoinColumn(referencedColumnName = "lookupValueId", name = "quantityUnit", nullable = false)
    private LookupValue quantityUnit;
}
