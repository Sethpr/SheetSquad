package com.sheetsquad.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pool.
 */
@Entity
@Table(name = "pool")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pool implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "normal")
    private Integer normal;

    @Column(name = "hard")
    private Integer hard;

    @Column(name = "wiggle")
    private Integer wiggle;

    @Column(name = "expert")
    private Integer expert;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pool id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNormal() {
        return this.normal;
    }

    public Pool normal(Integer normal) {
        this.setNormal(normal);
        return this;
    }

    public void setNormal(Integer normal) {
        this.normal = normal;
    }

    public Integer getHard() {
        return this.hard;
    }

    public Pool hard(Integer hard) {
        this.setHard(hard);
        return this;
    }

    public void setHard(Integer hard) {
        this.hard = hard;
    }

    public Integer getWiggle() {
        return this.wiggle;
    }

    public Pool wiggle(Integer wiggle) {
        this.setWiggle(wiggle);
        return this;
    }

    public void setWiggle(Integer wiggle) {
        this.wiggle = wiggle;
    }

    public Integer getExpert() {
        return this.expert;
    }

    public Pool expert(Integer expert) {
        this.setExpert(expert);
        return this;
    }

    public void setExpert(Integer expert) {
        this.expert = expert;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pool)) {
            return false;
        }
        return id != null && id.equals(((Pool) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pool{" +
            "id=" + getId() +
            ", normal=" + getNormal() +
            ", hard=" + getHard() +
            ", wiggle=" + getWiggle() +
            ", expert=" + getExpert() +
            "}";
    }
}
