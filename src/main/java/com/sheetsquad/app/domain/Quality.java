package com.sheetsquad.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sheetsquad.app.domain.enumeration.Capacity;
import com.sheetsquad.app.domain.enumeration.QualityType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Quality.
 */
@Entity
@Table(name = "quality")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quality implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private QualityType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "capacity_1", nullable = false)
    private Capacity capacity1;

    @Enumerated(EnumType.STRING)
    @Column(name = "capacity_2")
    private Capacity capacity2;

    @Enumerated(EnumType.STRING)
    @Column(name = "capacity_3")
    private Capacity capacity3;

    @NotNull
    @Column(name = "cost", nullable = false)
    private Integer cost;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pool", "owner" }, allowSetters = true)
    private Power owner;

    @ManyToOne
    @JsonIgnoreProperties(value = { "base" }, allowSetters = true)
    private Extra extra;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Quality id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QualityType getType() {
        return this.type;
    }

    public Quality type(QualityType type) {
        this.setType(type);
        return this;
    }

    public void setType(QualityType type) {
        this.type = type;
    }

    public Capacity getCapacity1() {
        return this.capacity1;
    }

    public Quality capacity1(Capacity capacity1) {
        this.setCapacity1(capacity1);
        return this;
    }

    public void setCapacity1(Capacity capacity1) {
        this.capacity1 = capacity1;
    }

    public Capacity getCapacity2() {
        return this.capacity2;
    }

    public Quality capacity2(Capacity capacity2) {
        this.setCapacity2(capacity2);
        return this;
    }

    public void setCapacity2(Capacity capacity2) {
        this.capacity2 = capacity2;
    }

    public Capacity getCapacity3() {
        return this.capacity3;
    }

    public Quality capacity3(Capacity capacity3) {
        this.setCapacity3(capacity3);
        return this;
    }

    public void setCapacity3(Capacity capacity3) {
        this.capacity3 = capacity3;
    }

    public Integer getCost() {
        return this.cost;
    }

    public Quality cost(Integer cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Power getOwner() {
        return this.owner;
    }

    public void setOwner(Power power) {
        this.owner = power;
    }

    public Quality owner(Power power) {
        this.setOwner(power);
        return this;
    }

    public Extra getExtra() {
        return this.extra;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    public Quality extra(Extra extra) {
        this.setExtra(extra);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quality)) {
            return false;
        }
        return id != null && id.equals(((Quality) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quality{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", capacity1='" + getCapacity1() + "'" +
            ", capacity2='" + getCapacity2() + "'" +
            ", capacity3='" + getCapacity3() + "'" +
            ", cost=" + getCost() +
            "}";
    }
}
