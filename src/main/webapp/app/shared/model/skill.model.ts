import { IPool } from 'app/shared/model/pool.model';
import { IExtra } from 'app/shared/model/extra.model';
import { ICharacter } from 'app/shared/model/character.model';
import { IRefrence } from 'app/shared/model/refrence.model';
import { SkillType } from 'app/shared/model/enumerations/skill-type.model';
import { StatType } from 'app/shared/model/enumerations/stat-type.model';

export interface ISkill {
  id?: number;
  skillType?: SkillType;
  refrenceStat?: StatType;
  pool?: IPool | null;
  extra?: IExtra | null;
  owner?: ICharacter | null;
  refrence?: IRefrence | null;
}

export const defaultValue: Readonly<ISkill> = {};
