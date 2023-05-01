package com.sheetsquad.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sheetsquad.app.domain.enumeration.Capacity;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Extra.
 */
@Entity
@Table(name = "extra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Extra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "multiplier", nullable = false)
    private Integer multiplier;

    @Column(name = "notes")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "capacity")
    private Capacity capacity;

    @ManyToOne
    @JsonIgnoreProperties(value = { "refrence" }, allowSetters = true)
    private BaseExtra base;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Extra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMultiplier() {
        return this.multiplier;
    }

    public Extra multiplier(Integer multiplier) {
        this.setMultiplier(multiplier);
        return this;
    }

    public void setMultiplier(Integer multiplier) {
        this.multiplier = multiplier;
    }

    public String getNotes() {
        return this.notes;
    }

    public Extra notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Capacity getCapacity() {
        return this.capacity;
    }

    public Extra capacity(Capacity capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public void setCapacity(Capacity capacity) {
        this.capacity = capacity;
    }

    public BaseExtra getBase() {
        return this.base;
    }

    public void setBase(BaseExtra baseExtra) {
        this.base = baseExtra;
    }

    public Extra base(BaseExtra baseExtra) {
        this.setBase(baseExtra);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Extra)) {
            return false;
        }
        return id != null && id.equals(((Extra) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Extra{" +
            "id=" + getId() +
            ", multiplier=" + getMultiplier() +
            ", notes='" + getNotes() + "'" +
            ", capacity='" + getCapacity() + "'" +
            "}";
    }
}
