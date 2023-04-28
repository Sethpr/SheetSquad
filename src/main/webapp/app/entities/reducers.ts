import character from 'app/entities/character/character.reducer';
import archetype from 'app/entities/archetype/archetype.reducer';
import stat from 'app/entities/stat/stat.reducer';
import skill from 'app/entities/skill/skill.reducer';
import pool from 'app/entities/pool/pool.reducer';
import powerCategory from 'app/entities/power-category/power-category.reducer';
import power from 'app/entities/power/power.reducer';
import quality from 'app/entities/quality/quality.reducer';
import extra from 'app/entities/extra/extra.reducer';
import baseExtra from 'app/entities/base-extra/base-extra.reducer';
import refrence from 'app/entities/refrence/refrence.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  character,
  archetype,
  stat,
  skill,
  pool,
  powerCategory,
  power,
  quality,
  extra,
  baseExtra,
  refrence,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
