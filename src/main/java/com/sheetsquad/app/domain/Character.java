package com.sheetsquad.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Character.
 */
@Entity
@Table(name = "character")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Character implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "talent_name")
    private String talentName;

    @Column(name = "loyalty")
    private String loyalty;

    @Column(name = "passion")
    private String passion;

    @Column(name = "inventory")
    private String inventory;

    @NotNull
    @Column(name = "point_total", nullable = false)
    private Integer pointTotal;

    @NotNull
    @Column(name = "spent_points", nullable = false)
    private Integer spentPoints;

    @OneToOne
    @JoinColumn(unique = true)
    private Archetype archetype;

    @ManyToOne
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Character id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Character name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTalentName() {
        return this.talentName;
    }

    public Character talentName(String talentName) {
        this.setTalentName(talentName);
        return this;
    }

    public void setTalentName(String talentName) {
        this.talentName = talentName;
    }

    public String getLoyalty() {
        return this.loyalty;
    }

    public Character loyalty(String loyalty) {
        this.setLoyalty(loyalty);
        return this;
    }

    public void setLoyalty(String loyalty) {
        this.loyalty = loyalty;
    }

    public String getPassion() {
        return this.passion;
    }

    public Character passion(String passion) {
        this.setPassion(passion);
        return this;
    }

    public void setPassion(String passion) {
        this.passion = passion;
    }

    public String getInventory() {
        return this.inventory;
    }

    public Character inventory(String inventory) {
        this.setInventory(inventory);
        return this;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public Integer getPointTotal() {
        return this.pointTotal;
    }

    public Character pointTotal(Integer pointTotal) {
        this.setPointTotal(pointTotal);
        return this;
    }

    public void setPointTotal(Integer pointTotal) {
        this.pointTotal = pointTotal;
    }

    public Integer getSpentPoints() {
        return this.spentPoints;
    }

    public Character spentPoints(Integer spentPoints) {
        this.setSpentPoints(spentPoints);
        return this;
    }

    public void setSpentPoints(Integer spentPoints) {
        this.spentPoints = spentPoints;
    }

    public Archetype getArchetype() {
        return this.archetype;
    }

    public void setArchetype(Archetype archetype) {
        this.archetype = archetype;
    }

    public Character archetype(Archetype archetype) {
        this.setArchetype(archetype);
        return this;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Character owner(User user) {
        this.setOwner(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Character)) {
            return false;
        }
        return id != null && id.equals(((Character) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Character{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", talentName='" + getTalentName() + "'" +
            ", loyalty='" + getLoyalty() + "'" +
            ", passion='" + getPassion() + "'" +
            ", inventory='" + getInventory() + "'" +
            ", pointTotal=" + getPointTotal() +
            ", spentPoints=" + getSpentPoints() +
            "}";
    }
}
