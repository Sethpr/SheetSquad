package com.sheetsquad.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sheetsquad.app.domain.enumeration.SkillType;
import com.sheetsquad.app.domain.enumeration.StatType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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

    @ManyToOne
    private Pool pool;

    @ManyToOne
    private Refrence refrence;

    @ManyToMany
    @JoinTable(name = "rel_skill__extra", joinColumns = @JoinColumn(name = "skill_id"), inverseJoinColumns = @JoinColumn(name = "extra_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "base", "powers", "stats", "skills" }, allowSetters = true)
    private Set<Extra> extras = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "rel_skill__owner", joinColumns = @JoinColumn(name = "skill_id"), inverseJoinColumns = @JoinColumn(name = "owner_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "archetype", "owner", "stats", "skills" }, allowSetters = true)
    private Set<Character> owners = new HashSet<>();

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

    public Set<Extra> getExtras() {
        return this.extras;
    }

    public void setExtras(Set<Extra> extras) {
        this.extras = extras;
    }

    public Skill extras(Set<Extra> extras) {
        this.setExtras(extras);
        return this;
    }

    public Skill addExtra(Extra extra) {
        this.extras.add(extra);
        extra.getSkills().add(this);
        return this;
    }

    public Skill removeExtra(Extra extra) {
        this.extras.remove(extra);
        extra.getSkills().remove(this);
        return this;
    }

    public Set<Character> getOwners() {
        return this.owners;
    }

    public void setOwners(Set<Character> characters) {
        this.owners = characters;
    }

    public Skill owners(Set<Character> characters) {
        this.setOwners(characters);
        return this;
    }

    public Skill addOwner(Character character) {
        this.owners.add(character);
        character.getSkills().add(this);
        return this;
    }

    public Skill removeOwner(Character character) {
        this.owners.remove(character);
        character.getSkills().remove(this);
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
