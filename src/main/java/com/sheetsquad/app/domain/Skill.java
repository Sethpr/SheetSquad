package com.sheetsquad.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sheetsquad.app.domain.enumeration.SkillType;
import com.sheetsquad.app.domain.enumeration.StatType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Skill.
 */
@Entity
@Table(name = "skill")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Skill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SkillType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "under", nullable = false)
    private StatType under;

    @OneToOne
    @JoinColumn(unique = true)
    private Pool pool;

    @ManyToOne
    @JsonIgnoreProperties(value = { "base" }, allowSetters = true)
    private Extra extra;

    @ManyToOne
    @JsonIgnoreProperties(value = { "archetype", "owner" }, allowSetters = true)
    private Character owner;

    @ManyToOne
    private Refrence refrence;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Skill id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SkillType getType() {
        return this.type;
    }

    public Skill type(SkillType type) {
        this.setType(type);
        return this;
    }

    public void setType(SkillType type) {
        this.type = type;
    }

    public StatType getUnder() {
        return this.under;
    }

    public Skill under(StatType under) {
        this.setUnder(under);
        return this;
    }

    public void setUnder(StatType under) {
        this.under = under;
    }

    public Pool getPool() {
        return this.pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public Skill pool(Pool pool) {
        this.setPool(pool);
        return this;
    }

    public Extra getExtra() {
        return this.extra;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    public Skill extra(Extra extra) {
        this.setExtra(extra);
        return this;
    }

    public Character getOwner() {
        return this.owner;
    }

    public void setOwner(Character character) {
        this.owner = character;
    }

    public Skill owner(Character character) {
        this.setOwner(character);
        return this;
    }

    public Refrence getRefrence() {
        return this.refrence;
    }

    public void setRefrence(Refrence refrence) {
        this.refrence = refrence;
    }

    public Skill refrence(Refrence refrence) {
        this.setRefrence(refrence);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Skill)) {
            return false;
        }
        return id != null && id.equals(((Skill) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Skill{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", under='" + getUnder() + "'" +
            "}";
    }
}
