package com.sheetsquad.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Power.
 */
@Entity
@Table(name = "power")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Power implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "cost", nullable = false)
    private Integer cost;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JsonIgnoreProperties(value = { "owner" }, allowSetters = true)
    private PowerCategory owner;

    @ManyToOne
    private Pool pool;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Power id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Power name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCost() {
        return this.cost;
    }

    public Power cost(Integer cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getNotes() {
        return this.notes;
    }

    public Power notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public PowerCategory getOwner() {
        return this.owner;
    }

    public void setOwner(PowerCategory powerCategory) {
        this.owner = powerCategory;
    }

    public Power owner(PowerCategory powerCategory) {
        this.setOwner(powerCategory);
        return this;
    }

    public Pool getPool() {
        return this.pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public Power pool(Pool pool) {
        this.setPool(pool);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Power)) {
            return false;
        }
        return id != null && id.equals(((Power) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Power{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", cost=" + getCost() +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
